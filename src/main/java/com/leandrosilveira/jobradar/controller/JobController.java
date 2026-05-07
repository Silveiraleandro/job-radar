package com.leandrosilveira.jobradar.controller;

import com.leandrosilveira.jobradar.connector.MockJobConnector;
import com.leandrosilveira.jobradar.connector.greenhouse.connector.GreenhouseJobConnector;
import com.leandrosilveira.jobradar.connector.scrap.connector.HtmlJobConnector;
import com.leandrosilveira.jobradar.dto.JobRequest;
import com.leandrosilveira.jobradar.dto.JobResponse;
import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.service.JobService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final MockJobConnector mockJobConnector;
    private final GreenhouseJobConnector greenhouseJobConnector;
    private final HtmlJobConnector htmlJobConnector;

    public JobController(JobService jobService, MockJobConnector mockJobConnector,
                         GreenhouseJobConnector greenhouseJobConnector, HtmlJobConnector htmlJobConnector) {
        this.jobService = jobService;
        this.mockJobConnector = mockJobConnector;
        this.greenhouseJobConnector = greenhouseJobConnector;
        this.htmlJobConnector = htmlJobConnector;
    }

    // Flow: Client → DTO → Controller → Service → Repository → Hibernate → DB → Response DTO → Client
    @PostMapping
    public JobResponse createJob(@Valid @RequestBody JobRequest request) {
        Job job = new Job(
                request.getTitle(),
                request.getCompany(),
                request.getLocation(),
                request.getUrl()
        );
        Job saved = jobService.save(job);

        return toResponse(saved);
    }

    @GetMapping
    public List<JobResponse> getAllJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Job> jobs = jobService.findJobs(location, keyword, pageable);

        return jobs.stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id);

        return toResponse(job);
    }

    @PostMapping("/import")
    public List<JobResponse> importJobs() {
        return jobService.importJobs(mockJobConnector)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/import/greenhouse")
    public List<JobResponse> importGreenhouseJobs() {
        return jobService.importJobs(greenhouseJobConnector)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportJobsToCsv() {
        String csvContent = jobService.exportJobsToCsv();
        // this tells the browser/Postman - this should be treated like a downloadable file
        // the content is CSV
        // sends the actual file contents
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=jobs.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent.getBytes(StandardCharsets.UTF_8));
    }

    @PostMapping("/import/html")
    public List<JobResponse> importScrapedJobs() {
        return jobService.importJobs(htmlJobConnector)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private JobResponse toResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getUrl()
        );
    }
}

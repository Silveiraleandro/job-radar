package com.leandrosilveira.jobradar.controller;

import com.leandrosilveira.jobradar.connector.MockJobConnector;
import com.leandrosilveira.jobradar.connector.greenhouse.connector.GreenhouseJobConnector;
import com.leandrosilveira.jobradar.dto.JobRequest;
import com.leandrosilveira.jobradar.dto.JobResponse;
import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.service.JobService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final MockJobConnector mockJobConnector;
    private final GreenhouseJobConnector greenhouseJobConnector;

    public JobController(JobService jobService, MockJobConnector mockJobConnector,
                         GreenhouseJobConnector greenhouseJobConnector) {
        this.jobService = jobService;
        this.mockJobConnector = mockJobConnector;
        this.greenhouseJobConnector = greenhouseJobConnector;
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

        return new JobResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getCompany(),
                saved.getLocation(),
                saved.getUrl()
        );
    }

    @GetMapping
    public List<JobResponse> getAllJobs(@RequestParam(required = false) String location,
                                        @RequestParam(required = false) String keyword) {
        List<Job> jobs;

        if(location != null && !location.isBlank()) {
            jobs = jobService.findByLocation(location);
        } else if(keyword != null && !keyword.isBlank()) {
            jobs = jobService.findByKeyword(keyword);
        } else {
            jobs = jobService.findAll();
        }

        return jobs.stream()
                .map(job -> new JobResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        job.getUrl()
                )).toList();
    }

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        Job job = jobService.findById(id);

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation(),
                job.getUrl()
        );
    }

    @PostMapping("/import")
    public List<JobResponse> importJobs() {
        return jobService.importJobs(mockJobConnector)
                .stream()
                .map(job -> new JobResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        job.getUrl()
                ))
                .toList();
    }

    @PostMapping("/import/greenhouse")
    public List<JobResponse> importGreenhouseJobs() {
        return jobService.importJobs(greenhouseJobConnector)
                .stream()
                .map(job -> new JobResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        job.getUrl()
                        ))
                .toList();
    }
}

package com.leandrosilveira.jobradar.controller;

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

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

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
    public List<JobResponse> getAllJobs() {
        return jobService.findAll()
                .stream()
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
}

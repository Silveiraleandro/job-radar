package com.leandrosilveira.jobradar.controller;

import com.leandrosilveira.jobradar.dto.JobRequest;
import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.service.JobService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Job createJob(@RequestBody JobRequest request) {
        Job job = new Job(
                request.getTitle(),
                request.getCompany(),
                request.getLocation(),
                request.getUrl()
        );
        return jobService.save(job);
    }
}

package com.leandrosilveira.jobradar.service;

import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.repository.JobRepository;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job save(Job job) {
        return jobRepository.save(job);
    }
}

package com.leandrosilveira.jobradar.service;

import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.exception.ResourceNotFoundException;
import com.leandrosilveira.jobradar.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job save(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Job findById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Job not found with id %s", id)));
    }

    public List<Job> findByLocation(String location) {
        return jobRepository.findByLocationIgnoreCase(location);
    }

    public List<Job> findByKeyword(String keyword) {
        return jobRepository.findByTitleContainingIgnoreCase(keyword);
    }
}

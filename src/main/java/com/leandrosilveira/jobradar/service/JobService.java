package com.leandrosilveira.jobradar.service;

import com.leandrosilveira.jobradar.connector.JobConnector;
import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.exception.ResourceNotFoundException;
import com.leandrosilveira.jobradar.repository.JobRepository;
import org.springframework.stereotype.Service;

import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    //if URL already exists → return existing job, if URL does not exist → save new job
    public Job save(Job job) {
        return jobRepository.findByUrl(job.getUrl())
                .map(existingJob -> {
                    LOGGER.info("Job already exists with url: {}", job.getUrl());
                    return existingJob;
                })
                .orElseGet(() -> {
                    LOGGER.info("Saving new job with url: {}", job.getUrl());
                    return jobRepository.save(job);
                });
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

    public List<Job> importJobs(JobConnector connector) {
        return connector.fetchJobs()
                .stream()
                .map(this::save)
                .toList();
    }

    public String exportJobsToCsv() {
        List<Job> jobs = jobRepository.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("id,title,company,location,url\n");

        for(Job job : jobs) {
            csv.append(job.getId()).append(",");
            csv.append(escapeCsv(job.getTitle())).append(",");
            csv.append(escapeCsv(job.getCompany())).append(",");
            csv.append(escapeCsv(job.getLocation())).append(",");
            csv.append(escapeCsv(job.getUrl())).append(",");
        }

        return csv.toString();
    }

    private String escapeCsv(String field) {
        if(field == null) {
            return "";
        }

        String escaped = field.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}

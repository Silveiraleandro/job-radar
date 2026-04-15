package com.leandrosilveira.jobradar;

import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobRadarApplication {

    @Autowired
    private JobRepository jobRepository;

    public static void main(String[] args) {
        SpringApplication.run(JobRadarApplication.class, args);
    }
}

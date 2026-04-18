package com.leandrosilveira.jobradar.connector;

import com.leandrosilveira.jobradar.entity.Job;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockJobConnector implements JobConnector {

    @Override
    public List<Job> fetchJobs() {
        return List.of(
                new Job(
                        "Java Backend Developer",
                        "Mock Company",
                        "Vancouver",
                        "http://mock-company.com/job-1"
                ),
                new Job(
                        "Spring Boot Engineer",
                        "Mock Company",
                        "Remote",
                        "http://mock-company.com/job-2"
                )
        );
    }
}

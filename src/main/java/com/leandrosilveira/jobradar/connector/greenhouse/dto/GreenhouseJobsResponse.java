package com.leandrosilveira.jobradar.connector.greenhouse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GreenhouseJobsResponse {
    private List<GreenhouseJobItem> jobs;

    public List<GreenhouseJobItem> getJobs() {
        return jobs;
    }

    public void setJobs(List<GreenhouseJobItem> jobs) {
        this.jobs = jobs;
    }
}

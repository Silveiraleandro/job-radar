package com.leandrosilveira.jobradar.connector;

import com.leandrosilveira.jobradar.entity.Job;

import java.util.List;

public interface JobConnector {
    List<Job> fetchJobs();
}

package com.leandrosilveira.jobradar.controller;

import com.leandrosilveira.jobradar.dto.JobRequest;
import com.leandrosilveira.jobradar.dto.JobResponse;
import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.service.JobService;
import commons.TestConstants;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    @Test
    void shouldReturnJobByIdTest() {
        Job job = new Job(TestConstants.JAVA_DEV, TestConstants.TEST_COMPANY, TestConstants.VANCOUVER, TestConstants.URL);
        when(jobService.findById(1L)).thenReturn(job);

        JobResponse response = jobController.getJobById(1L);

        Assertions.assertEquals(TestConstants.JAVA_DEV, response.getTitle());
        Assertions.assertEquals(TestConstants.TEST_COMPANY, response.getCompany());
    }

    @Test
    void shouldCreateJobTest() {
        @Valid JobRequest request = new JobRequest();
        request.setTitle(TestConstants.JAVA_DEV);
        request.setCompany(TestConstants.TEST_COMPANY);
        request.setLocation(TestConstants.VANCOUVER);
        request.setUrl(TestConstants.URL);

        Job savedJob = new Job(
                TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL
        );
        savedJob.setId(1L);

        when(jobService.save(ArgumentMatchers.any(Job.class))).thenReturn(savedJob);

        JobResponse response = jobController.createJob(request);

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals(TestConstants.JAVA_DEV, response.getTitle());
        Assertions.assertEquals(TestConstants.TEST_COMPANY, response.getCompany());
    }
}

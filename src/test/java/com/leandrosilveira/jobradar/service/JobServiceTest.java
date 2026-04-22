package com.leandrosilveira.jobradar.service;

import com.leandrosilveira.jobradar.connector.greenhouse.connector.GreenhouseJobConnector;
import com.leandrosilveira.jobradar.entity.Job;
import com.leandrosilveira.jobradar.repository.JobRepository;
import commons.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class JobServiceTest {
    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Mock
    private GreenhouseJobConnector gHConnector;

    @Test
    void shouldReturnAllJobsTest() {
        List<Job> jobs = List.of(new Job(TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL));

        Mockito.when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> result = jobService.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(TestConstants.JAVA_DEV, result.get(0).getTitle());
    }

    @Test
    void shouldReturnJobByIdTest() {
        Job job = new Job(
                TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL
        );

        Mockito.when(jobRepository.findById(1L))
                .thenReturn(java.util.Optional.of(job));

        Job result = jobService.findById(1L);

        Assertions.assertEquals(TestConstants.JAVA_DEV, result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenJobNotFoundTest() {
        Mockito.when(jobRepository.findById(1L))
                .thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> jobService.findById(1L)
        );
    }

    @Test
    void shouldReturnExistingJobWhenUrlAlreadyExistsTest() {
        Job existingJob = new Job(
                TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL
        );
        existingJob.setId(1L);

        Job newJob = new Job(
                TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL
        );

        Mockito.when(jobRepository.findByUrl(TestConstants.URL))
                .thenReturn(Optional.of(existingJob));

        Job result = jobService.save(newJob);

        Assertions.assertEquals(1L, result.getId());
        Mockito.verify(jobRepository, Mockito.never()).save(Mockito.any(Job.class));
    }

    @Test
    void shouldSaveJobWhenUrlDoesNotExistTest() {
        Job newJob = new Job(
                TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL
        );

        Job savedJob = new Job(
                TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL
        );
        savedJob.setId(1L);

        Mockito.when(jobRepository.findByUrl(TestConstants.URL))
                .thenReturn(Optional.empty());
        Mockito.when(jobRepository.save(newJob)).thenReturn(savedJob);

        Job result = jobService.save(newJob);

        Assertions.assertEquals(1L, result.getId());
        Mockito.verify(jobRepository).save(newJob);
    }

    @Test
    void shouldImportJobsFromConnector() {
        Job job = new Job(TestConstants.JAVA_DEV, TestConstants.TEST_COMPANY, TestConstants.VANCOUVER, TestConstants.URL);

        Mockito.when(gHConnector.fetchJobs()).thenReturn(List.of(job));
        Mockito.when(jobRepository.findByUrl(TestConstants.URL)).thenReturn(Optional.empty());
        Mockito.when(jobRepository.save(job)).thenReturn(job);

        List<Job> result = jobService.importJobs(gHConnector);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(jobRepository).save(job);
    }
}
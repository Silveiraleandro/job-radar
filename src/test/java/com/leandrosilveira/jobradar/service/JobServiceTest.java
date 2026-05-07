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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    void shouldImportJobsFromConnectorTest() {
        Job job = new Job(TestConstants.JAVA_DEV, TestConstants.TEST_COMPANY, TestConstants.VANCOUVER, TestConstants.URL);

        Mockito.when(gHConnector.fetchJobs()).thenReturn(List.of(job));
        Mockito.when(jobRepository.findByUrl(TestConstants.URL)).thenReturn(Optional.empty());
        Mockito.when(jobRepository.save(job)).thenReturn(job);

        List<Job> result = jobService.importJobs(gHConnector);

        Assertions.assertEquals(1, result.size());
        Mockito.verify(jobRepository).save(job);
    }

    @Test
    void shouldExportJobsToCsvTest() {
        Job job1 = new Job(
                TestConstants.JAVA_DEV,
                TestConstants.TEST_COMPANY,
                TestConstants.VANCOUVER,
                TestConstants.URL
        );
        job1.setId(1L);

        Job job2 = new Job(
                TestConstants.SPRING_BOOT_ENGINEER,
                TestConstants.MOCK_COMPANY,
                TestConstants.REMOTE,
                null
        );
        job2.setId(2L);

        Mockito.when(jobRepository.findAll()).thenReturn(List.of(job1, job2));

        String csv = jobService.exportJobsToCsv();

        String expected =
                "id,title,company,location,url\n" +
                        "1,\"Java Developer\",\"TestCompany\",\"Vancouver\",\"https://example.com\"\n" +
                        "2,\"Spring Boot Engineer\",\"Mock Company\",\"Remote\",\"\"\n";

        Assertions.assertEquals(expected, csv);
    }

    @Test
    void shouldFindJobsByLocationAndKeywordTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> jobs = new PageImpl<>(List.of());

        Mockito.when(jobRepository.findByLocationIgnoreCaseAndTitleContainingIgnoreCase(
                TestConstants.CANADA,
                TestConstants.ENGINEER,
                pageable
        )).thenReturn(jobs);

        Page<Job> result = jobService.findJobs(
                TestConstants.CANADA,
                TestConstants.ENGINEER,
                pageable
        );

        Assertions.assertEquals(0, result.getContent().size());

        Mockito.verify(jobRepository).findByLocationIgnoreCaseAndTitleContainingIgnoreCase(
                TestConstants.CANADA,
                TestConstants.ENGINEER,
                pageable
        );
    }

    @Test
    void shouldFindJobsByLocationOnlyTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> jobs = new PageImpl<>(List.of());

        Mockito.when(jobRepository.findByLocationIgnoreCase(
                TestConstants.CANADA,
                pageable
        )).thenReturn(jobs);

        Page<Job> result = jobService.findJobs(
                TestConstants.CANADA,
                null,
                pageable
        );

        Assertions.assertEquals(0, result.getContent().size());

        Mockito.verify(jobRepository).findByLocationIgnoreCase(
                TestConstants.CANADA,
                pageable
        );
    }

    @Test
    void shouldFindJobsByKeywordOnlyTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> jobs = new PageImpl<>(List.of());

        Mockito.when(jobRepository.findByTitleContainingIgnoreCase(
                TestConstants.ENGINEER,
                pageable
        )).thenReturn(jobs);

        Page<Job> result = jobService.findJobs(
                null,
                TestConstants.ENGINEER,
                pageable
        );

        Assertions.assertEquals(0, result.getContent().size());

        Mockito.verify(jobRepository).findByTitleContainingIgnoreCase(
                TestConstants.ENGINEER,
                pageable
        );
    }

    @Test
    void shouldFindAllJobsWhenNoFiltersProvidedTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> jobs = new PageImpl<>(List.of());

        Mockito.when(jobRepository.findAll(pageable)).thenReturn(jobs);

        Page<Job> result = jobService.findJobs(
                null,
                null,
                pageable
        );

        Assertions.assertEquals(0, result.getContent().size());

        Mockito.verify(jobRepository).findAll(pageable);
    }
}
package com.leandrosilveira.jobradar.connector.greenhouse.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandrosilveira.jobradar.common.Constants;
import com.leandrosilveira.jobradar.common.JobRadarException;
import com.leandrosilveira.jobradar.common.JsonUtilities;
import com.leandrosilveira.jobradar.entity.Job;
import commons.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GreenhouseJobConnectorTest {
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> httpResponse;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TO_JOBS_RESPONSE_FILE = "test/files/toJobsResponse.json";
    private static final String TO_JOBS_RESPONSE_UNKNOWN_FILE = "test/files/toJobsResponseUnknown.json";

    @Test
    void shouldMapGreenhouseResponseToJobsTest() throws Exception {
        String responseBody = JsonUtilities.getFileAsString(TO_JOBS_RESPONSE_FILE);
        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(responseBody);

        GreenhouseJobConnector connector = new GreenhouseJobConnector(httpClient, objectMapper);

        List<Job> jobs = connector.fetchJobs();

        Assertions.assertEquals(1, jobs.size());
        Assertions.assertEquals(TestConstants.ACCOUNT_EXC_AI_SALES, jobs.get(0).getTitle());
        Assertions.assertEquals(Constants.Companies.STRIPE, jobs.get(0).getCompany());
        Assertions.assertEquals(TestConstants.SAN_FRAN, jobs.get(0).getLocation());
        Assertions.assertEquals(TestConstants.STRIPE_URL, jobs.get(0).getUrl());
        Assertions.assertNotNull(jobs.get(0).getUrl());
        Mockito.verify(httpClient).send(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldUseUnknownWhenLocationIsMissingTest() throws Exception {
        String responseBody = JsonUtilities.getFileAsString(TO_JOBS_RESPONSE_UNKNOWN_FILE);
        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn(responseBody);

        GreenhouseJobConnector connector = new GreenhouseJobConnector(httpClient, objectMapper);

        List<Job> jobs = connector.fetchJobs();

        Assertions.assertEquals(1, jobs.size());
        Assertions.assertEquals(Constants.Greenhouse.UNKNOWN, jobs.get(0).getLocation());
    }

    @Test
    void shouldThrowExceptionWhenResponseStatusIsNot200Test() throws Exception {
        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(500);

        GreenhouseJobConnector connector = new GreenhouseJobConnector(httpClient, objectMapper);
        Assertions.assertThrows(JobRadarException.class, connector::fetchJobs);
    }

    @Test
    void shouldThrowExceptionWhenResponseBodyIsInvalidJsonTest() throws Exception {
        Mockito.when(httpClient.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("not-a-valid-json");

        GreenhouseJobConnector connector = new GreenhouseJobConnector(httpClient, objectMapper);

        Assertions.assertThrows(JobRadarException.class, connector::fetchJobs);
    }
}
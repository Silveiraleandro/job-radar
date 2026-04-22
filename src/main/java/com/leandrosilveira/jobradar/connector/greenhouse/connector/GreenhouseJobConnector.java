package com.leandrosilveira.jobradar.connector.greenhouse.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leandrosilveira.jobradar.common.Constants;
import com.leandrosilveira.jobradar.common.JobRadarException;
import com.leandrosilveira.jobradar.connector.JobConnector;
import com.leandrosilveira.jobradar.connector.greenhouse.dto.GreenhouseJobItem;
import com.leandrosilveira.jobradar.connector.greenhouse.dto.GreenhouseJobsResponse;
import com.leandrosilveira.jobradar.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class GreenhouseJobConnector implements JobConnector {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GreenhouseJobConnector(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Job> fetchJobs() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Constants.Greenhouse.GREENHOUSE_URL))
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                throw new JobRadarException(
                        String.format(Constants.GreenhouseError.FAILED_TO_FETCH, response.statusCode()));
            }

            GreenhouseJobsResponse greenhouseJobsResponse =
                    objectMapper.readValue(response.body(), GreenhouseJobsResponse.class);
            LOGGER.info("Fetched {} jobs from Greenhouse", greenhouseJobsResponse.getJobs().size());
            return greenhouseJobsResponse.getJobs()
                    .stream()
                    .map(this::toJob)
                    .toList();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JobRadarException(Constants.GreenhouseError.INTERRUPTED_WHILE_FETCHING, e);
        } catch (IOException e) {
            throw new JobRadarException(Constants.GreenhouseError.FAIL_TO_PARSE_JOB_RESPONSE, e);
        }
    }

    private Job toJob(GreenhouseJobItem item) {
        String location = item.getLocation() != null && item.getLocation().getName() != null
                ? item.getLocation().getName()
                : Constants.Greenhouse.UNKNOWN;
        return new Job(
                item.getTitle(),
                Constants.Companies.STRIPE,
                location,
                item.getAbsoluteUrl()
        );
    }
}

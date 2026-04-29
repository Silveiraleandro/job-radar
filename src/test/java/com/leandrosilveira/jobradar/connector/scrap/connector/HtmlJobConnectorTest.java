package com.leandrosilveira.jobradar.connector.scrap.connector;

import com.leandrosilveira.jobradar.common.JsonUtilities;
import com.leandrosilveira.jobradar.entity.Job;
import commons.TestConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class HtmlJobConnectorTest {

    private static final String HTML_JOBS_FILE = "test/files/htmlJobs.html";

    @Test
    void shouldParseJobsFromHtml() {
        String html = JsonUtilities.getFileAsString(HTML_JOBS_FILE);
        Document document = Jsoup.parse(html, TestConstants.URL);

        HtmlJobConnector connector = new HtmlJobConnector();

        List<Job> jobs = connector.parseJobs(document);

        Assertions.assertEquals(2, jobs.size());
        Assertions.assertEquals(TestConstants.JAVA_DEV, jobs.get(0).getTitle());
        Assertions.assertEquals(TestConstants.TEST_COMPANY, jobs.get(0).getCompany());
        Assertions.assertEquals(TestConstants.VANCOUVER, jobs.get(0).getLocation());
        Assertions.assertEquals(TestConstants.URL_JOBS_1, jobs.get(0).getUrl());
    }
}
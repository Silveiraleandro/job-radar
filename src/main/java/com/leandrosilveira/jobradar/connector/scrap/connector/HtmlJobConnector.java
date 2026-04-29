package com.leandrosilveira.jobradar.connector.scrap.connector;

import com.leandrosilveira.jobradar.common.Constants;
import com.leandrosilveira.jobradar.common.JobRadarException;
import com.leandrosilveira.jobradar.connector.JobConnector;
import com.leandrosilveira.jobradar.entity.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class HtmlJobConnector implements JobConnector {
    private static final String SOURCE_URL = "https://example.com/jobs";

    @Override
    public List<Job> fetchJobs() {
        try {
            Document document = Jsoup.connect(SOURCE_URL).get();
            return parseJobs(document);
        } catch (IOException e) {
            throw new JobRadarException(Constants.Error.FAILED_TO_FETCH, e);
        }
    }

    public List<Job> parseJobs(Document document) {
        Elements jobCards = document.select(Constants.JobConnector.JOB_CARD);

        return jobCards.stream()
                .map(this::toJob)
                .toList();
    }

    private Job toJob(Element element) {
        String title = element.select(Constants.JobConnector.JOB_TITLE).text();
        String company = element.select(Constants.JobConnector.COMPANY).text();
        String location = element.select(Constants.JobConnector.LOCATION).text();
        String url = element.select("a").attr(Constants.JobConnector.ABS_HREF);

        return new Job(title, company, location, url);
    }
}

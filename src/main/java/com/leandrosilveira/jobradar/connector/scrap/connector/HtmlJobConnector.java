package com.leandrosilveira.jobradar.connector.scrap.connector;

import com.leandrosilveira.jobradar.common.Constants;
import com.leandrosilveira.jobradar.common.JobRadarException;
import com.leandrosilveira.jobradar.connector.JobConnector;
import com.leandrosilveira.jobradar.entity.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class HtmlJobConnector implements JobConnector {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Value("${jobradar.connector.html.url}")
    private String sourceUrl;

    @Override
    public List<Job> fetchJobs() {
        try {
            Document document = Jsoup.connect(sourceUrl)
                    .userAgent(Constants.Html.USER_AGENT)
                    .timeout(10000)
                    .get();

            return parseJobs(document);
        } catch (IOException e) {
            throw new JobRadarException(Constants.Error.FAILED_TO_FETCH, e);
        }
    }

    public List<Job> parseJobs(Document document) {
        Elements jobCards = document.select(Constants.Html.POSTING);

        return jobCards.stream()
                .map(this::toJob)
                .toList();
    }

    private Job toJob(Element element) {
        String title = element.select(Constants.Html.POSTING_TITLE_H5).text();

        if (title.isBlank()) {
            title = element.select(Constants.Html.H5).text();
        }

        if (title.isBlank()) {
            title = element.select(Constants.Html.POSTING).text();
        }

        String location = element.select(Constants.Html.SORT_BY_LOCATION).text();
        String url = element.select("a").attr(Constants.Html.ABS_HREF);

        return new Job(
                title,
                Constants.Companies.PROPER,
                location,
                url
        );
    }
}

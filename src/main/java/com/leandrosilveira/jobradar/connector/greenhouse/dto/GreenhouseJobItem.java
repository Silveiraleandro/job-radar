package com.leandrosilveira.jobradar.connector.greenhouse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GreenhouseJobItem {
    private Long id;
    private String title;
    private GreenhouseLocation location;
    private String absoluteUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GreenhouseLocation getLocation() {
        return location;
    }

    public void setLocation(GreenhouseLocation location) {
        this.location = location;
    }

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    @JsonProperty("absolute_url")
    public void setAbsoluteUrl(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }
}

package com.leandrosilveira.jobradar.dto;

public class JobResponse {

    private Long id;
    private String title;
    private String company;
    private String location;
    private String url;

    public JobResponse(Long id, String title, String company, String location, String url) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.url = url;
    }

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

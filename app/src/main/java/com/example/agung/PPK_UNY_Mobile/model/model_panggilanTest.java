package com.example.agung.PPK_UNY_Mobile.model;

import java.util.Date;

public class model_panggilanTest {
    private String jobName;
    private String link;
    private Date date;

    public model_panggilanTest() {
    }

    public model_panggilanTest(String jobName, String link, Date date) {
        this.jobName = jobName;
        this.link = link;
        this.date = date;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

package com.example.agung.PPK_UNY_Mobile.model;

public class model_appliedJobs {

    private String companyName, jobName, date, jobID, detail;

    public model_appliedJobs() {
    }

    public model_appliedJobs(String companyName, String jobName, String date, String jobID, String detail) {
        this.companyName = companyName;
        this.jobName = jobName;
        this.date = date;
        this.jobID = jobID;
        this.detail = detail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

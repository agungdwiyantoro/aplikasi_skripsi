package com.example.agung.PPK_UNY_Mobile.model;


import java.util.Date;

public class model_job {

    private String jobName;
    private String detail;
    private Date startDate;
    private Date endDate;
    private String jobID;
    private String companyID;
    private String companyName;
    private boolean quickApply;
    private String companyEmail;
    private Date dateCreated;

    public model_job() {
    }


    public model_job(String jobName, String detail, Date startDate, Date endDate, String jobID, boolean quickApply, String companyID,   String companyName, String companyEmail) {
        this.jobName = jobName;
        this.detail = detail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobID = jobID;
        this.companyID = companyID;
        this.companyName = companyName;
        this.quickApply = quickApply;
        this.companyEmail = companyEmail;
    }

    public model_job(String jobName, String detail, Date startDate, Date endDate, String jobID, boolean quickApply, String companyID,   String companyName, String companyEmail, Date dateCreated) {
        this.jobName = jobName;
        this.detail = detail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobID = jobID;
        this.companyID = companyID;
        this.companyName = companyName;
        this.quickApply = quickApply;
        this.companyEmail = companyEmail;
        this.dateCreated = dateCreated;
    }

    public boolean isQuickApply() {
        return quickApply;
    }

    public void setQuickApply(boolean quickApply) {
        this.quickApply = quickApply;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}

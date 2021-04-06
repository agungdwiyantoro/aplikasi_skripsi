package com.example.agung.PPK_UNY_Mobile.model;

import java.util.Date;

public class model_file_meta_data {
    private String fullName, phoneNumber, fileName, email, sDate;
    private Date date;

    public model_file_meta_data(String fullName, String phoneNumber, String fileName, String email, Date date) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
        this.email = email;
        this.date = date;
    }

    public model_file_meta_data(String fileName, String sDate) {
        this.fileName = fileName;
        this.sDate = sDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }
}

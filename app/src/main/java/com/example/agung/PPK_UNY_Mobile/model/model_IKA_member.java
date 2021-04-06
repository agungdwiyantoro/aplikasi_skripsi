package com.example.agung.PPK_UNY_Mobile.model;

import java.util.Date;

public class model_IKA_member {
    String nama_lengkap, NIM, phoneNumber, fileName, diploma_path;
    Date fileDate;



    public model_IKA_member(String nama_lengkap, String NIM, String phoneNumber, String fileName, Date fileDate) {
        this.nama_lengkap = nama_lengkap;
        this.NIM = NIM;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
        this.fileDate = fileDate;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getNIM() {
        return NIM;
    }

    public void setNIM(String NIM) {
        this.NIM = NIM;
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

    public String getDiploma_path() {
        return diploma_path;
    }

    public void setDiploma_path(String diploma_path) {
        this.diploma_path = diploma_path;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }
}

package com.example.agung.PPK_UNY_Mobile.model;

import java.util.Date;

public class model_advertisement {
    private String informasi_name, category, by, detail,informasiID;
    private Date dateCreated;


    public String getInformasi_name() {
        return informasi_name;
    }

    public void setInformasi_name(String informasi_name) {
        this.informasi_name = informasi_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInformasiID() {
        return informasiID;
    }

    public void setInformasiID(String informasiID) {
        this.informasiID = informasiID;
    }
}

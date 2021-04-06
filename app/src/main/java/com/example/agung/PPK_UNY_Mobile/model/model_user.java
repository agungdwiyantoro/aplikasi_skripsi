package com.example.agung.PPK_UNY_Mobile.model;

public class model_user {

    private String name;
    private String birth_place;
    private String birth_date;
    private String email;
    private String handphone;
    private boolean jenisKelamin;
    private String token;

    public model_user(String name, String birth_place, String birth_date, String handphone, boolean jenisKelamin, String token) {
        this.name = name;
        this.birth_place = birth_place;
        this.birth_date = birth_date;
        this.handphone = handphone;
        this.jenisKelamin = jenisKelamin;
        this.token = token;
    }

    public model_user(String name, String birth_place, String birth_date, String email, String handphone, boolean jenisKelamin) {
        this.name = name;
        this.birth_place = birth_place;
        this.birth_date = birth_date;
        this.handphone = handphone;
        this.jenisKelamin = jenisKelamin;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public boolean isJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(boolean jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

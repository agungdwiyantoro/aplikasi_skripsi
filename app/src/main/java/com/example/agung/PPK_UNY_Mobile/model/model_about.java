package com.example.agung.PPK_UNY_Mobile.model;

public class model_about {
    private String address, latitude, longitude, location;
    private String phonenumber_one, phonenumber_two;
    private String email;
    private String inst_username;
    private String facebook_username, facebook_page_kode;
    private String twitter_username;
    private String website_url;
    private String fax_number;

    public model_about(){

    }
    public model_about(String address, String latitude, String longitude, String location, String phonenumber_one, String phonenumber_two, String email, String inst_username, String facebook_username, String facebook_page_kode, String twitter_username, String website_url, String fax_number) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.phonenumber_one = phonenumber_one;
        this.phonenumber_two = phonenumber_two;
        this.email = email;
        this.inst_username = inst_username;
        this.facebook_username = facebook_username;
        this.facebook_page_kode = facebook_page_kode;
        this.twitter_username = twitter_username;
        this.website_url = website_url;
        this.fax_number = fax_number;

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhonenumber_one() {
        return phonenumber_one;
    }

    public void setPhonenumber_one(String phonenumber_one) {
        this.phonenumber_one = phonenumber_one;
    }

    public String getPhonenumber_two() {
        return phonenumber_two;
    }

    public void setPhonenumber_two(String phonenumber_two) {
        this.phonenumber_two = phonenumber_two;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInst_username() {
        return inst_username;
    }

    public void setInst_username(String inst_username) {
        this.inst_username = inst_username;
    }

    public String getFacebook_username() {
        return facebook_username;
    }

    public void setFacebook_username(String facebook_username) {
        this.facebook_username = facebook_username;
    }

    public String getFacebook_page_kode() {
        return facebook_page_kode;
    }

    public void setFacebook_page_kode(String facebook_page_kode) {
        this.facebook_page_kode = facebook_page_kode;
    }

    public String getTwitter_username() {
        return twitter_username;
    }

    public void setTwitter_username(String twitter_username) {
        this.twitter_username = twitter_username;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getFax_number() {
        return fax_number;
    }

    public void setFax_number(String fax_number) {
        this.fax_number = fax_number;
    }
}

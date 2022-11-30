package com.example.nangkringbangdriver.Model;

import com.google.firebase.Timestamp;

public class Model_Profile {
    private String user_alamat, user_email, user_img, user_nama, user_type, user_username, user_telp;
    private Timestamp user_register;

    public Model_Profile() {}

    public Model_Profile(String user_alamat, String user_email, String user_img, String user_nama, String user_type, String user_username, String user_telp, Timestamp user_register) {
        this.user_alamat = user_alamat;
        this.user_email = user_email;
        this.user_img = user_img;
        this.user_nama = user_nama;
        this.user_type = user_type;
        this.user_username = user_username;
        this.user_telp = user_telp;
        this.user_register = user_register;
    }

    public String getUser_alamat() {
        return user_alamat;
    }

    public void setUser_alamat(String user_alamat) {
        this.user_alamat = user_alamat;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_nama() {
        return user_nama;
    }

    public void setUser_nama(String user_nama) {
        this.user_nama = user_nama;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_telp() {
        return user_telp;
    }

    public void setUser_telp(String user_telp) {
        this.user_telp = user_telp;
    }

    public Timestamp getUser_register() {
        return user_register;
    }

    public void setUser_register(Timestamp user_register) {
        this.user_register = user_register;
    }
}

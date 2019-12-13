package com.example.security_essentials;

public class User {
    private String uid;
    private String correo;

    public User(String uid, String correo) {
        this.uid = uid;
        this.correo = correo;
    }

    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}

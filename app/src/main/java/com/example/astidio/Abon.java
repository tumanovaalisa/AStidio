package com.example.astidio;

public class Abon {
    private String idU;
    private String email;
    private String lastname;
    private String name;
    private String date;

    public Abon() {
        this.lastname = "";
        this.name = "";
        this.idU = "";
        this.email = "";
        this.date = "";
    }

    public String getIdU() {
        return idU;
    }

    public void setIdU(String idU) {
        this.idU = idU;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

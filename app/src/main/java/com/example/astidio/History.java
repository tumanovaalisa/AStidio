package com.example.astidio;

import java.util.HashMap;
import java.util.Map;

public class History {
    private String idU;
    private String email;
    private String lastname;
    private String name;
    private String date;
    private double price;
    private Map<String, Integer> order;

    public History() {
        this.idU = "";
        this.order = new HashMap<>();
        this.lastname = "";
        this.name = "";
        this.date = "";
        this.email = "";
        this.price = 0;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, Integer> getOrder() {
        return order;
    }

    public void setOrder(Map<String, Integer> order) {
        this.order = order;
    }
}
package com.example.astidio;

public class Product {
    private int imageResId;
    private String name;
    private double price;
    private String description;
    private int sale;
    private int amount;

    public Product(int imageResId, String name, double price, String description, int sale, int amount) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.sale = sale;
        this.amount = amount;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }



}

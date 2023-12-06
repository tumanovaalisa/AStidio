package com.example.astidio;

public class Product {
    private int imageResId;
    private String name;
    private double price;
    private int amount;

    public Product(int imageResId, String name, double price, int amount) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

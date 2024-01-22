package com.example.astidio;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Product {
    private String idProduct;
    private String imgProduct;
    private String nameProduct;
    private double priceProduct;
    private String descriptionProduct;
    private int saleProduct;
    private int amountProduct;

    public Product() {
        this.idProduct = "";
        this.nameProduct = "";
        this.descriptionProduct = "";
        this.saleProduct = 0;
        this.amountProduct = 0;
        this.priceProduct = 0;
        this.imgProduct = "";
    }

    public Product(String idProduct, String imageResId, String name, double price,
                     String description, int sale, int amount) {
        this.idProduct = idProduct;
        this.imgProduct = imageResId;
        this.nameProduct = name;
        this.priceProduct = price;
        this.descriptionProduct = description;
        this.saleProduct = sale;
        this.amountProduct = amount;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public void setImgProduct(String imgProduct) {
        
        this.imgProduct = imgProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        
        this.descriptionProduct = descriptionProduct;
    }

    public int getSaleProduct() {
        return saleProduct;
    }

    public void setSaleProduct(int saleProduct) {
        this.saleProduct = saleProduct;
    }

    public int getAmountProduct() {
        return amountProduct;
    }

    public void setAmountProduct(int amountProduct) {
        this.amountProduct = amountProduct;
    }

}

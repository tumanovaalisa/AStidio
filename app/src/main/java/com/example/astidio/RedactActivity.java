package com.example.astidio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RedactActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    EditText nameTxt;
    EditText descriptionTxt;
    EditText amountTxt;
    EditText photoTxt;
    EditText priceTxt;
    EditText saleTxt;
    Product product;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_redact);

        db = FirebaseFirestore.getInstance();

        String prId = getIntent().getStringExtra("selectedId");
        String prName = getIntent().getStringExtra("selectedName");
        double prPrice = Double.parseDouble(getIntent().getStringExtra("selectedPrice"));
        int prSale = Integer.parseInt(getIntent().getStringExtra("selectedSale"));
        int prAmount = Integer.parseInt(getIntent().getStringExtra("selectedAmount"));
        String prDescription = getIntent().getStringExtra("selectedDescription");
        String prImage = getIntent().getStringExtra("selectedImage");

        nameTxt = findViewById(R.id.name_ET);
        nameTxt.setText(prName);
        descriptionTxt = findViewById(R.id.desc_ET);
        descriptionTxt.setText(prDescription);
        amountTxt = findViewById(R.id.amount_ET);
        amountTxt.setText(Integer.toString(prAmount));
        photoTxt = findViewById(R.id.photo_ET);
        photoTxt.setText(prImage);
        priceTxt = findViewById(R.id.price_ET);
        priceTxt.setText(Double.toString(prPrice));
        saleTxt = findViewById(R.id.sale_ET);
        saleTxt.setText(Integer.toString(prSale));

        product = new Product(prId, prImage, prName, prPrice, prDescription, prSale, prAmount);
    }

    public void redactProduct(View view) {
        if (!nameTxt.getText().toString().equals("") && !descriptionTxt.getText().toString().equals("")
                && !amountTxt.getText().toString().equals("") && !photoTxt.getText().toString().equals("")
                && !priceTxt.getText().toString().equals("") && !saleTxt.getText().toString().equals("")) {
            Intent intent = new Intent(this, AdminActivity.class);
            DocumentReference userRef = db.collection("Products").document(product.getIdProduct());
            if (!nameTxt.getText().equals(product.getNameProduct()))
                userRef.update("Name", nameTxt.getText().toString());
            if (!descriptionTxt.getText().equals(product.getDescriptionProduct()))
                userRef.update("Description", descriptionTxt.getText().toString());
            if (!amountTxt.getText().equals(product.getAmountProduct()))
                userRef.update("Amount", Integer.parseInt(amountTxt.getText().toString()));
            if (!photoTxt.getText().equals(product.getImgProduct()))
                userRef.update("Photo", photoTxt.getText().toString());
            if (!priceTxt.getText().equals(product.getPriceProduct()))
                userRef.update("Price", Double.parseDouble(priceTxt.getText().toString()));
            if (!saleTxt.getText().equals(product.getSaleProduct()))
                userRef.update("Sale", Integer.parseInt(saleTxt.getText().toString()));
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Введите все данные",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void toBack(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    public void deleteProduct(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        DocumentReference userRef = db.collection("Products").document(product.getIdProduct());
        userRef.delete();
        startActivity(intent);
    }
}

package com.example.astidio;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    EditText nameTxt;
    EditText descriptionTxt;
    EditText amountTxt;
    EditText photoTxt;
    EditText priceTxt;
    EditText saleTxt;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        db = FirebaseFirestore.getInstance();

        nameTxt = findViewById(R.id.name_ET);
        descriptionTxt = findViewById(R.id.desc_ET);
        amountTxt = findViewById(R.id.amount_ET);
        photoTxt = findViewById(R.id.photo_ET);
        priceTxt = findViewById(R.id.price_ET);
        saleTxt = findViewById(R.id.sale_ET);
    }

    public void newProduct(View view) {
        if (!nameTxt.getText().toString().equals("") && !descriptionTxt.getText().toString().equals("")
                && !amountTxt.getText().toString().equals("") && !photoTxt.getText().toString().equals("")
                && !priceTxt.getText().toString().equals("") && !saleTxt.getText().toString().equals("")) {
            Map<String, Object> newProduct = new HashMap<>();

            newProduct.put("Name", nameTxt.getText().toString());
            newProduct.put("Description", descriptionTxt.getText().toString());
            newProduct.put("Price", priceTxt.getText().toString());
            newProduct.put("Sale", saleTxt.getText().toString());
            newProduct.put("Amount", amountTxt.getText().toString());
            newProduct.put("Photo", photoTxt.getText().toString());

            db.collection("Products")
                    .add(newProduct)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
            Intent intent = new Intent(this, AdminFragment.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Введите все данные",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void toAdmin(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}
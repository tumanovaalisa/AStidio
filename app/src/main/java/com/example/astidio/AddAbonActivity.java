package com.example.astidio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAbonActivity  extends AppCompatActivity {
    private FirebaseFirestore db;
    TextView nameTxt;
    EditText dateTxt;
    TextView emailTxt;
    String prId;
    String prName;
    String prLastName;
    String prEmail;
    String prDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abon_redact);

        db = FirebaseFirestore.getInstance();

        prId = getIntent().getStringExtra("selectedId");
        prName = getIntent().getStringExtra("selectedName");
        prLastName = getIntent().getStringExtra("selectedLastname");
        prEmail = getIntent().getStringExtra("selectedEmail");
        prDate = getIntent().getStringExtra("selectedDate");

        nameTxt = findViewById(R.id.FIO_TV);
        nameTxt.setText(prName + " " + prLastName);
        dateTxt = findViewById(R.id.date_ET);
        if (!prDate.equals("")){
            dateTxt.setText(prDate);
        }
        emailTxt = findViewById(R.id.Mail_TV);
        emailTxt.setText(prEmail);
    }

    public void redactProduct(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        DocumentReference userRef = db.collection("Users").document(prId);
        if (!dateTxt.getText().equals(""))
            userRef.update("Date", dateTxt.getText().toString());
        startActivity(intent);
    }

    public void toBack(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}
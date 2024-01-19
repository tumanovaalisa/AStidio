package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_shop);


    }

    public void addProduct(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
}
package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_shop);

        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.shop_redact);

        ArrayList<Product> products = new ArrayList<>();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = new Product();
                                String id = document.getId().toString();
                                product.setIdProduct(id);
                                for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                    if (docs.getKey().equals("Name")) product.setNameProduct(docs.getValue().toString());
                                    if (docs.getKey().equals("Price")) product.setPriceProduct(Double.parseDouble(docs.getValue().toString()));
                                    if (docs.getKey().equals("Amount")) product.setAmountProduct(Integer.parseInt(docs.getValue().toString()));
                                    if (docs.getKey().equals("Sale")) product.setSaleProduct(Integer.parseInt(docs.getValue().toString()));
                                    if (docs.getKey().equals("Photo")) product.setImgProduct(docs.getValue().toString());
                                    if (docs.getKey().equals("Description")) product.setDescriptionProduct(docs.getValue().toString());
                                }
                                products.add(product);
                            }
                            AdminAdapter adapter = new AdminAdapter(getApplicationContext(), products);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    }
                });
        }

    public void addProduct(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void historyOrders(View view) {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
    }

    public void exitAdmin(View view) {
        Intent intent = new Intent(this, EntryActivity.class);
        startActivity(intent);
    }
}
package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AdminFragment extends Fragment {
    public AdminFragment() {
        super(R.layout.admin_shop);
    }

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.shop_redact);

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
                            AdminAdapter adapter = new AdminAdapter(getContext(), products);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });

        Button bt1 = view.findViewById(R.id.addProduct);
        ImageButton bt2 = view.findViewById(R.id.exitAdmin);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddActivity.class);
                startActivity(intent);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), EntryActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

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
import java.util.List;
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
    ArrayList<Product> products = new ArrayList<>();
    private SearchView searchView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.shop_redact);

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
        searchView = (SearchView) view.findViewById(R.id.search_admin);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
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

    private void filterList(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : products){
            if (text.toLowerCase().isEmpty()){
                filteredList.clear();
            }
            else if (!text.toLowerCase().isEmpty()){
                if (product.getNameProduct().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(product);
                }
            }
        }
        if (filteredList.isEmpty()){
            if (!text.equals("")){
                Toast.makeText(getContext(), "Товара с таким названием нет",
                        Toast.LENGTH_SHORT).show();
            }
            AdminAdapter adapter = new AdminAdapter(getContext(), products);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }else {
            AdminAdapter adapter = new AdminAdapter(getContext(), filteredList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}
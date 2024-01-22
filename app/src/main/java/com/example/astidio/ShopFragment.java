package com.example.astidio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ShopFragment extends Fragment {
    private FirebaseFirestore db;
    RecyclerView recyclerView;

    public ShopFragment(){super(R.layout.shop_fragment);}

    public static ShopFragment newInstance(){
        return new ShopFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.items);

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
                            ProductAdapter adapter = new ProductAdapter(getContext(), products);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });
    }

}
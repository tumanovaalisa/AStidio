package com.example.astidio;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopFragment extends Fragment {
    private FirebaseFirestore db;
    RecyclerView recyclerView;
    private SearchView searchView;
    static Button getOrder;

    public ShopFragment(){super(R.layout.shop_fragment);}

    public static ShopFragment newInstance(){
        return new ShopFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.items);
        getOrder = view.findViewById(R.id.getOrder_Btn);

        ProductAdapter adapter = new ProductAdapter(getContext(), MainActivity.products);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        getOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = "";
                double finalPrice = 0;
                Map<String, Integer> map = new HashMap<>();
                List<Product> keys = new ArrayList<Product>(CurrentUser.order.keySet());
                for (int i = 0; i < keys.size(); i ++) {
                    Product key = keys.get(i);
                    int n = CurrentUser.order.get(key);
                    map.put(key.getIdProduct(), n);
                    order += key.getNameProduct().toString() + "    -    " + n + "шт.\n";
                    finalPrice += ((key.getPriceProduct() * (100 - key.getSaleProduct()))/100) * n;
                }
                order += "\nК оплате будет - "+ finalPrice + " руб.";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                double price = finalPrice;
                builder
                        .setTitle("Подтвердите заказ следующих товаров:")
                        .setMessage(order)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = 0; i < keys.size(); i ++) {
                                    Product key = keys.get(i);
                                    int n = CurrentUser.order.get(key);
                                    DocumentReference userRef = db.collection("Products").document(key.getIdProduct());
                                    userRef.update("Amount", FieldValue.increment(-n));
                                }

                                Map<String, Object> orderUser = new HashMap<>();

                                LocalDate localDate = LocalDate.now();
                                LocalTime localTime = LocalTime.now();
                                orderUser.put("Date", localDate.toString() + " " + localTime.toString());
                                orderUser.put("IdC", CurrentUser.id);
                                orderUser.put("Price", price);
                                orderUser.put("Products", map);

                                db.collection("ShopOrders")
                                        .add(orderUser)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getContext(), "Заказ успешно оформлен!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                                CurrentUser.order.clear();
                                ProductAdapter adapter = new ProductAdapter(getContext(), MainActivity.products);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            }
                        })
                        .setNegativeButton("Отмена", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        searchView = (SearchView) view.findViewById(R.id.search_shop);
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
    }


    private void filterList(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : MainActivity.products){
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
            ProductAdapter adapter = new ProductAdapter(getContext(), MainActivity.products);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }else {
            ProductAdapter adapter = new ProductAdapter(getContext(), filteredList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}
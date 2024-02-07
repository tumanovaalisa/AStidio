package com.example.astidio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersFragment  extends Fragment {
    public OrdersFragment() {
        super(R.layout.orders_fragment);
    }

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }


    private FirebaseFirestore db;
    RecyclerView recyclerView;
    List<String> keys;
    Map<String, List<String>> usersInfo = new HashMap<>();

    ArrayList<History> histories = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.orders_list);

        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> info = new ArrayList<>();
                                String keyEmail = "";
                                info.add(document.getId().toString());
                                for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                    if (docs.getKey().equals("Email")) keyEmail = docs.getValue().toString();
                                    info.add(docs.getValue().toString());
                                }
                                usersInfo.put(keyEmail, info);
                            }
                            keys = new ArrayList<String>(usersInfo.keySet());
                            getOrders();
                        }
                    }
                });
    }

    public void getOrders() {
        db.collection("ShopOrders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                History history = new History();
                                for (int i = 0; i < keys.size(); i ++){
                                    String key = keys.get(i);
                                    List<String> user = usersInfo.get(key);
                                    for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                        if (docs.getKey().equals("IdC") && docs.getValue().toString().equals(user.get(0))){
                                            history.setIdU(user.get(0));
                                            for (Map.Entry<String,Object> docs1 : document.getData().entrySet()){
                                                if (docs1.getKey().equals("IdC")) history.setIdU(docs1.getValue().toString());
                                                if (docs1.getKey().equals("Price")) history.setPrice(Double.parseDouble(docs1.getValue().toString()));
                                                if (docs1.getKey().equals("Date")) history.setDate(docs1.getValue().toString());
                                                if (docs1.getKey().equals("Products")) {
                                                    Map<String, Integer> map = (Map<String, Integer>) docs1.getValue();
                                                    history.setOrder(map);
                                                }
                                            }
                                            history.setEmail(user.get(3));
                                            history.setName(user.get(5));
                                            history.setLastname(user.get(4));
                                            histories.add(history);
                                        }
                                    }
                                }
                            }
                            OrderAdapter adapter = new OrderAdapter(histories);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });
    }
}

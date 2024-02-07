package com.example.astidio;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.SearchView;
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

public class AbonsFragment extends Fragment {
    public AbonsFragment() {
        super(R.layout.abons_fragment);
    }

    public static AbonsFragment newInstance() {
        return new AbonsFragment();
    }

    private FirebaseFirestore db;
    RecyclerView recyclerView;
    private SearchView searchView;
    List<String> keys;
    Map<String, List<String>> usersInfo = new HashMap<>();
    List<Abon> abons = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.abons_list);

        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> info = new ArrayList<>();
                                String keyEmail = "";
                                Abon abon = new Abon();
                                abon.setIdU(document.getId().toString());
                                for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                    if (docs.getKey().equals("Email")) abon.setEmail(docs.getValue().toString());
                                    if (docs.getKey().equals("Name")) abon.setName(docs.getValue().toString());
                                    if (docs.getKey().equals("Lastname")) abon.setLastname(docs.getValue().toString());
                                    if (docs.getKey().equals("Date")) abon.setDate(docs.getValue().toString());
                                }
                                abons.add(abon);
                            }
                            AbonAdapter adapter = new AbonAdapter(getContext(), abons);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });

        searchView = (SearchView) view.findViewById(R.id.search_abon);
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
        List<Abon> filteredList = new ArrayList<>();
        for (Abon abon : abons){
            if (text.toLowerCase().isEmpty()){
                filteredList.clear();
            }
            else if (!text.toLowerCase().isEmpty()){
                if (abon.getEmail().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(abon);
                }
            }
        }
        if (filteredList.isEmpty()){
            if (!text.equals("")){
                Toast.makeText(getContext(), "Клиента с такой почтой нет",
                        Toast.LENGTH_SHORT).show();
            }
            AbonAdapter adapter = new AbonAdapter(getContext(), abons);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }else {
            AbonAdapter adapter = new AbonAdapter(getContext(), filteredList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}

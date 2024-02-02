package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class HistoryFragment extends Fragment {
    public HistoryFragment() {
        super(R.layout.history_fragment);
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    ArrayList<History> histories = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.history_list);
        ImageButton Button = view.findViewById(R.id.backProfile);
        db = FirebaseFirestore.getInstance();
        db.collection("ShopOrders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                History history = new History();
                                for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                    if (docs.getKey().equals("IdC") && docs.getValue().toString().equals(CurrentUser.id)){
                                        history.setIdU(CurrentUser.id);
                                        for (Map.Entry<String,Object> docs1 : document.getData().entrySet()){
                                            if (docs1.getKey().equals("Price")) history.setPrice(Double.parseDouble(docs1.getValue().toString()));
                                            if (docs1.getKey().equals("Date")) history.setDate(docs1.getValue().toString());
                                            if (docs1.getKey().equals("Products")) {
                                                Map<String, Integer> map = (Map<String, Integer>) docs1.getValue();
                                                history.setOrder(map);
                                            }
                                        }
                                        histories.add(history);
                                    }
                                }
                            }

                            HistoryAdapter adapter = new HistoryAdapter(histories);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }
                });
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Проверяем, есть ли фрагменты в стеке
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // Удаляем текущий фрагмент из стека
                    fragmentManager.popBackStack();
                } else {
                    ProfileFragment profile = new ProfileFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fl_content, profile)
                            .commit();
                }
            }
        });
    }
}

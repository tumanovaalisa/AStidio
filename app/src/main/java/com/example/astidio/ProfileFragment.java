package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProfileFragment extends Fragment {

    CardView cardView;
    CardView cardViewMap;
    ImageButton signOut;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public ProfileFragment(){super(R.layout.personal_fragment);}

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardView = view.findViewById(R.id.toHistory);
        cardViewMap = view.findViewById(R.id.map);
        signOut = view.findViewById(R.id.signOut);
        mAuth = FirebaseAuth.getInstance();
        TextView textView = view.findViewById(R.id.dateEnd);


        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String date = "";                                for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                    if (docs.getKey().equals("Email") && docs.getValue().toString().equals(CurrentUser.email)){
                                        for (Map.Entry<String,Object> docs1 : document.getData().entrySet()){
                                            if (docs1.getKey().equals("Date"))
                                                date = docs1.getValue().toString();
                                            if (date.equals("")){
                                                textView.setText("Не приобретен");
                                            }else{
                                                textView.setText("Истекает  " + date);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Проверяем, есть ли фрагменты в стеке
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // Удаляем текущий фрагмент из стека
                    fragmentManager.popBackStack();
                } else {
                    HistoryFragment map = new HistoryFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fl_content, map)
                            .commit();
                }
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), EntryActivity.class);
                startActivity(intent);
            }
        });
        cardViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Проверяем, есть ли фрагменты в стеке
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // Удаляем текущий фрагмент из стека
                    fragmentManager.popBackStack();
                } else {
                    MapFragment map = new MapFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fl_content, map)
                            .commit();
                }
            }
        });
    }
}

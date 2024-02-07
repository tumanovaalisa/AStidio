package com.example.astidio;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AbonsFragment extends Fragment {
    public AbonsFragment() {
        super(R.layout.history_fragment);
    }

    public static AbonsFragment newInstance() {
        return new AbonsFragment();
    }

    ArrayList<History> histories = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}

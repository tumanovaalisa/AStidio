package com.example.astidio;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TeachersFragment extends Fragment {
    public TeachersFragment(){super(R.layout.teachers_list);}

    public static TeachersFragment newInstance(){
        return new TeachersFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
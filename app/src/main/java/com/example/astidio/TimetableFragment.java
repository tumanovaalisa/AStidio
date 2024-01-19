package com.example.astidio;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TimetableFragment extends Fragment {
    public TimetableFragment(){super(R.layout.timetable_list);}

    public static TimetableFragment newInstance(){
        return new TimetableFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
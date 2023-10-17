package com.example.astidio;

import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ContentFragmentReg extends Fragment {

    public ContentFragmentReg(){
        super(R.layout.reg_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button toLoginBtn = view.findViewById(R.id.back);

        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) getActivity().getWindow().findViewById(R.id.fragment_container_view_reg);
                frameLayout.setVisibility(View.INVISIBLE);

                FrameLayout frameLayout2 = (FrameLayout) getActivity().getWindow().findViewById(R.id.fragment_container_view_login);
                frameLayout2.setVisibility(View.VISIBLE);
            }
        });
    }
}
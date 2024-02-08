package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    CardView cardView;
    CardView cardViewMap;
    CardView cardViewReg;
    ImageButton signOut;
    private FirebaseAuth mAuth;

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
        cardViewReg = view.findViewById(R.id.regs);
        mAuth = FirebaseAuth.getInstance();
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
        cardViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Проверяем, есть ли фрагменты в стеке
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    // Удаляем текущий фрагмент из стека
                    fragmentManager.popBackStack();
                } else {
                    RegistrationsFragment regs = new RegistrationsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fl_content, regs)
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

package com.example.astidio;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentFragmentLogin extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    Map<String, List<String>> usersInfo = new HashMap<>();

    public ContentFragmentLogin(){
        super(R.layout.login_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CurrentUser.initialization();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = new Intent(getContext(), MainActivity.class);
        Intent intent2 = new Intent(getContext(), AdminActivity.class);

        // Проверяем, авторизован ли пользователь
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if (CurrentUser.role.equals("а")) startActivity(intent2);
            else startActivity(intent);
        }

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
                                    if (docs.getKey().equals("Name")) info.add(docs.getValue().toString());
                                    if (docs.getKey().equals("Role")) info.add(docs.getValue().toString());
                                }
                                usersInfo.put(keyEmail, info);
                            }
                        }
                    }
                });

        Button toRegBtn = view.findViewById(R.id.signup);
        Button toMainBtn = view.findViewById(R.id.signin);
        EditText emailText = view.findViewById(R.id.email_ETL);
        EditText passwText = view.findViewById(R.id.passw_ET);

        toRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) getActivity().getWindow().findViewById(R.id.fragment_container_view_login);
                frameLayout.setVisibility(View.INVISIBLE);

                FrameLayout frameLayout2 = (FrameLayout) getActivity().getWindow().findViewById(R.id.fragment_container_view_reg);
                frameLayout2.setVisibility(View.VISIBLE);
            }
        });

        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailText.getText().toString().equals("") && !passwText.getText().toString().equals("")) {
                    mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passwText.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        CurrentUser.email = emailText.getText().toString();

                                        CurrentUser.getUser(emailText.getText().toString(),
                                                usersInfo.get(emailText.getText().toString()).get(2),
                                                usersInfo.get(emailText.getText().toString()).get(1),
                                                usersInfo.get(emailText.getText().toString()).get(0));

                                        emailText.setText("");
                                        passwText.setText("");
                                        if (CurrentUser.role.equals("а")) startActivity(intent2);
                                        else startActivity(intent);
                                    } else {
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Неверный логин или пароль!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(getContext(), "Введите данные",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

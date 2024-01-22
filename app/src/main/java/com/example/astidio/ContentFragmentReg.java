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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ContentFragmentReg extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ContentFragmentReg(){
        super(R.layout.reg_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Button createAccBtn = view.findViewById(R.id.done);
        Button toLoginBtn = view.findViewById(R.id.back);
        EditText loginText = view.findViewById(R.id.login_ET);
        EditText emailText = view.findViewById(R.id.email_ET);
        EditText passwText = view.findViewById(R.id.password_ET);

        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) getActivity().getWindow().findViewById(R.id.fragment_container_view_reg);
                frameLayout.setVisibility(View.INVISIBLE);

                FrameLayout frameLayout2 = (FrameLayout) getActivity().getWindow().findViewById(R.id.fragment_container_view_login);
                frameLayout2.setVisibility(View.VISIBLE);
            }
        });

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailText.getText().toString().equals("") && !loginText.getText().toString().equals("") && !passwText.getText().toString().equals("")) {
                    mAuth.createUserWithEmailAndPassword(emailText.getText().toString(), passwText.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                                        startActivity(intent);
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        FirebaseUser currentUser = auth.getCurrentUser();
                                        String uid = currentUser.getUid();
                                        Map<String, Object> user1 = new HashMap<>();
                                        user1.put("Uid", uid);
                                        user1.put("Name", loginText.getText().toString());
                                        user1.put("Email", emailText.getText().toString());
                                        user1.put("Role", "п");

                                        db.collection("Users")
                                                .add(user1)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                    } else {
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getContext(), "Введены неверные данные!",
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
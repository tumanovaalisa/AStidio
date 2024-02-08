package com.example.astidio;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    static ArrayList<Product> products = new ArrayList<>();

    private NavigationBarView.OnItemSelectedListener mOnItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.navigation_main) {
                loadFragment(MainFragment.newInstance());
                return true;
            }else if (item.getItemId() == R.id.navigation_timetable){
                loadFragment(TimetableFragment.newInstance());
                return true;
            }else if (item.getItemId() == R.id.navigation_teachers){
                loadFragment(TeachersFragment.newInstance());
                return true;
            }else if (item.getItemId() == R.id.navigation_shop){
                loadFragment(ShopFragment.newInstance());
                return true;
            }else if (item.getItemId() == R.id.navigation_profile){
                loadFragment(ProfileFragment.newInstance());
                return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        Intent intent = new Intent(this, AdminActivity.class);
        if (CurrentUser.name.equals("")){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            CurrentUser.email = currentUser.getEmail().toString();
            db.collection("Users")
                    .whereEqualTo("Email", CurrentUser.email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    CurrentUser.id = document.getId().toString();
                                    for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                        if (docs.getKey().equals("Name")) CurrentUser.name = docs.getValue().toString();
                                    }
                                }
                                if (CurrentUser.name.equals("admin")){
                                    startActivity(intent);
                                }else{
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.fl_content, fragment);
                                    ft.commit();
                                }
                            }
                        }
                    });
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_content, fragment);
            ft.commit();
        }

    }

    public void updateDB(){
        products.clear();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = new Product();
                                String id = document.getId().toString();
                                product.setIdProduct(id);
                                for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                    if (docs.getKey().equals("Name")) product.setNameProduct(docs.getValue().toString());
                                    if (docs.getKey().equals("Price")) product.setPriceProduct(Double.parseDouble(docs.getValue().toString()));
                                    if (docs.getKey().equals("Amount")) product.setAmountProduct(Integer.parseInt(docs.getValue().toString()));
                                    if (docs.getKey().equals("Sale")) product.setSaleProduct(Integer.parseInt(docs.getValue().toString()));
                                    if (docs.getKey().equals("Photo")) product.setImgProduct(docs.getValue().toString());
                                    if (docs.getKey().equals("Description")) product.setDescriptionProduct(docs.getValue().toString());
                                }
                                products.add(product);
                            }
                            }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        updateDB();
        mAuth = FirebaseAuth.getInstance();
        loadFragment(MainFragment.newInstance());

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(mOnItemSelectedListener);
        loadFragment(MainFragment.newInstance());
    }
}
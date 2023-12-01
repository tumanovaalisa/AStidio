package com.example.astidio;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(mOnItemSelectedListener);
    }
}
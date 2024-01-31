package com.example.astidio;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeachersFragment extends Fragment {
    private TeacherAdapter teacherAdapter;
    private List<Teacher> teacherList;
    private FirebaseFirestore db;
    RecyclerView recyclerView;
    private List<Teacher> filteredTeacherList;
    SearchView searchView;
    public TeachersFragment(){super(R.layout.teachers_list);}

    public static TeachersFragment newInstance(){
        return new TeachersFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        teacherList = new ArrayList<>();
        teacherAdapter = new TeacherAdapter(getContext(), filteredTeacherList);
        recyclerView = view.findViewById(R.id.teachers);
        recyclerView.setAdapter(teacherAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fetchTeacherDetails();
        searchView = view.findViewById(R.id.search_teacher);
        filteredTeacherList = new ArrayList<>(teacherList);
        setupSearchView();
    }
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }
    private void filter(String query) {
        filteredTeacherList.clear();
        for (Teacher teacher : teacherList) {
            if (teacher.getDanceType().toLowerCase().contains(query.toLowerCase())) {
                filteredTeacherList.add(teacher);
            }
        }
        teacherAdapter.notifyDataSetChanged();
    }
    private void fetchTeacherDetails() {
        db.collection("Teachers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String teacherImageResId = document.getString("image");
                            String teacherName = document.getString("name");
                            String danceType = document.getString("direction");
                            Teacher teacher = new Teacher(teacherImageResId,teacherName,danceType);
                            teacherList.add(teacher);
                            filteredTeacherList.add(teacher);
                        }
                        teacherAdapter.notifyDataSetChanged();
                    }
                });
    }
}
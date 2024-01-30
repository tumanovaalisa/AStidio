package com.example.astidio;

import android.os.Bundle;
import android.view.View;
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
    public TeachersFragment(){super(R.layout.teachers_list);}

    public static TeachersFragment newInstance(){
        return new TeachersFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        teacherList = new ArrayList<>();
        teacherAdapter = new TeacherAdapter(getContext(), teacherList);
        recyclerView = view.findViewById(R.id.teachers);
        recyclerView.setAdapter(teacherAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fetchTeacherDetails();
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
                        }
                        // Уведомление адаптера о изменении набора данных
                        teacherAdapter.notifyDataSetChanged();
                    }
                });
    }
}
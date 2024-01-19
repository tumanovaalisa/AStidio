package com.example.astidio;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TimetableFragment extends Fragment {
    private TimetableAdapter timetableAdapter;
    private List<Timetable> timetableList;
    private FirebaseFirestore db;
    public TimetableFragment(){super(R.layout.timetable_list);}

    public static TimetableFragment newInstance(){
        return new TimetableFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        // Инициализация RecyclerView и адаптера
        RecyclerView recyclerView = view.findViewById(R.id.timetable);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        timetableList = new ArrayList<>();
        timetableAdapter = new TimetableAdapter(getContext(), timetableList);
        recyclerView.setAdapter(timetableAdapter);

        // Получение данных из Firestore
        fetchDataFromFirestore();
        timetableAdapter.setOnEnrolButtonClickListener(new TimetableAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Получить выбранный элемент
                Timetable selected = timetableList.get(position);
            }
        });

    }
    private void fetchDataFromFirestore() {
        // Предполагается, что у вас есть коллекция 'Timetable' в Firestore
        db.collection("Timetable")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        timetableList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Извлечение данных из коллекции Timetable
                            String date = document.getString("date");
                            String name = document.getString("name");
                            String timeEnd = document.getString("timeEnd");
                            String timeStart = document.getString("timeStart");
                            String teacherId = document.getString("teacherId");
                            String amount = document.getString("amount");

                            // Получение дополнительных данных из коллекции Teachers с использованием teacherId
                            fetchTeacherDetails(teacherId, date, name, timeEnd, timeStart,Integer.parseInt(amount));
                        }
                    }
                });
    }

    private void fetchTeacherDetails(String id, String date, String name, String timeEnd, String timeStart, int amount) {
        db.collection("Teachers")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QueryDocumentSnapshot teacherDoc = (QueryDocumentSnapshot) task.getResult();
                        if (teacherDoc.exists()) {
                            // Извлечение данных о преподавателе
                            String teacherName = teacherDoc.getString("name");
                            // Предполагается, что у вас есть поле 'imageResId' в коллекции Teachers
                            String teacherImageResId = teacherDoc.getString("image");

                            // Создание объекта Timetable и добавление его в список
                            Timetable timetable = new Timetable(date, name, timeStart, timeEnd, teacherImageResId, teacherName, amount);
                            timetableList.add(timetable);

                            // Уведомление адаптера о изменении набора данных
                            timetableAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }


}

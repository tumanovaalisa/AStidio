package com.example.astidio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistrationsFragment extends Fragment {
    private RegistrationAdapter registrationAdapter;
    private List<Registration> registrationList;
    private FirebaseFirestore db;
    public RegistrationsFragment(){super(R.layout.registrations_fragment);}

    public static RegistrationsFragment newInstance(){
        return new RegistrationsFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        // Инициализация RecyclerView и адаптера
        RecyclerView recyclerView = view.findViewById(R.id.timetable);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registrationList = new ArrayList<>();
        registrationAdapter = new RegistrationAdapter(getContext(), registrationList);
        recyclerView.setAdapter(registrationAdapter);

        // Получение данных из Firestore
        fetchDataFromFirestore();
        registrationAdapter.setOnCancelButtonClickListener(new RegistrationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Получить выбранный элемент
                toCancel(registrationList.get(position));
            }
        });

    }
    private void toCancel(Registration registration){

    }
    private void fetchDataFromFirestore() {
        Date currentDate = new Date();
        // Форматирование даты
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        db.collection("Timetable")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registrationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Извлечение данных из коллекции Timetable
                            try {
                                String date = document.getString("date");
                                Date date1 = sdf.parse(date);
                                // Создание объекта Timetable и добавление его в список
                                if (date1.after(currentDate)) {
                                    String name = document.getString("name");
                                    String timeEnd = document.getString("timeEnd");
                                    String timeStart = document.getString("timeStart");
                                    String teacherId = document.getString("teacherId");

                                    // Получение дополнительных данных из коллекции Teachers с использованием teacherId
                                    fetchTeacherDetails(teacherId, document.getId(), date, name, timeEnd, timeStart);
                                }
                            } catch (ParseException e) {
                                // Обработка исключения, например, вывод сообщения об ошибке
                                System.err.println("Ошибка при обработке даты");
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void fetchTeacherDetails(String id, String idT, String date, String name, String timeEnd, String timeStart) {
        db.collection("Teachers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Извлечение данных из коллекции
                            if (document.getId().equals(id)){
                                String teacherImageResId = document.getString("image");
                                String teacherName = document.getString("name");
                                Registration registration = new Registration(idT, date, name, timeStart, timeEnd, teacherImageResId, teacherName);
                                registrationList.add(registration);
                            }
                        }
                        // Уведомление адаптера о изменении набора данных
                        registrationAdapter.notifyDataSetChanged();
                    }
                });
    }
}

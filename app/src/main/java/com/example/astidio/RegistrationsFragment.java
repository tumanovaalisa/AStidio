package com.example.astidio;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
    private List<Registration> registrationListCopy;
    private List<Regs> regList;
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
        registrationListCopy = new ArrayList<>();
        regList = new ArrayList<>();
        registrationAdapter = new RegistrationAdapter(getContext(), registrationListCopy);
        recyclerView.setAdapter(registrationAdapter);
        UserRegistered();
        registrationAdapter.setOnCancelButtonClickListener(new RegistrationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Получить выбранный элемент
                toCancel(registrationList.get(position));
            }
        });

    }
    private void toCancel(Registration registration){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        db.collection("RegistrationsForClasses")
                .whereEqualTo("uid", currentUser.getUid()) // используйте getUid() вместо currentUser
                .whereEqualTo("idTimetable", registration.getId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String registrationId = document.getId();
                            Log.d(TAG, "Registration document id: " + registrationId);
                            DocumentReference docRef = db.collection("RegistrationsForClasses").document(registrationId);
                            // Здесь можно выполнить необходимые действия с полученным id
                            docRef.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Документ успешно удален!");
                                            Toast.makeText(getContext(), "Запись отменена!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Ошибка при удалении документа", e);
                                            Toast.makeText(getContext(), "Ошибка!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
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
    public void UserRegistered() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(regList!=null) regList.clear(); // Очищаем список перед получением новых данных
        db.collection("RegistrationsForClasses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String idTimetable = document.getString("idTimetable");
                        String date = document.getString("regDate");
                        String uid = document.getString("uid");
                        if (uid.equals(currentUser.getUid())) regList.add(new Regs(uid, idTimetable, date));
                    }
                    fetchDataFromFirestore();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error getting documents: ", e));
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
                        registrationListCopy.clear();
                        for(int i=0; i<regList.size();i++){
                            for (int j=0; j<registrationList.size();j++){
                                if(regList.get(i).getIdTimetable().equals(registrationList.get(j).getId())) {
                                    registrationListCopy.add(registrationList.get(j));
                                }
                            }
                        }
                        registrationAdapter.notifyDataSetChanged();
                    }
                });
    }
}

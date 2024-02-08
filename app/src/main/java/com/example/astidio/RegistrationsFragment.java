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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistrationsFragment extends Fragment {
    private RegistrationAdapter registrationAdapter;
    private List<Registration> registrationList;
    //private List<Registration> registrationListCopy;
    private List<Regs> regList;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    TextView ms;
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
        ms = view.findViewById(R.id.text);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registrationList = new ArrayList<>();
        //registrationListCopy = new ArrayList<>();
        regList = new ArrayList<>();
        registrationAdapter = new RegistrationAdapter(getContext(), registrationList);
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
                                            // Удалить соответствующий объект Registration из registrationList
                                            registrationList.remove(registration);
                                            if (registrationList.size()==0) ms.setVisibility(View.VISIBLE);
                                            // Уведомить адаптер об изменениях в данных
                                            registrationAdapter.notifyDataSetChanged();

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

    private void fetchDataFromFirestore(String id) {
        Date currentDate = new Date();
        // Форматирование даты
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        DocumentReference documentRef = db.collection("Timetable").document(id);

        // Получение документа по его ID
        documentRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            try {
                                String date = documentSnapshot.getString("date");
                                Date date1 = sdf.parse(date);
                                //Date cur = sdf.parse(String.valueOf(currentDate));
                                // Создание объекта Timetable и добавление его в список
                                //if (date1.after(cur)||date1.equals(cur) ){
                                String name = documentSnapshot.getString("name");
                                String timeEnd = documentSnapshot.getString("timeEnd");
                                String timeStart = documentSnapshot.getString("timeStart");
                                String teacherId = documentSnapshot.getString("teacherId");

                                // Получение дополнительных данных из коллекции Teachers с использованием teacherId
                                fetchTeacherDetails(teacherId, documentSnapshot.getId(), date, name, timeEnd, timeStart);
                                //}
                            } catch (ParseException e) {
                                // Обработка исключения, например, вывод сообщения об ошибке
                                System.err.println("Ошибка при обработке даты");
                                e.printStackTrace();
                            }
                        } else {
                            // Обработка случая, когда документ не найден
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Обработка ошибки при получении документа
                        e.printStackTrace();
                    }
                });
    }

    public void UserRegistered() {
        if(regList!=null) regList.clear();
        if(registrationList!=null) registrationList.clear();
        CollectionReference registrationsCollectionRef = db.collection("RegistrationsForClasses");
        // Создание запроса для получения документов, где поле uid равно текущему пользователю
        registrationsCollectionRef.whereEqualTo("uid", currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Документов не найдено
                            ms.setVisibility(View.VISIBLE);
                            registrationAdapter.notifyDataSetChanged();
                        } else {
                            // Обработка успешно полученных документов
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String idTimetable = document.getString("idTimetable");
                                fetchDataFromFirestore(idTimetable);
                            }
                        }
                    } else {
                        // Обработка ошибки при получении документов
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
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
                        registrationAdapter.notifyDataSetChanged();
                    }
                });
    }
}

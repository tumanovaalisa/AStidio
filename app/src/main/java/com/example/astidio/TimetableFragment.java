package com.example.astidio;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
    private TextView available;
    public TimetableFragment(){super(R.layout.timetable_list);}

    public static TimetableFragment newInstance(){
        return new TimetableFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        available = view.findViewById(R.id.seats);

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
                toEnrol(timetableList.get(position));
            }
        });

    }
    private void toEnrol(Timetable selected){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        Date currentDate = new Date();
        builder.setTitle("Подтверждение");
        builder.setMessage("Вы уверены, что хотите записаться на " + selected.getDanceName() + " в " + selected.getTimeStart() + " " + selected.getDate() + "?");

        // Добавление кнопки "Да"
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Обработка подтверждения
                DocumentReference userRef = db.collection("Timetable").document(selected.getId());
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();
                Regs regs = new Regs(currentUser.getUid(), selected.getId(), currentDate.toString());
                userRef.update("amount", FieldValue.increment(-1))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("RegistrationsForClasses")
                                        .add(regs)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getContext(), "Вы записаны, ждем вас!",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("TAG", "Ошибка при добавлении регистрации", e);
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Логирование ошибки при обновлении количества
                                Log.e("TAG", "Ошибка при обновлении количества", e);
                                // Здесь можно предпринять другие действия по обработке ошибки
                            }
                        });
            }
        });

        // Добавление кнопки "Нет"
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Обработка отмены
                dialogInterface.dismiss(); // Закрываем диалоговое окно
            }
        });

        // Отображение диалогового окна
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void fetchDataFromFirestore() {
        Date currentDate = new Date();
        // Форматирование даты
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        // Предполагается, что у вас есть коллекция 'Timetable' в Firestore
        db.collection("Timetable")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        timetableList.clear();
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
                                Long amount = document.getLong("amount");

                                // Получение дополнительных данных из коллекции Teachers с использованием teacherId
                                fetchTeacherDetails(teacherId, document.getId(), date, name, timeEnd, timeStart, Integer.parseInt(String.valueOf(amount)));
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
    private void fetchTeacherDetails(String id, String idT, String date, String name, String timeEnd, String timeStart, int amount) {
        db.collection("Teachers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Извлечение данных из коллекции
                            if (document.getId().equals(id)){
                                String teacherImageResId = document.getString("image");
                                String teacherName = document.getString("name");
                                Timetable timetable = new Timetable(idT, date, name, timeStart, timeEnd, teacherImageResId, teacherName, amount);
                                timetableList.add(timetable);
                            }
                        }
                            // Уведомление адаптера о изменении набора данных
                            timetableAdapter.notifyDataSetChanged();
                    }
                });
    }


}

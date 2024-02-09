package com.example.astidio;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.astidio.databinding.TimetableListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimetableFragment extends Fragment implements CalendarAdapter.onItemClickListener {
    private TimetableListBinding binding;
    private TimetableAdapter timetableAdapter;
    private List<Timetable> timetableList;
    private FirebaseFirestore db;
    private TextView available;
    private CalendarAdapter adapter;
    private List<Regs> registrationList = new ArrayList<>();
    private ArrayList<CalendarDateModel> calendarList2;
    private ArrayList<Date> dates;
    private Calendar cal = Calendar.getInstance(new Locale("ru", "RU"));
    private Calendar currentDate = Calendar.getInstance(new Locale("ru", "RU"));
    private SimpleDateFormat sdf = new SimpleDateFormat("LLLL yyyy", new Locale("ru", "RU"));


    public TimetableFragment(){super(R.layout.timetable_list);}

    public static TimetableFragment newInstance(){
        return new TimetableFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TimetableListBinding.inflate(inflater, container, false);
        setUpAdapter();

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar futureMonths = Calendar.getInstance(Locale.ENGLISH);
                futureMonths.add(Calendar.MONTH, 1);
                if (cal.compareTo(currentDate) >= 0 && cal.compareTo(futureMonths) < 0) {
                    cal.add(Calendar.MONTH, 1);
                    setUpCalendar();
                }
            }
        });

        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal.compareTo(currentDate) > 0) {
                    cal.add(Calendar.MONTH, -1);
                    if (cal.compareTo(currentDate) == 0) {
                        setUpCalendar();
                    } else {
                        setUpCalendar();
                    }
                }
            }
        });
        setUpCalendar();
        return binding.getRoot();
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
                isUserAlreadyRegistered(timetableList.get(position));
            }
        });


    }

    private void setUpAdapter() {
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.DateRecyclerView);
        adapter = new CalendarAdapter() {
            @Override
            public void onItemClick(CalendarDateModel model, int position) {
                for (int index = 0; index < calendarList2.size(); index++) {
                    CalendarDateModel calendarModel = calendarList2.get(index);
                    calendarModel.setSelected(index == position);
                }

                adapter.setData(calendarList2);
                onItemClick(model, position);
            }
        };

        binding.DateRecyclerView.setAdapter(adapter);

        // Установка слушателя кликов для адаптера
        adapter.setOnItemClickListener(this);
    }

    public void isUserAlreadyRegistered(Timetable timetable) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(registrationList!=null) registrationList.clear(); // Очищаем список перед получением новых данных
        db.collection("RegistrationsForClasses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String idTimetable = document.getString("idTimetable");
                        String date = document.getString("regDate");
                        String uid = document.getString("uid");
                        registrationList.add(new Regs(uid, idTimetable, date));
                    }
                    boolean isRegistered = checkIfRegistered(currentUser.getUid(), timetable.getId());
                    if (isRegistered) {
                        // Показываем предупреждение, что пользователь уже записан
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Предупреждение");
                        builder.setMessage("Вы уже записаны на это занятие!");

                        // Добавляем кнопку "Ок"
                        builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Закрываем диалоговое окно
                                dialog.dismiss();
                            }
                        });

                        // Отображаем диалоговое окно
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        toEnrol(timetable);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error getting documents: ", e));
    }

    private boolean checkIfRegistered(String userId, String timetableId) {
        for (Regs registration : registrationList) {
            if (timetableId.equals(registration.getIdTimetable()) && userId.equals(registration.getUid())) {
                return true;
            }
        }
        return false;
    }

    private void toEnrol(Timetable selected){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
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
                                                    fetchDataFromFirestore();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("TAG", "Ошибка при записи", e);
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
                                // Проверка, является ли дата сегодняшней
                                if (isToday(date1)) {
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
    private void fetchDataFromFirestore1(String date2) {
        timetableList.clear();
        db.collection("Timetable")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        timetableList.clear();
                        int i = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                                String date = document.getString("date");
                                // Проверка, является ли дата сегодняшней
                                if (date.equals(date2)) {
                                    String name = document.getString("name");
                                    String timeEnd = document.getString("timeEnd");
                                    String timeStart = document.getString("timeStart");
                                    String teacherId = document.getString("teacherId");
                                    Long amount = document.getLong("amount");
                                    i = 1;

                                    // Получение дополнительных данных из коллекции Teachers с использованием teacherId
                                    fetchTeacherDetails(teacherId, document.getId(), date, name, timeEnd, timeStart, Integer.parseInt(String.valueOf(amount)));
                                }
                                if(i==0) timetableAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private boolean isToday(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date);
        cal2.setTime(new Date());
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
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

    @Override
    public void onItemClick(CalendarDateModel model, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String selectedDate = sdf.format(model.getData());
        fetchDataFromFirestore1(selectedDate);
    }


    private void setUpCalendar() {
        ArrayList<CalendarDateModel> calendarList = new ArrayList<>();
        binding.textDateMonth.setText(sdf.format(cal.getTime()));
        Calendar monthCalendar = (Calendar) cal.clone();
        int maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Инициализация списка дат
        dates = new ArrayList<>();
        // Инициализация списка календарных моделей
        calendarList2 = new ArrayList<>();

        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        Calendar currentDate = Calendar.getInstance();
        currentDate.clear(Calendar.HOUR_OF_DAY);
        currentDate.clear(Calendar.MINUTE);
        currentDate.clear(Calendar.SECOND);
        currentDate.clear(Calendar.MILLISECOND);

        while (dates.size() < maxDaysInMonth) {
            Date date = monthCalendar.getTime();
            if (monthCalendar.get(Calendar.MONTH) != cal.get(Calendar.MONTH)) {
                break;
            }
            if (date.compareTo(currentDate.getTime()) >= 0) {
                dates.add(date);
                calendarList.add(new CalendarDateModel(date));
            }

            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarList2.clear();
        calendarList2.addAll(calendarList);
        adapter.setOnItemClickListener(TimetableFragment.this);
        adapter.setData(calendarList);
    }


}

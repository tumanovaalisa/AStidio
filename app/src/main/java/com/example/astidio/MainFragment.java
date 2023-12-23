package com.example.astidio;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;
    public MainFragment(){super(R.layout.mainpage_fragment);}

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(getActivity(), newsList);
        recyclerView.setAdapter(newsAdapter);

        // Убедитесь, что адаптер установлен перед вызовом loadNewsDataFromFirebase()
        loadNewsDataFromFirebase();
    }
    private void loadNewsDataFromFirebase() {
        // Инициализация Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Получение данных из коллекции "users"
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        newsList.clear(); // Очистка списка перед добавлением новых данных
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Получение данных из документа и добавление в список
                            String title = document.getString("name");
                            String imageUrl = document.getString("image");
                            String description = document.getString("description");

                            News news = new News(title, imageUrl, description);
                            newsList.add(news);
                        }
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        // Обработка ошибки
                        // task.getException() содержит информацию об ошибке
                    }
                });
    }
}
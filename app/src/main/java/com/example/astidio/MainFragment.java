package com.example.astidio;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
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
        TextView textView = view.findViewById(R.id.name);
        setUserName(textView);
        loadNewsDataFromFirebase();
    }
    private void loadNewsDataFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("News")
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
                        task.getException();
                    }
                });
    }
    private void setUserName(TextView textView) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String Uid = auth.getCurrentUser().getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .document(Uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("Name");
                            textView.setText("Привет, "+name+"!");
                        } else {
                            textView.setText("Привет!");
                        }
                    })
                    .addOnFailureListener(e -> {
                        textView.setText("Error: " + e.getMessage());
                    });
        }
    }


}
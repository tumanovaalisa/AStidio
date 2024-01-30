package com.example.astidio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    // Конструктор адаптера
    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    // Создание нового элемента списка
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    // Привязка данных к элементу списка
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);

        // Устанавливаем данные в элементы интерфейса
        holder.nameTextView.setText(news.getTitle());
        Glide.with(context)
                .load(news.getImageUrl())
                .into(holder.pictureImageView);
        holder.buttonTextView.setText(news.getDescription());

    }

    // Возвращает количество элементов в списке
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ViewHolder для элемента новости
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView pictureImageView;
        TextView nameTextView;
        TextView buttonTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            pictureImageView = itemView.findViewById(R.id.picture);
            nameTextView = itemView.findViewById(R.id.name);
            buttonTextView = itemView.findViewById(R.id.button);
        }
    }
}



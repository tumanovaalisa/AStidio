package com.example.astidio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.RegistrationViewHolder> {
    private List<Registration> registrationList;
    private Context context;

    public RegistrationAdapter(Context context, List<Registration> registrationList) {
        this.context = context;
        this.registrationList = registrationList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return registrationList.size();
    }

    @NonNull
    @Override
    public RegistrationAdapter.RegistrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.registration_item, parent, false);
        return new RegistrationAdapter.RegistrationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistrationAdapter.RegistrationViewHolder holder, int position) {
        // Get the current timetable entry
        Registration registration = registrationList.get(position);
        // Set data to your views
        holder.danceNameTextView.setText(registration.getDanceName());
        String time = registration.getTimeStart() + " - " + registration.getTimeEnd() + " ~ " + registration.getDate();
        holder.timeInfoTextView.setText(time);
        Glide.with(context)
                .load(registration.getTeacherImageResId()) // Замените на ваш путь к изображению
                .into(holder.teacherImageView);// Замените на ваш способ загрузки изображения
        holder.teacherNameTextView.setText(registration.getTeacherName());
        // Обработка события кнопки enrol
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (cancelButtonClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    cancelButtonClickListener.onItemClick(adapterPosition);
                }
            }
        });
    }
    private RegistrationAdapter.OnItemClickListener cancelButtonClickListener;
    public void setOnCancelButtonClickListener(RegistrationAdapter.OnItemClickListener listener) {
        this.cancelButtonClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class RegistrationViewHolder extends RecyclerView.ViewHolder{
        TextView danceNameTextView;
        TextView timeInfoTextView;
        ImageView teacherImageView;
        TextView teacherNameTextView;
        Button cancelButton;
        public RegistrationViewHolder(@NonNull View itemView) {
            super(itemView);
            danceNameTextView = itemView.findViewById(R.id.dance_TV);
            timeInfoTextView = itemView.findViewById(R.id.time_TV);
            teacherImageView = itemView.findViewById(R.id.img_teacher);
            teacherNameTextView = itemView.findViewById(R.id.name_TV);
            cancelButton = itemView.findViewById(R.id.enrol);
        }
    }

}

package com.example.astidio;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {
    private List<Timetable> timetableList;
    private Context context;

    public TimetableAdapter(Context context, List<Timetable> timetableList) {
        this.context = context;
        this.timetableList = timetableList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return timetableList.size();
    }

    @NonNull
    @Override
    public TimetableAdapter.TimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timetable_item, parent, false);
        return new TimetableAdapter.TimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableAdapter.TimetableViewHolder holder, int position) {
        // Get the current timetable entry
        Timetable timetable = timetableList.get(position);
        // Set data to your views
        holder.danceNameTextView.setText(timetable.getDanceName());
        String time = timetable.getTimeStart() + " - " + timetable.getTimeEnd() + " ~ " + timetable.getDate();
        holder.timeInfoTextView.setText(time);
        Glide.with(context)
                .load(timetable.getTeacherImageResId()) // Замените на ваш путь к изображению
                .into(holder.teacherImageView);// Замените на ваш способ загрузки изображения
        holder.teacherNameTextView.setText(timetable.getTeacherName());
        holder.availableSeatsTextView.setText(String.valueOf(timetable.getAvailableSeats()));
        // Обработка события кнопки enrol
        if (timetable.getAvailableSeats()<1){
            holder.enrolButton.setEnabled(false);
            holder.enrolButton.setText("мест нет");
            holder.enrolButton.setTextSize(10);
        }  else {
            holder.enrolButton.setEnabled(true);
            holder.enrolButton.setText("иду!");
            holder.enrolButton.setTextSize(12);
        }
        holder.enrolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (enrolButtonClickListener != null && adapterPosition != RecyclerView.NO_POSITION) {
                    enrolButtonClickListener.onItemClick(adapterPosition);
                }
            }
        });
    }
    public void setData(List<Timetable> newData) {
        timetableList.clear();
        timetableList.addAll(newData);
        notifyDataSetChanged(); // Обновляем RecyclerView
    }
    private TimetableAdapter.OnItemClickListener enrolButtonClickListener;
    public void setOnEnrolButtonClickListener(TimetableAdapter.OnItemClickListener listener) {
        this.enrolButtonClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class TimetableViewHolder extends RecyclerView.ViewHolder{
        TextView danceNameTextView;
        TextView timeInfoTextView;
        ImageView teacherImageView;
        TextView teacherNameTextView;
        TextView availableSeatsTextView;
        Button enrolButton;
        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            danceNameTextView = itemView.findViewById(R.id.dance_TV);
            timeInfoTextView = itemView.findViewById(R.id.time_TV);
            teacherImageView = itemView.findViewById(R.id.img_teacher);
            teacherNameTextView = itemView.findViewById(R.id.name_TV);
            availableSeatsTextView = itemView.findViewById(R.id.seats);
            enrolButton = itemView.findViewById(R.id.enrol);
        }
    }

}

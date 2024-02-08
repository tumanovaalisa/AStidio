package com.example.astidio;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private ArrayList<CalendarDateModel> list = new ArrayList<>();
    private int adapterPosition = 0;
    private onItemClickListener mListener;

    public abstract void onItemClick(CalendarDateModel model, int position);

    public interface onItemClickListener {
        void onItemClick(CalendarDateModel model, int position);
    }


    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.date_layout, parent, false);
        return new CalendarViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CalendarDateModel itemList = list.get(position);
        holder.calendarDay.setText(itemList.getCalendarDay());
        holder.calendarDate.setText(itemList.getCalendarDate());


        holder.itemView.setOnClickListener(view -> {
            adapterPosition = position;
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.onItemClick(itemList, position); // Уведомить слушателя о щелчке на элементе
            }
        });

        Context context = holder.itemView.getContext();
        Drawable backgroundDrawable;
        int textColor;

        if (position == adapterPosition) {
            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_fill);
            textColor = ContextCompat.getColor(context, R.color.white);
        } else {
            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.rectangle_outline);
            textColor = ContextCompat.getColor(context, R.color.purple);
        }

        holder.linear.setBackground(backgroundDrawable);
        holder.calendarDay.setTextColor(textColor);
        holder.calendarDate.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<CalendarDateModel> calendarList) {
        list.clear();
        list.addAll(calendarList);
        notifyDataSetChanged();
    }



    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView calendarDay;
        TextView calendarDate;
        LinearLayout linear;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            calendarDay = itemView.findViewById(R.id.tv_calendar_day);
            calendarDate = itemView.findViewById(R.id.tv_calendar_date);
            linear = itemView.findViewById(R.id.linear_calendar);
        }
    }
}

package com.example.astidio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class TimetableAdapter extends BaseAdapter {
    private List<Timetable> timetableList;
    private Context context;

    public TimetableAdapter(Context context, List<Timetable> timetableList) {
        this.context = context;
        this.timetableList = timetableList;
    }

    @Override
    public int getCount() {
        return timetableList.size();
    }

    @Override
    public Object getItem(int position) {
        return timetableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // Inflate the layout for each list item
            convertView = LayoutInflater.from(context).inflate(R.layout.timetable_item, parent, false);

            // Create a ViewHolder to hold references to your views
            holder = new ViewHolder();
            holder.danceNameTextView = convertView.findViewById(R.id.dance_TV);
            holder.timeInfoTextView = convertView.findViewById(R.id.time_TV);
            holder.teacherImageView = convertView.findViewById(R.id.img_teacher);
            holder.teacherNameTextView = convertView.findViewById(R.id.name_TV);
            holder.availableSeatsTextView = convertView.findViewById(R.id.seats);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current timetable entry
        Timetable timetable = timetableList.get(position);

        // Set data to your views
        holder.danceNameTextView.setText(timetable.getDanceName());
        holder.timeInfoTextView.setText(timetable.getTimeInfo());
        holder.teacherImageView.setImageResource(timetable.getTeacherImageResId());
        holder.teacherNameTextView.setText(timetable.getTeacherName());
        holder.availableSeatsTextView.setText(String.valueOf(timetable.getAvailableSeats()));

        return convertView;
    }

    private static class ViewHolder {
        TextView danceNameTextView;
        TextView timeInfoTextView;
        ImageView teacherImageView;
        TextView teacherNameTextView;
        TextView availableSeatsTextView;
    }
}

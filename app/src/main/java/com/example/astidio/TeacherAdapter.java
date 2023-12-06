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

public class TeacherAdapter extends BaseAdapter {
    private List<Teacher> teacherList;
    private Context context;

    public TeacherAdapter(Context context, List<Teacher> teacherList) {
        this.context = context;
        this.teacherList = teacherList;
    }

    @Override
    public int getCount() {
        return teacherList.size();
    }

    @Override
    public Object getItem(int position) {
        return teacherList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.teachers_item, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.img_IV);
            holder.nameTextView = convertView.findViewById(R.id.name_TV);
            holder.danceTypeTextView = convertView.findViewById(R.id.dance_TV);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current teacher
        Teacher teacher = teacherList.get(position);

        // Set data to your views
        holder.imageView.setImageResource(teacher.getImageResId());
        holder.nameTextView.setText(teacher.getName());
        holder.danceTypeTextView.setText(teacher.getDanceType());

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView danceTypeTextView;
    }
}

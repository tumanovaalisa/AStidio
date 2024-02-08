package com.example.astidio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AbonAdapter  extends RecyclerView.Adapter<AbonAdapter.ViewHolder> {
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView mailTextView;
        TextView dateTextView;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.FIO);
            mailTextView = (TextView) itemView.findViewById(R.id.mail);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
            button = itemView.findViewById(R.id.addTime);
        }
    }

    private List<Abon> mAbons;

    public AbonAdapter(Context context, List<Abon> abons) {
        this.context = context;
        mAbons = abons;
    }

    @Override
    public AbonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.abon_item, parent, false);

        AbonAdapter.ViewHolder viewHolder = new AbonAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AbonAdapter.ViewHolder holder, int position) {
        Abon abon = mAbons.get(position);
        TextView textView1 = holder.nameTextView;
        textView1.setText(abon.getName() + " " + abon.getLastname());
        TextView textView2 = holder.mailTextView;
        textView2.setText(abon.getEmail());
        TextView textView3 = holder.dateTextView;
        if (!abon.getDate().equals("")){
            textView3.setText("Истекает:    " + abon.getDate());
        } else {
            textView3.setText("Абонемент еще не оформлен");
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddAbonActivity.class);
                intent.putExtra("selectedId", abon.getIdU());
                intent.putExtra("selectedName", abon.getName());
                intent.putExtra("selectedLastname", abon.getLastname());
                intent.putExtra("selectedEmail", abon.getEmail());
                intent.putExtra("selectedDate", abon.getDate());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAbons.size();
    }
}
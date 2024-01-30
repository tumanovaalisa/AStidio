package com.example.astidio;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView descTextView;
        TextView amountTextView;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            priceTextView = (TextView) itemView.findViewById(R.id.price);
            descTextView = (TextView) itemView.findViewById(R.id.description);
            amountTextView = (TextView) itemView.findViewById(R.id.amount);
            button = itemView.findViewById(R.id.redact);
        }
    }

    private List<Product> mAdminList;

    public AdminAdapter(Context context, List<Product> productList) {
        this.context = context;
        mAdminList = productList;
    }

    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.admin_item, parent, false);

        ViewHolder viewHolder = new AdminAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.ViewHolder holder, int position) {
        Product product = mAdminList.get(position);
        TextView textView1 = holder.nameTextView;
        textView1.setText(product.getNameProduct());
        double finalPrice = (product.getPriceProduct() * (100 - product.getSaleProduct()))/100;
        TextView textView2 = holder.priceTextView;
        textView2.setText(String.valueOf(finalPrice) + " руб.");
        ImageView imageView = holder.imageView;
        Glide.with(context)
                .load(product.getImgProduct()) // Замените на ваш путь к изображению
                .into(imageView);// Замените на ваш способ загрузки изображения
        TextView textView3 = holder.amountTextView;
        textView3.setText("Осталось: " + String.valueOf(product.getAmountProduct()));
        TextView textView4 = holder.descTextView;
        textView4.setText(product.getDescriptionProduct());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RedactActivity.class);
                intent.putExtra("selectedId", product.getIdProduct());
                intent.putExtra("selectedName", product.getNameProduct());
                intent.putExtra("selectedPrice", Double.toString(product.getPriceProduct()));
                intent.putExtra("selectedAmount", Integer.toString(product.getAmountProduct()));
                intent.putExtra("selectedSale", Integer.toString(product.getSaleProduct()));
                intent.putExtra("selectedDescription", product.getDescriptionProduct());
                intent.putExtra("selectedImage", product.getImgProduct());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdminList.size();
    }
}

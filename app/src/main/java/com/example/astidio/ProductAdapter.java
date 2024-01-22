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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends
        RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        Button addButton;
        TextView amountTextView;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            priceTextView = (TextView) itemView.findViewById(R.id.price);
            amountTextView = (TextView) itemView.findViewById(R.id.amount);
        }
    }

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.product_item, parent, false);

        ViewHolder viewHolder = new ProductAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);
        // Set data to your views
        Glide.with(context)
                .load(product.getImgProduct()) // Замените на ваш путь к изображению
                .into(holder.imageView);// Замените на ваш способ загрузки изображения
        holder.nameTextView.setText(product.getNameProduct());
        double finalPrice = (product.getPriceProduct() * (100 - product.getSaleProduct()))/100;
        holder.priceTextView.setText(String.valueOf(finalPrice) + " руб.");
        holder.amountTextView.setText(String.valueOf(product.getAmountProduct()));
        // Set click listeners for buttons (you can use onClick attribute in XML instead)
        holder.addButton.setOnClickListener(v -> addOne(position));
        holder.deleteButton.setOnClickListener(v -> deleteOne(position));

    }

    private void addOne(int position) {

    }

    private void deleteOne(int position) {

    }
}

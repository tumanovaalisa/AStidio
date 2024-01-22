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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView descTextView;
        TextView amountTextView;
        Button addButton;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            priceTextView = (TextView) itemView.findViewById(R.id.price);
            descTextView = (TextView) itemView.findViewById(R.id.description);
            amountTextView = (TextView) itemView.findViewById(R.id.amount);
            addButton = (Button) itemView.findViewById(R.id.add_button);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
        }
    }

    private List<Product> mProductList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        mProductList = productList;
    }

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
        Product product = mProductList.get(position);
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
        textView3.setText("В наличии: " + String.valueOf(product.getAmountProduct()));
        TextView textView4 = holder.descTextView;
        textView4.setText(product.getDescriptionProduct());
        Button button1 = holder.addButton;
        Button button2 = holder.deleteButton;
        button1.setOnClickListener(v -> addOne(position));
        button2.setOnClickListener(v -> deleteOne(position));
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    private void addOne(int position) {

    }

    private void deleteOne(int position) {

    }
}

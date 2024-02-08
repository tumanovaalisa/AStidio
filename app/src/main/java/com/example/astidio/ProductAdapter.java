package com.example.astidio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        TextView nTextView;
        LinearLayout linearLayout;
        Button addButton;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            priceTextView = (TextView) itemView.findViewById(R.id.price);
            descTextView = (TextView) itemView.findViewById(R.id.description);
            amountTextView = (TextView) itemView.findViewById(R.id.amount);
            nTextView = (TextView) itemView.findViewById(R.id.n);
            linearLayout = itemView.findViewById(R.id.main);
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
        LinearLayout layout = holder.linearLayout;
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
        if (product.getAmountProduct() == 0) holder.addButton.setEnabled(false);
        TextView textView4 = holder.descTextView;
        textView4.setText(product.getDescriptionProduct());
        TextView textView5 = holder.nTextView;
        holder.deleteButton.setEnabled(false);
        if (CurrentUser.order.containsKey(product)){
            textView5.setText(CurrentUser.order.get(product).toString());
            int bg = ContextCompat.getColor(context, R.color.light_pink);
            layout.setBackgroundColor(bg);
            ShopFragment.getOrder.setVisibility(View.VISIBLE);
            holder.deleteButton.setEnabled(true);
        }
        else textView5.setText("0");


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(textView5.getText().toString());
                n++;
                CurrentUser.order.put(product, n);
                textView5.setText(Integer.toString(n));
                int bg = ContextCompat.getColor(context, R.color.light_pink);
                layout.setBackgroundColor(bg);
                if (n == product.getAmountProduct()) holder.addButton.setEnabled(false);
                holder.deleteButton.setEnabled(true);
                ShopFragment.getOrder.setVisibility(View.VISIBLE);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(textView5.getText().toString());
                if (n > 1) n--;
                else if (n == 1){
                    CurrentUser.order.remove(product);
                    n--;
                    int bg = ContextCompat.getColor(context, R.color.white);
                    layout.setBackgroundColor(bg);
                    holder.deleteButton.setEnabled(false);

                }
                if (n < product.getAmountProduct()) holder.addButton.setEnabled(true);
                if (CurrentUser.order.isEmpty()) ShopFragment.getOrder.setVisibility(View.INVISIBLE);
                textView5.setText(Integer.toString(n));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }
}

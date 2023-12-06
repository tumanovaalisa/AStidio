package com.example.astidio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);

            // Create a ViewHolder to hold references to your views
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image);
            holder.nameTextView = convertView.findViewById(R.id.name);
            holder.priceTextView = convertView.findViewById(R.id.price);
            holder.addButton = convertView.findViewById(R.id.add_button);
            holder.amountTextView = convertView.findViewById(R.id.amount);
            holder.deleteButton = convertView.findViewById(R.id.delete_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current product
        Product product = productList.get(position);

        // Set data to your views
        holder.imageView.setImageResource(product.getImageResId());
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));
        holder.amountTextView.setText(String.valueOf(product.getAmount()));

        // Set click listeners for buttons (you can use onClick attribute in XML instead)
        holder.addButton.setOnClickListener(v -> addOne(position));
        holder.deleteButton.setOnClickListener(v -> deleteOne(position));

        return convertView;
    }

    private void addOne(int position) {

    }

    private void deleteOne(int position) {

    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        Button addButton;
        TextView amountTextView;
        Button deleteButton;
    }
}

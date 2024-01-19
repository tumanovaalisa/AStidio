package com.example.astidio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdminAdapter extends BaseAdapter {
    private List<Product> productList;
    private Context context;

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView priceTextView;
        TextView descTextView;
        TextView amountTextView;
    }

    public AdminAdapter(Context context, List<Product> productList) {
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

        return convertView;
    }


}

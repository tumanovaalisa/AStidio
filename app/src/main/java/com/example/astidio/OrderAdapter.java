package com.example.astidio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends
        RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_TV;
        public TextView email_TV;
        public TextView order_TV;
        public TextView price_TV;
        public TextView date_TV;


        public ViewHolder(View itemView) {
            super(itemView);

            name_TV = (TextView) itemView.findViewById(R.id.username);
            email_TV = (TextView) itemView.findViewById(R.id.email);
            order_TV = (TextView) itemView.findViewById(R.id.order);
            price_TV = (TextView) itemView.findViewById(R.id.price);
            date_TV = (TextView) itemView.findViewById(R.id.date);
        }
    }

    private List<History> historyList;

    public OrderAdapter(List<History> histories ) {
        historyList = histories;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.order_item, parent, false);

        ViewHolder viewHolder = new OrderAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        History history = historyList.get(position);
        TextView textView1 = holder.name_TV;
        textView1.setText(history.getLastname() + "     "
                + history.getName());
        TextView textView2 = holder.email_TV;
        textView2.setText(history.getEmail());
        TextView textView3 = holder.date_TV;
        textView3.setText(history.getDate());
        TextView textView4 = holder.price_TV;
        textView4.setText(Double.toString(history.getPrice()) + " руб.");
        TextView textView = holder.order_TV;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<Product> products = new ArrayList<>();
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = new Product();
                                String id = document.getId().toString();
                                product.setIdProduct(id);
                                for(Map.Entry<String,Object> docs : document.getData().entrySet()){
                                    if (docs.getKey().equals("Name")) product.setNameProduct(docs.getValue().toString());
                                }
                                products.add(product);
                            }
                            String order = "";
                            for (Product product : products){
                                if (history.getOrder().containsKey(product.getIdProduct())){
                                    order += product.getNameProduct() + "     "
                                            + history.getOrder().get(product.getIdProduct()) + " шт.\n";
                                }
                            }
                            textView.setText(order);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}

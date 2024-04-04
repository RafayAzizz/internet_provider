package com.example.internet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.internet.R;

import java.util.ArrayList;

public class Recycler_view extends RecyclerView.Adapter<Recycler_view.recycler_holder> {
    Context context;
    ArrayList<DTO> originalList = new ArrayList<>();
    ArrayList<DTO> filteredList = new ArrayList<>();

    public Recycler_view(Context context, ArrayList<DTO> arrayList) {
        this.context = context;
        this.originalList = new ArrayList<>(arrayList);
        this.filteredList.addAll(originalList);
    }

    @NonNull
    @Override
    public recycler_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout, parent, false);
        return new recycler_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_holder holder, int position) {
        DTO customer = filteredList.get(position);
        holder.Name.setText(customer.getName());
        holder.Amount.setText(customer.getAmount());

        if (customer.isPaid()) {
            holder.paid_unpaid.setText("Paid");
        } else {
            holder.paid_unpaid.setText("UN_Paid");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Custmer_info.class);
                intent.putExtra("name",holder.Name.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            query = query.toLowerCase();
            for (DTO item : originalList) {
                if (item.getName().toLowerCase().contains(query) || item.getArea().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class recycler_holder extends RecyclerView.ViewHolder {
        TextView Name, paid_unpaid, Amount;

        public recycler_holder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name);
            paid_unpaid = itemView.findViewById(R.id.paid);
            Amount = itemView.findViewById(R.id.price);
        }
    }
}
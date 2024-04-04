package com.example.internet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycleforpayment extends RecyclerView.Adapter<Recycleforpayment.recycler_holder> {
    Context context;
    ArrayList<DTO_For_payment_list> arrayList;

    public Recycleforpayment(Context context, ArrayList<DTO_For_payment_list> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public recycler_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list, parent, false);
        return new recycler_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_holder holder, int position) {
        DTO_For_payment_list data = arrayList.get(position);
       holder.payment.setText(data.getAmount());holder.Month.setText(data.getMonth());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class recycler_holder extends RecyclerView.ViewHolder {
        TextView Month,payment;

        public recycler_holder(@NonNull View itemView) {
            super(itemView);
            Month=itemView.findViewById(R.id.month_list_show);
            payment=itemView.findViewById(R.id.Amount_list_show);

        }


    }
}

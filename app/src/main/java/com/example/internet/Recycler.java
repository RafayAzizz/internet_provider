package com.example.internet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    Context context;
    ArrayList<DTO_Expance> arrayList;

    public Recycler(Context context, ArrayList<DTO_Expance> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_layout, parent, false);
        FirebaseApp.initializeApp(context);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DTO_Expance data = arrayList.get(position);
        holder.expanse.setText(data.getExpance());
        holder.amount.setText(data.getAmount());
        holder.date.setText(data.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.delete_layout);

                TextView cancel = dialog.findViewById(R.id.cancel);
                TextView delete = dialog.findViewById(R.id.delete);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Deleting");
                        progressDialog.show();
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            DTO_Expance dataToDelete = arrayList.get(adapterPosition);

                            // Assuming you have a reference to your Firestore database
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            // Get the document ID of the item to be deleted
                            String documentId = arrayList.get(position).getDocID(); // Adjust this based on your DTO

                            // Delete the document from Firestore
                            db.collection("expenses").document(documentId)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Double total_amount = Double.parseDouble(Expance.totalamount.getText().toString());
                                            Double b = Double.parseDouble(arrayList.get(position).getAmount());
                                            total_amount = total_amount - b;
                                            Expance.totalamount.setText(String.valueOf(total_amount));
                                            arrayList.remove(position);
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                            if (progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            dialog.dismiss();
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                dialog.show();
                return true; // Return true to consume the long click event
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size(); // Return the actual size of the arrayList
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView expanse, amount, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expanse = itemView.findViewById(R.id.expanse);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);

        }
    }

}

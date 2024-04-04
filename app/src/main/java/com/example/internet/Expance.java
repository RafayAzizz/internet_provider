package com.example.internet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Expance extends AppCompatActivity {
    TextView add;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<DTO_Expance> arrayList = new ArrayList<>();
    Recycler adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static TextView totalamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expance);
        add = findViewById(R.id.expense);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        totalamount = findViewById(R.id.totalamount);

//

        layoutManager = new LinearLayoutManager(this);
        adapter = new Recycler(this, arrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        loadDataForCurrentDate();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        Dialog dialog = new Dialog(Expance.this);
        dialog.setContentView(R.layout.expanse_layout);

        EditText expenseEditText = dialog.findViewById(R.id.expanse);
        EditText priceEditText = dialog.findViewById(R.id.amount);
        Button btn = dialog.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String expenseText = expenseEditText.getText().toString();
                String priceText = priceEditText.getText().toString();

                if (!expenseText.isEmpty() && !priceText.isEmpty()) {
                    addExpense(expenseText, priceText);
                    dialog.dismiss();
                } else {
                    Toast.makeText(Expance.this, "Please Enter Expense and Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void addExpense(String expense, String amount) {
        Map<String, Object> expenseData = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
       Date date=new Date();

        int year = date.getYear() + 1900;


        expenseData.put("things", expense);
        expenseData.put("amount", amount);
        expenseData.put("Year", year);
        expenseData.put("date", currentDate); // Add current date

        double newExpenseAmount = 0.0;
        try {
            newExpenseAmount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            // Handle parsing errors if needed
        }

        double finalNewExpenseAmount = newExpenseAmount;
        db.collection("expenses")
                .add(expenseData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Update the local ArrayList with the new data
//                        arrayList.add(0, new DTO(currentDate, expense, amount,""));
                        int size = arrayList.size();
                        recyclerView.removeViewsInLayout(0,size);
                        loadDataForCurrentDate();
//                        adapter.notifyDataSetChanged();

                        // Update the total amount
                        double currentTotal = Double.parseDouble(totalamount.getText().toString());
                        double updatedTotal = currentTotal + finalNewExpenseAmount;
                        totalamount.setText(String.format(Locale.getDefault(), "%.2f", updatedTotal));

                        Toast.makeText(Expance.this, "Expense Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Expance.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadDataForCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        db.collection("expenses")
                .whereEqualTo("date", currentDate) // Filter by the current date
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear(); // Clear existing data
                            double total = 0.0; // Initialize total amount

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String things = document.getString("things");
                                String amount = document.getString("amount");
                                String docId= document.getId();

                                arrayList.add(new DTO_Expance(currentDate, things, amount,docId));

                                // Calculate total amount
                                try {
                                    double currentAmount = Double.parseDouble(amount);
                                    total += currentAmount;
                                } catch (NumberFormatException e) {
                                    // Handle parsing errors if needed
                                }
                            }

                            // Set total amount to the TextView
                            totalamount.setText(String.format(Locale.getDefault(), "%.2f", total));

                            adapter.notifyDataSetChanged(); // Notify adapter about the data change
                        } else {
                            Toast.makeText(Expance.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Expance.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}







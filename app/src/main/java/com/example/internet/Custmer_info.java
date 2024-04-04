package com.example.internet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Custmer_info extends AppCompatActivity {
EditText Amount;
TextView tv_amount;
ArrayList<DTO_For_payment_list> arrayList=new ArrayList<>();
Button submit;
RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custmer_info);
        Amount=findViewById(R.id.amount);
        rv=findViewById(R.id.lv_1);

        submit=findViewById(R.id.update);
        tv_amount=findViewById(R.id.get_amount);
        Intent intent= getIntent();String getn=intent.getStringExtra("name");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> items=new ArrayList<>();items.add("Unpaid");items.add("Paid");
        ArrayList<String> pakcage=new ArrayList<>();
        pakcage.add("2MB+Cdn");
        pakcage.add("4MB+Cdn");
        pakcage.add("6MB+Cdn");
        pakcage.add("8MB+Cdn");
        pakcage.add("10MB+Cdn");
        pakcage.add("8MB Pure Internet");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,pakcage);
        Spinner listView2 =findViewById(R.id.Package);
        listView2.setAdapter(adapter2);

//       Get  Amount from database
        db.collection("Users").document(getn)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String amount = document.getString("Total Amount");
                                tv_amount.setText(amount);
                            } else {
                                Toast.makeText(Custmer_info.this, "Document Not Exist", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Custmer_info.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//        show monthly payment List of client
        db.collection("CustmerPayment").document(getn).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();

                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String fieldName = entry.getKey();
                            Object fieldValue = entry.getValue();
                            String payment = fieldValue.toString();
                            DTO_For_payment_list dto = new DTO_For_payment_list(fieldName, payment);
                            arrayList.add(dto);
                            Recycleforpayment adapter = new Recycleforpayment(Custmer_info.this, arrayList);
                            rv.setLayoutManager(new LinearLayoutManager(Custmer_info.this));
                            rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    } else {
                        // Document doesn't exist
                    }
                } else {
                    // Handle errors
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failures
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Package=listView2.getSelectedItem().toString();
                boolean payment;
                String amountText = Amount.getText().toString();
                String abcText = tv_amount.getText().toString();

                int get1 = Integer.parseInt(amountText);
                int get2 = Integer.parseInt(abcText);
                int totalAmount = get2 - get1;
                String totalAmountString = String.valueOf(totalAmount);
                if (totalAmountString.equals("0") ){
                   payment=true;
                }
                else {
                    payment=false;
                }
                Map<String, Object> user = new HashMap<>();
                user.put("payment",payment);
                user.put("package",Package);
                user.put("Total Amount",totalAmountString);
                db.collection("Users").document(getn).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent goback=new Intent(getApplicationContext(),Home_Page.class);
                        startActivity(goback);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


                Map<String, Object> userpayment = new HashMap<>();
                LocalDate currentDate = LocalDate.now();
                Month currentMonth = currentDate.getMonth();
                String monthString = String.valueOf(currentMonth);
                userpayment.put(monthString,amountText);
                db.collection("CustmerPayment").document(getn).set(userpayment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent goback=new Intent(getApplicationContext(),Home_Page.class);
                        startActivity(goback);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


            }
        });


    }
    public boolean payment(String a){
        if (a.equals("Paid")){
            boolean ad=true;
            return ad;
        }
        else {
            return false;
        }
    }

}
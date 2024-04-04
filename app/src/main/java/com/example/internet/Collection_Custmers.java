package com.example.internet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.internet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Collection_Custmers extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ArrayList<DTO> custumerlist;
    RecyclerView collection;
    Recycler_view adapter;
    SearchView editsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_custmers);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collection=(RecyclerView)findViewById(R.id.ReView_Collection);
        custumerlist= new ArrayList<>();
        ArrayList<DTO> unpaidCustomers = new ArrayList<>();

        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    String name = document.getString("full_name");
                                    String amount = document.getString("Total Amount");
                                    boolean payment = document.getBoolean("payment");
                                    String location = document.getString("Area");

                                    custumerlist.add(new DTO(name, amount, payment, location));
                                }


                                for (DTO customer : custumerlist) {
                                    if (!customer.isPaid()) {
                                        unpaidCustomers.add(customer);
                                    }
                                }
                                adapter = new Recycler_view(Collection_Custmers.this,unpaidCustomers);
                                collection.setLayoutManager(new LinearLayoutManager(Collection_Custmers.this));
                                collection.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                // Set up RecyclerView and Adapter

                            }
                        } else {
                            // Task failed
                        }
                    }
                });
        editsearch = (SearchView) findViewById(R.id.simpleSearchView);
        editsearch.setOnQueryTextListener(Collection_Custmers.this);
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
}
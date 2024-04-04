package com.example.internet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Home_Page extends AppCompatActivity implements SearchView.OnQueryTextListener {
CardView collection_card,adduser,expance;

//For Recyclerview
    ArrayList<DTO> custumerlist;
    ArrayList<DTO> unpaidCustomers;
    RecyclerView collection;
    Recycler_view adapter;
    SearchView editsearch;



    FloatingActionButton referesh;

  int totalamount =0;
    TextView total;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        db = FirebaseFirestore.getInstance();


        collection_card = findViewById(R.id.payment);
        mAuth = FirebaseAuth.getInstance();
        String uid=mAuth.getUid();
        editsearch = (SearchView) findViewById(R.id.simpleSearchView);
        editsearch.setOnQueryTextListener(Home_Page.this);






        db = FirebaseFirestore.getInstance();
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Type");
        arrayList.add("IPTV");
        arrayList.add("Broad Band");
        arrayList.add("Fiber cable");
        ArrayList<String> packages=new ArrayList<>();
        packages.add("Package");
        packages.add("2+4MB CDN");
        packages.add("3+4MB CDN");
        packages.add("4+4MB CDN");
        packages.add("5+4MB CDN");
        ArrayList<String> IPapackages=new ArrayList<>();
        IPapackages.add("600 (1k channel) Month");
        ArrayList<String> arraytype=new ArrayList<>();

        expance = findViewById(R.id.expanse);

        expance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent intent=new Intent(Home_Page.this,Expance.class);
                  startActivity(intent);
            }
        });
          adduser=findViewById(R.id.addusers);
          LinearLayout ln=(LinearLayout)findViewById(R.id.ln1);
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ln.setVisibility(View.GONE);
                totalamount =0;

                Dialog customDialog = new Dialog(Home_Page.this);
                customDialog.setContentView(R.layout.added_user);
                customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                total=(TextView)customDialog.findViewById(R.id.total);
                total.setText("0");
                TextView service=(TextView) customDialog.findViewById(R.id.servicecharge);
                String serviceprice=service.getText().toString();
                CheckBox servicecharge=(CheckBox)customDialog.findViewById(R.id.sc);
                TextView internetcabel=(TextView) customDialog.findViewById(R.id.internetcabel);
                String internetcabelprice=internetcabel.getText().toString();
                CheckBox internet=(CheckBox)customDialog.findViewById(R.id.ic);
                TextView router=(TextView) customDialog.findViewById(R.id.routerprice);
                String routerprice=router.getText().toString();
                CheckBox routercharge=(CheckBox)customDialog.findViewById(R.id.router);
                TextView switchc=(TextView) customDialog.findViewById(R.id.switchprice);
                String switchprice=switchc.getText().toString();
                CheckBox switchcharge=(CheckBox)customDialog.findViewById(R.id.switc);
                CheckBox services=(CheckBox)customDialog.findViewById(R.id.check);
                LinearLayout serviceb=(LinearLayout)customDialog.findViewById(R.id.servicess);
                serviceb.setVisibility(View.GONE);
                Spinner type=(Spinner)customDialog.findViewById(R.id.spiner1);
                Spinner pack=(Spinner)customDialog.findViewById(R.id.spiner2);
                ArrayAdapter arrayAdapter = new ArrayAdapter<>(Home_Page.this, android.R.layout.simple_spinner_item, arrayList);
                type.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(Home_Page.this, android.R.layout.simple_spinner_item, packages);
                ArrayAdapter arrayAdapter3=new ArrayAdapter<>(Home_Page.this, android.R.layout.simple_spinner_item,IPapackages);
                ArrayAdapter arrayAdapter4=new ArrayAdapter<>(Home_Page.this, android.R.layout.simple_spinner_item,arraytype);
                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String select=type.getSelectedItem().toString();
                        if (select.equals("IPTV")) {
                            pack.setAdapter(arrayAdapter3);
                        }
                        else if (select.equals("Broad Band")||select.equals("Fiber cable")){
                            pack.setAdapter(arrayAdapter1);
                        } else if (select.equals("Type")) {
                            pack.setAdapter(arrayAdapter4);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(Home_Page.this, "Must select the type", Toast.LENGTH_SHORT).show();
                    }
                });
                services.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            if (services.isChecked()){
                                serviceb.setVisibility(View.VISIBLE);
                            }
                        }else {
                            if (!services.isChecked()){
                                serviceb.setVisibility(View.GONE);
                            }
                        }
                    }

                });
                servicecharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            double servicechargeamount=Double.parseDouble(serviceprice);
                            if (servicecharge.isChecked()) {
                                totalamount += servicechargeamount;
                            }
                        }
                        else{
                            double servicechargeamount=Double.parseDouble(serviceprice);
                            if (!servicecharge.isChecked()) {
                                totalamount -= servicechargeamount;
                            }
                        }
                        total.setText(String.valueOf(totalamount));

                    }
                });
                internet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            double internetcharge=Double.parseDouble(internetcabelprice);
                            if (internet.isChecked()) {
                                totalamount += internetcharge;
                            }
                        } else {
                            double internetcharge=Double.parseDouble(internetcabelprice);
                            if (!internet.isChecked()) {
                                totalamount -= internetcharge;
                            }
                        }
                        total.setText(String.valueOf(totalamount));
                    }
                });
                routercharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            double routerchargee=Double.parseDouble(routerprice);
                            if (routercharge.isChecked()) {
                                totalamount += routerchargee;
                            }
                        } else{
                            double routerchargee=Double.parseDouble(routerprice);
                            if (!routercharge.isChecked()) {
                                totalamount -= routerchargee;
                            }
                        }
                        total.setText(String.valueOf(totalamount));
                    }
                });
                switchcharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            double switchchargee=Double.parseDouble(switchprice);
                            if (switchcharge.isChecked()) {
                                totalamount += switchchargee;
                            }
                        } else {
                            double switchchargee=Double.parseDouble(switchprice);
                            if (!switchcharge.isChecked()) {
                                totalamount -= switchchargee;
                            }
                        }
                        total.setText(String.valueOf(totalamount));
                    }
                });
                EditText id=(EditText)customDialog.findViewById(R.id.id);
                EditText full_name=(EditText)customDialog.findViewById(R.id.full_name);
                EditText cnic=(EditText)customDialog.findViewById(R.id.CNIC);
                EditText mobile=(EditText)customDialog.findViewById(R.id.mobile_number);
                EditText address=(EditText)customDialog.findViewById(R.id.address);
                EditText areas=(EditText)customDialog.findViewById(R.id.areas);
                Button submit=(Button)customDialog.findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ids=id.getText().toString();
                        String name=full_name.getText().toString();
                        String cnics=cnic.getText().toString();
                        String mobilen=mobile.getText().toString();
                        String addres=address.getText().toString();
                        String totalp=total.getText().toString();
                        String area=areas.getText().toString();
                        String gett=type.getSelectedItem().toString();


                        if (!ids.isEmpty()&&!name.isEmpty()&&!cnics.isEmpty()&&!mobilen.isEmpty()&&!addres.isEmpty()&&!area.isEmpty()){
                            if (gett.equals("Type")){
                                Toast.makeText(Home_Page.this, "Please Select Type", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String getp = pack.getSelectedItem().toString();
                                if (getp.equals("Package")) {
                                } else {


                                    String selecte = type.getSelectedItem().toString();
                                    String spack = pack.getSelectedItem().toString();
                                    Toast.makeText(Home_Page.this, "Welcome", Toast.LENGTH_LONG).show();
                                    // Create a new user with a first and last name
                                    boolean check = true;
                                    Map<String, Object> users = new HashMap<>();

                                    users.put("id", ids);
                                    users.put("full_name", name);
                                    users.put("cnic", cnics);
                                    users.put("mobile", mobilen);
                                    users.put("address", addres);
                                    users.put("internet_type", selecte);
                                    users.put("package", spack);
                                    users.put("Total Amount", totalp);
                                    users.put("Area", area);
                                    users.put("payment", check);


                                    db.collection("Users")
                                            .document(name)
                                            .set(users)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    customDialog.dismiss();
                                                    Toast.makeText(Home_Page.this, "Data Successfully Added in Record", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    customDialog.dismiss();
                                                    Toast.makeText(Home_Page.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }


                        }else {
                            Toast.makeText(Home_Page.this, "Must Fill all Fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                total.setText("");
                customDialog.show();
                customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        ln.setVisibility(View.VISIBLE);
                    }
                });
            }



        });

        collection=(RecyclerView)findViewById(R.id.ReView_Collection);
        referesh = findViewById(R.id.referesh);



        custumerlist= new ArrayList<>();

        db.collection("Users").whereEqualTo("payment",false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getString("full_name");
                        String amount = document.getString("Total Amount");
                        boolean payment = document.getBoolean("payment");
                        String location = document.getString("Area");

                        custumerlist.add(new DTO(name, amount, payment, location));

                    }

                    adapter = new Recycler_view(Home_Page.this,custumerlist);
                    collection.setLayoutManager(new LinearLayoutManager(Home_Page.this));
                    collection.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    // Set up RecyclerView and Adapter
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Home_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        referesh.setOnClickListener(new View.OnClickListener() {
            private static final long CLICK_DELAY = 1000; // 1000 milliseconds = 1 second
            private long lastClickTime = 0;

            @Override
            public void onClick(View view) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime < CLICK_DELAY) {
                    // Ignore the click if it's too close to the previous click
                    return;
                }

                // Set the last click time to the current time
                lastClickTime = currentTime;

                try {
                    // Clear the list before fetching new data
                    custumerlist.clear();
                    adapter = new Recycler_view(Home_Page.this, custumerlist);
                    collection.setAdapter(adapter);

                    db.collection("Users").whereEqualTo("payment", false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots != null) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    String name = document.getString("full_name");
                                    String amount = document.getString("Total Amount");
                                    boolean payment = document.getBoolean("payment");
                                    String location = document.getString("Area");

                                    custumerlist.add(new DTO(name, amount, payment, location));
                                }
                                adapter = new Recycler_view(Home_Page.this, custumerlist);
                                collection.setAdapter(adapter);
                            } else {
                                Toast.makeText(Home_Page.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(Home_Page.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public boolean onQueryTextSubmit(String query) {

        return false;
    }
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }
    public void clear(){
        int size = custumerlist.size();
        if (size >0){
            for (int i=0;i<size;i++){
                custumerlist.remove(0);
            }
            adapter.notifyItemRangeRemoved(0,size);
        }
    }
}
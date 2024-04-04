package com.example.internet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_Screen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            FirebaseUser currentUser = mAuth.getCurrentUser();
               @Override
                public void run() {
                   if(currentUser != null){
                       Intent mainIntent = new Intent(Splash_Screen.this, Home_Page.class);
                       startActivity(mainIntent);
                       finish();
                   }else {
                       Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, 3000);




    }

}
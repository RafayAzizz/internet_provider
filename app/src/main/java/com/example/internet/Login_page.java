package com.example.internet;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.internet.Home_Page;
import com.example.internet.Otprecever;
import com.example.internet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class Login_page extends Fragment {
    private FirebaseAuth mAuth;
    EditText Email,password;
    Button submit;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationId;

    public Login_page() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_login_page, container, false);
        mAuth = FirebaseAuth.getInstance();
        submit=(Button) view.findViewById(R.id.LoginButton);
        Email=(EditText) view.findViewById(R.id.Email);
        password=(EditText) view.findViewById(R.id.Password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String  emailget=Email.getText().toString();
              String  Passget=password.getText().toString();
                mAuth.signInWithEmailAndPassword(emailget, Passget)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent=new Intent(getContext(), Home_Page.class);
                                    startActivity(intent);

                                } else {

                                    Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        return  view;
    }

}
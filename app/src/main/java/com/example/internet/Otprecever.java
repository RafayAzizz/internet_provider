package com.example.internet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otprecever extends Fragment {
EditText getopt;
Button otpcheck;
private   String number;
private   String otpid;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View view=inflater.inflate(R.layout.fragment_otprecever, container, false);
        Bundle args = getArguments();
        if (args != null) {number = args.getString("countryCode");}
        getopt=(EditText) view.findViewById(R.id.otpget);
 otpcheck=(Button) view.findViewById(R.id.LoginButton);
        
        initlionotp();
        otpcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (getopt.getText().toString().isEmpty())
               {
                   Toast.makeText(getContext(), "Blank failed not Acepet", Toast.LENGTH_SHORT).show();
               }
               else if(getopt.getText().toString().length()!=6){
                   Toast.makeText(getContext(), "INVAlID OTP", Toast.LENGTH_SHORT).show();
               }
               else {
                   PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,getopt.getText().toString());
                   signInWithPhoneAuthCredential(credential);
               }
            }
        });


        return view;
    }

    private void initlionotp() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // (optional) Activity for callback binding
                        .setCallbacks(  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid=s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("error",e.getMessage());
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           Intent intent=new Intent(getContext(), Home_Page.class);
                           startActivity(intent);

                       }
                       else {
                           Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }
}
package com.codeinfinity.homescope;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {

    EditText otpbox;
    EditText number;
    Button button, send;

    LinearLayout box;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String phonenumber, otpid, phfinal;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getInstance().getReference("usersDetails");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);
        mAuth = FirebaseAuth.getInstance();
        number = findViewById(R.id.mobilenumber);
        otpbox = findViewById(R.id.otpet);
        button = findViewById(R.id.verifyBtn);

        send = findViewById(R.id.sendOtp);
        box = findViewById(R.id.otpLayout);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateotp();
                box.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpbox.getText().toString().isEmpty()) {
                    Toast.makeText(OTPActivity.this, "Blank field cannot be processed", Toast.LENGTH_SHORT).show();
                } else if (otpbox.getText().toString().length() != 6) {
                    Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otpbox.getText().toString());
                    //signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void initiateotp() {
        phonenumber = number.getText().toString();
        phfinal = "+91" + phonenumber;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phfinal)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();

                                rootRef.child(uid).child("phone").child(phfinal);

                                Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                intent.putExtra("phone", phfinal);
                                startActivity(intent);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}

package com.codeinfinity.homescope;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextView login;
    Button register;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

//    String currentUserId = user.getUid();


    TextView what;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getInstance().getReference();
    EditText email, password, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.btnRegister);
        email = findViewById(R.id.emailEditRegister);
        password = findViewById(R.id.passEditRegister);
        name = findViewById(R.id.NameEditRegister);
        login = findViewById(R.id.loginText);






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valdateDetails();
            }
        });

    }
    public void valdateDetails(){
        String names = Objects.requireNonNull(name.getText()).toString();
        String emails = email.getText().toString();
        String passwords = password.getText().toString();
        if (names.isEmpty()){
            name.setError("Name cannot be empty");
        }else if(!emails.contains("@gmail.com")){
            email.setError("Enter a valid email!");
        } else if (passwords.length() < 6) {
            password.setError("Enter must contain atleast 6 characters!");
        } else{
            registerUser();

        }
    }

    public void registerUser(){
        String emailr = email.getText().toString();
        String passr = password.getText().toString();
        mAuth.createUserWithEmailAndPassword(emailr, passr)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            uploadDetails();


                        } else {
                           // Toast.makeText((Context) RegisterActivity.this, (CharSequence) task.getException(),
                             //       Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    public void uploadDetails() {

        String namer = name.getText().toString();
        String emailr = email.getText().toString();
        String passr = Objects.requireNonNull(password.getText()).toString();
        FirebaseUser user = mAuth.getCurrentUser();
        String uids = user.getUid();



        RegisterDetails registerDetails = new RegisterDetails(namer, emailr, passr, uids);
        rootRef.child("usersDetails").child(uids).setValue(registerDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    //Toast.makeText(RegisterActivity.this, "Details saved", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        });

       // DatabaseReference userRef = database.getReference("users");

        //DatabaseReference favRef = database.getReference("favorites");

        //favRef.setValue(uids);




        //DatabaseReference profileRef = database.getReference("profile");

       // DatabaseReference uidRef = database.getReference("Uids");







    }

}
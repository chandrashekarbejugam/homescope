package com.codeinfinity.homescope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendRequestActivity extends AppCompatActivity {

    TextView flatName;
    EditText name, number;
    Button send;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String currentUserId = user.getUid();

    String nameProperty, priceProperty, uids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);


        flatName = findViewById(R.id.nameHomeReq);
        name = findViewById(R.id.userNameReq);
        number = findViewById(R.id.userMobile);
        send = findViewById(R.id.senReqBtn);


        nameProperty = getIntent().getStringExtra("homeName");
        priceProperty = getIntent().getStringExtra("homePrice");
        uids = getIntent().getStringExtra("sellerUid");

        flatName.setText(nameProperty);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (name.getText().toString().isEmpty()){
                    name.setError("Name cant be empty");
                }else if (!(number.getText().toString().length() == 10)){
                    number.setError("Enter a valid mobile number");
                }else {
                    DatabaseReference sellers = FirebaseDatabase.getInstance().getReference("sellers").child(uids);
                    Property property = new Property();
                    property.setName(nameProperty);
                    property.setPrice(priceProperty);
                    property.setUserName(name.getText().toString());
                    property.setUserNumber(number.getText().toString());
                    property.setUserUid(currentUserId);



                    DatabaseReference propertyRef = sellers.child("requests").push();
                    propertyRef.setValue(property).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SendRequestActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SendRequestActivity.this, "request failed", Toast.LENGTH_SHORT).show();
                        }
                    });



                    DatabaseReference NotifyRef = FirebaseDatabase.getInstance().getReference("userRequests").child("requests").child(currentUserId).push();
                    NotifyRef.setValue(property).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            startActivity(new Intent(SendRequestActivity.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SendRequestActivity.this, "error occured", Toast.LENGTH_SHORT).show();
                        }
                    });



                }

            }
        });






    }
}
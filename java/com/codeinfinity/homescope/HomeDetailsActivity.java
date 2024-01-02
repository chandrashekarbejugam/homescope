package com.codeinfinity.homescope;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeDetailsActivity extends AppCompatActivity {

    private String propertyName, uid;
    public String priceTextView;

    public String descriptionTextView;
    public String addressTextView;

    public String homeAreas;
    public String beds;
    public String baths, facilities, dates;
    String living;
    String imagePath;

    Button bookFlat;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String currentUserId = user.getUid();

    public ImageView imageView;
    ProgressBar progressBar;
    TextView name, desc, address, price, area, bed, bath, livingRoom, facility, nameUser, dateUpload, sent;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_details);


        name = findViewById(R.id.nameHome);
        image = findViewById(R.id.homeDetailsImage);
        desc = findViewById(R.id.descriptionHome);
        address = findViewById(R.id.addressFlat);
        price = findViewById(R.id.priceHome);
        area = findViewById(R.id.areaValue);
        bed = findViewById(R.id.bedNoTxt);
        bath = findViewById(R.id.bathNoTxt);
        livingRoom = findViewById(R.id.livingNoTxt);
        facility = findViewById(R.id.facilitiesText);
        nameUser = findViewById(R.id.userNameText);
        dateUpload = findViewById(R.id.dateText);
        bookFlat = findViewById(R.id.homeRegister);
        progressBar = findViewById(R.id.progress);
        sent = findViewById(R.id.sentText);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("sellerDetails").child(currentUserId).child("name");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user = snapshot.getValue(String.class);
                nameUser.setText(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent intent = getIntent();
        if (intent != null) {

            propertyName = intent.getStringExtra("property_name");
            priceTextView = intent.getStringExtra("property_price");
            descriptionTextView = intent.getStringExtra("property_description");
            addressTextView = intent.getStringExtra("property_address");
            homeAreas = intent.getStringExtra("property_area");
            beds = intent.getStringExtra("property_beds");
            baths = intent.getStringExtra("property_baths");
            living = intent.getStringExtra("property_living");
            facilities = intent.getStringExtra("property_facilities");
            imagePath = intent.getStringExtra("image_path");
            dates = intent.getStringExtra("property_date");
            uid = intent.getStringExtra("property_uid");


            Glide.with(this)
                    .load(imagePath)  //Assuming you have a getImageUrl() method in your Property class that returns the image URL
                    .into(image);


        }
        name.setText(propertyName);
        desc.setText(descriptionTextView);
        address.setText(addressTextView);
        price.setText("Rs. " + priceTextView);
        facility.setText(facilities);
        area.setText(homeAreas);
        bed.setText(beds);
        dateUpload.setText(dates);
        bath.setText(baths);
        livingRoom.setText(living);


        bookFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HomeDetailsActivity.this, SendRequestActivity.class);
                intent1.putExtra("homeName", propertyName);
                intent1.putExtra("homePrice", priceTextView);
                intent1.putExtra("sellerUid", uid);
                startActivity(intent1);




            }
        });








    }
}
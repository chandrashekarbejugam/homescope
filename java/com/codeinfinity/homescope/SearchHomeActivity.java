package com.codeinfinity.homescope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchAdapter adapter;

    private DatabaseReference propertiesRef;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();


    String uid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_home);

        recyclerView = findViewById(R.id.recyclerViewSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);


        propertiesRef = FirebaseDatabase.getInstance().getReference("properties");

        retrieveDataFromFirebase();
    }

    private void retrieveDataFromFirebase() {
        propertiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Property> propertyList = new ArrayList<>();
                for (DataSnapshot propertySnapshot : dataSnapshot.getChildren()) {
                    Property property = propertySnapshot.getValue(Property.class);
                    propertyList.add(property);
                }
                adapter.setPropertyList(propertyList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });


    }

}
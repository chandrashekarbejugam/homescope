package com.codeinfinity.homescope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyRequests extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String currentUserId = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        DatabaseReference notifyRef = FirebaseDatabase.getInstance().getReference("userRequests/requests/" + currentUserId);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutRequest);

        notifyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews(); // Clear the existing views

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    if (name != null) {
                        createCardView(name, linearLayout);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    private void createCardView(String value, LinearLayout linearLayout) {
        CardView cardView = new CardView(MyRequests.this);

        // Set the layout parameters for the CardView
        LinearLayout.LayoutParams cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                convertDpToPx(50) // Set the desired height in pixels
        );
        int margin = convertDpToPx(10); // Set the desired margin in pixels
        cardLayoutParams.setMargins(margin, margin, margin, margin);
        cardView.setLayoutParams(cardLayoutParams);

        // Set the corner radius for the CardView
        float cornerRadius = convertDpToPx(8); // Set the desired corner radius in pixels
        cardView.setRadius(cornerRadius);

        // Set the elevation for the CardView
        float elevation = convertDpToPx(4); // Set the desired elevation in pixels
        cardView.setCardElevation(elevation);

        // Set the padding for the CardView
        int padding = convertDpToPx(8); // Set the desired padding in pixels
        cardView.setContentPadding(padding, padding, padding, padding);

        // Create and configure the TextView
        TextView textView = new TextView(MyRequests.this);

        textView.setText("You have sent a request for " + value);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        // Add the TextView to the CardView
        cardView.addView(textView);

        // Add the CardView to the LinearLayout
        linearLayout.addView(cardView);
    }

    private int convertDpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);

    }
}
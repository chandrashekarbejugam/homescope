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

public class DecisionActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String currentUserId = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);

        DatabaseReference notifyRef = FirebaseDatabase.getInstance().getReference("userRequests/requests/" + currentUserId);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutDecision);




        notifyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews(); // Clear the existing views

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    String decision = childSnapshot.child("decision").getValue(String.class);

                    if (decision != null && name != null) {
                        createCardViewS(name, decision, linearLayout);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });



    }



    private void createCardViewS(String value, String decisions, LinearLayout linearLayout) {
        CardView cardView = new CardView(DecisionActivity.this);

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
        TextView textView = new TextView(DecisionActivity.this);
        textView.setText("Your request for " + value + " has been " + decisions);
        if (decisions.equals("disagreed!")){
            textView.setTextColor(Color.RED);
        }else {
            textView.setTextColor(Color.GREEN);
        }
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
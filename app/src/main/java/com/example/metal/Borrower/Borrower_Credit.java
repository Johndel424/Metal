package com.example.metal.Borrower;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Borrower_Credit extends AppCompatActivity {
    private TextView creditScoreTextView;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrower_credit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        creditScoreTextView = findViewById(R.id.credit_score_textview);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get current user's UID
        String userUid = mAuth.getCurrentUser().getUid();

        // Firebase reference to the user's creditScore
        userRef = FirebaseDatabase.getInstance().getReference("users").child(userUid);

        // Fetch creditScore from Firebase
        fetchCreditScore();
        back = findViewById(R.id.back);
        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());
    }
    private void fetchCreditScore() {
        userRef.child("creditScore").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve the creditScore value
                    int creditScore = snapshot.getValue(Integer.class);

                    // Set the creditScore in the TextView
                    creditScoreTextView.setText(String.valueOf(creditScore));
                } else {
                    // Handle if the creditScore doesn't exist
                    creditScoreTextView.setText("No credit score available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Toast.makeText(Borrower_Credit.this, "Error loading credit score", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
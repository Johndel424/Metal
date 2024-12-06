package com.example.metal.Borrower;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.Borrower.Profile_Feature.ChangePin;
import com.example.metal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Borrower_ForgotPin extends AppCompatActivity {
    private EditText editTextMother, editTextFather;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrower_forgot_pin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize EditTexts
        editTextMother = findViewById(R.id.editTextMother);
        editTextFather = findViewById(R.id.editTextFather);
        TextView buttonSaveBackup = findViewById(R.id.buttonSaveBackup);
        buttonSaveBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBackup();
            }
        });

    }
    private void saveBackup() {
        // Initialize progress dialog
        progressDialog = new ProgressDialog(Borrower_ForgotPin.this);
        progressDialog.setMessage("Checking Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Retrieve the user inputs
        String motherName = editTextMother.getText().toString().trim();
        String fatherName = editTextFather.getText().toString().trim();

        // Validate inputs
        if (motherName.isEmpty() || fatherName.isEmpty()) {
            // Show error message if either field is empty
            Toast.makeText(Borrower_ForgotPin.this, "Both Mother Name and Father Name are required.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUserRef = databaseReference.child("users").child(userUid);

        // Retrieve existing names from the database
        currentUserRef.child("motherName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String storedMotherName = snapshot.getValue(String.class);

                currentUserRef.child("fatherName").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String storedFatherName = snapshot.getValue(String.class);

                        // Dismiss progress dialog after checking
                        progressDialog.dismiss();

                        // Check if input matches stored values
                        if (motherName.equals(storedMotherName) && fatherName.equals(storedFatherName)) {
                            // Inputs match, navigate to the new page
                            Intent intent = new Intent(Borrower_ForgotPin.this, ChangePin.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show error message if the input does not match
                            Toast.makeText(Borrower_ForgotPin.this, "Mother's Name or Father's Name is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Dismiss progress dialog if there is an error
                        progressDialog.dismiss();
                        Toast.makeText(Borrower_ForgotPin.this, "Failed to retrieve father name: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Dismiss progress dialog if there is an error
                progressDialog.dismiss();
                Toast.makeText(Borrower_ForgotPin.this, "Failed to retrieve mother name: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
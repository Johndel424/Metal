package com.example.metal.Secretary.Secretary_LoanHistory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SecretaryBorrowerDetail extends AppCompatActivity {
    TextView amountCollected, formattedDateTextView,statusPaymentTextView, update, show,cancel;
    ImageView paymentProof;
    EditText amountShouldBeCollect;
    ImageButton back;
    RelativeLayout updateLayout;
    String amountPaidField;
    String userUid, loanId;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_secretary_borrower_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        updateLayout = findViewById(R.id.updateLayout);
        show = findViewById(R.id.show);
        show.setOnClickListener(v -> {
            // Check current visibility and toggle it
            if (updateLayout.getVisibility() == View.GONE) {
                updateLayout.setVisibility(View.VISIBLE); // Show the layout
            } else {
                updateLayout.setVisibility(View.GONE); // Hide the layout
            }

            // Make the 'show' button itself disappear
            show.setVisibility(View.GONE);
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            // Check current visibility and toggle it
            if (updateLayout.getVisibility() == View.VISIBLE) {
                updateLayout.setVisibility(View.GONE); // Show the layout
            } else {
                updateLayout.setVisibility(View.VISIBLE); // Hide the layout
            }

            // Make the 'show' button itself disappear
            show.setVisibility(View.VISIBLE);
        });
        update = findViewById(R.id.update);
        statusPaymentTextView = findViewById(R.id.statusPaymentTextView);
        paymentProof = findViewById(R.id.paymentProof);
        amountCollected = findViewById(R.id.amountToBeCollected);
        amountShouldBeCollect = findViewById(R.id.amountShouldBeCollect);
        back = findViewById(R.id.backSecond);
        formattedDateTextView = findViewById(R.id.formattedDateTextView);
        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        // Extract the data passed with the intent
        String formattedDate = intent.getStringExtra("formattedDate");
        String formattedPayment = intent.getStringExtra("formattedPayment");
        String proofPayment = intent.getStringExtra("proofPayment");
        String needToPay = intent.getStringExtra("needToPay");
        String nname = intent.getStringExtra("nname");
        userUid = intent.getStringExtra("userUid");
        amountPaidField = intent.getStringExtra("amountPaidbyBorrower");
        loanId = intent.getStringExtra("loanId");



        amountShouldBeCollect.setText(needToPay);
        amountCollected.setText(formattedPayment);
        formattedDateTextView.setText(formattedDate);
        statusPaymentTextView.setText(nname);
        if (nname == null || nname.isEmpty() || nname.equals("notPaid")) {
            // Hide the show button if nname is null, empty, or equals "notPaid"
            show.setVisibility(View.GONE);
        } else {
            // Show the show button if nname has a valid value
            show.setVisibility(View.VISIBLE);
            statusPaymentTextView.setText(nname);
        }


        Picasso.get().load(proofPayment).into(paymentProof);

        // Firebase reference for the user balance
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userUid);

        EditText editTextMinus = findViewById(R.id.editTextMinus);


        DatabaseReference repaymentsRef = FirebaseDatabase.getInstance().getReference("repaymentDetails").child(loanId);

//        update.setOnClickListener(v -> {
//            // Get input values
//            String addValueStr = amountShouldBeCollect.getText().toString().trim();
//            String minusValueStr = editTextMinus.getText().toString().trim();
//
//            double addValue = addValueStr.isEmpty() ? 0 : Double.parseDouble(addValueStr);
//            double minusValue = minusValueStr.isEmpty() ? 0 : Double.parseDouble(minusValueStr);
//
//            // Fetch current balance
//            userRef.child("previousLoanBalance").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        double currentBalance = snapshot.getValue(Double.class);
//
//                        // Compute new balance
//                        double newBalance = currentBalance + addValue - minusValue;
//
//                        // Update the balance in Firebase
//                        userRef.child("previousLoanBalance").setValue(newBalance)
//                                .addOnSuccessListener(aVoid ->
//                                        Toast.makeText(getApplicationContext(), "Balance updated successfully!", Toast.LENGTH_SHORT).show())
//                                .addOnFailureListener(e ->
//                                        Toast.makeText(getApplicationContext(), "Failed to update balance: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//
//                        // Update amountPaid in repayments
//                        repaymentsRef.child(amountPaidField).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                double currentAmountPaid = snapshot.exists() ? snapshot.getValue(Double.class) : 0;
//
//                                // Add minusValue to current amountPaid
//                                double updatedAmountPaid = minusValue;
//
//                                repaymentsRef.child(amountPaidField).setValue(updatedAmountPaid)
//                                        .addOnSuccessListener(aVoid -> {
//                                            Toast.makeText(getApplicationContext(), "Repayments updated successfully!", Toast.LENGTH_SHORT).show();
//                                            // Hide the updateLayout after all updates are successful
//                                            updateLayout.setVisibility(View.GONE);
//                                            finish();
//                                        })
//                                        .addOnFailureListener(e ->
//                                                Toast.makeText(getApplicationContext(), "Failed to update repayments: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Balance not found!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        });

        update.setOnClickListener(v -> {
            // Get input values
            String addValueStr = amountShouldBeCollect.getText().toString().trim();
            String minusValueStr = editTextMinus.getText().toString().trim();

            // Validation for minusValueStr
            if (minusValueStr.isEmpty() || Double.parseDouble(minusValueStr) <= 0) {
                Toast.makeText(getApplicationContext(), "New amount must be greater than 0!", Toast.LENGTH_SHORT).show();
                return; // Stop further execution if validation fails
            }

            double addValue = addValueStr.isEmpty() ? 0 : Double.parseDouble(addValueStr);
            double minusValue = Double.parseDouble(minusValueStr);

            // Compute the negative result
            double result = -(addValue - minusValue);
            double result1 = result * 1;

            // Fetch current balance
            userRef.child("previousLoanBalance").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        double currentBalance = snapshot.getValue(Double.class);

                        // Compute new balance
                        double newBalance = currentBalance + addValue - minusValue;

                        // Update the balance in Firebase
                        userRef.child("previousLoanBalance").setValue(newBalance)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(getApplicationContext(), "Balance updated successfully!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(getApplicationContext(), "Failed to update balance: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                        // Update amountPaid in repayments
                        repaymentsRef.child(amountPaidField).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double currentAmountPaid = snapshot.exists() ? snapshot.getValue(Double.class) : 0;

                                // Add minusValue to current amountPaid
                                double updatedAmountPaid = minusValue;

                                repaymentsRef.child(amountPaidField).setValue(updatedAmountPaid)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getApplicationContext(), "Repayments updated successfully!", Toast.LENGTH_SHORT).show();

                                            // Add negative result to 'payments' table
                                            DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference("payments");
                                            String timestamp = String.valueOf(System.currentTimeMillis());

                                            // Create a map to store payment details
                                            Map<String, Object> paymentDetails = new HashMap<>();
                                            paymentDetails.put("timestamp", timestamp);
                                            paymentDetails.put("amount", result1);

                                            // Save payment details to Firebase
                                            paymentRef.child(timestamp).setValue(paymentDetails)
                                                    .addOnSuccessListener(aVoid1 -> {
                                                        Toast.makeText(getApplicationContext(), "Payment logged successfully!", Toast.LENGTH_SHORT).show();
                                                        // Hide the updateLayout after all updates are successful
                                                        updateLayout.setVisibility(View.GONE);
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e ->
                                                            Toast.makeText(getApplicationContext(), "Failed to log payment: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(getApplicationContext(), "Failed to update repayments: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }

                                @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Balance not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });





    }
}
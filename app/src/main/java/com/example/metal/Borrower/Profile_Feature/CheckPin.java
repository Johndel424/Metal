package com.example.metal.Borrower.Profile_Feature;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckPin extends AppCompatActivity {
    private TextView[] pinDigits;
    private StringBuilder pin;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_pin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.back);

        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());
        pinDigits = new TextView[]{
                findViewById(R.id.pin_digit_1),
                findViewById(R.id.pin_digit_2),
                findViewById(R.id.pin_digit_3),
                findViewById(R.id.pin_digit_4)
        };
        pin = new StringBuilder();


        setupKeypad();
    }
    private void clearPinDisplay() {
        for (TextView pinDigit : pinDigits) {
            pinDigit.setText(""); // Set each TextView to an empty string
        }
    }

    private void setupKeypad() {
        int[] buttonIds = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9, R.id.button_delete
        };

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeypadButtonClick(v);
            }
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void onKeypadButtonClick(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;
            String text = button.getText().toString();

            if (text.equals("Del")) {
                if (pin.length() > 0) {
                    pin.deleteCharAt(pin.length() - 1);
                }
            } else {
                if (pin.length() < 4) {
                    pin.append(text);
                }
            }

            updatePinDisplay();
        }
    }

    private void updatePinDisplay() {
        for (int i = 0; i < pinDigits.length; i++) {
            if (i < pin.length()) {
                pinDigits[i].setText(String.valueOf(pin.charAt(i)));
            } else {
                pinDigits[i].setText("");
            }
        }

        if (pin.length() == 4) {
            onPinEntered(pin.toString());
        }
    }
    private void onPinEntered(String pin) {
        // Assuming you have already authenticated the user and have access to the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Show a progress dialog while checking the PIN
            ProgressDialog progressDialog = new ProgressDialog(CheckPin.this);
            progressDialog.setMessage("Checking PIN...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Reference to the user's data in the Firebase Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            // Retrieve the current user's PIN from the database
            userRef.child("pin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Dismiss the progress dialog
                    progressDialog.dismiss();

                    // Check if the PIN exists in the database
                    if (dataSnapshot.exists()) {
                        String storedPin = dataSnapshot.getValue(String.class); // Assuming PIN is stored as String

                        // Check if the entered PIN matches the stored PIN
                        if (storedPin != null && storedPin.equals(pin)) {
                            // PIN matches, start the new activity
                            Intent intent = new Intent(CheckPin.this, ChangePin.class); // Replace NewActivity.class with your target activity
                            startActivity(intent);
                        } else {
                            // PIN does not match
                            Toast.makeText(CheckPin.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(CheckPin.this, "PIN not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Dismiss the progress dialog on failure
                    progressDialog.dismiss();
                    Toast.makeText(CheckPin.this, "Failed to check PIN: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(CheckPin.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

}

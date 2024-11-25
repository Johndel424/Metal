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

import com.example.metal.Borrower.Home_Feature.HomeBorrower;
import com.example.metal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePin extends AppCompatActivity {
    private TextView[] pinDigits;
    private StringBuilder pin;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_pin);
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

            // Show a progress dialog while saving the PIN
            ProgressDialog progressDialog = new ProgressDialog(ChangePin.this);
            progressDialog.setMessage("Saving New PIN...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Write the PIN to the Firebase Database under the current user's UID
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            userRef.child("pin").setValue(pin)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Dismiss the progress dialog when PIN is saved successfully
                            progressDialog.dismiss();

                            Toast.makeText(ChangePin.this, "PIN reset successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePin.this, HomeBorrower.class);
                            startActivity(intent); // Start the new activity
                            finish(); // Finish the current activity
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Dismiss the progress dialog when PIN saving fails
                            progressDialog.dismiss();

                            Toast.makeText(ChangePin.this, "Failed to save PIN: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(ChangePin.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

}
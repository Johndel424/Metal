package com.example.metal.Borrower;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.example.metal.Borrower.Home_Feature.HomeBorrower;
import com.example.metal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Borrower_PinRegistration extends AppCompatActivity {
    private TextView[] pinDigits;
    private RelativeLayout pinRemember, pinn;
    private LinearLayout backUp;
    TextView pinTextView;
    private StringBuilder pin;
    TextView pin_ok_button;
    private EditText editTextMother, editTextFather;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrower_pin_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pinRemember = findViewById(R.id.pinRemember);
        pin_ok_button = findViewById(R.id.pin_ok_button);
        pinTextView = findViewById(R.id.pinTextView);
        backUp = findViewById(R.id.backUp);
        pinn = findViewById(R.id.pin);

        pinDigits = new TextView[]{
                findViewById(R.id.pin_digit_1),
                findViewById(R.id.pin_digit_2),
                findViewById(R.id.pin_digit_3),
                findViewById(R.id.pin_digit_4)
        };
        pin = new StringBuilder();

        pin_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinRemember.setVisibility(View.GONE);
                backUp.setVisibility(View.VISIBLE);
            }
        });
        // for backup of pin incase na want ireset
        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize EditTexts
        editTextMother = findViewById(R.id.editTextMother);
        editTextFather = findViewById(R.id.editTextFather);

        // Initialize Button
        TextView buttonSaveBackup = findViewById(R.id.buttonSaveBackup);
        buttonSaveBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBackup();
            }
        });
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
            ProgressDialog progressDialog = new ProgressDialog(Borrower_PinRegistration.this);
            progressDialog.setMessage("Saving PIN...");
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

                            // Show PIN in TextView
                            TextView pinTextView = findViewById(R.id.pinTextView);
                            pinTextView.setText(pin);
                            Toast.makeText(Borrower_PinRegistration.this, "PIN saved successfully", Toast.LENGTH_SHORT).show();
                            pinRemember.setVisibility(View.VISIBLE);
                            pinn.setVisibility(View.GONE);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Dismiss the progress dialog when PIN saving fails
                            progressDialog.dismiss();

                            Toast.makeText(Borrower_PinRegistration.this, "Failed to save PIN: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(Borrower_PinRegistration.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveBackup() {
        // Initialize progress dialog
        progressDialog = new ProgressDialog(Borrower_PinRegistration.this);
        progressDialog.setMessage("Saving Backup...");
        progressDialog.setCancelable(false);

        // Retrieve the user inputs
        String motherName = editTextMother.getText().toString().trim();
        String fatherName = editTextFather.getText().toString().trim();

        // Validate inputs
        if (motherName.isEmpty() || fatherName.isEmpty()) {
            // Show error message if either field is empty
            Toast.makeText(Borrower_PinRegistration.this, "Both Mother Name and Father Name are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the progress dialog
        progressDialog.show();

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a new child node under "users" with user's UID
        DatabaseReference currentUserRef = databaseReference.child("users").child(userUid);

        currentUserRef.child("motherName").setValue(motherName);
        currentUserRef.child("fatherName").setValue(fatherName, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                // Dismiss progress dialog
                progressDialog.dismiss();

                if (error == null) {
                    // Data saved successfully
                    Toast.makeText(Borrower_PinRegistration.this, "Backup saved successfully", Toast.LENGTH_SHORT).show();

                    // Create an AlertDialog
                    new AlertDialog.Builder(Borrower_PinRegistration.this)
                            .setTitle("Backup Saved")
                            .setMessage("Your backup has been saved. Click OK to continue.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Show the pinRemember view
//                                    pinRemember.setVisibility(View.VISIBLE);
                                    // Hide the backup page
                                    backUp.setVisibility(View.GONE);
                                    Intent intent = new Intent(Borrower_PinRegistration.this, HomeBorrower.class);
                                    startActivity(intent);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();

                } else {
                    // Error saving data
                    Toast.makeText(Borrower_PinRegistration.this, "Failed to save backup: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
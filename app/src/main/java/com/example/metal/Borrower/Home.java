package com.example.metal.Borrower;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.Borrower.Home_Feature.HomeBorrower;
import com.example.metal.R;
import com.example.metal.SignIn;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
public class Home extends AppCompatActivity {
    private TextView[] pinDigits;
    private StringBuilder pin;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextView nameTextView, phoneTextView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameTextView = findViewById(R.id.user);
        phoneTextView = findViewById(R.id.number);
        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and show the AlertDialog
                new AlertDialog.Builder(Home.this)
                        .setTitle("Logout Confirmation")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform logout action
                                FirebaseAuth.getInstance().signOut(); // Sign out from Firebase

                                // Redirect to login or home activity
                                Intent intent = new Intent(Home.this, SignIn.class);
                                startActivity(intent);
                                finish(); // Close the current activity
                            }
                        })
                        .setNegativeButton("No", null) // Dismiss the dialog if "No" is clicked
                        .show();
            }
        });
        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get current user's UID

            // Reference to Firebase Realtime Database "users" node
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Retrieve name and phone from Firebase Realtime Database
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get name and phone from the snapshot
                        String name = dataSnapshot.child("Name").getValue(String.class);
                        String phone = dataSnapshot.child("phoneNumber").getValue(String.class);

                        // Format the name as "Hi, User"
                        String formattedName = "Hi, " + (name != null ? name : "User");

                        // Append 'X' at the end of the phone number
                        String formattedPhone = phone + " X ";

                        // Display formatted name and phone in TextView
                        nameTextView.setText(formattedName);
                        phoneTextView.setText(formattedPhone);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors
                }
            });
        }

        TextView forgot = findViewById(R.id.forgot);
        SpannableString content = new SpannableString("Forgot PIN?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        forgot.setText(content);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the forgotPin
                Intent intent = new Intent(Home.this, Borrower_ForgotPin.class);
                startActivity(intent);
            }
        });

        final FrameLayout btn = findViewById(R.id.frameLayoutFingerprint);

        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);

        Executor executor = ContextCompat.getMainExecutor(this);

        final BiometricPrompt biometricPrompt = new BiometricPrompt(Home.this, executor, new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                // Get the current user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // Update the user's location
                    updateUserLocation(currentUser.getUid());
                } else {
                    Toast.makeText(Home.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("METAL5").setDescription("Use Your Fingerprint to Login").setNegativeButtonText("Cancel").build();
        // click finger to show the finger
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
        // no need to click finger
        biometricPrompt.authenticate(promptInfo);


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
                pinDigits[i].setText("·");  // Set the text of pinDigits[i] to an asterisk
            } else {
                pinDigits[i].setText("");  // Clear the text of pinDigits[i] if there's no corresponding character in the pin
            }
        }

        if (pin.length() == 4) {
            onPinEntered(pin.toString());
        }
    }
    private void clearPinAfterDelay() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                clearPin();
            }
        }, 1000); // 3000 milliseconds = 3 seconds
    }

    private void clearPin() {
        pin.setLength(0);  // Assuming `pin` is a StringBuilder
        updatePinDisplay();
    }

    private void onPinEntered(String pin) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            ProgressDialog progressDialog = new ProgressDialog(Home.this);
            progressDialog.setMessage("Checking PIN...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    if (dataSnapshot.exists()) {
                        String storedPin = dataSnapshot.child("pin").getValue(String.class);
                        if (storedPin != null && storedPin.equals(pin)) {
                            Toast.makeText(Home.this, "PIN is correct", Toast.LENGTH_SHORT).show();

                            // Request location and update latitude and longitude
                            updateUserLocation(userId);
                        } else {
                            Toast.makeText(Home.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                            clearPinAfterDelay();
                        }
                    } else {
                        Toast.makeText(Home.this, "User data not found", Toast.LENGTH_SHORT).show();
                        clearPinAfterDelay();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(Home.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    clearPinAfterDelay();
                }
            });

        } else {
            Toast.makeText(Home.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            clearPinAfterDelay();
        }
    }

    // Method to update the user's location in Firebase
    private void updateUserLocation(String userId) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the user's last known location
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Update the user's latitude and longitude in Firebase
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            Map<String, Object> locationUpdates = new HashMap<>();
                            locationUpdates.put("latitude", latitude);
                            locationUpdates.put("longitude", longitude);
                            userRef.updateChildren(locationUpdates)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // Proceed to the main page activity
                                            Intent intent = new Intent(Home.this, HomeBorrower.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Home.this, "Open Location and restart the app.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Home.this, "Open Location and restart the app", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Home.this, "Open Location and restart the app", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Request location permission
            requestLocationPermission();
        }
    }

    // Method to request location permission
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app requires location access to function correctly. Please grant location permission.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Request the permission
                        ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    })
                    .create()
                    .show();
        } else {
            // No explanation needed, we can request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, update location
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    updateUserLocation(currentUser.getUid());
                }
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Location permission denied. Please grant permission to use this feature.", Toast.LENGTH_SHORT).show();

                // Ask again after a delay, you can set a maximum number of attempts if needed
                requestLocationPermission();
            }
        }
    }



}
package com.example.metal.Secretary.Secretary_Approval.Secretary_AccountApproval;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.example.metal.FullScreen;
import com.example.metal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SecretaryAccountVerificationDetail extends AppCompatActivity {
    String uid,status;
    private DatabaseReference mDatabase;
    private TextView nameTextView,ageTextView,provinceTextView,cityTextView,baranggayTextView,detailedAddressTextView
            ,maritalsStatusTextView,occupationTextView,monthlyIncomeTextView,meralcoBilltView,waterBilltView,wifiBilltView,houseBilltView
            ,internationalTransfertView;
    private FirebaseAuth mAuth;
    ImageButton back;
    private ProgressDialog progressDialog;
    RelativeLayout button;
    private ImageView profile, imageView1, imageView2, imageView3, imageView4, imageView5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_secretary_account_verification_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();  // Initialize FirebaseAuth
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameTextView = findViewById(R.id.nameTextView);
        button = findViewById(R.id.status);
        profile = findViewById(R.id.profile);
        ageTextView = findViewById(R.id.ageTextView);
        provinceTextView = findViewById(R.id.provinceTextView);
        cityTextView = findViewById(R.id.cityTextView);
        baranggayTextView = findViewById(R.id.baranggayTextView);
        detailedAddressTextView = findViewById(R.id.detailedAddressTextView);
        maritalsStatusTextView = findViewById(R.id.maritalsStatusTextView);
        occupationTextView = findViewById(R.id.occupationTextView);
        monthlyIncomeTextView = findViewById(R.id.monthlyIncomeTextView);
        meralcoBilltView = findViewById(R.id.meralcoBilltView);
        waterBilltView = findViewById(R.id.waterBilltView);
        wifiBilltView = findViewById(R.id.wifiBilltView);
        houseBilltView = findViewById(R.id.houseBilltView);
        internationalTransfertView = findViewById(R.id.internationalTransfertView);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        back = findViewById(R.id.back);

        // Get the intent and extract extras
        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("uid");
            status = intent.getStringExtra("status");

            // Check the status and set the layout visibility
            if ("accepted".equals(status)||"rejected".equals(status)) {
                button.setVisibility(View.GONE); // Hide the layout if status is "yes"
            } else {
                button.setVisibility(View.VISIBLE); // Show the layout otherwise
            }
        }

        // Load user details if uid is not null
        if (uid != null) {
            loadUserDetails(uid);
        }

        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void loadUserDetails(String uid) {
        ProgressDialog progressDialog = new ProgressDialog(SecretaryAccountVerificationDetail.this);
        progressDialog.setMessage("Loading user details...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference userData = mDatabase.child("users").child(uid);
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve details
                    String profilePicUrl = dataSnapshot.child("ProfileImage").getValue(String.class);
                    String displayName = dataSnapshot.child("Name").getValue(String.class);
                    String Age = dataSnapshot.child("Age").getValue(String.class);
                    String Brgy = dataSnapshot.child("Brgy").getValue(String.class);
                    String City = dataSnapshot.child("City").getValue(String.class);
                    String MaritalStatus = dataSnapshot.child("MaritalStatus").getValue(String.class);
                    String MonthlyIncome = dataSnapshot.child("MonthlyIncome").getValue(String.class);
                    String Padala = dataSnapshot.child("Padala").getValue(String.class);
                    String Province = dataSnapshot.child("Province").getValue(String.class);
                    String Street = dataSnapshot.child("Street").getValue(String.class);
                    String Work = dataSnapshot.child("Work").getValue(String.class);
                    String InternetBill = dataSnapshot.child("InternetBill").getValue(String.class);
                    String MeralcoBill = dataSnapshot.child("MeralcoBill").getValue(String.class);
                    String WaterBill = dataSnapshot.child("WaterBill").getValue(String.class);
                    String HouseBill = dataSnapshot.child("HouseBill").getValue(String.class);

                    String image1 = dataSnapshot.child("Img Internet Bill").getValue(String.class);
                    String image2 = dataSnapshot.child("Img Meralco Bill").getValue(String.class);
                    String image3 = dataSnapshot.child("Img Permit").getValue(String.class);
                    String image4 = dataSnapshot.child("Img Valid ID").getValue(String.class);
                    String image5 = dataSnapshot.child("Img Water Bill").getValue(String.class);

                    // Set retrieved data to views
                    nameTextView.setText(displayName);
                    ageTextView.setText(Age);
                    provinceTextView.setText(Province);
                    cityTextView.setText(City);
                    baranggayTextView.setText(Brgy);
                    detailedAddressTextView.setText(Street);
                    maritalsStatusTextView.setText(MaritalStatus);
                    occupationTextView.setText(Work);
                    monthlyIncomeTextView.setText(MonthlyIncome);
                    meralcoBilltView.setText(MeralcoBill);
                    waterBilltView.setText(WaterBill);
                    wifiBilltView.setText(InternetBill);
                    houseBilltView.setText(HouseBill);
                    internationalTransfertView.setText(Padala);
                    Picasso.get().load(profilePicUrl).into(profile);

                    // Counter for loaded images
                    AtomicInteger imagesLoaded = new AtomicInteger(0);

                    // Load images with Picasso and handle clicks
                    loadImageWithClick(imageView1, image1, progressDialog, imagesLoaded);
                    loadImageWithClick(imageView2, image2, progressDialog, imagesLoaded);
                    loadImageWithClick(imageView3, image3, progressDialog, imagesLoaded);
                    loadImageWithClick(imageView4, image4, progressDialog, imagesLoaded);
                    loadImageWithClick(imageView5, image5, progressDialog, imagesLoaded);

                    // Handle accept button click
                    TextView btnAccept = findViewById(R.id.btnAccept);
                    btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Create a confirmation dialog
                            new AlertDialog.Builder(SecretaryAccountVerificationDetail.this)
                                    .setTitle("Confirm Action")
                                    .setMessage("Are you sure you want to approve this user ?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Prepare the updates
                                            Map<String, Object> updates = new HashMap<>();
                                            updates.put("accountStatus", "accepted");
                                            updates.put("creditScore", 20); // Set the default credit score here

                                            // Update verificationStatus and creditScore
                                            mDatabase.child("users").child(uid).updateChildren(updates)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(SecretaryAccountVerificationDetail.this, "User accepted and credit score updated", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SecretaryAccountVerificationDetail.this, "Failed to accept user and update credit score: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss(); // Close the dialog
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert) // Optional: Add an icon
                                    .show(); // Show the dialog
                        }
                    });

                    TextView btnReject = findViewById(R.id.btnReject);
                    btnReject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Array of rejection reasons
                            final String[] rejectionReasons = {"Incomplete documents", "Not eligible", "Verification failed", "Other"};
                            final int[] selectedReasonIndex = {0}; // Default to the first option

                            // Show AlertDialog with reasons and confirm/cancel buttons
                            AlertDialog.Builder builder = new AlertDialog.Builder(SecretaryAccountVerificationDetail.this);
                            builder.setTitle("Select Rejection Reason")
                                    .setSingleChoiceItems(rejectionReasons, selectedReasonIndex[0], new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Update the selected index
                                            selectedReasonIndex[0] = which;
                                        }
                                    })
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Get the selected reason
                                            String selectedReason = rejectionReasons[selectedReasonIndex[0]];

                                            // Update verificationStatus and rejectionReason in the database
                                            Map<String, Object> updateData = new HashMap<>();
                                            updateData.put("accountStatus", "rejected");
                                            updateData.put("rejectionReason", selectedReason);

                                            mDatabase.child("users").child(uid).updateChildren(updateData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(SecretaryAccountVerificationDetail.this, "User rejected: " + selectedReason, Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SecretaryAccountVerificationDetail.this, "Failed to reject user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Dismiss the dialog and do nothing
                                            dialog.dismiss();
                                        }
                                    });

                            // Show the dialog
                            builder.show();
                        }
                    });

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SecretaryAccountVerificationDetail.this, "User details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(SecretaryAccountVerificationDetail.this, "Failed to load user details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImageWithClick(ImageView imageView, String imageUrl, ProgressDialog progressDialog, AtomicInteger imagesLoaded) {
        Picasso.get().load(imageUrl).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                // Increment the counter when an image successfully loads
                imagesLoaded.incrementAndGet();

                // Check if all images have finished loading
                if (imagesLoaded.get() == 5) { // Adjust the count based on your number of images
                    progressDialog.dismiss(); // Dismiss the dialog after all images are loaded
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle error case if needed
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecretaryAccountVerificationDetail.this, FullScreen.class);
                intent.putExtra("imageUri", imageUrl); // Pass the clicked image URL to FullScreenActivity
                startActivity(intent);
            }
        });
    }

}
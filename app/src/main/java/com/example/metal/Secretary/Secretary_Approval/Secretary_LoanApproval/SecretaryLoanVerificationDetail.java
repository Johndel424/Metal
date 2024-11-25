package com.example.metal.Secretary.Secretary_Approval.Secretary_LoanApproval;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.example.metal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SecretaryLoanVerificationDetail extends AppCompatActivity {
    String uid,status;
    private DatabaseReference mDatabase;
    private TextView nameTextView,detailedAddressTextView,monthlyIncomeTextView,meralcoBilltView,waterBilltView,wifiBilltView,houseBilltView
            ,internationalTransfertView;
    private FirebaseAuth mAuth;
    private String selectedCollectorName;

    TextView loanAmountTextview,totalLoanAmountTextview,dailyInstallmentTextview,previousLoanTextview,initialPayoutPayoutTextview,totalPayoutTextview;
    ImageButton back;
    private ProgressDialog progressDialog;
    double previousLoanBalance;
    double loanAmount,totalLoanAmount,dailyInstallment,initialPayout ;
    RelativeLayout button;
    private ImageView profile;
    String LoanId;
    private static final double INTEREST_RATE = 0.20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_secretary_loan_verification_detail);
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
        detailedAddressTextView = findViewById(R.id.detailedAddressTextView);

        monthlyIncomeTextView = findViewById(R.id.monthlyIncomeTextView);
        meralcoBilltView = findViewById(R.id.meralcoBilltView);
        waterBilltView = findViewById(R.id.waterBilltView);
        wifiBilltView = findViewById(R.id.wifiBilltView);
        houseBilltView = findViewById(R.id.houseBilltView);
        internationalTransfertView = findViewById(R.id.internationalTransfertView);

        loanAmountTextview = findViewById(R.id.loanAmountTextview);
        totalLoanAmountTextview = findViewById(R.id.totalLoanAmountTextview);
        dailyInstallmentTextview = findViewById(R.id.dailyInstallmentTextview);
        previousLoanTextview = findViewById(R.id.previousLoanTextview);
        initialPayoutPayoutTextview = findViewById(R.id.initialPayoutPayoutTextview);

        totalPayoutTextview = findViewById(R.id.totalPayoutTextview);
        back = findViewById(R.id.back);

        // Get the intent and extract extras
        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getStringExtra("uid");
            status = intent.getStringExtra("status");
            loanAmount = intent.getDoubleExtra("loanAmount", 0.0);
            totalLoanAmount = intent.getDoubleExtra("totalLoanAmount", 0.0);
            dailyInstallment = intent.getDoubleExtra("daily", 0.0);
            initialPayout = intent.getDoubleExtra("initialPayout", 0.0);
            LoanId = intent.getStringExtra("loanId");

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
//        ProgressDialog progressDialog = new ProgressDialog(SecretaryLoanVerificationDetail.this);
//        progressDialog.setMessage("Loading loan details...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        DatabaseReference userData = mDatabase.child("users").child(uid);
        userData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve details
                    String profilePicUrl = dataSnapshot.child("ProfileImage").getValue(String.class);
                    String displayName = dataSnapshot.child("Name").getValue(String.class);
                    String Brgy = dataSnapshot.child("Brgy").getValue(String.class);
                    String City = dataSnapshot.child("City").getValue(String.class);
                    String MonthlyIncome = dataSnapshot.child("MonthlyIncome").getValue(String.class);
                    String Padala = dataSnapshot.child("Padala").getValue(String.class);
                    String Province = dataSnapshot.child("Province").getValue(String.class);
                    String Street = dataSnapshot.child("Street").getValue(String.class);
                    String InternetBill = dataSnapshot.child("InternetBill").getValue(String.class);
                    String MeralcoBill = dataSnapshot.child("MeralcoBill").getValue(String.class);
                    String WaterBill = dataSnapshot.child("WaterBill").getValue(String.class);
                    String HouseBill = dataSnapshot.child("HouseBill").getValue(String.class);

                    Double previousLoanBalanceObj = dataSnapshot.child("previousLoanBalance").getValue(Double.class);

                    if (previousLoanBalanceObj != null) {
                        previousLoanBalance = previousLoanBalanceObj;
                        // Proceed with the logic that uses previousLoanBalance
                    } else {
                        // Handle the null case, either by assigning a default value or notifying the user
                        previousLoanBalance = 0.0; // Assign a default value if needed
                        Log.e("SecretaryLoanVerificationDetail", "previousLoanBalance is null.");
                    }


                    previousLoanTextview.setText("₱ "  + previousLoanBalance);
                    loanAmountTextview.setText("₱ "  + loanAmount);
                    totalLoanAmountTextview.setText("₱ "  + totalLoanAmount);
                    dailyInstallmentTextview.setText("₱ "  + dailyInstallment);
                    initialPayoutPayoutTextview.setText("₱ "  + initialPayout);

                    // Check if previousLoanBalance is less than loanAmount
                    if (previousLoanBalance < loanAmount) {
                        // Calculate the difference
                        double difference = loanAmount - previousLoanBalance;

                        // Display a message indicating how much is needed
                        totalPayoutTextview.setText("₱ " + String.format("%.2f", difference));
                    } else if (previousLoanBalance > loanAmount) {
                        // If previousLoanBalance is greater than loanAmount
                        totalPayoutTextview.setText("Previous Balance is greater than new loan amount.");
                    } else {
                        // If they are equal
                        totalPayoutTextview.setText("Your balance matches the loan amount.");
                    }

                    String Address = Street + " "+Brgy+ " " + City + " "+ Province;
                    // Set retrieved data to views
                    nameTextView.setText(displayName);
                    detailedAddressTextView.setText(Address);
                    monthlyIncomeTextView.setText("₱ " +MonthlyIncome);
                    meralcoBilltView.setText("₱ " +MeralcoBill);
                    waterBilltView.setText("₱ " +WaterBill);
                    wifiBilltView.setText("₱ " +InternetBill);
                    houseBilltView.setText("₱ " +HouseBill);
                    internationalTransfertView.setText("₱ " +Padala);


                    Picasso.get().load(profilePicUrl).into(profile);
                    Double selectedLoanAmount = loanAmount;

                    // Handle accept button click
                    TextView btnAccept = findViewById(R.id.btnAccept);

                    btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fetchCollectors();
                        }
                    });

                    // Handle reject button click
                    TextView btnReject = findViewById(R.id.btnReject);
                    btnReject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference loansRef = FirebaseDatabase.getInstance().getReference().child("loans").child(LoanId);
                            loansRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        //Toast.makeText(getApplicationContext(), "Loan data exists in Firebase for ID: " + loanId, Toast.LENGTH_SHORT).show();

                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("status", "rejected");
                                        // Adding notification logic
                                        long timestamp = System.currentTimeMillis(); // Get current timestamp
                                        String notificationId = uid + "_" + timestamp; // Custom ID for the notification

                                        Map<String, Object> notificationData = new HashMap<>();
                                        notificationData.put("notificationId", notificationId); // Add notification ID
                                        notificationData.put("userId", uid);
                                        notificationData.put("message", "Your loan has been rejected. Apply again");
                                        notificationData.put("timestamp", timestamp);
                                        notificationData.put("remark", "unread");

                                        // Save notificationData to Firebase under 'notifications' node
                                        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("notifications");
                                        notificationsRef.child(notificationId).setValue(notificationData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                        finish();
                                        loansRef.updateChildren(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "rejected successfully!", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed to update previous loan balance and current loan: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {
                                        // Toast.makeText(getApplicationContext(), "Loan data does not exist in Firebase for ID: " + LoanId, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            DatabaseReference currentloansRef = FirebaseDatabase.getInstance().getReference().child("loan_application").child(LoanId);
                            currentloansRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(getApplicationContext(), "Current loan data exists in Firebase for ID: " + LoanId, Toast.LENGTH_SHORT).show();

                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("status", "rejected");

                                        currentloansRef.updateChildren(updates)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Previous loan balance and current loan updated successfully!", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Failed to update previous loan balance and current loan: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {
                                        //  Toast.makeText(getApplicationContext(), "Current loan data does not exist in Firebase for ID: " + LoanId, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });



                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SecretaryLoanVerificationDetail.this, "User details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(SecretaryLoanVerificationDetail.this, "Failed to load user details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchCollectors() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("usertype").equalTo("collector").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> collectorList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming the user data has a 'name' field
                    String userName = snapshot.child("Name").getValue(String.class);
                    collectorList.add(userName);
                }
                showCollectorsDialog(collectorList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to fetch collectors", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showCollectorsDialog(List<String> collectorList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Collector");

        String[] collectorsArray = collectorList.toArray(new String[0]);

        builder.setItems(collectorsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Store the selected collector's name in the variable
                selectedCollectorName = collectorList.get(which);
                Double selectedLoanAmount = loanAmount;
                int loanAmount = selectedLoanAmount != null ? selectedLoanAmount.intValue() : 0;
                double interestAmount = loanAmount * INTEREST_RATE;
                double totalRepayment = loanAmount + interestAmount;
                int daysInMonth = 60;
                double dailyInstallment = totalRepayment / daysInMonth;

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                String startDate = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, daysInMonth - 1);
                String endDate = dateFormat.format(calendar.getTime());

                calendar.add(Calendar.DAY_OF_YEAR, 1);
                String startDateStr = dateFormat.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, daysInMonth - 1);
                String endDateStr = dateFormat.format(calendar.getTime());

                long startDateTimestamp = 0;
                long endDateTimestamp = 0;
                try {
                    startDateTimestamp = dateFormat.parse(startDateStr).getTime();
                    endDateTimestamp = dateFormat.parse(endDateStr).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //loanId = uid + "_" + startDateTimestamp + "_" + endDateTimestamp;

                DatabaseReference loansRef = FirebaseDatabase.getInstance().getReference().child("loans").child(LoanId);
                loansRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Map<String, Object> updates = new HashMap<>();
                            updates.put("status", "accepted");
                            updates.put("approvedStartDate", startDate);
                            updates.put("approvedEndDate", endDate);

                            loansRef.updateChildren(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Loan updated successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Failed to update loan loan: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Loan data does not exist in Firebase for ID: " + LoanId, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                DatabaseReference currentloansRef = FirebaseDatabase.getInstance().getReference().child("loan_application").child(LoanId);
                currentloansRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("status", "accepted");
                            updates.put("approvedStartDate", startDate);
                            updates.put("approvedEndDate", endDate);

                            currentloansRef.updateChildren(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                        } else {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                DatabaseReference userInfoRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Fetch user data from the 'users' node
                            String userName = dataSnapshot.child("Name").getValue(String.class);
                            String userProfile = dataSnapshot.child("ProfileImage").getValue(String.class);
                            String Brgy = dataSnapshot.child("Brgy").getValue(String.class);
                            String City = dataSnapshot.child("City").getValue(String.class);
                            String Province = dataSnapshot.child("Province").getValue(String.class);
                            String PhoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                            Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                            Double longitude = dataSnapshot.child("longitude").getValue(Double.class);


                            String Address = Brgy +" "+ City+ " "+ Province;

                            // Create the repaymentDetails map
                            Map<String, Object> repaymentDetails = new HashMap<>();
                            Instant startInstant = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startInstant = Instant.now();
                            }

                            for (int day = 0; day < 60; day++) {
                                Instant currentInstant = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    currentInstant = startInstant.plus(day + 1, ChronoUnit.DAYS);
                                }
                                double amountDue = dailyInstallment;
                                String paymentNote = "notPaid";
                                int dayNumber = day + 1;

                                if (currentInstant != null) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        repaymentDetails.put("day_" + dayNumber, currentInstant.toEpochMilli());
                                    }
                                }
                                repaymentDetails.put("toCollect_" + dayNumber, amountDue);
                                repaymentDetails.put("paidOrNot_" + dayNumber, paymentNote);
                                repaymentDetails.put("amountPaid_" + dayNumber, 0);
                                repaymentDetails.put("proofPayment_" + dayNumber, "null");
                            }

                            // Add repayment details and user info
                            repaymentDetails.put("repaymentBalance", totalRepayment);
                            repaymentDetails.put("repaymentUserId", uid);
                            repaymentDetails.put("loanId", LoanId);
                            repaymentDetails.put("status", "ongoing");
                            repaymentDetails.put("collector", selectedCollectorName);

                            // Add user info to repayment details
                            repaymentDetails.put("userName", userName);
                            repaymentDetails.put("userProfile", userProfile);
                            repaymentDetails.put("userAddress", Address);
                            repaymentDetails.put("userLatitude", latitude);
                            repaymentDetails.put("userLongitude", longitude);
                            repaymentDetails.put("PhoneNumber", PhoneNumber);

                            // Update repaymentDetails in Firebase under 'repaymentDetails' node
                            DatabaseReference repaymentDetailsRef = FirebaseDatabase.getInstance().getReference().child("repaymentDetails").child(LoanId);
                            repaymentDetailsRef.updateChildren(repaymentDetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("applyLoanActivity", "Repayment details updated successfully!");
                                            Toast.makeText(getApplicationContext(), "Payment list made successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("applyLoanActivity", "Failed to update repayment details: " + e.getMessage());
                                            Toast.makeText(getApplicationContext(), "Failed to update repayment details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            // Handle the failure scenario
                                        }
                                    });
                        } else {
                            Log.e("applyLoanActivity", "User data not found!");
                            Toast.makeText(getApplicationContext(), "User data not found!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("applyLoanActivity", "Error fetching user data: " + databaseError.getMessage());
                        Toast.makeText(getApplicationContext(), "Error fetching user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



                //////////////////////////////////////////////////////////////////////////////////////////////////////////\
                //Update the prevLoanBalance from user node
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                // Get user UID
                // Database reference for user profiles
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                // Fetch current previousLoanBalance and update both previousLoanBalance and currentLoan
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Prepare the update map
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("previousLoanBalance", totalRepayment);
                            updates.put("currentLoan", totalRepayment);  // Assuming newLoanAmount is the new value for currentLoan

                            // Update both previousLoanBalance and currentLoan in Firebase
                            userRef.updateChildren(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Loan Approved Succesfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("applyLoanActivity", "Failed to update previous loan balance and current loan: " + e.getMessage());
                                            // Handle the failure scenario
                                        }
                                    });
                        } else {
                            Log.e("applyLoanActivity", "User data does not exist in Firebase");
                            // Handle the scenario where user data is not found
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        Log.e("applyLoanActivity", "Database error: " + databaseError.getMessage());
                    }
                });
                // Adding notification logic
                long timestamp = System.currentTimeMillis(); // Get current timestamp
                String notificationId = uid + "_" + timestamp; // Custom ID for the notification

                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("notificationId", notificationId); // Add notification ID
                notificationData.put("userId", uid);
                notificationData.put("message", "Loan Approved");
                notificationData.put("timestamp", timestamp);
                notificationData.put("remark", "unread");

                // Save notificationData to Firebase under 'notifications' node
                DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("notifications");
                notificationsRef.child(notificationId).setValue(notificationData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                finish();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


}
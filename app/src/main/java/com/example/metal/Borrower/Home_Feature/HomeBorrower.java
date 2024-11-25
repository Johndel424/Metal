package com.example.metal.Borrower.Home_Feature;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.metal.Borrower.ApplyLoan_Feature.Borrower_ApplyLoan;
import com.example.metal.Borrower.Borrower_Credit;
import com.example.metal.Borrower.LoanHistory_Feature.LoanHistory;
import com.example.metal.Borrower.Notification_Feature.Borrower_Notification;
import com.example.metal.Borrower.Profile_Feature.Borrower_Profile;
import com.example.metal.Borrower.UploadDocuments_Feature.Borrower_CompleteProfile;
import com.example.metal.Borrower.UploadDocuments_Feature.Borrower_UploadDocuments;
import com.example.metal.Borrower.Chat_Feature.MessageActivity;
import com.example.metal.Collector.CollectorDailyCollectionModel;
import com.example.metal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeBorrower extends AppCompatActivity {
    FirebaseAuth auth;
    ImageButton loansButton , historyButton, userButton, chatButton,statusButton;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    private ImageView gifImageView,gifImageView1,gifRejected;
    String userUid, chatRoomId;
    RelativeLayout updateProfileRelativeLayout,uploadDocumentsRelativeLayout;
    ImageButton profile, home, history,loan;
    TextView homeIndicator,profileIndicator,historyIndicator,loanIndicator;
    RelativeLayout homeLayout,verifyingLayout,rejectedRelativeLayout;
    FrameLayout frameLayout;
    private FirebaseAuth mAuth;
    private ImageView profileImageView,creditScore;
    private TextView name, balance;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<CollectorDailyCollectionModel> currentLoanList;
    private ProgressDialog progressDialog;
    HomeRecentAdapter adapter;
    RelativeLayout loanHistoryLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_borrower);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        creditScore = findViewById(R.id.creditScore);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();

        currentLoanList = new ArrayList<>();
        adapter = new HomeRecentAdapter(this, currentLoanList);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("repaymentDetails");


        loanHistoryLayout = findViewById(R.id.loanHistoryLayout);
        loanHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeBorrower.this, LoanHistory.class);
                startActivity(intent);
            }
        });
        frameLayout = findViewById(R.id.frameLayout);
        homeLayout = findViewById(R.id.homeLayout);
        homeIndicator = findViewById(R.id.homeIndicator);
        profileIndicator = findViewById(R.id.profileIndicator);
        historyIndicator = findViewById(R.id.historynIndicator);
        loanIndicator = findViewById(R.id.loanIndicator);
        rejectedRelativeLayout = findViewById(R.id.rejectedRelativeLayout);
        home = findViewById(R.id.home);
        gifRejected = findViewById(R.id.gifRejected);
        history = findViewById(R.id.history);
        loan = findViewById(R.id.loan);
        profile = findViewById(R.id.profile);
        verifyingLayout = findViewById(R.id.pendingRelativeLayout);


        mAuth = FirebaseAuth.getInstance();
        profileImageView = findViewById(R.id.avatarImageView);
        name = findViewById(R.id.name);
        balance = findViewById(R.id.previousBalance);

        loadUserProfile();


        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                homeIndicator.setVisibility(View.INVISIBLE);
                loanIndicator.setVisibility(View.VISIBLE);
                historyIndicator.setVisibility(View.INVISIBLE);
                profileIndicator.setVisibility(View.INVISIBLE);
                profile.setImageResource(R.drawable.menu_profile_unselected);
                home.setImageResource(R.drawable.menu_home_unselected);
                loan.setImageResource(R.drawable.menu_loan_selected);
                history.setImageResource(R.drawable.menu_history_unselected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new Borrower_ApplyLoan())
                        .addToBackStack(null)
                        .commit();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                homeIndicator.setVisibility(View.INVISIBLE);
                loanIndicator.setVisibility(View.INVISIBLE);
                historyIndicator.setVisibility(View.INVISIBLE);
                profileIndicator.setVisibility(View.VISIBLE);
                profile.setImageResource(R.drawable.menu_profile_selected);
                home.setImageResource(R.drawable.menu_home_unselected);
                loan.setImageResource(R.drawable.menu_loan_unselected);
                history.setImageResource(R.drawable.menu_history_unselected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new Borrower_Profile())
                        .addToBackStack(null)
                        .commit();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                homeIndicator.setVisibility(View.INVISIBLE);
                loanIndicator.setVisibility(View.INVISIBLE);
                historyIndicator.setVisibility(View.VISIBLE);
                profileIndicator.setVisibility(View.INVISIBLE);
                profile.setImageResource(R.drawable.menu_profile_unselected);
                home.setImageResource(R.drawable.menu_home_unselected);
                loan.setImageResource(R.drawable.menu_loan_unselected);
                history.setImageResource(R.drawable.menu_history_selected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new Borrower_Notification())
                        .addToBackStack(null)
                        .commit();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLayout.setVisibility(View.VISIBLE);
                homeIndicator.setVisibility(View.VISIBLE);
                profileIndicator.setVisibility(View.INVISIBLE);
                loanIndicator.setVisibility(View.INVISIBLE);
                historyIndicator.setVisibility(View.INVISIBLE);
                profile.setImageResource(R.drawable.menu_profile_unselected);
                home.setImageResource(R.drawable.menu_home_selected);
                history.setImageResource(R.drawable.menu_history_unselected);
                loan.setImageResource(R.drawable.menu_loan_unselected);
                frameLayout.setVisibility(View.GONE);
            }
        });

        // Initialize ImageView
        gifImageView = findViewById(R.id.gifWelcome);
        gifImageView1 = findViewById(R.id.gifWelcome1);
        ImageView gifVerifying = findViewById(R.id.gifVerifying);

        // Load GIF using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.welcomegif)
                .into(gifImageView);
        Glide.with(this)
                .asGif()
                .load(R.drawable.welcomegif)
                .into(gifImageView1);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gifverifying)
                .into(gifVerifying);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gifverifying)
                .into(gifRejected);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userUid = currentUser.getUid();
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users").child(userUid);

        updateProfileRelativeLayout = findViewById(R.id.updateProfileRelativeLayout);
        uploadDocumentsRelativeLayout = findViewById(R.id.uploadDocumentsRelativeLayout);

        //connect
        chatButton = findViewById(R.id.chatButton);
        // Check verificationStatus from Firebase Database
        checkVerificationStatus();

        ImageButton completeProfile = findViewById(R.id.completeProfile);

        completeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Assuming you get the userUid from FirebaseAuth
                Intent intent = new Intent(HomeBorrower.this, Borrower_CompleteProfile.class);
                intent.putExtra("userUid", userUid); // Add the userUid as an extra
                startActivity(intent);
            }
        });
        ImageButton applyButtonAgain = findViewById(R.id.applyButtonAgain);

        applyButtonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Assuming you get the userUid from FirebaseAuth
                Intent intent = new Intent(HomeBorrower.this, Borrower_CompleteProfile.class);
                intent.putExtra("userUid", userUid); // Add the userUid as an extra
                startActivity(intent);
            }
        });
        ImageButton completeDocuments = findViewById(R.id.completeDocuments);

        completeDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeBorrower.this, Borrower_UploadDocuments.class);
                startActivity(intent);
            }
        });
        creditScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeBorrower.this, Borrower_Credit.class);
                startActivity(intent);
            }
        });

        ////////////////////////////////////////
        /////for chat creating chatroomid//////
        //////////////////////////////////////
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate the chat room ID
                String borrowerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String secretaryUid = "2XxknCQbmdcRdlURPk8puj1Vxy82";
                // check if sino mas mahaba
                if (borrowerUid.compareTo(secretaryUid) > 0) {
                    chatRoomId = borrowerUid + "_" + secretaryUid;
                } else {
                    chatRoomId = secretaryUid + "_" + borrowerUid;
                }
                // Retrieve profile images for each user
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                usersRef.child(borrowerUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String borrowerProfile = dataSnapshot.child("ProfileImage").getValue(String.class);
                            String borrowerName = dataSnapshot.child("Name").getValue(String.class);
                            String borrowerPhone = dataSnapshot.child("phoneNumber").getValue(String.class);
                            // Retrieve other user's profile image
                            usersRef.child(secretaryUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String secretaryProfile = snapshot.child("ProfileImage").getValue(String.class);
                                        String secretaryName = snapshot.child("Name").getValue(String.class);
                                        String secretaryPhone = snapshot.child("phoneNumber").getValue(String.class);

                                        // Create a HashMap to save chat room details
                                        HashMap<String, Object> chatRoomInfo = new HashMap<>();
                                        chatRoomInfo.put("borrowerId", borrowerUid);
                                        chatRoomInfo.put("secretaryId", secretaryUid);
                                        chatRoomInfo.put("chatRoomIdTrue", true); // Set the value as true
                                        chatRoomInfo.put("borrowerProfile", borrowerProfile);
                                        chatRoomInfo.put("borrowerName", borrowerName);
                                        chatRoomInfo.put("secretaryProfile", secretaryProfile);
                                        chatRoomInfo.put("secretaryName", secretaryName);
                                        chatRoomInfo.put("borrowerPhone", borrowerPhone);
                                        chatRoomInfo.put("secretaryPhone", secretaryPhone);
                                        chatRoomInfo.put("statusSecretary", "unread");

                                        // Save chat room details to the database
                                        DatabaseReference chatRoomRef = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatRoomId);
                                        chatRoomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    // Chat room exists, update the details except "lastChat"
                                                    chatRoomRef.updateChildren(chatRoomInfo)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // Start the ConversationActivity
                                                                    Intent intent = new Intent(HomeBorrower.this, MessageActivity.class);
                                                                    intent.putExtra("chatRoomId", chatRoomId);
                                                                    startActivity(intent);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(HomeBorrower.this, "Failed to start chat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                } else {
                                                    // Chat room does not exist, create it with all details
                                                    chatRoomRef.setValue(chatRoomInfo)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // Start the ConversationActivity
                                                                    Intent intent = new Intent(HomeBorrower.this, MessageActivity.class);
                                                                    intent.putExtra("chatRoomId", chatRoomId);
                                                                    startActivity(intent);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(HomeBorrower.this, "Failed to start chat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(HomeBorrower.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(HomeBorrower.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeBorrower.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //////////////////////////////////////
        ///end for chat creating chatroomid///
        //////////////////////////////////////
    }
    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentLoanList.clear();

                if (snapshot.exists()) { // Check if snapshot exists to avoid null issues
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        CollectorDailyCollectionModel dataClass = itemSnapshot.getValue(CollectorDailyCollectionModel.class);
                        // Retrieve current user UID
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserUid = currentUser != null ? currentUser.getUid() : null;

                        if (dataClass != null && currentUserUid != null
                                && dataClass.getRepaymentUserId().equals(currentUserUid)
                                && "ongoing".equals(dataClass.getStatus())) {  // Check if status is "ongoing"

                            currentLoanList.add(dataClass);
                        }



                    }
                    adapter.notifyDataSetChanged(); // Notify adapter after data is added
                } else {
                    // Handle the case when snapshot doesn't exist or is empty
                    Toast.makeText(HomeBorrower.this, "No data available.", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(HomeBorrower.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
    private void loadUserProfile() {
        String currentUserUID = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserUID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePicUrl = dataSnapshot.child("ProfileImage").getValue(String.class);
                    String full_name = dataSnapshot.child("Name").getValue(String.class);

                    // Check if the previousLoanBalance node exists
                    if (dataSnapshot.hasChild("previousLoanBalance")) {
                        // Retrieve the previousLoanBalance as an Integer
                        Integer previousLoanBalance = dataSnapshot.child("previousLoanBalance").getValue(Integer.class);

                        // If previousLoanBalance is not null and not 0, set the value. Otherwise, set "No balance".
                        if (previousLoanBalance != null && previousLoanBalance != 0) {
                            balance.setText("₱ " + previousLoanBalance);
                        } else {
                            balance.setText("No balance");
                        }
                    } else {
                        // If the node doesn't exist, set the default value
                        balance.setText("No balance");
                    }

                    // Load profile picture using Glide
                    RequestOptions options = new RequestOptions()
                            .circleCrop() // Make the image circular
                            .placeholder(R.drawable.logo); // Placeholder image while loading

                    Glide.with(HomeBorrower.this)
                            .load(profilePicUrl)
                            .apply(options)
                            .into(profileImageView); // Assume profileImageView is your ImageView in the activity

                    // Set name text
                    name.setText(full_name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(HomeBorrower.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkVerificationStatus() {
        // Show loading indicator
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking verification status...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Check 'allDocuments' status
        usersRef.child("accountDocumentsComplete").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot allDocumentsSnapshot) {
                if (allDocumentsSnapshot.exists()) {
                    String documentsStatus = allDocumentsSnapshot.getValue(String.class);

                    // Check 'allDetails' status
                    usersRef.child("accountDetailsComplete").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot allDetailsSnapshot) {
                            if (allDetailsSnapshot.exists()) {
                                String detailsStatus = allDetailsSnapshot.getValue(String.class);

                                // Check 'verificationStatus'
                                usersRef.child("accountStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot verificationStatusSnapshot) {
                                        // Dismiss the loading indicator
                                        progressDialog.dismiss();

                                        if (verificationStatusSnapshot.exists()) {
                                            String verificationStatus = verificationStatusSnapshot.getValue(String.class);

                                            // If either 'allDocuments' or 'allDetails' is "no"
                                            if ("no".equals(detailsStatus)) {
                                                // Make updateProfileRelativeLayout visible
                                                updateProfileRelativeLayout.setVisibility(View.VISIBLE);
                                            } else if ("yes".equals(detailsStatus) && "no".equals(documentsStatus)) {
                                                // Make uploadDocumentsRelativeLayout visible
                                                uploadDocumentsRelativeLayout.setVisibility(View.VISIBLE);
                                            } else if ("yes".equals(detailsStatus) && "yes".equals(documentsStatus) && "pending".equals(verificationStatus)) {
                                                // Make verification layout visible if both are "yes" and status is "pending"
                                                verifyingLayout.setVisibility(View.VISIBLE);
                                            } else if ("yes".equals(detailsStatus) && "yes".equals(documentsStatus) && "rejected".equals(verificationStatus)) {
                                                // Make verification layout visible if both are "yes" and status is "pending"
                                                rejectedRelativeLayout.setVisibility(View.VISIBLE);
                                            } else {
                                                // Make RelativeLayout invisible or gone as needed
                                                homeLayout.setVisibility(View.VISIBLE);
                                                updateProfileRelativeLayout.setVisibility(View.GONE);
                                                uploadDocumentsRelativeLayout.setVisibility(View.GONE);
                                                verifyingLayout.setVisibility(View.GONE); // Ensure verifying layout is hidden if conditions not met
                                            }
                                        } else {
                                            // Handle case where 'verificationStatus' does not exist
                                            updateProfileRelativeLayout.setVisibility(View.GONE);
                                            uploadDocumentsRelativeLayout.setVisibility(View.GONE);
                                            verifyingLayout.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Dismiss the loading indicator and handle possible error for 'verificationStatus'
                                        progressDialog.dismiss();
                                        updateProfileRelativeLayout.setVisibility(View.GONE);
                                        uploadDocumentsRelativeLayout.setVisibility(View.GONE);
                                        verifyingLayout.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                // Dismiss the loading indicator and handle case where 'allDetails' does not exist
                                progressDialog.dismiss();
                                updateProfileRelativeLayout.setVisibility(View.GONE);
                                uploadDocumentsRelativeLayout.setVisibility(View.GONE);
                                verifyingLayout.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Dismiss the loading indicator and handle possible error for 'allDetails'
                            progressDialog.dismiss();
                            updateProfileRelativeLayout.setVisibility(View.GONE);
                            uploadDocumentsRelativeLayout.setVisibility(View.GONE);
                            verifyingLayout.setVisibility(View.GONE);
                        }
                    });
                } else {
                    // Dismiss the loading indicator and handle case where 'allDocuments' does not exist
                    progressDialog.dismiss();
                    updateProfileRelativeLayout.setVisibility(View.GONE);
                    uploadDocumentsRelativeLayout.setVisibility(View.GONE);
                    verifyingLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Dismiss the loading indicator and handle possible error for 'allDocuments'
                progressDialog.dismiss();
                updateProfileRelativeLayout.setVisibility(View.GONE);
                uploadDocumentsRelativeLayout.setVisibility(View.GONE);
                verifyingLayout.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {
        // Make the RelativeLayout visible when back is pressed
        super.onBackPressed();
        homeLayout.setVisibility(View.VISIBLE);
        homeIndicator.setVisibility(View.VISIBLE);
        profileIndicator.setVisibility(View.INVISIBLE);
        loanIndicator.setVisibility(View.INVISIBLE);
        historyIndicator.setVisibility(View.INVISIBLE);
        profile.setImageResource(R.drawable.menu_profile_unselected);
        home.setImageResource(R.drawable.menu_home_selected);
        history.setImageResource(R.drawable.menu_history_unselected);
        loan.setImageResource(R.drawable.menu_loan_unselected);
        frameLayout.setVisibility(View.GONE);
    }

}
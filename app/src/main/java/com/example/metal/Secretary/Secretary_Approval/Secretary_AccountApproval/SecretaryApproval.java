package com.example.metal.Secretary.Secretary_Approval.Secretary_AccountApproval;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.Borrower.Profile_Feature.UserModel;
import com.example.metal.R;
import com.example.metal.Secretary.Secretary_Approval.Secretary_LoanApproval.SecretaryLoanApprovalPending;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SecretaryApproval extends AppCompatActivity {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<UserModel> userList;
    LinearLayout homeLayout;
    secretaryAccountVerificationAdapter adapter;
    ImageButton loan,account;
    TextView accountIndicator,loanIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_secretary_approval);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        homeLayout = findViewById(R.id.homeLayout);
        loan = findViewById(R.id.loan);
        account = findViewById(R.id.account);
        loanIndicator = findViewById(R.id.loanIndicator);
        accountIndicator = findViewById(R.id.accountIndicator);
        // Navigate
        TextView approved = findViewById(R.id.approved);
        TextView pending = findViewById(R.id.pending);
        TextView rejected = findViewById(R.id.rejected);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(SecretaryApproval.this, SecretaryApproval.class);
                startActivity(intent);
                finish();
            }
        });
        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                accountIndicator.setVisibility(View.INVISIBLE);
                loanIndicator.setVisibility(View.VISIBLE);
                loan.setImageResource(R.drawable.menu_loan_selected);
                account.setImageResource(R.drawable.menu_account_unselected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SecretaryLoanApprovalPending())
                        .commit();
            }
        });
        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SecretaryAccountApprovalApproved())
                        .commit();
            }
        });
        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SecretaryAccountApprovalRejected())
                        .commit();
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(SecretaryApproval.this, SecretaryApproval.class);
                startActivity(intent);
                finish();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        dialog.show();

        userList = new ArrayList<>();
        adapter = new secretaryAccountVerificationAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUid = currentUser.getUid();

        databaseReference = getInstance().getReference("users");

// Set up the ValueEventListener for databaseReference
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                // Fetch and filter the data
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    UserModel dataClass = itemSnapshot.getValue(UserModel.class);
                    if (dataClass != null
                            && "yes".equals(dataClass.getAccountDocumentsComplete())
                            && "pending".equals(dataClass.getAccountStatus())) {
                        userList.add(dataClass);
                    }
                }

                // Sort the userList by timestamp in descending order (latest first)
                Collections.sort(userList, new Comparator<UserModel>() {
                    @Override
                    public int compare(UserModel u1, UserModel u2) {
                        // Sort in descending order by timestamp
                        return Long.compare(u2.getTimestamp(), u1.getTimestamp());
                    }
                });

                // Update the adapter
                adapter.notifyDataSetChanged();

                // Check if the list is empty
                if (userList.isEmpty()) {
                    // Dismiss the AlertDialog if no items found
                    dialog.dismiss();
                    // Optionally, show a message to the user
                    Toast.makeText(SecretaryApproval.this, "No users found.", Toast.LENGTH_SHORT).show();
                } else {
                    // Dismiss the AlertDialog if there are items
                    dialog.dismiss();
                }

                // Always dismiss the ProgressDialog after loading data
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Dismiss both dialogs on error
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(SecretaryApproval.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void searchList(String text) {
        if (text == null || text.isEmpty()) {
            adapter.searchDataList((ArrayList<UserModel>) userList); // Show all data if search text is null or empty
            return;
        }

        String searchText = text.toLowerCase(); // Convert search text to lowercase once

        ArrayList<UserModel> searchList = new ArrayList<>();
        for (UserModel dataClass : userList) {
            String displayName = dataClass.getName();
            if (displayName != null && displayName.toLowerCase().contains(searchText)) {
                searchList.add(dataClass);
            }
        }

        adapter.searchDataList(searchList); // Update adapter with filtered list
    }
}
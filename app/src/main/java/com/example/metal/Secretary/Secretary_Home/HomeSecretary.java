package com.example.metal.Secretary.Secretary_Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

import com.example.metal.Collector.CollectorDailyCollectionModel;
import com.example.metal.R;
import com.example.metal.Secretary.SecretaryProfile;
import com.example.metal.Secretary.Secretary_Approval.Secretary_AccountApproval.SecretaryApproval;
import com.example.metal.Secretary.Secretary_Capital.SecretaryCapital;
import com.example.metal.Secretary.Secretary_Chat.SecretaryChatList;
import com.example.metal.Secretary.Secretary_DataAnalytics.SecretaryData;
import com.example.metal.Secretary.Secretary_LoanHistory.SecretaryLoanHistory;
import com.example.metal.Secretary.Secretary_PassDue.Secretary_PassDue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeSecretary extends AppCompatActivity {
    RelativeLayout homeLayout;
    TextView homeIndicator,profileIndicator,capitalIndicator,dataIndicator,previousBalance;
    ImageButton loanHistory,home,approval, capital, data, profile, chatSupport,pastDue;
    FrameLayout frameLayout;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<CollectorDailyCollectionModel> currentLoanList;
    private ProgressDialog progressDialog;
    SecretaryHomeRecentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_secretary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pastDue = findViewById(R.id.pastDue);
        loanHistory = findViewById(R.id.loanHistory);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();

        currentLoanList = new ArrayList<>();
        adapter = new SecretaryHomeRecentAdapter(this, currentLoanList);
        recyclerView.setAdapter(adapter);

        previousBalance = findViewById(R.id.previousBalance);
        displayTotalForToday();
        databaseReference = FirebaseDatabase.getInstance().getReference("repaymentDetails");

        //initialize ui
        frameLayout = findViewById(R.id.fragment_container);
        homeLayout = findViewById(R.id.homeLayout);
        approval = findViewById(R.id.approval);
        home = findViewById(R.id.home);
        homeIndicator = findViewById(R.id.homeIndicator);
        capitalIndicator = findViewById(R.id.capitalIndicator);
        profileIndicator = findViewById(R.id.profileIndicator);
        dataIndicator = findViewById(R.id.dataIndicator);

        capital = findViewById(R.id.capital);
        data = findViewById(R.id.data);
        profile = findViewById(R.id.profile);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLayout.setVisibility(View.VISIBLE);
                homeIndicator.setVisibility(View.VISIBLE);
                profileIndicator.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.GONE);
                capitalIndicator.setVisibility(View.INVISIBLE);
                dataIndicator.setVisibility(View.INVISIBLE);
                profile.setImageResource(R.drawable.menu_profile_unselected);
                home.setImageResource(R.drawable.menu_home_selected);
                capital.setImageResource(R.drawable.menu_capital_unselected);
                data.setImageResource(R.drawable.menu_data_unselected);

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                homeIndicator.setVisibility(View.INVISIBLE);
                profileIndicator.setVisibility(View.VISIBLE);
                capitalIndicator.setVisibility(View.INVISIBLE);
                dataIndicator.setVisibility(View.INVISIBLE);
                profile.setImageResource(R.drawable.menu_profile_selected);
                home.setImageResource(R.drawable.menu_home_unselected);
                capital.setImageResource(R.drawable.menu_capital_unselected);
                data.setImageResource(R.drawable.menu_data_unselected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SecretaryProfile())
                        .addToBackStack(null)
                        .commit();
            }
        });
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                homeIndicator.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
                profileIndicator.setVisibility(View.INVISIBLE);
                capitalIndicator.setVisibility(View.INVISIBLE);
                dataIndicator.setVisibility(View.VISIBLE);
                profile.setImageResource(R.drawable.menu_profile_unselected);
                home.setImageResource(R.drawable.menu_home_unselected);
                capital.setImageResource(R.drawable.menu_capital_unselected);
                data.setImageResource(R.drawable.menu_data_selected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SecretaryData())
                        .addToBackStack(null)
                        .commit();
            }
        });
        capital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                homeIndicator.setVisibility(View.INVISIBLE);
                profileIndicator.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
                capitalIndicator.setVisibility(View.VISIBLE);
                dataIndicator.setVisibility(View.INVISIBLE);
                profile.setImageResource(R.drawable.menu_profile_unselected);
                home.setImageResource(R.drawable.menu_home_unselected);
                capital.setImageResource(R.drawable.menu_capital_selected);
                data.setImageResource(R.drawable.menu_data_unselected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SecretaryCapital())
                        .addToBackStack(null)
                        .commit();
            }
        });
        approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(HomeSecretary.this, SecretaryApproval.class);
                startActivity(intent);
            }
        });
        loanHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(HomeSecretary.this, SecretaryLoanHistory.class);
                startActivity(intent);
            }
        });
        pastDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(HomeSecretary.this, Secretary_PassDue.class);
                startActivity(intent);
            }
        });

        chatSupport = findViewById(R.id.chatSupport);
        chatSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SecretaryChatList())
                        .addToBackStack(null)
                        .commit();
            }
        });


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

                        if (dataClass != null
                                && "ongoing".equals(dataClass.getStatus())) {  // Check if status is "ongoing"

                            currentLoanList.add(dataClass);
                        }



                    }
                    adapter.notifyDataSetChanged(); // Notify adapter after data is added
                } else {
                    // Handle the case when snapshot doesn't exist or is empty
                    Toast.makeText(HomeSecretary.this, "No data available.", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(HomeSecretary.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onBackPressed() {
        // Make the RelativeLayout visible when back is pressed
        super.onBackPressed();
        homeLayout.setVisibility(View.VISIBLE);
        homeIndicator.setVisibility(View.VISIBLE);
        profileIndicator.setVisibility(View.INVISIBLE);
        capitalIndicator.setVisibility(View.INVISIBLE);
        frameLayout.setVisibility(View.GONE);
        dataIndicator.setVisibility(View.INVISIBLE);
        profile.setImageResource(R.drawable.menu_profile_unselected);
        home.setImageResource(R.drawable.menu_home_selected);
        capital.setImageResource(R.drawable.menu_capital_unselected);
        data.setImageResource(R.drawable.menu_data_unselected);
    }
    public void displayTotalForToday() {
        // Kumuha ng timestamp para sa simula at katapusan ng araw
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDay = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        long endOfDay = calendar.getTimeInMillis();

        // Reference sa payments sa Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("payments");

        // I-retrieve ang payments para sa araw na ito at mag-set ng listener for real-time updates
        databaseRef.orderByKey().startAt(String.valueOf(startOfDay)).endAt(String.valueOf(endOfDay))
                .addValueEventListener(new ValueEventListener() { // Ginawang ValueEventListener for auto-refresh
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double totalAmount = 0.0;

                        // I-iterate ang mga payments
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Double amount = snapshot.child("amount").getValue(Double.class);
                            if (amount != null) {
                                totalAmount += amount; // I-accumulate ang total amount
                            }
                        }

                        // I-display ang total amount sa TextView
                        if (totalAmount > 0) {
                            previousBalance.setText("₱" + totalAmount);
                        } else {
                            previousBalance.setText("No collection yet.");
                        }

                        // I-display ang total amount
                        Toast.makeText(HomeSecretary.this, "Total Amount for Today: ₱" + totalAmount, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // I-handle ang mga posibleng error
                        Toast.makeText(HomeSecretary.this, "Error retrieving data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
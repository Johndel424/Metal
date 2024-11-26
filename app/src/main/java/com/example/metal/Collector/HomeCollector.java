package com.example.metal.Collector;

import android.app.ProgressDialog;
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

import com.example.metal.Collector.Profile_Feature.CollectorMePage;
import com.example.metal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeCollector extends AppCompatActivity {
    ImageButton profile, home;
    TextView homeIndicator,profileIndicator,previousBalance;
    RelativeLayout homeLayout;
    FrameLayout frameLayout;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<CollectorDailyCollectionModel> currentLoanList;
    private ProgressDialog progressDialog;
    CollectorDailyCollectionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_collector);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        previousBalance = findViewById(R.id.previousBalance);
        frameLayout = findViewById(R.id.frameLayout);
        homeLayout = findViewById(R.id.homeLayout);
        homeIndicator = findViewById(R.id.homeIndicator);
        profileIndicator = findViewById(R.id.profileIndicator);
        home = findViewById(R.id.home);

        displayTotalForToday();

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the image is clicked
                homeLayout.setVisibility(View.GONE);
                homeIndicator.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
                profileIndicator.setVisibility(View.VISIBLE);
                profile.setImageResource(R.drawable.menu_profile_selected);
                home.setImageResource(R.drawable.menu_home_unselected);
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if (currentFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(currentFragment)
                            .commit();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new CollectorMePage())
                        .addToBackStack(null)
                        .commit();
            }
        });
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLayout.setVisibility(View.VISIBLE);
                homeIndicator.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                profileIndicator.setVisibility(View.INVISIBLE);
                profile.setImageResource(R.drawable.menu_profile_unselected);
                home.setImageResource(R.drawable.menu_home_selected);

            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();

        currentLoanList = new ArrayList<>();
        adapter = new CollectorDailyCollectionAdapter(this, currentLoanList);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("repaymentDetails");
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        progressDialog.show();
//
//        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                currentLoanList.clear();
//
//                if (snapshot.exists()) { // Check if snapshot exists to avoid null issues
//                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
//                        CollectorDailyCollectionModel dataClass = itemSnapshot.getValue(CollectorDailyCollectionModel.class);
//                        // Check if dataClass is not null before adding it to the list
//                        if (dataClass != null) {
//                            currentLoanList.add(dataClass);
//                        }
//                    }
//                    adapter.notifyDataSetChanged(); // Notify adapter after data is added
//                } else {
//                    // Handle the case when snapshot doesn't exist or is empty
//                    Toast.makeText(HomeCollector.this, "No data available.", Toast.LENGTH_SHORT).show();
//                }
//
//                progressDialog.dismiss();
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                progressDialog.dismiss();
//                dialog.dismiss();
//                Toast.makeText(HomeCollector.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
@Override
protected void onStart() {
    super.onStart();
    progressDialog.show();

    // Get logged-in user's name from the 'users' table
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);

    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
            if (userSnapshot.exists()) {
                String loggedInUserName = userSnapshot.child("Name").getValue(String.class);

                // Fetch loans where the collector's name matches the logged-in user's name
                eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentLoanList.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                CollectorDailyCollectionModel dataClass = itemSnapshot.getValue(CollectorDailyCollectionModel.class);

                                if (dataClass != null && loggedInUserName != null && loggedInUserName.equals(dataClass.getCollector())) {
                                    currentLoanList.add(dataClass);
                                }
                            }

                            adapter.notifyDataSetChanged(); // Notify adapter
                        } else {
                            Toast.makeText(HomeCollector.this, "No data available.", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        Toast.makeText(HomeCollector.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressDialog.dismiss();
                Toast.makeText(HomeCollector.this, "User data not found.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            progressDialog.dismiss();
            Toast.makeText(HomeCollector.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
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
        frameLayout.setVisibility(View.GONE);
        profileIndicator.setVisibility(View.INVISIBLE);
        profile.setImageResource(R.drawable.menu_profile_unselected);
        home.setImageResource(R.drawable.menu_home_selected);
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
                        Toast.makeText(HomeCollector.this, "Total Amount for Today: ₱" + totalAmount, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // I-handle ang mga posibleng error
                        Toast.makeText(HomeCollector.this, "Error retrieving data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
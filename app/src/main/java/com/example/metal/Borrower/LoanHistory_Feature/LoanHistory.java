package com.example.metal.Borrower.LoanHistory_Feature;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoanHistory extends AppCompatActivity {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<LoanHistoryModel> list;
    private ProgressDialog progressDialog;
    LoanHistoryAdapter adapter;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loan_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        back = findViewById(R.id.back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();

        list = new ArrayList<>();
        adapter = new LoanHistoryAdapter(this, list);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("loans");
        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());
    }
    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                if (snapshot.exists()) { // Check if snapshot exists to avoid null issues
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        LoanHistoryModel dataClass = itemSnapshot.getValue(LoanHistoryModel.class);

                        // Retrieve current user UID
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserUid = currentUser != null ? currentUser.getUid() : null;

                        // Check if dataClass and userId are not null before comparing
                        if (dataClass != null && dataClass.getUserId() != null && currentUserUid != null
                                && dataClass.getUserId().equals(currentUserUid)) {

                            list.add(dataClass); // Add to list if the userId matches the current user
                        }
                    }

                    // Notify adapter after data is added
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case when snapshot doesn't exist or is empty
                    Toast.makeText(LoanHistory.this, "No data available.", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(LoanHistory.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
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
}
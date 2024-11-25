package com.example.metal.Borrower.LoanHistory_Feature;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.metal.Collector.CollectorDailyCollectionModel;
import com.example.metal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoanHistoryDetail extends AppCompatActivity {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<CollectorDailyCollectionModel> currentLoanList;
    private ProgressDialog progressDialog;
    LoanHistoryDetailAdapter adapter;
    String receivedVariable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loan_history_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        receivedVariable = intent.getStringExtra("loanId");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();

        currentLoanList = new ArrayList<>();
        adapter = new LoanHistoryDetailAdapter(this, currentLoanList);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("repaymentDetails");
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
                                && receivedVariable.equals(dataClass.getLoanId())) {  // Check if status is "ongoing"

                            currentLoanList.add(dataClass);
                        }

                    }
                    adapter.notifyDataSetChanged(); // Notify adapter after data is added
                } else {
                    // Handle the case when snapshot doesn't exist or is empty
                    Toast.makeText(LoanHistoryDetail.this, "No data available.", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(LoanHistoryDetail.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
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
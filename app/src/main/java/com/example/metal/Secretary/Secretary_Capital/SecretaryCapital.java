package com.example.metal.Secretary.Secretary_Capital;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.Borrower.Profile_Feature.UserModel;
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


public class SecretaryCapital extends Fragment {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<UserModel> userModelList;
    double totalPreviousLoan = 0;
    SecretaryCapitalAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_secretary_capital, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // para hindi mabuksan ng user

        // Ipakita ang Progress Dialog
        progressDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = builder.create();
        dialog.show();

        userModelList = new ArrayList<>();
        adapter = new SecretaryCapitalAdapter(getActivity(), userModelList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUid = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // I-set ang ValueEventListener para sa databaseReference
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelList.clear();
                totalPreviousLoan = 0; // Reset previous loan total
                double totalCurrentLoan = 0; // Change to double to be consistent with previous loan balance

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    UserModel dataClass = itemSnapshot.getValue(UserModel.class);
                    if (dataClass != null) {
                        // Check if userType is "borrower"
                        //if ("borrower".equals(dataClass.getUserType())) {
                        // Check and add to previous loan total
                        if (dataClass.getPreviousLoanBalance() > 0) {
                            userModelList.add(dataClass);
                            totalPreviousLoan += dataClass.getPreviousLoanBalance(); // Update previous loan total
                        }

                        // Check and add to current loan total
                        if (dataClass.getCurrentLoan() > 0) {
                            totalCurrentLoan += dataClass.getCurrentLoan(); // Update current loan total
                        }
                        //}
                    }
                }


                adapter.notifyDataSetChanged();
                progressDialog.dismiss(); // Close progress dialog when done
                dialog.dismiss();

                // Set total previous loan in TextView
                TextView totalPreviousLoanTextView = rootView.findViewById(R.id.totalPreviousLoanTextView);
                totalPreviousLoanTextView.setText(String.format("₱%,.2f", totalPreviousLoan)); // Format as double with peso sign

                // Set total current loan in another TextView
                TextView totalCurrentLoanTextView = rootView.findViewById(R.id.totalCurrentLoanTextView);
                totalCurrentLoanTextView.setText(String.format("₱%,.2f", totalCurrentLoan)); // Format as double with peso sign
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss(); // Dismiss Progress Dialog on error
                dialog.dismiss();
                Toast.makeText(getActivity(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });



        SearchView searchView = rootView.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        return rootView;
    }
    public void searchList(String text) {
        if (text == null || text.isEmpty()) {
            adapter.searchDataList((ArrayList<UserModel>) userModelList); // Show all data if search text is null or empty
            return;
        }

        String searchText = text.toLowerCase(); // Convert search text to lowercase once

        ArrayList<UserModel> searchList = new ArrayList<>();
        for (UserModel dataClass : userModelList) {
            String displayName = dataClass.getName();
            if (displayName != null && displayName.toLowerCase().contains(searchText)) {
                searchList.add(dataClass);
            }
        }

        adapter.searchDataList(searchList); // Update adapter with filtered list
    }


}
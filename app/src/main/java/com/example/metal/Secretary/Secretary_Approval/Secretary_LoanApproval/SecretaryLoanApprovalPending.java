package com.example.metal.Secretary.Secretary_Approval.Secretary_LoanApproval;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

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

public class SecretaryLoanApprovalPending extends Fragment {

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<SecretaryLoanModel> loanList;
    SecretaryLoanApprovalAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_secretary_loan_approval_pending, container, false);
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

        loanList = new ArrayList<>();
        adapter = new SecretaryLoanApprovalAdapter(getActivity(), loanList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUid = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("loan_application");

        // I-set ang ValueEventListener para sa databaseReference
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loanList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    SecretaryLoanModel dataClass = itemSnapshot.getValue(SecretaryLoanModel.class);
                    if (dataClass != null && "pending".equals(dataClass.getStatus())) {
                        loanList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss(); // Isara ang Progress Dialog kapag tapos na
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss(); // Isara ang Progress Dialog kung may error
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
            adapter.searchDataList((ArrayList<SecretaryLoanModel>) loanList); // Show all data if search text is null or empty
            return;
        }

        String searchText = text.toLowerCase(); // Convert search text to lowercase once

        ArrayList<SecretaryLoanModel> searchList = new ArrayList<>();
        for (SecretaryLoanModel dataClass : loanList) {
            String displayName = dataClass.getUserName();
            if (displayName != null && displayName.toLowerCase().contains(searchText)) {
                searchList.add(dataClass);
            }
        }

        adapter.searchDataList(searchList); // Update adapter with filtered list
    }


}
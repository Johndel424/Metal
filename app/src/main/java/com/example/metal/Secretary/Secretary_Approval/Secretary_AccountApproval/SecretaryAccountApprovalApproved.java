package com.example.metal.Secretary.Secretary_Approval.Secretary_AccountApproval;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SecretaryAccountApprovalApproved extends Fragment {

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<UserModel> userList;
    secretaryAccountVerificationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_secretary_account_approval_approved, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        //navigate
        TextView approved = rootView.findViewById(R.id.approved);
        TextView pending = rootView.findViewById(R.id.pending);
        TextView rejected = rootView.findViewById(R.id.rejected);
        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the ImageView is clicked
                SecretaryAccountApprovalApproved fragmentB = new SecretaryAccountApprovalApproved();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragmentB);

                transaction.commit();
            }
        });
        rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the fragment when the ImageView is clicked
                SecretaryAccountApprovalRejected fragmentB = new SecretaryAccountApprovalRejected();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragmentB);

                transaction.commit();
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(getActivity(), SecretaryApproval.class);

                // Optionally, add extra data to the Intent
                // intent.putExtra("key", "value");

                // Start the new activity
                startActivity(intent);
            }
        });

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // para hindi mabuksan ng user

        // Ipakita ang Progress Dialog
        progressDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = builder.create();
        dialog.show();

        userList = new ArrayList<>();
        adapter = new secretaryAccountVerificationAdapter(getActivity(), userList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUid = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // I-set ang ValueEventListener para sa databaseReference
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    UserModel dataClass = itemSnapshot.getValue(UserModel.class);
                    if (dataClass != null && "yes".equals(dataClass.getAccountDocumentsComplete()) && "accepted".equals(dataClass.getAccountStatus())&&
                            "borrower".equals(dataClass.getUserType())) {
                        userList.add(dataClass);
                    }
                }

                // Sort the userList based on the timestamp in descending order (latest first)
                Collections.sort(userList, new Comparator<UserModel>() {
                    @Override
                    public int compare(UserModel user1, UserModel user2) {
                        return Long.compare(user2.getTimestamp(), user1.getTimestamp()); // Change order here
                    }
                });

                adapter.notifyDataSetChanged();
                progressDialog.dismiss(); // Dismiss the Progress Dialog when done
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss(); // Dismiss the Progress Dialog if there's an error
                dialog.dismiss();
                Toast.makeText(getActivity(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });




//        SearchView searchView = rootView.findViewById(R.id.searchView);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchList(newText);
//                return true;
//            }
//        });

        return rootView;
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
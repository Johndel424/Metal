package com.example.metal.Borrower.Notification_Feature;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Borrower_Notification extends Fragment {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<NotificationModel> notificationModelList;
    private ProgressDialog progressDialog;
    NotificationAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_borrower__notification, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        //navigate

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // para hindi mabuksan ng user

        // Ipakita ang Progress Dialog
        progressDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = builder.create();
        dialog.show();

        notificationModelList = new ArrayList<>();
        adapter = new NotificationAdapter(getActivity(), notificationModelList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUid = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

        // I-set ang ValueEventListener para sa databaseReference
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationModelList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    NotificationModel dataClass = itemSnapshot.getValue(NotificationModel.class);
                    if (dataClass != null && currentUid.equals(dataClass.getUserId())) {
                        notificationModelList.add(dataClass);
                    }
                }

                // Sort the userList based on the timestamp in descending order
                Collections.sort(notificationModelList, new Comparator<NotificationModel>() {
                    @Override
                    public int compare(NotificationModel user1, NotificationModel user2) {
                        return Long.compare(user2.getTimestamp(), user1.getTimestamp());
                    }
                });


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


        return rootView;
    }


}
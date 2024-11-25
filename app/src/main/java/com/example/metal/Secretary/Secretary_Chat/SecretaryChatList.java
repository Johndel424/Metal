package com.example.metal.Secretary.Secretary_Chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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


public class SecretaryChatList extends Fragment {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    AlertDialog dialog;
    List<secretaryChatRoomModel> chatList;
    secretaryChatListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_secretary_chat_list, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Find the ImageView
        ImageView gifImageView = rootView.findViewById(R.id.gifImageView);

        // Load the GIF from the drawable folder using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.gifnomessage)  // Reference to the GIF in drawable
                .into(gifImageView);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // para hindi mabuksan ng user

        // Ipakita ang Progress Dialog
        progressDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = builder.create();
        dialog.show();

        chatList = new ArrayList<>();
        adapter = new secretaryChatListAdapter(getActivity(), chatList);
        recyclerView.setAdapter(adapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUid = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms");

// I-set ang ValueEventListener para sa databaseReference
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    secretaryChatRoomModel dataClass = itemSnapshot.getValue(secretaryChatRoomModel.class);
                    if (dataClass != null) {
                        if ((dataClass.getSecretaryId().equals(currentUid) || dataClass.getBorrowerId().equals(currentUid)) && dataClass.getLastMessage() != null) {
                            chatList.add(dataClass);
                        }
                    }
                }

                // Sort the chatList by lastMessageTime in descending order (most recent first)
                Collections.sort(chatList, new Comparator<secretaryChatRoomModel>() {
                    @Override
                    public int compare(secretaryChatRoomModel o1, secretaryChatRoomModel o2) {
                        return Long.compare(o2.getLastMessageTimestamp(), o1.getLastMessageTimestamp()); // descending order
                    }
                });

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(getContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
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
        ArrayList<secretaryChatRoomModel> searchList = new ArrayList<>();
        for (secretaryChatRoomModel dataClass : chatList) {
            if (dataClass.getBorrowerName().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }


}
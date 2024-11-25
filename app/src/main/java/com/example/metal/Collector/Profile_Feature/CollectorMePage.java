package com.example.metal.Collector.Profile_Feature;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.metal.Borrower.Home;
import com.example.metal.Borrower.Profile_Feature.CheckPin;
import com.example.metal.MainActivity;
import com.example.metal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollectorMePage extends Fragment {
    private TextView name;

    private ImageView profileImageView;
    LinearLayout logout_button, changePin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collector_me_page, container, false);


        name = view.findViewById(R.id.name);
        changePin = view.findViewById(R.id.changePin);
        profileImageView = view.findViewById(R.id.avatarImageView);
        logout_button = view.findViewById(R.id.logout_button);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        changePin = view.findViewById(R.id.changePin);

        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the ChangePinActivity
                Intent intent = new Intent(getContext(), CheckPin.class);
                startActivity(intent);
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the confirmation dialog
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Log out the user from Firebase
                                FirebaseAuth.getInstance().signOut();

                                // Redirect to Home activity
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // Optional: Close the current activity
                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User cancelled the dialog, do nothing
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUID = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserUID);
        userRef.addValueEventListener(new ValueEventListener() { // Change here
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profilePicUrl = dataSnapshot.child("ProfileImage").getValue(String.class);
                    String full_name = dataSnapshot.child("Name").getValue(String.class);

                    // Load profile picture using Glide or any image loading library
                    RequestOptions options = new RequestOptions()
                            .circleCrop() // Make the image circular
                            .placeholder(R.drawable.logo); // Placeholder image while loading

                    Context context = getActivity(); // or getContext(), depending on where you are

                    // Load profile picture using Glide
                    Glide.with(context)
                            .load(profilePicUrl)
                            .apply(options)
                            .into(profileImageView); // Assume profileImageView is your ImageView in the fragment
                    name.setText(full_name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Log.e("UserProfile", "Database error: " + databaseError.getMessage());
            }
        });




        return view;


    }


}
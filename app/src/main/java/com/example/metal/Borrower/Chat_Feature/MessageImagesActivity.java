package com.example.metal.Borrower.Chat_Feature;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageImagesActivity extends AppCompatActivity {
    String chatroomId,nameString,profileUri;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<MessageModel> dataList;
    ImageButton back;
    MessageImagesActivityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_images);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.back);

        Intent intent = getIntent();
        if (intent != null) {
            chatroomId = intent.getStringExtra("chatRoomId");
            nameString = intent.getStringExtra("name");
            profileUri = intent.getStringExtra("profile");
        }
        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Initialize the ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(MessageImagesActivity.this);
        progressDialog.setMessage("Loading Images...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        dataList = new ArrayList<>();
        adapter = new MessageImagesActivityAdapter(MessageImagesActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("messages").child(chatroomId);

        eventListener = databaseReference.orderByChild("messageType").equalTo("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    MessageModel dataClass = itemSnapshot.getValue(MessageModel.class);
                    // Ensure that the message belongs to the correct chatRoomId
                    if (dataClass != null) {
                        dataClass.setKey(itemSnapshot.getKey());
                        dataList.add(dataClass);
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();  // Dismiss the ProgressDialog once data is loaded
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();  // Dismiss the ProgressDialog if an error occurs
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
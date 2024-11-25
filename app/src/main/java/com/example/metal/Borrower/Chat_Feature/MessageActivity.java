package com.example.metal.Borrower.Chat_Feature;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    String chatRoomId, number , secretaryPhone ,borrowerPhone, currentUserUid;
    private static final int CALL_PERMISSION_REQUEST_CODE = 1;
    ImageButton call, info, sendButton, imageSend, back;
    private ImageView profile,imageholder;
    String messageType;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText messageEditText;
    private TextView name,statusText;
    private DatabaseReference usersRef;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String imageURL = null,secretaryProfile ,secretaryName;
    private Uri uri = null;
    private List<MessageModel> messageList;

    private StorageReference storageReference;
    private static final int PERMISSION_REQUEST_CODE = 100;
    RelativeLayout imageRelativeHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        // Check for notification permission and request if not granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
        }
        // Check for notification channel and create if not exists
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "Default Channel";
            String channelDescription = "This is the default notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        // Initialize views
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        imageholder= findViewById(R.id.imageholder);
        imageRelativeHolder = findViewById(R.id.imageRelativeHolder);
        imageSend = findViewById(R.id.imgSend);
        recyclerView = findViewById(R.id.recycler_view);
        profile = findViewById(R.id.profile);
        back = findViewById(R.id.back);
        statusText = findViewById(R.id.statusText);
        name = findViewById(R.id.name);
        call = findViewById(R.id.call);
        info = findViewById(R.id.infoImages);
        // Get the value of chatroomid from the previous page
        Intent intent = getIntent();
        if (intent != null) {
            chatRoomId = intent.getStringExtra("chatRoomId");
        }
        // Set an OnClickListener for the info button
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent to start the desired activity
                Intent infoIntent = new Intent(MessageActivity.this, MessageImagesActivity.class);

                // Put the chatRoomId into the Intent
                infoIntent.putExtra("chatRoomId", chatRoomId);
                infoIntent.putExtra("name", secretaryName);
                infoIntent.putExtra("profile", secretaryProfile);
                // Start the new activity
                startActivity(infoIntent);
            }
        });
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();  // Initialize FirebaseAuth
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Load user details if chatRoomId is not null
        if (chatRoomId != null) {
            loadUserDetails(chatRoomId);
        }
        // logic for photo pick up
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uri = data != null ? data.getData() : null;
                        if (uri != null) {
                            // Load image into ImageView
                            imageRelativeHolder.setVisibility(View.VISIBLE);
                            imageholder.setImageURI(uri);
                        } else {
                            // If URI is null, show a toast indicating that the image cannot be loaded
                            Toast.makeText(MessageActivity.this, "Image cannot be loaded", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        imageRelativeHolder.setVisibility(View.GONE);
                        // If result is not OK, show a toast indicating that no image was selected
                        Toast.makeText(MessageActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                });
        // Initialize RecyclerView and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this,messageList);

        // Set the layout manager to stack from the end
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(messageAdapter);

        // Load existing messages from the chat room
        loadMessages();

        // Scroll to the bottom after the layout is complete
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.removeOnLayoutChangeListener(this);
                if (messageAdapter.getItemCount() > 0) {
                    recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                }
            }
        });


        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());

        imageSend.setOnClickListener(v -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });
        // Send button click listener
        sendButton.setOnClickListener(v -> {
            String messageText = messageEditText.getText().toString().trim();
            if (!messageText.isEmpty()) {
                // If message is not empty, send message
                sendMessage();
                DatabaseReference chatroomRef = mDatabase.child("chatrooms").child(chatRoomId);

                chatroomRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Assuming 'borrowerRead' is a child of 'chatrooms' node
                        String secretaryRead = dataSnapshot.child("statusSecretary").getValue(String.class);

                        // Update your TextView with the new value
                        statusText.setText(secretaryRead);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        Log.e("FirebaseError", "onCancelled", databaseError.toException());
                    }
                });
            } else if (uri != null) {
                // If message is empty but there's an image, save data
                saveData();
                DatabaseReference chatroomRef = mDatabase.child("chatrooms").child(chatRoomId);

                chatroomRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Assuming 'borrowerRead' is a child of 'chatrooms' node
                        String secretaryRead = dataSnapshot.child("statusSecretary").getValue(String.class);

                        // Update your TextView with the new value
                        statusText.setText(secretaryRead);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        Log.e("FirebaseError", "onCancelled", databaseError.toException());
                    }
                });
            } else {
                // If both message and image are empty, show toast message
                Toast.makeText(MessageActivity.this, "No message or image selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void loadMessages() {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(chatRoomId);
        messagesRef.limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel message = dataSnapshot.getValue(MessageModel.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                // Scroll to the bottom of the RecyclerView
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, "Failed to load messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // load user details
    private void loadUserDetails(String chatRoomId) {
        // Ensure mAuth is not null before using it
        if (mAuth != null) {
            final FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                final String currentUserUid = currentUser.getUid();
                DatabaseReference chatroomRef = mDatabase.child("chatrooms").child(chatRoomId);

                chatroomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve secretary details
                            String secretaryUid = dataSnapshot.child("secretaryId").getValue(String.class);
                            secretaryProfile = dataSnapshot.child("secretaryProfile").getValue(String.class);
                            secretaryName = dataSnapshot.child("secretaryName").getValue(String.class);
                            secretaryPhone = dataSnapshot.child("secretaryPhone").getValue(String.class);
                            // Retrieve borrower details
                            String borrowerUid = dataSnapshot.child("borrowerId").getValue(String.class);
                            String borrowerProfile = dataSnapshot.child("borrowerProfile").getValue(String.class);
                            String borrowerName = dataSnapshot.child("borrowerName").getValue(String.class);

                            String secretaryRead = dataSnapshot.child("statusSecretary").getValue(String.class);

                            borrowerPhone = dataSnapshot.child("borrowerPhone").getValue(String.class);
                            // Determine which user's details to show based on currentUserUid
                            if (currentUserUid.equals(borrowerUid)) {
                                // Display secretary's details
                                name.setText(secretaryName);
                                Picasso.get().load(secretaryProfile).into(profile);  // Load secretary's profile image
                                number  = secretaryPhone;
                                // Set OnClickListener for call button
                                call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!number.isEmpty()) {
                                            makeCall(number); // Call your method to make emergency call
                                        } else {
                                            Toast.makeText(MessageActivity.this, "Phone number is empty", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // Display borrower's details
                                name.setText(borrowerName);
                                Picasso.get().load(borrowerProfile).into(profile);  // Load borrower's profile image
                                number = borrowerPhone;
                                // Set OnClickListener for call button
                                call.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!number.isEmpty()) {
                                            makeCall(number); // Call your method to make emergency call
                                        } else {
                                            Toast.makeText(MessageActivity.this, "Phone number is empty", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            //check if may message na
                            DatabaseReference chatroomRef = mDatabase.child("messages").child(chatRoomId);

                            chatroomRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                                        // May laman ang database
                                        statusText.setText(secretaryRead);
                                    } else {
                                        // Walang laman ang database
                                        statusText.setText("");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Handle possible errors
                                    Log.e("FirebaseError", "onCancelled", databaseError.toException());
                                }
                            });

                        } else {
                            // Chatroom does not exist
                            name.setText("Chatroom does not exist");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Error loading user details
                        name.setText("Error loading user details: " + databaseError.getMessage());
                    }
                });
            } else {
                // Current user is null
                name.setText("Current user is null");
            }
        } else {
            // FirebaseAuth instance is null
            name.setText("FirebaseAuth instance is null");
        }
    }
    // Method to make call
    private void makeCall(String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);

        if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MessageActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(intent);
        }
    }
    // send message not image
    private void sendMessage() {
        // Get message text from EditText
        String messageText = messageEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Get sender's username from database
            usersRef.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String senderUsername = snapshot.child("Name").getValue(String.class);
                        String senderProfileImg = snapshot.child("ProfileImage").getValue(String.class);
                        long timestamp = System.currentTimeMillis();

                        if (senderUsername != null) {
                            // Set default message type
                            messageType = "text"; // Default type is text

                            // Create Message object with senderUsername and messageType
                            MessageModel message = new MessageModel(senderProfileImg, senderUid, senderUsername, messageText, timestamp, messageType);

                            // Save message to the database
                            saveMessageToDatabase(message);
                        } else {
                            Toast.makeText(getApplicationContext(), "Sender username is null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Snapshot does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                    Toast.makeText(getApplicationContext(), "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            messageEditText.setError("Message is required");
            messageEditText.requestFocus();
        }
    }
    //save the image in the storsge for easy access
//    private void saveData() {
//        if (uri != null) {
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                    .child("Conversation Images")
//                    .child(uri.getLastPathSegment());
//
//            // Remove AlertDialog - Use Snackbar instead
//            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Sending image...", Snackbar.LENGTH_INDEFINITE);
//            snackbar.show();
//
//            storageReference.putFile(uri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isComplete()) ;
//
//                        Uri urlImage = uriTask.getResult();
//                        imageURL = urlImage.toString();
//                        sendImageMessage();
//
//                        // Dismiss Snackbar when upload is successful
//                        snackbar.dismiss();
//
//                        // Optional: Reset imageSend ImageView
//                        imageRelativeHolder.setVisibility(View.GONE);
//                        uri = null;
//
//                        // Show success message using Snackbar
//                        Snackbar.make(findViewById(android.R.id.content), "Image sent successfully", Snackbar.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        // Dismiss Snackbar on failure
//                        snackbar.dismiss();
//
//                        // Handle failure
//                        Toast.makeText(MessageActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        } else {
//            Toast.makeText(MessageActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
//        }
//    }
    private void saveData() {
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Conversation Images")
                    .child(uri.getLastPathSegment());

            // Show DialogFragment
            LoadingDialogFragment loadingDialog = new LoadingDialogFragment();
            loadingDialog.show(getSupportFragmentManager(), "loading");

            storageReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());

                        Uri urlImage = uriTask.getResult();
                        imageURL = urlImage.toString();
                        sendImageMessage();

                        // Dismiss DialogFragment on success
                        loadingDialog.dismiss();

                        // Reset imageSend ImageView
                        imageRelativeHolder.setVisibility(View.GONE);
                        uri = null;

                        // Show success message using Toast
                        Toast.makeText(MessageActivity.this, "Image sent successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Dismiss DialogFragment on failure
                        loadingDialog.dismiss();

                        // Handle failure
                        Toast.makeText(MessageActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(MessageActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    // method for sending image
    private void sendImageMessage() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersRef.child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String senderUsername = snapshot.child("Name").getValue(String.class);
                    String senderProfileImg = snapshot.child("ProfileImage").getValue(String.class);
                    long timestamp = System.currentTimeMillis();

                    if (imageURL != null) {
                        messageType = "image"; // Default type is image

                        // Create Message object with senderUsername and messageType
                        MessageModel message = new MessageModel(senderProfileImg, senderUid, senderUsername, imageURL, timestamp, messageType);

                        // Save message to the database
                        saveMessageToDatabase(message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(MessageActivity.this, "Failed to send image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveMessageToDatabase(MessageModel message) {
        // Show sending message
        Toast sendingToast = Toast.makeText(this, "Sending message...", Toast.LENGTH_SHORT);
        sendingToast.show();

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(chatRoomId);
        String messageId = messagesRef.push().getKey();
        if (messageId != null) {
            messagesRef.child(messageId).setValue(message)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update last message and timestamp in the chat room
                            DatabaseReference chatRoomRef = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatRoomId);

                            Map<String, Object> lastMessageInfo = new HashMap<>();
                            lastMessageInfo.put("lastMessage", message.getText());
                            lastMessageInfo.put("lastMessageType", messageType);
                            lastMessageInfo.put("lastMessageTimestamp", System.currentTimeMillis());
                            lastMessageInfo.put("statusSecretary", "unread");
                            lastMessageInfo.put("statusBorrower", "read");

                            chatRoomRef.updateChildren(lastMessageInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Clear message edit text
                                            messageEditText.setText("");
                                            sendingToast.cancel();
                                            Toast.makeText(getApplicationContext(), "Message sent successfully", Toast.LENGTH_SHORT).show();

                                            // Show notification with message text
                                            // showNotification(message.getText());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            sendingToast.cancel();
                                            Toast.makeText(getApplicationContext(), "Error updating last message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            sendingToast.cancel();
                            Toast.makeText(getApplicationContext(), "Error saving message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            sendingToast.cancel();
            Toast.makeText(this, "Message ID is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Notification permission granted, proceed with showing notifications
                } else {
                    // Notification permission denied, handle accordingly (e.g., show message to user)
                }
                break;
        }
    }

}
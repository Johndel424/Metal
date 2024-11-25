package com.example.metal.Secretary.Secretary_Chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class secretaryChatListAdapter extends RecyclerView.Adapter<MyChatViewHolder> {
    private Context context;
    private List<secretaryChatRoomModel> dataList;
    public secretaryChatListAdapter(Context context, List<secretaryChatRoomModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secretary_chat_item, parent, false);
        return new MyChatViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyChatViewHolder holder, int position) {
        secretaryChatRoomModel chatRoom = dataList.get(position);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Long lastMessageTimestamp = chatRoom.getLastMessageTimestamp();

// Check if lastMessageTimestamp is not null
        if (lastMessageTimestamp != null) {
            // Get the current time in milliseconds
            long currentTime = System.currentTimeMillis();

            // Calculate the difference in milliseconds
            long timeDifference = currentTime - lastMessageTimestamp;

            // Convert the time difference into days
            long daysPassed = timeDifference / (1000 * 60 * 60 * 24);  // 1 day in milliseconds

            // If the message is from the same day, show the time
            if (daysPassed == 0) {
                // Get the time and format it
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String time = sdf.format(new Date(lastMessageTimestamp));
                holder.timestamp.setText(time);
            } else {
                // Display the number of days passed
                holder.timestamp.setText(daysPassed + " days ago");
            }
        }


        holder.name.setText(chatRoom.getBorrowerName());

        String lastMessage = chatRoom.getLastMessage();
        String messageType = chatRoom.getLastMessageType();

        if (messageType != null && messageType.equals("image")) {
            holder.lastmesssage.setText("Photo");
        } else {
            // Check if the message length is more than 10 characters
            if (lastMessage.length() > 10) {
                // Trim the message to 10 characters and add "..."
                holder.lastmesssage.setText(lastMessage.substring(0, 10) + "...");
            } else {
                // If the message is 10 characters or less, show it as it is
                holder.lastmesssage.setText(lastMessage);
            }
        }



        Glide.with(context).load(chatRoom.getBorrowerProfile()).into(holder.profile);

//        // Set the typeface based on the status
//        if (chatRoom.getStatusSecretary().equals("unread")) {
//            holder.timestamp.setTypeface(null, Typeface.BOLD);
//            holder.name.setTypeface(null, Typeface.BOLD);
//            holder.lastmesssage.setTypeface(null, Typeface.BOLD);
//        } else {
//            holder.lastmesssage.setTypeface(null, Typeface.NORMAL);
//            holder.timestamp.setTypeface(null, Typeface.NORMAL);
//            holder.name.setTypeface(null, Typeface.NORMAL);
//        }



        String userId = chatRoom.getSecretaryId();
        String otherId = chatRoom.getBorrowerId();

        String chatRoomId;
        if (userId.compareTo(otherId) > 0) {
            chatRoomId = userId + "_" + otherId;
        } else {
            chatRoomId = otherId + "_" + userId;
        }

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the value of statusSecretary in Firebase
                DatabaseReference chatroomRef = FirebaseDatabase.getInstance().getReference("chatrooms").child(chatRoomId);
                chatroomRef.child("statusSecretary").setValue("read").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // If the update is successful, proceed to start the activity
                            Intent intent = new Intent(context, SecretaryChat.class);
                            intent.putExtra("chatRoomId", chatRoomId);
                            context.startActivity(intent);
                        } else {
                            // Handle the error if the update fails
                            Log.e("FirebaseError", "Error updating statusSecretary", task.getException());
                            Toast.makeText(context, "Error updating status", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<secretaryChatRoomModel> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }

}
class MyChatViewHolder extends RecyclerView.ViewHolder{
    ImageView profile;
    TextView name,lastmesssage,timestamp ;
    RelativeLayout recCard;
    public MyChatViewHolder(@NonNull View itemView) {
        super(itemView);


        recCard = itemView.findViewById(R.id.recCard);
        name = itemView.findViewById(R.id.name);
        profile = itemView.findViewById(R.id.profile);
        lastmesssage = itemView.findViewById(R.id.lastmesssage);
        timestamp = itemView.findViewById(R.id.timestamp);

    }
}

package com.example.metal.Borrower.Chat_Feature;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metal.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private List<MessageModel> messageList;
    public MessageAdapter(Context context, List<MessageModel> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentUserUID = mAuth.getCurrentUser().getUid();

        if (message.getSenderUid().equals(currentUserUID)) {
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.otherLayout.setVisibility(View.GONE);
            if (message.getMessageType().equals("image")) {
                holder.userImageLayout.setVisibility(View.VISIBLE);
                holder.userImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create an intent to open FullScreenImageActivity
                        Intent intent = new Intent(context, MessageImagesActivityFullScreen.class);

                        // Pass the image URL to the intent
                        intent.putExtra("image_url", message.getText()); // Assuming dataClass contains the image URL

                        // Start the fullscreen activity
                        context.startActivity(intent);
                    }
                });
                holder.userTextLayout.setVisibility(View.GONE);
                Glide.with(holder.itemView.getContext()).load(message.getText()).into(holder.userImageText);
                Glide.with(holder.itemView.getContext()).load(message.getSenderProfileImg()).into(holder.userProfile);
            } else {
                holder.userImageLayout.setVisibility(View.GONE);
                holder.userTextLayout.setVisibility(View.VISIBLE);
                holder.userText.setText(message.getText());
                Glide.with(holder.itemView.getContext()).load(message.getSenderProfileImg()).into(holder.userProfile);
            }
        } else {
            holder.otherLayout.setVisibility(View.VISIBLE);
            holder.userLayout.setVisibility(View.GONE);
            if (message.getMessageType().equals("image")) {
                holder.otherImageLayout.setVisibility(View.VISIBLE);
                holder.otherImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Create an intent to open FullScreenImageActivity
                        Intent intent = new Intent(context, MessageImagesActivityFullScreen.class);

                        // Pass the image URL to the intent
                        intent.putExtra("image_url", message.getText()); // Assuming dataClass contains the image URL

                        // Start the fullscreen activity
                        context.startActivity(intent);
                    }
                });
                holder.otherTextLayout.setVisibility(View.GONE);
                Glide.with(holder.itemView.getContext()).load(message.getText()).into(holder.otherImageText);
                Glide.with(holder.itemView.getContext()).load(message.getSenderProfileImg()).into(holder.otherProfile);
            } else {
                holder.otherImageLayout.setVisibility(View.GONE);
                holder.otherTextLayout.setVisibility(View.VISIBLE);
                holder.otherText.setText(message.getText());
                holder.otherName.setText(message.getSenderUsername());
                Glide.with(holder.itemView.getContext()).load(message.getSenderProfileImg()).into(holder.otherProfile);
            }
        }
        // time to show
        holder.userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the timestamp as a Date object
                Date timestampDate = new Date(message.getTimestamp());

                // Get current date
                Calendar calendar = Calendar.getInstance();
                Calendar currentCalendar = Calendar.getInstance();

                // Set calendar to the timestamp date
                calendar.setTime(timestampDate);

                // Check if the timestamp is from today
                boolean isToday = (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR));

                // Format the timestamp including time if not today
                String formattedTimestamp;
                if (isToday) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    formattedTimestamp = "Today " + timeFormat.format(timestampDate);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm a", Locale.getDefault());
                    formattedTimestamp = dateFormat.format(timestampDate);
                }


                // Toggle visibility of the timestamp TextView
                if (holder.timestampTextViewLayout.getVisibility() == View.VISIBLE) {
                    holder.timestampTextViewLayout.setVisibility(View.GONE);
                } else {
                    holder.timestampTextView.setText(formattedTimestamp);
                    holder.timestampTextViewLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        // shoshow time pag naclcick
        holder.otherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the timestamp as a Date object
                Date timestampDate = new Date(message.getTimestamp());

                // Get current date
                Calendar calendar = Calendar.getInstance();
                Calendar currentCalendar = Calendar.getInstance();

                // Set calendar to the timestamp date
                calendar.setTime(timestampDate);

                // Check if the timestamp is from today
                boolean isToday = (calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                        calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR));

                // Format the timestamp including time if not today
                String formattedTimestamp;
                if (isToday) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    formattedTimestamp = "Today " + timeFormat.format(timestampDate);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm a", Locale.getDefault());
                    formattedTimestamp = dateFormat.format(timestampDate);
                }

                // Toggle visibility of the timestamp TextView
                if (holder.timestampTextViewLayout.getVisibility() == View.VISIBLE) {
                    holder.timestampTextViewLayout.setVisibility(View.GONE);
                } else {
                    holder.timestampTextView.setText(formattedTimestamp);
                    holder.timestampTextViewLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout userLayout, otherLayout, userImageLayout, userTextLayout,otherImageLayout,otherTextLayout;
        ImageView otherProfile, userProfile, userImageText,otherImageText;
        TextView userText, otherText, timestampTextView,otherName;
        LinearLayout timestampTextViewLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            userLayout = itemView.findViewById(R.id.userLayout);
            userImageLayout = itemView.findViewById(R.id.userImageLayout);
            userTextLayout = itemView.findViewById(R.id.userTextLayout);
            userProfile = itemView.findViewById(R.id.userProfile);
            userText = itemView.findViewById(R.id.userText);
            userImageText = itemView.findViewById(R.id.userImageText);
            timestampTextViewLayout = itemView.findViewById(R.id.timestampTextViewLayout);

            otherLayout = itemView.findViewById(R.id.otherLayout);
            otherTextLayout = itemView.findViewById(R.id.otherTextLayout);
            otherImageLayout = itemView.findViewById(R.id.otherImageLayout);
            otherProfile = itemView.findViewById(R.id.otherProfile);
            otherText = itemView.findViewById(R.id.otherText);
            otherImageText = itemView.findViewById(R.id.otherImageText);
            timestampTextView= itemView.findViewById(R.id.timestampTextView);
            otherName= itemView.findViewById(R.id.otherName);
        }
    }
}

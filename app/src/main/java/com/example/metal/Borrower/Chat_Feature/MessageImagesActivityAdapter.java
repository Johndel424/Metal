package com.example.metal.Borrower.Chat_Feature;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metal.R;

import java.util.List;

public class MessageImagesActivityAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<MessageModel> dataList;
    public MessageImagesActivityAdapter(Context context, List<MessageModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_image, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String imageUrl = dataList.get(position).getText();
        Glide.with(context).load(dataList.get(position).getText()).into(holder.recImage);
        // Set an OnClickListener on the image
        holder.recImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start FullscreenImageActivity
                Intent intent = new Intent(context, MessageImagesActivityFullScreen.class);
                intent.putExtra("image_url", imageUrl);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
    }
}

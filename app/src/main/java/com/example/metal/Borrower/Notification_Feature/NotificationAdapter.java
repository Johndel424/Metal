package com.example.metal.Borrower.Notification_Feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private Context context;
    private List<NotificationModel> dataList;
    public NotificationAdapter(Context context, List<NotificationModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = dataList.get(position);

        holder.textMessage.setText(notificationModel.getMessage());
        holder.textRemark.setText(notificationModel.getRemark());

        long timestamp = notificationModel.getTimestamp(); // Siguraduhing ang method ay nagbabalik ng long timestamp

        // I-format ang timestamp
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        holder.textTimestamp.setText(formattedDate);

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
class NotificationViewHolder extends RecyclerView.ViewHolder{
    TextView textMessage,textRemark ,textTimestamp;
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        textMessage = itemView.findViewById(R.id.textMessage);
        textRemark = itemView.findViewById(R.id.textRemark);
        textTimestamp = itemView.findViewById(R.id.textTimestamp);

    }
}
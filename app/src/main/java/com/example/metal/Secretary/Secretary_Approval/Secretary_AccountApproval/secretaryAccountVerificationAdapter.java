package com.example.metal.Secretary.Secretary_Approval.Secretary_AccountApproval;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metal.Borrower.Profile_Feature.UserModel;
import com.example.metal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class secretaryAccountVerificationAdapter extends RecyclerView.Adapter<AccountVerificationViewHolder> {
    private Context context;
    private List<UserModel> dataList;
    public secretaryAccountVerificationAdapter(Context context, List<UserModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public AccountVerificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secretary_account_verification_item, parent, false);
        return new AccountVerificationViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AccountVerificationViewHolder holder, int position) {
        UserModel userModel = dataList.get(position);
        String uid = userModel.getUid();

        //address
        String city = userModel.getCity();
        String province = userModel.getProvince();


        holder.name.setText(userModel.getName());
        holder.address.setText(city + " " + province);
        Glide.with(context).load(userModel.getProfileImage()).into(holder.profile);

        long timestamp = userModel.getTimestamp(); // Siguraduhing ang method ay nagbabalik ng long timestamp

        // I-format ang timestamp
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        holder.applicationDate.setText(formattedDate);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lumikha ng isang Intent para sa DetailActivity
                Intent intent = new Intent(context, SecretaryAccountVerificationDetail.class);

                // Ilagay ang chatroom ID bilang isang extra sa Intent
                intent.putExtra("uid", uid);
                intent.putExtra("status", userModel.getAccountStatus());

                // Simulan ang DetailActivity gamit ang Intent
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<UserModel> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }

}
class AccountVerificationViewHolder extends RecyclerView.ViewHolder{
    ImageView profile;
    TextView name,address ,applicationDate;
    RelativeLayout recCard;
    ImageView view;
    public AccountVerificationViewHolder(@NonNull View itemView) {
        super(itemView);


        recCard = itemView.findViewById(R.id.recCard);
        name = itemView.findViewById(R.id.name);
        profile = itemView.findViewById(R.id.profile);
        address = itemView.findViewById(R.id.address);
        view = itemView.findViewById(R.id.view);
        applicationDate = itemView.findViewById(R.id.applicationDate);

    }
}


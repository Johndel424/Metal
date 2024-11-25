package com.example.metal.Secretary.Secretary_Approval.Secretary_LoanApproval;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metal.R;
import com.example.metal.Secretary.Secretary_Approval.Secretary_AccountApproval.SecretaryAccountVerificationDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SecretaryLoanApprovalAdapter  extends RecyclerView.Adapter<secretaryApprovedLoanViewHolder> {
    private Context context;
    private List<SecretaryLoanModel> dataList;
    public SecretaryLoanApprovalAdapter(Context context, List<SecretaryLoanModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public secretaryApprovedLoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secretary_approved_loan_item, parent, false);
        return new secretaryApprovedLoanViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull secretaryApprovedLoanViewHolder holder, int position) {
        SecretaryLoanModel loanModel = dataList.get(position);
        String loanId = loanModel.getLoanId();

        holder.name.setText(loanModel.getUserName());
        String address = loanModel.getUserAddress();
        if (address.length() > 12) {
            address = address.substring(0, 12) + "...";
        }
        holder.address.setText(address);

        Glide.with(context).load(loanModel.getUserProfile()).into(holder.profile);

        long timestamp = loanModel.getTimestamp(); // Siguraduhing ang method ay nagbabalik ng long timestamp

        // I-format ang timestamp
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        holder.applicationDate.setText(formattedDate);


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lumikha ng isang Intent para sa DetailActivity
                Intent intent = new Intent(context, SecretaryLoanVerificationDetail.class);

                // Ilagay ang chatroom ID bilang isang extra sa Intent
                intent.putExtra("loanId", loanId);
                intent.putExtra("uid", loanModel.getUserId());
                intent.putExtra("loanAmount", loanModel.getLoanAmount());
                intent.putExtra("totalLoanAmount", loanModel.getTotalRepayment());
                intent.putExtra("daily", loanModel.getDailyInstallment());
                intent.putExtra("initialPayout", loanModel.getLoanAmount());

                // Simulan ang DetailActivity gamit ang Intent
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<SecretaryLoanModel> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }

}
class secretaryApprovedLoanViewHolder extends RecyclerView.ViewHolder{
    ImageView profile;
    TextView name,address ,applicationDate;
    RelativeLayout recCard;
    ImageView view;
    public secretaryApprovedLoanViewHolder(@NonNull View itemView) {
        super(itemView);

        recCard = itemView.findViewById(R.id.recCard);
        name = itemView.findViewById(R.id.name);
        profile = itemView.findViewById(R.id.profile);
        address = itemView.findViewById(R.id.address);
        view = itemView.findViewById(R.id.view);
        applicationDate = itemView.findViewById(R.id.applicationDate);

    }
}
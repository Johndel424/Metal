package com.example.metal.Secretary.Secretary_Capital;

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

public class SecretaryCapitalAdapter  extends RecyclerView.Adapter<secretaryCapitalViewHolder> {
    private Context context;
    private List<UserModel> dataList;
    public SecretaryCapitalAdapter(Context context, List<UserModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public secretaryCapitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secretary_capital_item, parent, false);
        return new secretaryCapitalViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull secretaryCapitalViewHolder holder, int position) {
        UserModel userModel = dataList.get(position);

        holder.name.setText(userModel.getName());
        holder.currentLoan.setText(String.format("₱%,d", userModel.getCurrentLoan()));
        double previousBalance = userModel.getPreviousLoanBalance();
        holder.previousBalance.setText(String.format("₱%s",
                (previousBalance % 1 == 0) ? String.valueOf((int) previousBalance) : String.format("%.2f", previousBalance)));



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
class secretaryCapitalViewHolder extends RecyclerView.ViewHolder{

    TextView name,previousBalance ,currentLoan;

    public secretaryCapitalViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        currentLoan = itemView.findViewById(R.id.currentLoan);
        previousBalance = itemView.findViewById(R.id.previousBalance);

    }
}

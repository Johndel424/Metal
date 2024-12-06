package com.example.metal.Secretary.Secretary_LoanHistory;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.Borrower.LoanHistory_Feature.LoanHistoryDetail;
import com.example.metal.Borrower.LoanHistory_Feature.LoanHistoryModel;
import com.example.metal.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class SecretaryLoanHistoryAdapter extends RecyclerView.Adapter<SecretaryLoanHistoryViewHolder> {
    private Context context;
    private List<LoanHistoryModel> dataList;
    public SecretaryLoanHistoryAdapter(Context context, List<LoanHistoryModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SecretaryLoanHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secretary_layout_loan_history_item, parent, false);
        return new SecretaryLoanHistoryViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SecretaryLoanHistoryViewHolder holder, int position) {
        LoanHistoryModel LoanHistoryModel = dataList.get(position);


        String startDateStr = LoanHistoryModel.getApprovedStartDate(); // Should return yyyy-MM-dd format
        String endDateStr = LoanHistoryModel.getApprovedEndDate(); // Should return yyyy-MM-dd format

        LocalDate startDate = null;
        LocalDate endDate = null;

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.getDefault());

        try {
            // Parse the start date
            if (startDateStr != null) {
                startDate = LocalDate.parse(startDateStr, inputFormatter);
            }

            // Parse the end date
            if (endDateStr != null) {
                endDate = LocalDate.parse(endDateStr, inputFormatter);
            }
        } catch (DateTimeParseException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        // Format the dates to the desired output format
        String formattedStartDate = (startDate != null) ? startDate.format(outputFormatter) : "N/A"; // Fallback if null
        String formattedEndDate = (endDate != null) ? endDate.format(outputFormatter) : "N/A"; // Fallback if null

        holder.loanId.setText("Id: " + LoanHistoryModel.getStartDate());
        holder.name.setText(LoanHistoryModel.getUserName());
        holder.startDate.setText(formattedStartDate);
        holder.endDate.setText(formattedEndDate);

        String loanAmount = String.format("%.2f", LoanHistoryModel.getLoanAmount());
        String dailyAmount = String.format("%.2f", LoanHistoryModel.getDailyInstallment());

        holder.totalLoan.setText(loanAmount);
        holder.dailyPayment.setText(dailyAmount);
        // Get the status from the LoanHistoryModel
        String status = LoanHistoryModel.getStatus();

        // Check if the status is "accepted" or "Ongoing" and set the output accordingly
        if ("accepted".equalsIgnoreCase(status)) {
            holder.status.setText("Ongoing");
        } else {
            holder.status.setText(status); // Display the original status if it's neither
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoanHistoryDetail2.class);
                intent.putExtra("loanId", LoanHistoryModel.getLoanId());
                // Start the new activity
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }





}
class SecretaryLoanHistoryViewHolder extends RecyclerView.ViewHolder{

    TextView loanId, startDate,endDate,totalLoan,dailyPayment,status,name;
    LinearLayout item;
    public SecretaryLoanHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        status = itemView.findViewById(R.id.status);
        dailyPayment = itemView.findViewById(R.id.dailyPayment);
        totalLoan = itemView.findViewById(R.id.totalLoan);
        endDate = itemView.findViewById(R.id.endDate);
        startDate = itemView.findViewById(R.id.startDate);
        loanId = itemView.findViewById(R.id.loanId);
        item = itemView.findViewById(R.id.item);
        name = itemView.findViewById(R.id.name);
    }
}

package com.example.metal.Borrower.LoanHistory_Feature;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LoanHistoryAdapter extends RecyclerView.Adapter<LoanHistoryViewHolder> {
    private Context context;
    private List<LoanHistoryModel> dataList;
    public LoanHistoryAdapter(Context context, List<LoanHistoryModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public LoanHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loan_history_item, parent, false);
        return new LoanHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanHistoryViewHolder holder, int position) {
        LoanHistoryModel LoanHistoryModel = dataList.get(position);


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

// Get the start and end date strings
        String startDateStr = LoanHistoryModel.getApprovedStartDate();
        String endDateStr = LoanHistoryModel.getApprovedEndDate();

        String formattedStartDate = null;
        String formattedEndDate = null;

// Check if the date strings are not null or empty
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // For Android O and above, use LocalDate and DateTimeFormatter
                try {
                    LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
                    LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("MMMM dd, yyyy"));

                    // Format the dates
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
                    formattedStartDate = startDate.format(formatter);
                    formattedEndDate = endDate.format(formatter);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
            } else {
                // For Android versions lower than O, use SimpleDateFormat
                try {
                    Date startDate = dateFormat.parse(startDateStr);
                    Date endDate = dateFormat.parse(endDateStr);

                    // Format the dates
                    formattedStartDate = dateFormat.format(startDate);
                    formattedEndDate = dateFormat.format(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Handle cases where dates are null or empty
            formattedStartDate = "Start date not available";
            formattedEndDate = "End date not available";
        }

// Set the text to the views
        holder.loanId.setText("Id: " + LoanHistoryModel.getStartDate());
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
                Intent intent = new Intent(context, LoanHistoryDetail.class);
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
class LoanHistoryViewHolder extends RecyclerView.ViewHolder{

    TextView loanId, startDate,endDate,totalLoan,dailyPayment,status;
    LinearLayout item;
    public LoanHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        status = itemView.findViewById(R.id.status);
        dailyPayment = itemView.findViewById(R.id.dailyPayment);
        totalLoan = itemView.findViewById(R.id.totalLoan);
        endDate = itemView.findViewById(R.id.endDate);
        startDate = itemView.findViewById(R.id.startDate);
        loanId = itemView.findViewById(R.id.loanId);
        item = itemView.findViewById(R.id.item);

    }
}

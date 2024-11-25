package com.example.metal.Secretary.Secretary_PassDue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.Borrower.LoanHistory_Feature.LoanHistoryModel;
import com.example.metal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PassDueAdapter extends RecyclerView.Adapter<PassDueViewHolder> {
    private Context context;
    private List<LoanHistoryModel> dataList;
    public PassDueAdapter(Context context, List<LoanHistoryModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public PassDueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pass_due_item, parent, false);
        return new PassDueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassDueViewHolder holder, int position) {
        LoanHistoryModel LoanHistoryModel = dataList.get(position);

        String userId = LoanHistoryModel.getUserId();

        // Fetch the previous balance from Firebase
        // Fetch previousLoanBalance and currentLoan from Firebase
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve previousLoanBalance
        usersRef.child(userId).child("previousLoanBalance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double previousBalance = dataSnapshot.getValue(Double.class);
                    String formattedBalance = String.format("%.2f", previousBalance);
                    holder.balance.setText(formattedBalance);
                } else {
                    holder.balance.setText("0.00"); // Default value if it doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });

        // Retrieve currentLoan
        usersRef.child(userId).child("currentLoan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double currentLoan = dataSnapshot.getValue(Double.class);
                    String formattedCurrentLoan = String.format("%.2f", currentLoan);
                    holder.totalLoan.setText(formattedCurrentLoan);
                } else {
                    holder.totalLoan.setText("0.00"); // Default value if it doesn't exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });


        holder.loanId.setText("Loan Id: " + LoanHistoryModel.getStartDate());

        holder.name.setText(LoanHistoryModel.getUserName());
        // Get the status from the LoanHistoryModel
        String status = LoanHistoryModel.getStatus();

        // Check if the status is "accepted" or "Ongoing" and set the output accordingly
        if ("accepted".equalsIgnoreCase(status)) {
            holder.status.setText("Ongoing");
        } else {
            holder.status.setText(status); // Display the original status if it's neither
        }

        holder.submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming you have the loanId for the current loan
                String loanId = LoanHistoryModel.getLoanId(); // Adjust based on your data class structure
                String userId = LoanHistoryModel.getUserId(); // Get user ID associated with the loan

                // Create a reference to the loan node
                DatabaseReference loanRef = FirebaseDatabase.getInstance().getReference("loans").child(loanId);
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                // Update the loan status to "complete"
                loanRef.child("status").setValue("Complete").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // If loan status updated successfully, now update the user's loan balances
                            usersRef.child("currentLoan").setValue(0);
                            usersRef.child("previousLoanBalance").setValue(0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Optionally show a success message or update UI
                                        Toast.makeText(context, "Loan completed and balances reset.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Handle errors for user balance update
                                        Log.e("FirebaseError", "Failed to update user's loan balances: " + task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            // Handle errors for loan status update
                            Log.e("FirebaseError", "Failed to update loan status: " + task.getException().getMessage());
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





}
class PassDueViewHolder extends RecyclerView.ViewHolder{

    TextView loanId,totalLoan,balance,status,name,submit_button;
    public PassDueViewHolder(@NonNull View itemView) {
        super(itemView);

        status = itemView.findViewById(R.id.status);
        balance = itemView.findViewById(R.id.balance);
        totalLoan = itemView.findViewById(R.id.totalLoan);
        loanId = itemView.findViewById(R.id.loanId);
        submit_button = itemView.findViewById(R.id.submit_button);
        name = itemView.findViewById(R.id.name);

    }
}

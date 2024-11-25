package com.example.metal.Secretary.Secretary_DataAnalytics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.metal.CustomCircularProgressBarAll;
import com.example.metal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecretaryData extends Fragment {
    CustomCircularProgressBarAll loanCircularProgressBar;
    private TextView paid, unpaid,userWithLoan,userWithOutLoan,totalLoanpaid,totalLoanUnpaid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_secretary_data, container, false);
        paid = view.findViewById(R.id.paid);
        unpaid = view.findViewById(R.id.unpaid);
        userWithLoan = view.findViewById(R.id.userWithLoan);
        userWithOutLoan = view.findViewById(R.id.userWithOutLoan);
        totalLoanpaid = view.findViewById(R.id.totalLoanpaid);
        totalLoanUnpaid = view.findViewById(R.id.totalLoanUnpaid);
        loanCircularProgressBar = view.findViewById(R.id.loanCircularProgressBar);
        // Initialize TextViews
        fetchLoanData();
        TextView loan = view.findViewById(R.id.loan);
        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new SecretaryDataUser();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        TextView user = view.findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new SecretaryData();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        return view;


    }
    private void fetchLoanData() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference loansRef = FirebaseDatabase.getInstance().getReference("loans");

        // Fetching users data
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalUsers = 0;
                int borrowersWithLoans = 0;
                int borrowersWithoutLoans = 0;
                double totalLoanAmount = 0; // Total amount loaned (currentLoan)
                double totalRemainingLoanBalance = 0; // Remaining unpaid loan amount (prevLoanBalance)

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Fetching userType, currentLoan, and prevLoanBalance directly from the snapshot
                    String userType = snapshot.child("usertype").getValue(String.class);
                    Double currentLoan = snapshot.child("currentLoan").getValue(Double.class);
                    Double prevLoanBalance = snapshot.child("previousLoanBalance").getValue(Double.class); // Remaining loan

                    if (userType != null && "borrower".equals(userType)) {
                        totalUsers++; // Increment total borrower count

                        // Calculate total loan amount (currentLoan)
                        if (currentLoan != null) {
                            totalLoanAmount += currentLoan; // Summing up total loan amounts
                        }

                        // Calculate remaining unpaid loan balance (prevLoanBalance)
                        if (prevLoanBalance != null) {
                            totalRemainingLoanBalance += prevLoanBalance; // Summing up remaining unpaid loan amounts
                        }

                        // Check if borrower has a loan
                        if (currentLoan != null && currentLoan > 0) {
                            borrowersWithLoans++; // Increment borrowers with loans
                        } else {
                            borrowersWithoutLoans++; // Increment borrowers without loans
                        }
                    }
                }

                // Calculate percentages for users with/without loans
                double percentageWithLoans = totalUsers > 0 ? (borrowersWithLoans * 100.0) / totalUsers : 0;
                double percentageWithoutLoans = totalUsers > 0 ? (borrowersWithoutLoans * 100.0) / totalUsers : 0;
                double percentagePaid = totalLoanAmount > 0 ? ((totalLoanAmount - totalRemainingLoanBalance) * 100.0) / totalLoanAmount : 0;
                double percentageUnpaid = totalLoanAmount > 0 ? (totalRemainingLoanBalance * 100.0) / totalLoanAmount : 0;

                // Fetching loans data
                int finalTotalUsers = totalUsers;
                int finalBorrowersWithLoans = borrowersWithLoans;
                int finalBorrowersWithoutLoans = borrowersWithoutLoans;
                double finalTotalLoanAmount = totalLoanAmount;
                double finalTotalRemainingLoanBalance = totalRemainingLoanBalance;
                loansRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int totalLoans = 0;
                        int pendingLoans = 0;
                        int completeLoans = 0;
                        int rejectedLoans = 0;
                        int pastDueLoans = 0;
                        int acceptedLoans = 0;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String loanStatus = snapshot.child("status").getValue(String.class);

                            // Increment counts based on loan status
                            if (loanStatus != null) {
                                totalLoans++; // Increment total loans
                                switch (loanStatus) {
                                    case "pending":
                                        pendingLoans++;
                                        break;
                                    case "Complete":
                                        completeLoans++;
                                        break;
                                    case "rejected":
                                        rejectedLoans++;
                                        break;
                                    case "Past Due":
                                        pastDueLoans++;
                                        break;
                                    case "accepted":
                                        acceptedLoans++;
                                        break;
                                }
                            }
                        }

                        // Calculate loan status percentages
                        double percentagePending = totalLoans > 0 ? (pendingLoans * 100.0) / totalLoans : 0;
                        double percentageComplete = totalLoans > 0 ? (completeLoans * 100.0) / totalLoans : 0;
                        double percentageRejected = totalLoans > 0 ? (rejectedLoans * 100.0) / totalLoans : 0;
                        double percentagePastDue = totalLoans > 0 ? (pastDueLoans * 100.0) / totalLoans : 0;
                        double percentageAccepted = totalLoans > 0 ? (acceptedLoans * 100.0) / totalLoans : 0;

                        // Update your TextView with the results for both users and loans
                        String result = "Total Users: " + finalTotalUsers +
                                "\nBorrowers with Loans: " + finalBorrowersWithLoans +
                                " (" + String.format("%.2f", percentageWithLoans) + "%)" +
                                "\nBorrowers without Loans: " + finalBorrowersWithoutLoans +
                                " (" + String.format("%.2f", percentageWithoutLoans) + "%)" +
                                "\nTotal Loan Amount: " + String.format("%.2f", finalTotalLoanAmount) +
                                "\nRemaining Loan Balance: " + String.format("%.2f", finalTotalRemainingLoanBalance) +
                                "\nPercentage Paid: " + String.format("%.2f", percentagePaid) + "%" +
                                "\nPercentage Unpaid: " + String.format("%.2f", percentageUnpaid) + "%" +
                                "\nTotal Loans: " + totalLoans +
                                "\nPending: " + pendingLoans +
                                " (" + String.format("%.2f", percentagePending) + "%)" +
                                "\nComplete: " + completeLoans +
                                " (" + String.format("%.2f", percentageComplete) + "%)" +
                                "\nRejected: " + rejectedLoans +
                                " (" + String.format("%.2f", percentageRejected) + "%)" +
                                "\nPast Due: " + pastDueLoans +
                                " (" + String.format("%.2f", percentagePastDue) + "%)" +
                                "\nAccepted: " + acceptedLoans +
                                " (" + String.format("%.2f", percentageAccepted) + "%)";

                        // Calculate percentages
                        float borrowersWithLoansPercentage = finalTotalUsers > 0 ? ((float) finalBorrowersWithLoans / finalTotalUsers) * 100 : 0;
                        float borrowersWithoutLoansPercentage = finalTotalUsers > 0 ? ((float) (finalTotalUsers - finalBorrowersWithLoans) / finalTotalUsers) * 100 : 0;
                        loanCircularProgressBar.setPaidPercentage((float) borrowersWithLoansPercentage);
                        loanCircularProgressBar.setUnpaidPercentage((float) borrowersWithoutLoansPercentage);



                        paid.setText(String.format("%.2f", percentageComplete) + "%" );
                        unpaid.setText(String.format("%.2f", percentagePastDue) + "%");
                        userWithLoan.setText(String.format("%.2f", percentageWithLoans) + "%" );
                        userWithOutLoan.setText(String.format("%.2f", percentageWithoutLoans) + "%");
                        totalLoanpaid.setText(String.format("%.2f", percentagePaid) + "%" );
                        totalLoanUnpaid.setText(String.format("%.2f", percentageUnpaid) + "%");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle possible errors
                        Log.e("FirebaseError", databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }





//    private void fetchLoanData() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference usersRef = database.getReference("users");
//
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int totalBorrowers = 0;
//                int borrowersWithLoans = 0;
//
//                // Loop through each user and count borrowers
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String userType = snapshot.child("usertype").getValue(String.class); // Get the user type
//                    if ("borrower".equals(userType)) { // Check if user is a borrower
//                        totalBorrowers++; // Increment total borrowers
//                        Double loan = snapshot.child("currentLoan").getValue(Double.class);
//
//                        // Check if the borrower has a current loan
//                        if (loan != null && loan > 0) {
//                            borrowersWithLoans++; // Increment borrowers with loans
//                        }
//                    }
//                }
//
//                // Calculate percentages
//                float borrowersWithoutLoans = totalBorrowers - borrowersWithLoans;
//                float borrowersWithLoansPercentage = totalBorrowers > 0 ? ((float) borrowersWithLoans / totalBorrowers) * 100 : 0;
//                float borrowersWithoutLoansPercentage = totalBorrowers > 0 ? ((float) borrowersWithoutLoans / totalBorrowers) * 100 : 0;
//
//                // Set the calculated percentages to the circular progress bar
//                loanCircularProgressBar.setUsersWithLoansPercentage(borrowersWithLoansPercentage);
//                loanCircularProgressBar.setUsersWithoutLoansPercentage(borrowersWithoutLoansPercentage);
//
//                // You can set other percentages as needed (e.g., for paid/unpaid loans, etc.)
//                // Assuming you still want to calculate total loan and balance
//                // You may include the previous logic for paid/unpaid percentages here if needed.
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("FirebaseError", "Error getting data", databaseError.toException());
//            }
//        });
//    }







}

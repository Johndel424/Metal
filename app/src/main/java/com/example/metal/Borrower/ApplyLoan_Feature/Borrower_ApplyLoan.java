package com.example.metal.Borrower.ApplyLoan_Feature;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.metal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Borrower_ApplyLoan extends Fragment {

    private Spinner loanAmountSpinner;
    private TextView submitButton;
    private TextView dailyInstallmentText, previousBalance, totalPayout;
    private TextView startDateText;
    private TextView endDateText;
    private TextView interestAmountText;
    private TextView totalRepaymentText;
    private String selectedLoanAmount;
    private int maximumLoanValue, previousBalanceLoan, totalPayoutAmount;
    private FirebaseAuth mAuth;
    private int currentBalanceValueInt;
    private DatabaseReference userRef;
    private DatabaseReference loansRef;

    private static final double INTEREST_RATE = 0.20; // 20% interest rate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrower__apply_loan, container, false);

        // Initialize UI components
        loanAmountSpinner = view.findViewById(R.id.loan_amount_spinner);
        submitButton = view.findViewById(R.id.submit_button);
        dailyInstallmentText = view.findViewById(R.id.daily_installment_text);
        startDateText = view.findViewById(R.id.start_date_text);
        endDateText = view.findViewById(R.id.end_date_text);
        interestAmountText = view.findViewById(R.id.interest_amount_text);
        totalRepaymentText = view.findViewById(R.id.total_repayment_text);
        previousBalance = view.findViewById(R.id.previousBalance_text);
        totalPayout = view.findViewById(R.id.total_payout_text);

        // Get current user UID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserUID = currentUser != null ? currentUser.getUid() : null;

        // Database reference for user profiles
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserUID);
        loansRef = FirebaseDatabase.getInstance().getReference().child("application loans");

        // Retrieve profile information including profile picture URL
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the credit score as a Long
                    Long creditScoreValueLong = dataSnapshot.child("creditScore").getValue(Long.class);
                    Long previousLoanBalance = dataSnapshot.child("previousLoanBalance").getValue(Long.class);
                    Long currentLoanBalance = dataSnapshot.child("currentLoan").getValue(Long.class);
                    // Convert Long to int
                    int creditScoreValueInt = (creditScoreValueLong != null) ? creditScoreValueLong.intValue() : 0;
                    int previousBalanceValueInt = (previousLoanBalance != null) ? previousLoanBalance.intValue() : 0;
                    currentBalanceValueInt = (currentLoanBalance != null) ? currentLoanBalance.intValue() : 0;

                    // Multiply the credit score by 1000 to get the maximum loan value
                    maximumLoanValue = creditScoreValueInt * 1000;
                    previousBalanceLoan = previousBalanceValueInt;

                    // Populate the Spinner with values up to the maximum loan value
                    populateLoanAmountSpinner(maximumLoanValue);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        submitButton.setOnClickListener(v -> {
            if (selectedLoanAmount != null) {
                // Check the previous loan balance condition before saving
                if (previousBalanceLoan <= (currentBalanceValueInt * 0.3)) {
                    saveLoanToDatabase();
                    Toast.makeText(getActivity(), "Loan Amount Selected: " + selectedLoanAmount, Toast.LENGTH_SHORT).show();
                    // Navigate to home page or perform another action
                } else {
                    Toast.makeText(getActivity(), "Previous loan balance is more than 30% below current loan balance", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please select a loan amount", Toast.LENGTH_SHORT).show();
            }
        });

        loanAmountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLoanAmount = parent.getItemAtPosition(position).toString();
                calculateInstallmentAndDates();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedLoanAmount = null;
            }
        });

        return view;
    }

    private void populateLoanAmountSpinner(int maximumLoanValue) {
        List<String> loanAmounts = new ArrayList<>();
        for (int i = 1000; i <= maximumLoanValue; i += 1000) {
            loanAmounts.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, loanAmounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loanAmountSpinner.setAdapter(adapter);
    }

    private void calculateInstallmentAndDates() {
        if (selectedLoanAmount != null) {
            // Parse the selected loan amount
            int loanAmount = Integer.parseInt(selectedLoanAmount);

            // Calculate the interest amount
            double interestAmount = loanAmount * INTEREST_RATE;

            // Calculate the total amount to be repaid with interest
            double totalRepayment = loanAmount + interestAmount;

            // Assume the repayment period is 60 days
            int daysInMonth = 60;

            // Calculate the daily installment amount
            double dailyInstallment = totalRepayment / daysInMonth;

            // Set the interest amount text
            interestAmountText.setText(String.format(Locale.getDefault(), " %.2f", interestAmount));

            // Set the total repayment text
            totalRepaymentText.setText(String.format(Locale.getDefault(), " %.2f", totalRepayment));

            // Set the total previous balance loan text
            previousBalance.setText(String.format(Locale.getDefault(), " %d", previousBalanceLoan));

            // Calculate total payout
            totalPayoutAmount = loanAmount - previousBalanceLoan;
            totalPayout.setText(String.format(Locale.getDefault(), " %d", totalPayoutAmount));

            // Set the daily installment text
            dailyInstallmentText.setText(String.format(Locale.getDefault(), " %.2f", dailyInstallment));

            // Calculate start and end dates
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Start date is tomorrow
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String startDate = dateFormat.format(calendar.getTime());

            // End date is 60 days from tomorrow
            calendar.add(Calendar.DAY_OF_YEAR, 59);
            String endDate = dateFormat.format(calendar.getTime());

            // Set the start and end date texts
            startDateText.setText(String.format(" %s", startDate));
            endDateText.setText(String.format(" %s", endDate));
        }
    }
    private void saveLoanToDatabase() {
        if (selectedLoanAmount != null) {
            int loanAmount = Integer.parseInt(selectedLoanAmount);
            double interestAmount = loanAmount * INTEREST_RATE;
            double totalRepayment = loanAmount + interestAmount;
            int daysInMonth = 60;
            double dailyInstallment = totalRepayment / daysInMonth;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String startDateStr = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, daysInMonth - 1);
            String endDateStr = dateFormat.format(calendar.getTime());

            // Convert startDate and endDate to timestamps
            long startDate = 0;
            long endDate = 0;
            try {
                startDate = dateFormat.parse(startDateStr).getTime();
                endDate = dateFormat.parse(endDateStr).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Create a custom ID for the loan
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userId = mAuth.getCurrentUser().getUid();
            String loanId = userId + "_" + startDate + "_" + endDate;

            DatabaseReference loansRef = FirebaseDatabase.getInstance().getReference().child("loans");
            long finalStartDate = startDate;
            long finalEndDate = endDate;
            loansRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean hasPendingOrAcceptedLoan = false;

                    // Iterate through the user's loans and check the status
                    for (DataSnapshot loanSnapshot : dataSnapshot.getChildren()) {
                        String status = loanSnapshot.child("status").getValue(String.class);
                        if ("pending".equals(status) || "accepted".equals(status)) {
                            hasPendingOrAcceptedLoan = true;
                            break; // No need to check further if one is found
                        }
                    }

                    if (hasPendingOrAcceptedLoan) {
                        // User has existing pending or accepted loan applications
                        Toast.makeText(getActivity(), "You have a pending or ongoing loan application.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Proceed with saving the new loan application
                        saveNewLoanApplication(loanId, loanAmount, interestAmount, totalRepayment, dailyInstallment, finalStartDate, finalEndDate, userId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                    Log.e("DatabaseError", "Error checking existing loans: " + databaseError.getMessage());
                }
            });

        }
    }

    private void saveNewLoanApplication(String loanId, int loanAmount, double interestAmount, double totalRepayment, double dailyInstallment, long startDate, long endDate, String userId) {
        // Reference to the user's profile to get the name
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        long finalStartDate = startDate;
        long finalEndDate = endDate;
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the user's name from the user profile
                String Name = dataSnapshot.child("Name").getValue(String.class);
                String profileImage = dataSnapshot.child("ProfileImage").getValue(String.class);
                String Brgy = dataSnapshot.child("Brgy").getValue(String.class);
                String City = dataSnapshot.child("City").getValue(String.class);
                String Province = dataSnapshot.child("Province").getValue(String.class);
                String Street = dataSnapshot.child("Street").getValue(String.class);
                String Address = Street + " " + Brgy + " " + City + " " + Province;
                long timestamp = System.currentTimeMillis();

                Map<String, Object> loanData = new HashMap<>();
                loanData.put("loanId", loanId);
                loanData.put("loanAmount", loanAmount);
                loanData.put("interestAmount", interestAmount);
                loanData.put("totalRepayment", totalRepayment);
                loanData.put("dailyInstallment", dailyInstallment);
                loanData.put("startDate", finalStartDate);
                loanData.put("endDate", finalEndDate);
                loanData.put("userId", userId);
                loanData.put("userName", Name);
                loanData.put("userAddress", Address);
                loanData.put("userProfile", profileImage);
                loanData.put("status", "pending");
                loanData.put("totalRepaymentBalance", totalRepayment);
                loanData.put("totalPayoutAmount", totalPayoutAmount);
                loanData.put("timestamp", timestamp);

                // Save loanData to Firebase under 'loans' node
                DatabaseReference loansRef = FirebaseDatabase.getInstance().getReference().child("loans");
                DatabaseReference currentLoansRef = FirebaseDatabase.getInstance().getReference().child("loan_application");
                loansRef.child(loanId).setValue(loanData);
                currentLoansRef.child(loanId).setValue(loanData);

                String notificationId = userId + "_" + timestamp; // Custom ID for the notification

                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("notificationId", notificationId); // Add notification ID
                notificationData.put("userId", userId);
                notificationData.put("message", "Application Loan Successful");
                notificationData.put("timestamp", timestamp);
                Toast.makeText(getActivity(), "Application successful, please wait for approval.", Toast.LENGTH_LONG).show();
                // Save notificationData to Firebase under 'notifications' node
                DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("notifications");
                notificationsRef.child(notificationId).setValue(notificationData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
                Log.e("DatabaseError", "Error retrieving user name: " + databaseError.getMessage());
            }
        });
    }



}
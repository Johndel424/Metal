package com.example.metal.Collector;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CollectorDailyCollectionAdapter extends RecyclerView.Adapter<CollectorDailyViewHolder> {
    private Context context;
    private List<CollectorDailyCollectionModel> dataList;
    SimpleDateFormat sdf, sddf;
    String currentDateAndTime, currentDate;

    public CollectorDailyCollectionAdapter(Context context, List<CollectorDailyCollectionModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CollectorDailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collector_daily_loan_collection, parent, false);
        return new CollectorDailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectorDailyViewHolder holder, int position) {
        CollectorDailyCollectionModel currentLoanModel = dataList.get(position);


        // Get current date and time
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(now);

        if (currentLoanModel.getPaidOrNot_60() != null) {
            Long timestampLong = currentLoanModel.getDay_60();
            Date dateFromTimestamp = new Date(timestampLong);
            String dayDate = sdf.format(dateFromTimestamp);

            // Compare the formatted current date and date
            if (!currentDate.equals(dayDate) || "yes".equalsIgnoreCase(currentLoanModel.getPaidOrNot_60())) {
                // If the dates don't match or the loan is paid, hide the layout
                holder.day60.setVisibility(View.GONE);
            } else {
                // Set OnClickListener on the profile image to open the new activity if conditions are met
                holder.day60.setVisibility(View.VISIBLE);
                holder.borrowerName60.setText(currentLoanModel.getUserName());
                holder.address60.setText(currentLoanModel.getUserAddress());
                holder.needToPay60.setText("Due Date");
                String profileImageUrl = currentLoanModel.getUserProfile();
                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                    Glide.with(context)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.logo)  // Optional: placeholder image
                            .error(R.drawable.logo)              // Optional: error image
                            .into(holder.profile60);
                }

                holder.day60.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentLoanModel.getRepaymentUserId());
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Assuming 'latitude' and 'longitude' are stored as doubles in the 'users' node
                                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                                    // Assuming balance is retrieved and is a Double
                                    Double balance = dataSnapshot.child("previousLoanBalance").getValue(Double.class);
                                    // Create an Intent to open the new activity
                                    Intent intent = new Intent(context, CollectorDailyCollectionDetail2.class);
                                    long day = currentLoanModel.getDay_60();
                                    // Convert the long timestamp to a Date object
                                    Date date = new Date(day);
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                    // Format the date into a string
                                    String formattedDate = sdf.format(date);

                                    intent.putExtra("dateBorrowerValue", formattedDate);
                                    intent.putExtra("needToPayToday", balance);
                                    intent.putExtra("loanId", currentLoanModel.getLoanId());
                                    intent.putExtra("Name", currentLoanModel.getUserName());
                                    intent.putExtra("Profile", currentLoanModel.getUserProfile());
                                    intent.putExtra("Address", currentLoanModel.getUserAddress());
                                    intent.putExtra("PhoneNumber", currentLoanModel.getPhoneNumber());
                                    intent.putExtra("userUid", currentLoanModel.getRepaymentUserId());

                                    // Pass the value of 'need to update' fields
                                    intent.putExtra("amountPaidbyBorrower", "amountPaid_60");
                                    intent.putExtra("markBorrower", "paidOrNot_60");
                                    intent.putExtra("paymentStatusBorrower", currentLoanModel.getPaidOrNot_60());
                                    intent.putExtra("proofPaymentBorrower", "proofPayment_60");
                                    intent.putExtra("Latitude", latitude);
                                    intent.putExtra("Longitude", longitude);

                                    // Start the new activity
                                    context.startActivity(intent);
                                } else {
                                    Log.e("Firebase", "User not found in the database");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
                            }
                        });
                    }
                });
            }
        }
//        if (currentLoanModel.getPaidOrNot_2() != null) {
//            Long timestampLong = currentLoanModel.getDay_2();
//            Date dateFromTimestamp = new Date(timestampLong);
//            String dayDate = sdf.format(dateFromTimestamp);
//
//            // Compare the formatted current date and date
//            if (!currentDate.equals(dayDate) || "yes".equalsIgnoreCase(currentLoanModel.getPaidOrNot_2())) {
//                // If the dates don't match or the loan is paid, hide the layout
//                holder.day2.setVisibility(View.GONE);
//            } else {
//                // Set OnClickListener on the profile image to open the new activity if conditions are met
//                holder.day2.setVisibility(View.VISIBLE);
//                holder.borrowerName2.setText(currentLoanModel.getUserName());
//                holder.address2.setText(currentLoanModel.getUserAddress());
//                holder.needToPay2.setText(String.format(Locale.getDefault(), "%.2f", currentLoanModel.getToCollect_2()));
//                holder.day2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentLoanModel.getRepaymentUserId());
//                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    // Assuming 'latitude' and 'longitude' are stored as doubles in the 'users' node
//                                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
//                                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);
//
//                                    // Create an Intent to open the new activity
//                                    Intent intent = new Intent(context, CollectorDailyCollectionDetail.class);
//                                    long day = currentLoanModel.getDay_2();
//                                    // Convert the long timestamp to a Date object
//                                    Date date = new Date(day);
//                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                                    // Format the date into a string
//                                    String formattedDate = sdf.format(date);
//
//                                    intent.putExtra("dateBorrowerValue", formattedDate);
//                                    intent.putExtra("needToPayToday", String.valueOf(currentLoanModel.getToCollect_2()));
//                                    intent.putExtra("loanId", currentLoanModel.getLoanId());
//                                    intent.putExtra("Name", currentLoanModel.getUserName());
//                                    intent.putExtra("Profile", currentLoanModel.getUserProfile());
//                                    intent.putExtra("Address", currentLoanModel.getUserAddress());
//                                    intent.putExtra("PhoneNumber", currentLoanModel.getUserPhoneNumber());
//                                    intent.putExtra("userUid", currentLoanModel.getRepaymentUserId());
//
//                                    // Pass the value of 'need to update' fields
//                                    intent.putExtra("amountPaidbyBorrower", "amountPaid_2");
//                                    intent.putExtra("markBorrower", "paidOrNot_2");
//                                    intent.putExtra("paymentStatusBorrower", currentLoanModel.getPaidOrNot_2());
//                                    intent.putExtra("proofPaymentBorrower", "proofPayment_2");
//                                    intent.putExtra("Latitude", latitude);
//                                    intent.putExtra("Longitude", longitude);
//
//                                    // Start the new activity
//                                    context.startActivity(intent);
//                                } else {
//                                    Log.e("Firebase", "User not found in the database");
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
//                            }
//                        });
//                    }
//                });
//            }
//        }
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_1(), currentLoanModel.getDay_1(), currentLoanModel.getToCollect_1(), holder.day1,
                holder.borrowerName1, holder.address1, holder.needToPay1, "amountPaid_1", "paidOrNot_1",
                "proofPayment_1", context, sdf, currentDate, holder.profile1
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_2(), currentLoanModel.getDay_2(), currentLoanModel.getToCollect_2(), holder.day2,
                holder.borrowerName2, holder.address2, holder.needToPay2, "amountPaid_2", "paidOrNot_2",
                "proofPayment_2", context, sdf, currentDate, holder.profile2
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_3(), currentLoanModel.getDay_3(), currentLoanModel.getToCollect_3(), holder.day3,
                holder.borrowerName3, holder.address3, holder.needToPay3, "amountPaid_3", "paidOrNot_3",
                "proofPayment_3", context, sdf, currentDate, holder.profile3
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_4(), currentLoanModel.getDay_4(), currentLoanModel.getToCollect_4(), holder.day4,
                holder.borrowerName4, holder.address4, holder.needToPay4, "amountPaid_4", "paidOrNot_4",
                "proofPayment_4", context, sdf, currentDate, holder.profile4
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_5(), currentLoanModel.getDay_5(), currentLoanModel.getToCollect_5(), holder.day5,
                holder.borrowerName5, holder.address5, holder.needToPay5, "amountPaid_5", "paidOrNot_5",
                "proofPayment_5", context, sdf, currentDate, holder.profile5
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_6(), currentLoanModel.getDay_6(), currentLoanModel.getToCollect_6(), holder.day6,
                holder.borrowerName6, holder.address6, holder.needToPay6, "amountPaid_6", "paidOrNot_6",
                "proofPayment_6", context, sdf, currentDate, holder.profile6
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_7(), currentLoanModel.getDay_7(), currentLoanModel.getToCollect_7(), holder.day7,
                holder.borrowerName7, holder.address7, holder.needToPay7, "amountPaid_7", "paidOrNot_7",
                "proofPayment_7", context, sdf, currentDate, holder.profile7
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_8(), currentLoanModel.getDay_8(), currentLoanModel.getToCollect_8(), holder.day8,
                holder.borrowerName8, holder.address8, holder.needToPay8, "amountPaid_8", "paidOrNot_8",
                "proofPayment_8", context, sdf, currentDate, holder.profile8
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_9(), currentLoanModel.getDay_9(), currentLoanModel.getToCollect_9(), holder.day9,
                holder.borrowerName9, holder.address9, holder.needToPay9, "amountPaid_9", "paidOrNot_9",
                "proofPayment_9", context, sdf, currentDate, holder.profile9
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_10(), currentLoanModel.getDay_10(), currentLoanModel.getToCollect_1(), holder.day10,
                holder.borrowerName10, holder.address10, holder.needToPay10, "amountPaid_10", "paidOrNot_10",
                "proofPayment_10", context, sdf, currentDate, holder.profile10
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_11(), currentLoanModel.getDay_11(), currentLoanModel.getToCollect_11(), holder.day11,
                holder.borrowerName11, holder.address11, holder.needToPay11, "amountPaid_11", "paidOrNot_11",
                "proofPayment_11", context, sdf, currentDate, holder.profile11
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_12(), currentLoanModel.getDay_12(), currentLoanModel.getToCollect_12(), holder.day12,
                holder.borrowerName12, holder.address12, holder.needToPay12, "amountPaid_12", "paidOrNot_12",
                "proofPayment_12", context, sdf, currentDate, holder.profile12
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_13(), currentLoanModel.getDay_13(), currentLoanModel.getToCollect_13(), holder.day13,
                holder.borrowerName13, holder.address13, holder.needToPay13, "amountPaid_13", "paidOrNot_13",
                "proofPayment_13", context, sdf, currentDate, holder.profile13
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_14(), currentLoanModel.getDay_14(), currentLoanModel.getToCollect_14(), holder.day14,
                holder.borrowerName14, holder.address14, holder.needToPay14, "amountPaid_14", "paidOrNot_14",
                "proofPayment_14", context, sdf, currentDate, holder.profile14
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_15(), currentLoanModel.getDay_15(), currentLoanModel.getToCollect_15(), holder.day15,
                holder.borrowerName15, holder.address15, holder.needToPay15, "amountPaid_15", "paidOrNot_15",
                "proofPayment_15", context, sdf, currentDate, holder.profile15
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_16(), currentLoanModel.getDay_16(), currentLoanModel.getToCollect_16(), holder.day16,
                holder.borrowerName16, holder.address16, holder.needToPay16, "amountPaid_16", "paidOrNot_16",
                "proofPayment_16", context, sdf, currentDate, holder.profile16
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_17(), currentLoanModel.getDay_17(), currentLoanModel.getToCollect_17(), holder.day17,
                holder.borrowerName17, holder.address17, holder.needToPay17, "amountPaid_17", "paidOrNot_17",
                "proofPayment_17", context, sdf, currentDate, holder.profile17
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_18(), currentLoanModel.getDay_18(), currentLoanModel.getToCollect_18(), holder.day18,
                holder.borrowerName18, holder.address18, holder.needToPay18, "amountPaid_18", "paidOrNot_18",
                "proofPayment_18", context, sdf, currentDate, holder.profile18
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_19(), currentLoanModel.getDay_19(), currentLoanModel.getToCollect_19(), holder.day19,
                holder.borrowerName19, holder.address19, holder.needToPay19, "amountPaid_19", "paidOrNot_19",
                "proofPayment_19", context, sdf, currentDate, holder.profile19
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_20(), currentLoanModel.getDay_20(), currentLoanModel.getToCollect_20(), holder.day20,
                holder.borrowerName20, holder.address20, holder.needToPay20, "amountPaid_20", "paidOrNot_20",
                "proofPayment_20", context, sdf, currentDate, holder.profile20
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_21(), currentLoanModel.getDay_21(), currentLoanModel.getToCollect_21(), holder.day21,
                holder.borrowerName21, holder.address21, holder.needToPay21, "amountPaid_21", "paidOrNot_21",
                "proofPayment_21", context, sdf, currentDate, holder.profile21
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_22(), currentLoanModel.getDay_22(),
                currentLoanModel.getToCollect_22(), holder.day22,
                holder.borrowerName22, holder.address22, holder.needToPay22,
                "amountPaid_22", "paidOrNot_22",
                "proofPayment_22", context, sdf, currentDate, holder.profile22
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_23(), currentLoanModel.getDay_23(),
                currentLoanModel.getToCollect_23(), holder.day23,
                holder.borrowerName23, holder.address23, holder.needToPay23, "amountPaid_23",
                "paidOrNot_23",
                "proofPayment_23", context, sdf, currentDate, holder.profile23
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_24(), currentLoanModel.getDay_24(),
                currentLoanModel.getToCollect_24(), holder.day24,
                holder.borrowerName24, holder.address24, holder.needToPay24, "amountPaid_24",
                "paidOrNot_24",
                "proofPayment_24", context, sdf, currentDate, holder.profile24
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_25(), currentLoanModel.getDay_25(),
                currentLoanModel.getToCollect_25(), holder.day25,
                holder.borrowerName25, holder.address25, holder.needToPay25, "amountPaid_25",
                "paidOrNot_25",
                "proofPayment_25", context, sdf, currentDate, holder.profile25
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_26(), currentLoanModel.getDay_26(),
                currentLoanModel.getToCollect_26(), holder.day26,
                holder.borrowerName26, holder.address26, holder.needToPay26, "amountPaid_26",
                "paidOrNot_26",
                "proofPayment_26", context, sdf, currentDate, holder.profile26
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_27(), currentLoanModel.getDay_27(),
                currentLoanModel.getToCollect_27(), holder.day27,
                holder.borrowerName27, holder.address27, holder.needToPay27, "amountPaid_27",
                "paidOrNot_27",
                "proofPayment_27", context, sdf, currentDate, holder.profile27
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_28(), currentLoanModel.getDay_28(),
                currentLoanModel.getToCollect_28(), holder.day28,
                holder.borrowerName28, holder.address28, holder.needToPay28, "amountPaid_28",
                "paidOrNot_28",
                "proofPayment_28", context, sdf, currentDate, holder.profile28
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_29(), currentLoanModel.getDay_29(),
                currentLoanModel.getToCollect_29(), holder.day29,
                holder.borrowerName29, holder.address29, holder.needToPay29, "amountPaid_29",
                "paidOrNot_29",
                "proofPayment_29", context, sdf, currentDate, holder.profile29
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_30(), currentLoanModel.getDay_30(),
                currentLoanModel.getToCollect_30(), holder.day30,
                holder.borrowerName30, holder.address30, holder.needToPay30, "amountPaid_30",
                "paidOrNot_30",
                "proofPayment_30", context, sdf, currentDate, holder.profile30
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_31(), currentLoanModel.getDay_31(),
                currentLoanModel.getToCollect_31(), holder.day31,
                holder.borrowerName31, holder.address31, holder.needToPay31, "amountPaid_31",
                "paidOrNot_31",
                "proofPayment_31", context, sdf, currentDate, holder.profile31
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_32(), currentLoanModel.getDay_32(),
                currentLoanModel.getToCollect_32(), holder.day32,
                holder.borrowerName32, holder.address32, holder.needToPay32, "amountPaid_32",
                "paidOrNot_32",
                "proofPayment_32", context, sdf, currentDate, holder.profile32
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_33(), currentLoanModel.getDay_33(),
                currentLoanModel.getToCollect_33(), holder.day33,
                holder.borrowerName33, holder.address33, holder.needToPay33, "amountPaid_33",
                "paidOrNot_33",
                "proofPayment_33", context, sdf, currentDate, holder.profile33
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_34(), currentLoanModel.getDay_34(),
                currentLoanModel.getToCollect_34(), holder.day34,
                holder.borrowerName34, holder.address34, holder.needToPay34, "amountPaid_34",
                "paidOrNot_34",
                "proofPayment_34", context, sdf, currentDate, holder.profile34
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_35(), currentLoanModel.getDay_35(),
                currentLoanModel.getToCollect_35(), holder.day35,
                holder.borrowerName35, holder.address35, holder.needToPay35, "amountPaid_35",
                "paidOrNot_35",
                "proofPayment_35", context, sdf, currentDate, holder.profile35
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_36(), currentLoanModel.getDay_36(),
                currentLoanModel.getToCollect_36(), holder.day36,
                holder.borrowerName36, holder.address36, holder.needToPay36, "amountPaid_36",
                "paidOrNot_36",
                "proofPayment_36", context, sdf, currentDate, holder.profile36
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_37(), currentLoanModel.getDay_37(),
                currentLoanModel.getToCollect_37(), holder.day37,
                holder.borrowerName37, holder.address37, holder.needToPay37, "amountPaid_37",
                "paidOrNot_37",
                "proofPayment_37", context, sdf, currentDate, holder.profile37
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_38(), currentLoanModel.getDay_38(),
                currentLoanModel.getToCollect_38(), holder.day38,
                holder.borrowerName38, holder.address38, holder.needToPay38, "amountPaid_38",
                "paidOrNot_38",
                "proofPayment_38", context, sdf, currentDate, holder.profile38
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_39(), currentLoanModel.getDay_39(),
                currentLoanModel.getToCollect_39(), holder.day39,
                holder.borrowerName39, holder.address39, holder.needToPay39, "amountPaid_39",
                "paidOrNot_39",
                "proofPayment_39", context, sdf, currentDate, holder.profile39
        );
        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_40(), currentLoanModel.getDay_40(),
                currentLoanModel.getToCollect_40(), holder.day40,
                holder.borrowerName40, holder.address40, holder.needToPay40, "amountPaid_40",
                "paidOrNot_40", "proofPayment_40", context, sdf, currentDate, holder.profile40);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_41(), currentLoanModel.getDay_41(),
                currentLoanModel.getToCollect_41(), holder.day41,
                holder.borrowerName41, holder.address41, holder.needToPay41, "amountPaid_41",
                "paidOrNot_41", "proofPayment_41", context, sdf, currentDate, holder.profile41);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_42(), currentLoanModel.getDay_42(),
                currentLoanModel.getToCollect_42(), holder.day42,
                holder.borrowerName42, holder.address42, holder.needToPay42, "amountPaid_42",
                "paidOrNot_42", "proofPayment_42", context, sdf, currentDate, holder.profile42);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_43(), currentLoanModel.getDay_43(),
                currentLoanModel.getToCollect_43(), holder.day43,
                holder.borrowerName43, holder.address43, holder.needToPay43, "amountPaid_43",
                "paidOrNot_43", "proofPayment_43", context, sdf, currentDate, holder.profile43);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_44(), currentLoanModel.getDay_44(),
                currentLoanModel.getToCollect_44(), holder.day44,
                holder.borrowerName44, holder.address44, holder.needToPay44, "amountPaid_44",
                "paidOrNot_44", "proofPayment_44", context, sdf, currentDate, holder.profile44);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_45(), currentLoanModel.getDay_45(),
                currentLoanModel.getToCollect_45(), holder.day45,
                holder.borrowerName45, holder.address45, holder.needToPay45, "amountPaid_45",
                "paidOrNot_45", "proofPayment_45", context, sdf, currentDate, holder.profile45);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_46(), currentLoanModel.getDay_46(),
                currentLoanModel.getToCollect_46(), holder.day46,
                holder.borrowerName46, holder.address46, holder.needToPay46, "amountPaid_46",
                "paidOrNot_46", "proofPayment_46", context, sdf, currentDate, holder.profile46);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_47(), currentLoanModel.getDay_47(),
                currentLoanModel.getToCollect_47(), holder.day47,
                holder.borrowerName47, holder.address47, holder.needToPay47, "amountPaid_47",
                "paidOrNot_47", "proofPayment_47", context, sdf, currentDate, holder.profile47);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_48(), currentLoanModel.getDay_48(),
                currentLoanModel.getToCollect_48(), holder.day48,
                holder.borrowerName48, holder.address48, holder.needToPay48, "amountPaid_48",
                "paidOrNot_48", "proofPayment_48", context, sdf, currentDate, holder.profile48);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_49(), currentLoanModel.getDay_49(),
                currentLoanModel.getToCollect_49(), holder.day49,
                holder.borrowerName49, holder.address49, holder.needToPay49, "amountPaid_49",
                "paidOrNot_49", "proofPayment_49", context, sdf, currentDate, holder.profile49);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_50(), currentLoanModel.getDay_50(),
                currentLoanModel.getToCollect_50(), holder.day50,
                holder.borrowerName50, holder.address50, holder.needToPay50, "amountPaid_50",
                "paidOrNot_50", "proofPayment_50", context, sdf, currentDate, holder.profile50);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_51(), currentLoanModel.getDay_51(),
                currentLoanModel.getToCollect_51(), holder.day51,
                holder.borrowerName51, holder.address51, holder.needToPay51, "amountPaid_51",
                "paidOrNot_51", "proofPayment_51", context, sdf, currentDate, holder.profile51);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_52(), currentLoanModel.getDay_52(),
                currentLoanModel.getToCollect_52(), holder.day52,
                holder.borrowerName52, holder.address52, holder.needToPay52, "amountPaid_52",
                "paidOrNot_52", "proofPayment_52", context, sdf, currentDate, holder.profile52);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_53(), currentLoanModel.getDay_53(),
                currentLoanModel.getToCollect_53(), holder.day53,
                holder.borrowerName53, holder.address53, holder.needToPay53, "amountPaid_53",
                "paidOrNot_53", "proofPayment_53", context, sdf, currentDate, holder.profile53);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_54(), currentLoanModel.getDay_54(),
                currentLoanModel.getToCollect_54(), holder.day54,
                holder.borrowerName54, holder.address54, holder.needToPay54, "amountPaid_54",
                "paidOrNot_54", "proofPayment_54", context, sdf, currentDate, holder.profile54);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_55(), currentLoanModel.getDay_55(),
                currentLoanModel.getToCollect_55(), holder.day55,
                holder.borrowerName55, holder.address55, holder.needToPay55, "amountPaid_55",
                "paidOrNot_55", "proofPayment_55", context, sdf, currentDate, holder.profile55);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_56(), currentLoanModel.getDay_56(),
                currentLoanModel.getToCollect_56(), holder.day56,
                holder.borrowerName56, holder.address56, holder.needToPay56, "amountPaid_56",
                "paidOrNot_56", "proofPayment_56", context, sdf, currentDate, holder.profile56);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_57(), currentLoanModel.getDay_57(),
                currentLoanModel.getToCollect_57(), holder.day57,
                holder.borrowerName57, holder.address57, holder.needToPay57, "amountPaid_57",
                "paidOrNot_57", "proofPayment_57", context, sdf, currentDate, holder.profile57);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_58(), currentLoanModel.getDay_58(),
                currentLoanModel.getToCollect_58(), holder.day58,
                holder.borrowerName58, holder.address58, holder.needToPay58, "amountPaid_58",
                "paidOrNot_58", "proofPayment_58", context, sdf, currentDate, holder.profile58);

        handleLoanVisibility(currentLoanModel, currentLoanModel.getPaidOrNot_59(), currentLoanModel.getDay_59(),
                currentLoanModel.getToCollect_59(), holder.day59,
                holder.borrowerName59, holder.address59, holder.needToPay59, "amountPaid_59",
                "paidOrNot_59", "proofPayment_59", context, sdf, currentDate, holder.profile59);



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void handleLoanVisibility(final CollectorDailyCollectionModel currentLoanModel, String paidOrNot, long timestamp, double toCollect,
                                      View layout, TextView borrowerName, TextView address, TextView needToPay,
                                      String amountPaidField, String markBorrowerField, String proofPaymentField,
                                      final Context context, final SimpleDateFormat sdf, final String currentDate, ImageView profileImageView) {
        if (paidOrNot != null) {
            Date dateFromTimestamp = new Date(timestamp);
            String dayDate = sdf.format(dateFromTimestamp);

            // Compare the formatted current date and date
            if (!currentDate.equals(dayDate) || "yes".equalsIgnoreCase(paidOrNot)) {
                // If the dates don't match or the loan is paid, hide the layout
                layout.setVisibility(View.GONE);
            } else {
                // Set OnClickListener on the profile image to open the new activity if conditions are met
                layout.setVisibility(View.VISIBLE);
                borrowerName.setText(currentLoanModel.getUserName());
                address.setText(currentLoanModel.getUserAddress());
                needToPay.setText(String.format(Locale.getDefault(), "%.2f", toCollect));

                // Load profile image into ImageView using Glide or Picasso
                String profileImageUrl = currentLoanModel.getUserProfile();
                if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                    Glide.with(context)
                            .load(profileImageUrl)
                            .placeholder(R.drawable.logo)  // Optional: placeholder image
                            .error(R.drawable.logo)              // Optional: error image
                            .into(profileImageView);
                }

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users")
                                .child(currentLoanModel.getRepaymentUserId());
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                                    Intent intent = new Intent(context, CollectorDailyCollectionDetail.class);
                                    // Convert the long timestamp to a Date object
                                    Date date = new Date(timestamp);
                                    String formattedDate = sdf.format(date);

                                    intent.putExtra("dateBorrowerValue", formattedDate);
                                    intent.putExtra("needToPayToday", String.valueOf(toCollect));
                                    intent.putExtra("loanId", currentLoanModel.getLoanId());
                                    intent.putExtra("Name", currentLoanModel.getUserName());
                                    intent.putExtra("Profile", currentLoanModel.getUserProfile());
                                    intent.putExtra("Address", currentLoanModel.getUserAddress());
                                    intent.putExtra("PhoneNumber", currentLoanModel.getPhoneNumber());
                                    intent.putExtra("userUid", currentLoanModel.getRepaymentUserId());

                                    // Pass the value of 'need to update' fields
                                    intent.putExtra("amountPaidbyBorrower", amountPaidField);
                                    intent.putExtra("markBorrower", markBorrowerField);
                                    intent.putExtra("paymentStatusBorrower", paidOrNot);
                                    intent.putExtra("proofPaymentBorrower", proofPaymentField);
                                    intent.putExtra("Latitude", latitude);
                                    intent.putExtra("Longitude", longitude);

                                    // Start the new activity
                                    context.startActivity(intent);
                                } else {
                                    Log.e("Firebase", "User not found in the database");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
                            }
                        });
                    }
                });
            }
        }
    }



}
class CollectorDailyViewHolder extends RecyclerView.ViewHolder{
    TextView borrowerName2,needToPay2 ,address2;
    TextView borrowerName3, needToPay3, address3 ;
    TextView borrowerName4,needToPay4 ,address4;
    TextView borrowerName5, needToPay5, address5 ;
    TextView borrowerName6,needToPay6 ,address6;
    TextView borrowerName7, needToPay7, address7 ;
    TextView borrowerName8,needToPay8 ,address8;
    TextView borrowerName9, needToPay9, address9;
    TextView borrowerName10,needToPay10 ,address10;
    TextView borrowerName11, needToPay11, address11 ;
    TextView borrowerName12,needToPay12 ,address12;
    TextView borrowerName13, needToPay13, address13 ;
    TextView borrowerName14,needToPay14 ,address14;
    TextView borrowerName15, needToPay15, address15 ;
    TextView borrowerName16,needToPay16 ,address16;
    TextView borrowerName17, needToPay17, address17 ;
    TextView borrowerName18,needToPay18 ,address18;
    TextView borrowerName19, needToPay19, address19;
    TextView borrowerName20,needToPay20 ,address20;
    TextView borrowerName21,needToPay21 ,address21;
    TextView borrowerName22,needToPay22 ,address22;
    TextView borrowerName23,needToPay23 ,address23;
    TextView borrowerName24,needToPay24 ,address24;
    TextView borrowerName25,needToPay25 ,address25;
    TextView borrowerName26,needToPay26 ,address26;
    TextView borrowerName27,needToPay27 ,address27;
    TextView borrowerName28,needToPay28 ,address28;
    TextView borrowerName29,needToPay29 ,address29;
    TextView borrowerName30, needToPay30, address30 ;
    TextView borrowerName31, needToPay31, address31 ;
    TextView borrowerName32, needToPay32, address32 ;
    TextView borrowerName33, needToPay33, address33 ;
    TextView borrowerName34, needToPay34, address34 ;
    TextView borrowerName35, needToPay35, address35 ;
    TextView borrowerName36, needToPay36, address36 ;
    TextView borrowerName37, needToPay37, address37 ;
    TextView borrowerName38, needToPay38, address38 ;
    TextView borrowerName39, needToPay39, address39 ;
    TextView borrowerName40,needToPay40 ,address40;
    TextView borrowerName41,needToPay41 ,address41;
    TextView borrowerName42,needToPay42 ,address42;
    TextView borrowerName43,needToPay43 ,address43;
    TextView borrowerName44,needToPay44 ,address44;
    TextView borrowerName45,needToPay45 ,address45;
    TextView borrowerName46,needToPay46 ,address46;
    TextView borrowerName47,needToPay47 ,address47;
    TextView borrowerName48,needToPay48 ,address48;
    TextView borrowerName49,needToPay49 ,address49;
    TextView borrowerName50, needToPay50, address50 ;
    TextView borrowerName51, needToPay51, address51 ;
    TextView borrowerName52, needToPay52, address52 ;
    TextView borrowerName53, needToPay53, address53 ;
    TextView borrowerName54, needToPay54, address54 ;
    TextView borrowerName55, needToPay55, address55 ;
    TextView borrowerName56, needToPay56, address56 ;
    TextView borrowerName57, needToPay57, address57 ;
    TextView borrowerName58, needToPay58, address58 ;
    TextView borrowerName59, needToPay59, address59 ;
    TextView borrowerName60,needToPay60 ,address60;

    RelativeLayout day1, day2,day3,day4,day5,day6,day7,day8,day9,day10,day11,day12,day13,day14,day15,day16,day17,day18,day19,day20,day21,day22,day23,day24,day25,day26,day27,day28,day29,day30,day31,day32,day33,day34,day35,day36,day37,day38,day39,day40
            ,day41,day42,day43,day44,day45,day46,day47,day48,day49,day50,day51,day52,day53,day54,day55,day56,day57,day58,day59,day60;
    ImageView profile1,profile2,profile3,profile4,profile5,profile6,profile7,profile8,profile9,profile10,
            profile11,profile12,profile13,profile14,profile15,profile16,profile17,profile18,profile19,profile20,
            profile21,profile22,profile23,profile24,profile25,profile26,profile27,profile28,profile29,profile30,
            profile31,profile32,profile33,profile34,profile35,profile36,profile37,profile38,profile39,profile40,
            profile41,profile42,profile43,profile44,profile45,profile46,profile47,profile48,profile49,profile50,
            profile51,profile52,profile53,profile54,profile55,profile56,profile57,profile58,profile59,profile60;
    TextView borrowerName1, needToPay1, address1 ;
    public CollectorDailyViewHolder(@NonNull View itemView) {
        super(itemView);


        //1st day
        needToPay1 = itemView.findViewById(R.id.needToPay1);
        profile1 = itemView.findViewById(R.id.profile1);
        address1 = itemView.findViewById(R.id.address1);
        borrowerName1 = itemView.findViewById(R.id.borrowerName1);
        day1 = itemView.findViewById(R.id.day1);
        //2nd day
        needToPay2 = itemView.findViewById(R.id.needToPay2);
        profile2 = itemView.findViewById(R.id.profile2);
        address2 = itemView.findViewById(R.id.address2);
        borrowerName2 = itemView.findViewById(R.id.borrowerName2);
        day2 = itemView.findViewById(R.id.day2);

        needToPay3 = itemView.findViewById(R.id.needToPay3);
        profile3 = itemView.findViewById(R.id.profile3);
        address3 = itemView.findViewById(R.id.address3);
        borrowerName3 = itemView.findViewById(R.id.borrowerName3);
        day3 = itemView.findViewById(R.id.day3);

        needToPay4 = itemView.findViewById(R.id.needToPay4);
        profile4 = itemView.findViewById(R.id.profile4);
        address4 = itemView.findViewById(R.id.address4);
        borrowerName4 = itemView.findViewById(R.id.borrowerName4);
        day4 = itemView.findViewById(R.id.day4);

        needToPay5 = itemView.findViewById(R.id.needToPay5);
        profile5 = itemView.findViewById(R.id.profile5);
        address5 = itemView.findViewById(R.id.address5);
        borrowerName5 = itemView.findViewById(R.id.borrowerName5);
        day5 = itemView.findViewById(R.id.day5);

        needToPay6 = itemView.findViewById(R.id.needToPay6);
        profile6 = itemView.findViewById(R.id.profile6);
        address6 = itemView.findViewById(R.id.address6);
        borrowerName6 = itemView.findViewById(R.id.borrowerName6);
        day6 = itemView.findViewById(R.id.day6);

        needToPay7 = itemView.findViewById(R.id.needToPay7);
        profile7 = itemView.findViewById(R.id.profile7);
        address7 = itemView.findViewById(R.id.address7);
        borrowerName7 = itemView.findViewById(R.id.borrowerName7);
        day7 = itemView.findViewById(R.id.day7);

        needToPay8 = itemView.findViewById(R.id.needToPay8);
        profile8 = itemView.findViewById(R.id.profile8);
        address8 = itemView.findViewById(R.id.address8);
        borrowerName8 = itemView.findViewById(R.id.borrowerName8);
        day8 = itemView.findViewById(R.id.day8);

        needToPay9 = itemView.findViewById(R.id.needToPay9);
        profile9 = itemView.findViewById(R.id.profile9);
        address9 = itemView.findViewById(R.id.address9);
        borrowerName9 = itemView.findViewById(R.id.borrowerName9);
        day9 = itemView.findViewById(R.id.day9);

        needToPay10 = itemView.findViewById(R.id.needToPay10);
        profile10 = itemView.findViewById(R.id.profile10);
        address10 = itemView.findViewById(R.id.address10);
        borrowerName10 = itemView.findViewById(R.id.borrowerName10);
        day10 = itemView.findViewById(R.id.day10);

        needToPay11 = itemView.findViewById(R.id.needToPay11);
        profile11 = itemView.findViewById(R.id.profile11);
        address11 = itemView.findViewById(R.id.address11);
        borrowerName11 = itemView.findViewById(R.id.borrowerName11);
        day11 = itemView.findViewById(R.id.day11);

        needToPay12 = itemView.findViewById(R.id.needToPay12);
        profile12 = itemView.findViewById(R.id.profile12);
        address12 = itemView.findViewById(R.id.address12);
        borrowerName12 = itemView.findViewById(R.id.borrowerName12);
        day12 = itemView.findViewById(R.id.day12);

        needToPay13 = itemView.findViewById(R.id.needToPay13);
        profile13 = itemView.findViewById(R.id.profile13);
        address13 = itemView.findViewById(R.id.address13);
        borrowerName13 = itemView.findViewById(R.id.borrowerName13);
        day13 = itemView.findViewById(R.id.day13);

        needToPay14 = itemView.findViewById(R.id.needToPay14);
        profile14 = itemView.findViewById(R.id.profile14);
        address14 = itemView.findViewById(R.id.address14);
        borrowerName14 = itemView.findViewById(R.id.borrowerName14);
        day14 = itemView.findViewById(R.id.day14);

        needToPay15 = itemView.findViewById(R.id.needToPay15);
        profile15 = itemView.findViewById(R.id.profile15);
        address15 = itemView.findViewById(R.id.address15);
        borrowerName15 = itemView.findViewById(R.id.borrowerName15);
        day15 = itemView.findViewById(R.id.day15);

        needToPay16 = itemView.findViewById(R.id.needToPay16);
        profile16 = itemView.findViewById(R.id.profile16);
        address16 = itemView.findViewById(R.id.address16);
        borrowerName16 = itemView.findViewById(R.id.borrowerName16);
        day16 = itemView.findViewById(R.id.day16);

        needToPay17 = itemView.findViewById(R.id.needToPay17);
        profile17 = itemView.findViewById(R.id.profile17);
        address17 = itemView.findViewById(R.id.address17);
        borrowerName17 = itemView.findViewById(R.id.borrowerName17);
        day17 = itemView.findViewById(R.id.day17);

        needToPay18 = itemView.findViewById(R.id.needToPay18);
        profile18 = itemView.findViewById(R.id.profile18);
        address18 = itemView.findViewById(R.id.address18);
        borrowerName18 = itemView.findViewById(R.id.borrowerName18);
        day18 = itemView.findViewById(R.id.day18);

        needToPay19 = itemView.findViewById(R.id.needToPay19);
        profile19 = itemView.findViewById(R.id.profile19);
        address19 = itemView.findViewById(R.id.address19);
        borrowerName19 = itemView.findViewById(R.id.borrowerName19);
        day19 = itemView.findViewById(R.id.day19);

        needToPay20 = itemView.findViewById(R.id.needToPay20);
        profile20 = itemView.findViewById(R.id.profile20);
        address20 = itemView.findViewById(R.id.address20);
        borrowerName20 = itemView.findViewById(R.id.borrowerName20);
        day20 = itemView.findViewById(R.id.day20);

        needToPay21 = itemView.findViewById(R.id.needToPay21);
        profile21 = itemView.findViewById(R.id.profile21);
        address21 = itemView.findViewById(R.id.address21);
        borrowerName21 = itemView.findViewById(R.id.borrowerName21);
        day21 = itemView.findViewById(R.id.day21);

        needToPay22 = itemView.findViewById(R.id.needToPay22);
        profile22 = itemView.findViewById(R.id.profile22);
        address22 = itemView.findViewById(R.id.address22);
        borrowerName22 = itemView.findViewById(R.id.borrowerName22);
        day22 = itemView.findViewById(R.id.day22);

        needToPay23 = itemView.findViewById(R.id.needToPay23);
        profile23 = itemView.findViewById(R.id.profile23);
        address23 = itemView.findViewById(R.id.address23);
        borrowerName23 = itemView.findViewById(R.id.borrowerName23);
        day23 = itemView.findViewById(R.id.day23);

        needToPay24 = itemView.findViewById(R.id.needToPay24);
        profile24 = itemView.findViewById(R.id.profile24);
        address24 = itemView.findViewById(R.id.address24);
        borrowerName24 = itemView.findViewById(R.id.borrowerName24);
        day24 = itemView.findViewById(R.id.day24);

        needToPay25 = itemView.findViewById(R.id.needToPay25);
        profile25 = itemView.findViewById(R.id.profile25);
        address25 = itemView.findViewById(R.id.address25);
        borrowerName25 = itemView.findViewById(R.id.borrowerName25);
        day25 = itemView.findViewById(R.id.day25);

        needToPay26 = itemView.findViewById(R.id.needToPay26);
        profile26 = itemView.findViewById(R.id.profile26);
        address26 = itemView.findViewById(R.id.address26);
        borrowerName26 = itemView.findViewById(R.id.borrowerName26);
        day26 = itemView.findViewById(R.id.day26);

        needToPay27 = itemView.findViewById(R.id.needToPay27);
        profile27 = itemView.findViewById(R.id.profile27);
        address27 = itemView.findViewById(R.id.address27);
        borrowerName27 = itemView.findViewById(R.id.borrowerName27);
        day27 = itemView.findViewById(R.id.day27);

        needToPay28 = itemView.findViewById(R.id.needToPay28);
        profile28 = itemView.findViewById(R.id.profile28);
        address28 = itemView.findViewById(R.id.address28);
        borrowerName28 = itemView.findViewById(R.id.borrowerName28);
        day28 = itemView.findViewById(R.id.day28);

        needToPay29 = itemView.findViewById(R.id.needToPay29);
        profile29 = itemView.findViewById(R.id.profile29);
        address29 = itemView.findViewById(R.id.address29);
        borrowerName29 = itemView.findViewById(R.id.borrowerName29);
        day29 = itemView.findViewById(R.id.day29);

        needToPay30 = itemView.findViewById(R.id.needToPay30);
        profile30 = itemView.findViewById(R.id.profile30);
        address30 = itemView.findViewById(R.id.address30);
        borrowerName30 = itemView.findViewById(R.id.borrowerName30);
        day30 = itemView.findViewById(R.id.day30);

        needToPay31 = itemView.findViewById(R.id.needToPay31);
        profile31 = itemView.findViewById(R.id.profile31);
        address31 = itemView.findViewById(R.id.address31);
        borrowerName31 = itemView.findViewById(R.id.borrowerName31);
        day31 = itemView.findViewById(R.id.day31);

        needToPay32 = itemView.findViewById(R.id.needToPay32);
        profile32 = itemView.findViewById(R.id.profile32);
        address32 = itemView.findViewById(R.id.address32);
        borrowerName32 = itemView.findViewById(R.id.borrowerName32);
        day32 = itemView.findViewById(R.id.day32);

        needToPay33 = itemView.findViewById(R.id.needToPay33);
        profile33 = itemView.findViewById(R.id.profile33);
        address33 = itemView.findViewById(R.id.address33);
        borrowerName33 = itemView.findViewById(R.id.borrowerName33);
        day33 = itemView.findViewById(R.id.day33);

        needToPay34 = itemView.findViewById(R.id.needToPay34);
        profile34 = itemView.findViewById(R.id.profile34);
        address34 = itemView.findViewById(R.id.address34);
        borrowerName34 = itemView.findViewById(R.id.borrowerName34);
        day34 = itemView.findViewById(R.id.day34);

        needToPay35 = itemView.findViewById(R.id.needToPay35);
        profile35 = itemView.findViewById(R.id.profile35);
        address35 = itemView.findViewById(R.id.address35);
        borrowerName35 = itemView.findViewById(R.id.borrowerName35);
        day35 = itemView.findViewById(R.id.day35);

        needToPay36 = itemView.findViewById(R.id.needToPay36);
        profile36 = itemView.findViewById(R.id.profile36);
        address36 = itemView.findViewById(R.id.address36);
        borrowerName36 = itemView.findViewById(R.id.borrowerName36);
        day36 = itemView.findViewById(R.id.day36);

        needToPay37 = itemView.findViewById(R.id.needToPay37);
        profile37 = itemView.findViewById(R.id.profile37);
        address37 = itemView.findViewById(R.id.address37);
        borrowerName37 = itemView.findViewById(R.id.borrowerName37);
        day37 = itemView.findViewById(R.id.day37);

        needToPay38 = itemView.findViewById(R.id.needToPay38);
        profile38 = itemView.findViewById(R.id.profile38);
        address38 = itemView.findViewById(R.id.address38);
        borrowerName38 = itemView.findViewById(R.id.borrowerName38);
        day38 = itemView.findViewById(R.id.day38);

        needToPay39 = itemView.findViewById(R.id.needToPay39);
        profile39 = itemView.findViewById(R.id.profile39);
        address39 = itemView.findViewById(R.id.address39);
        borrowerName39 = itemView.findViewById(R.id.borrowerName39);
        day39 = itemView.findViewById(R.id.day39);

        needToPay40 = itemView.findViewById(R.id.needToPay40);
        profile40 = itemView.findViewById(R.id.profile40);
        address40 = itemView.findViewById(R.id.address40);
        borrowerName40 = itemView.findViewById(R.id.borrowerName40);
        day40 = itemView.findViewById(R.id.day40);

        needToPay41 = itemView.findViewById(R.id.needToPay41);
        profile41 = itemView.findViewById(R.id.profile41);
        address41 = itemView.findViewById(R.id.address41);
        borrowerName41 = itemView.findViewById(R.id.borrowerName41);
        day41 = itemView.findViewById(R.id.day41);

        needToPay42 = itemView.findViewById(R.id.needToPay42);
        profile42 = itemView.findViewById(R.id.profile42);
        address42 = itemView.findViewById(R.id.address42);
        borrowerName42 = itemView.findViewById(R.id.borrowerName42);
        day42 = itemView.findViewById(R.id.day42);

        needToPay43 = itemView.findViewById(R.id.needToPay43);
        profile43 = itemView.findViewById(R.id.profile43);
        address43 = itemView.findViewById(R.id.address43);
        borrowerName43 = itemView.findViewById(R.id.borrowerName43);
        day43 = itemView.findViewById(R.id.day43);

        needToPay44 = itemView.findViewById(R.id.needToPay44);
        profile44 = itemView.findViewById(R.id.profile44);
        address44 = itemView.findViewById(R.id.address44);
        borrowerName44 = itemView.findViewById(R.id.borrowerName44);
        day44 = itemView.findViewById(R.id.day44);

        needToPay45 = itemView.findViewById(R.id.needToPay45);
        profile45 = itemView.findViewById(R.id.profile45);
        address45 = itemView.findViewById(R.id.address45);
        borrowerName45 = itemView.findViewById(R.id.borrowerName45);
        day45 = itemView.findViewById(R.id.day45);

        needToPay46= itemView.findViewById(R.id.needToPay46);
        profile46 = itemView.findViewById(R.id.profile46);
        address46 = itemView.findViewById(R.id.address46);
        borrowerName46 = itemView.findViewById(R.id.borrowerName46);
        day46 = itemView.findViewById(R.id.day46);

        needToPay47 = itemView.findViewById(R.id.needToPay47);
        profile47 = itemView.findViewById(R.id.profile47);
        address47 = itemView.findViewById(R.id.address47);
        borrowerName47 = itemView.findViewById(R.id.borrowerName47);
        day47 = itemView.findViewById(R.id.day47);

        needToPay48 = itemView.findViewById(R.id.needToPay48);
        profile48 = itemView.findViewById(R.id.profile48);
        address48 = itemView.findViewById(R.id.address48);
        borrowerName48 = itemView.findViewById(R.id.borrowerName48);
        day48 = itemView.findViewById(R.id.day48);

        needToPay49 = itemView.findViewById(R.id.needToPay49);
        profile49 = itemView.findViewById(R.id.profile49);
        address49 = itemView.findViewById(R.id.address49);
        borrowerName49 = itemView.findViewById(R.id.borrowerName49);
        day49 = itemView.findViewById(R.id.day49);

        needToPay50 = itemView.findViewById(R.id.needToPay50);
        profile50 = itemView.findViewById(R.id.profile50);
        address50 = itemView.findViewById(R.id.address50);
        borrowerName50 = itemView.findViewById(R.id.borrowerName50);
        day50 = itemView.findViewById(R.id.day50);

        needToPay51 = itemView.findViewById(R.id.needToPay51);
        profile51 = itemView.findViewById(R.id.profile51);
        address51 = itemView.findViewById(R.id.address51);
        borrowerName51 = itemView.findViewById(R.id.borrowerName51);
        day51 = itemView.findViewById(R.id.day51);

        needToPay52 = itemView.findViewById(R.id.needToPay52);
        profile52 = itemView.findViewById(R.id.profile52);
        address52 = itemView.findViewById(R.id.address52);
        borrowerName52 = itemView.findViewById(R.id.borrowerName52);
        day52 = itemView.findViewById(R.id.day52);

        needToPay53 = itemView.findViewById(R.id.needToPay53);
        profile53 = itemView.findViewById(R.id.profile53);
        address53 = itemView.findViewById(R.id.address53);
        borrowerName53 = itemView.findViewById(R.id.borrowerName53);
        day53 = itemView.findViewById(R.id.day53);

        needToPay54 = itemView.findViewById(R.id.needToPay54);
        profile54 = itemView.findViewById(R.id.profile54);
        address54 = itemView.findViewById(R.id.address54);
        borrowerName54 = itemView.findViewById(R.id.borrowerName54);
        day54 = itemView.findViewById(R.id.day54);

        needToPay55 = itemView.findViewById(R.id.needToPay55);
        profile55 = itemView.findViewById(R.id.profile55);
        address55 = itemView.findViewById(R.id.address55);
        borrowerName55 = itemView.findViewById(R.id.borrowerName55);
        day55 = itemView.findViewById(R.id.day55);

        needToPay56 = itemView.findViewById(R.id.needToPay56);
        profile56 = itemView.findViewById(R.id.profile56);
        address56 = itemView.findViewById(R.id.address56);
        borrowerName56 = itemView.findViewById(R.id.borrowerName56);
        day56 = itemView.findViewById(R.id.day56);

        needToPay57 = itemView.findViewById(R.id.needToPay57);
        profile57 = itemView.findViewById(R.id.profile57);
        address57 = itemView.findViewById(R.id.address57);
        borrowerName57 = itemView.findViewById(R.id.borrowerName57);
        day57 = itemView.findViewById(R.id.day57);

        needToPay58 = itemView.findViewById(R.id.needToPay58);
        profile58 = itemView.findViewById(R.id.profile58);
        address58 = itemView.findViewById(R.id.address58);
        borrowerName58 = itemView.findViewById(R.id.borrowerName58);
        day58 = itemView.findViewById(R.id.day58);

        needToPay59 = itemView.findViewById(R.id.needToPay59);
        profile59 = itemView.findViewById(R.id.profile59);
        address59 = itemView.findViewById(R.id.address59);
        borrowerName59 = itemView.findViewById(R.id.borrowerName59);
        day59 = itemView.findViewById(R.id.day59);

        needToPay60 = itemView.findViewById(R.id.needToPay60);
        profile60 = itemView.findViewById(R.id.profile60);
        address60 = itemView.findViewById(R.id.address60);
        borrowerName60 = itemView.findViewById(R.id.borrowerName60);
        day60 = itemView.findViewById(R.id.day60);
    }
}
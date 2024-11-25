package com.example.metal.Borrower.Home_Feature;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metal.Collector.CollectorDailyCollectionModel;
import com.example.metal.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeRecentAdapter extends RecyclerView.Adapter<HomeRecentViewHolder> {
    private Context context;
    private List<CollectorDailyCollectionModel> dataList;
    public HomeRecentAdapter(Context context, List<CollectorDailyCollectionModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public HomeRecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_borrower_paid_item, parent, false);
        return new HomeRecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecentViewHolder holder, int position) {
        CollectorDailyCollectionModel currentLoanModel = dataList.get(position);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_1(),
                new Date(currentLoanModel.getDay_1()),
                currentLoanModel.getAmountPaid_1(),
                currentLoanModel.getToCollect_1(),
                currentLoanModel.getProofPayment_1(),
                holder.day1, holder.date1, holder.payment1);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_2(),
                new Date(currentLoanModel.getDay_2()),
                currentLoanModel.getAmountPaid_2(),
                currentLoanModel.getToCollect_2(),
                currentLoanModel.getProofPayment_2(),
                holder.day2, holder.date2, holder.payment2);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_3(),
                new Date(currentLoanModel.getDay_3()),
                currentLoanModel.getAmountPaid_3(),
                currentLoanModel.getToCollect_3(),
                currentLoanModel.getProofPayment_3(),
                holder.day3, holder.date3, holder.payment3);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_4(),
                new Date(currentLoanModel.getDay_4()),
                currentLoanModel.getAmountPaid_4(),
                currentLoanModel.getToCollect_4(),
                currentLoanModel.getProofPayment_4(),
                holder.day4, holder.date4, holder.payment4);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_5(),
                new Date(currentLoanModel.getDay_5()),
                currentLoanModel.getAmountPaid_5(),
                currentLoanModel.getToCollect_5(),
                currentLoanModel.getProofPayment_5(),
                holder.day5, holder.date5, holder.payment5);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_6(),
                new Date(currentLoanModel.getDay_6()),
                currentLoanModel.getAmountPaid_6(),
                currentLoanModel.getToCollect_6(),
                currentLoanModel.getProofPayment_6(),
                holder.day6, holder.date6, holder.payment6);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_7(),
                new Date(currentLoanModel.getDay_7()),
                currentLoanModel.getAmountPaid_7(),
                currentLoanModel.getToCollect_7(),
                currentLoanModel.getProofPayment_7(),
                holder.day7, holder.date7, holder.payment7);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_8(),
                new Date(currentLoanModel.getDay_8()),
                currentLoanModel.getAmountPaid_8(),
                currentLoanModel.getToCollect_8(),
                currentLoanModel.getProofPayment_8(),
                holder.day8, holder.date8, holder.payment8);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_9(),
                new Date(currentLoanModel.getDay_9()),
                currentLoanModel.getAmountPaid_9(),
                currentLoanModel.getToCollect_9(),
                currentLoanModel.getProofPayment_9(),
                holder.day9, holder.date9, holder.payment9);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_10(),
                new Date(currentLoanModel.getDay_10()),
                currentLoanModel.getAmountPaid_10(),
                currentLoanModel.getToCollect_10(),
                currentLoanModel.getProofPayment_10(),
                holder.day10, holder.date10, holder.payment10);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_11(),
                new Date(currentLoanModel.getDay_11()),
                currentLoanModel.getAmountPaid_11(),
                currentLoanModel.getToCollect_11(),
                currentLoanModel.getProofPayment_11(),
                holder.day11, holder.date11, holder.payment11);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_12(),
                new Date(currentLoanModel.getDay_12()),
                currentLoanModel.getAmountPaid_12(),
                currentLoanModel.getToCollect_12(),
                currentLoanModel.getProofPayment_12(),
                holder.day12, holder.date12, holder.payment12);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_13(),
                new Date(currentLoanModel.getDay_13()),
                currentLoanModel.getAmountPaid_13(),
                currentLoanModel.getToCollect_13(),
                currentLoanModel.getProofPayment_13(),
                holder.day13, holder.date13, holder.payment13);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_14(),
                new Date(currentLoanModel.getDay_14()),
                currentLoanModel.getAmountPaid_14(),
                currentLoanModel.getToCollect_14(),
                currentLoanModel.getProofPayment_14(),
                holder.day14, holder.date14, holder.payment14);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_15(),
                new Date(currentLoanModel.getDay_15()),
                currentLoanModel.getAmountPaid_15(),
                currentLoanModel.getToCollect_15(),
                currentLoanModel.getProofPayment_15(),
                holder.day15, holder.date15, holder.payment15);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_16(),
                new Date(currentLoanModel.getDay_16()),
                currentLoanModel.getAmountPaid_16(),
                currentLoanModel.getToCollect_16(),
                currentLoanModel.getProofPayment_16(),
                holder.day16, holder.date16, holder.payment16);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_17(),
                new Date(currentLoanModel.getDay_17()),
                currentLoanModel.getAmountPaid_17(),
                currentLoanModel.getToCollect_17(),
                currentLoanModel.getProofPayment_17(),
                holder.day17, holder.date17, holder.payment17);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_18(),
                new Date(currentLoanModel.getDay_18()),
                currentLoanModel.getAmountPaid_18(),
                currentLoanModel.getToCollect_18(),
                currentLoanModel.getProofPayment_18(),
                holder.day18, holder.date18, holder.payment18);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_19(),
                new Date(currentLoanModel.getDay_19()),
                currentLoanModel.getAmountPaid_19(),
                currentLoanModel.getToCollect_19(),
                currentLoanModel.getProofPayment_19(),
                holder.day19, holder.date19, holder.payment19);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_20(),
                new Date(currentLoanModel.getDay_20()),
                currentLoanModel.getAmountPaid_20(),
                currentLoanModel.getToCollect_20(),
                currentLoanModel.getProofPayment_20(),
                holder.day20, holder.date20, holder.payment20);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_21(),
                new Date(currentLoanModel.getDay_21()),
                currentLoanModel.getAmountPaid_21(),
                currentLoanModel.getToCollect_21(),
                currentLoanModel.getProofPayment_21(),
                holder.day21, holder.date21, holder.payment21);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_22(),
                new Date(currentLoanModel.getDay_22()),
                currentLoanModel.getAmountPaid_22(),
                currentLoanModel.getToCollect_22(),
                currentLoanModel.getProofPayment_22(),
                holder.day22, holder.date22, holder.payment22);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_23(),
                new Date(currentLoanModel.getDay_23()),
                currentLoanModel.getAmountPaid_23(),
                currentLoanModel.getToCollect_23(),
                currentLoanModel.getProofPayment_23(),
                holder.day23, holder.date23, holder.payment23);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_24(),
                new Date(currentLoanModel.getDay_24()),
                currentLoanModel.getAmountPaid_24(),
                currentLoanModel.getToCollect_24(),
                currentLoanModel.getProofPayment_24(),
                holder.day24, holder.date24, holder.payment24);
        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_25(),
                new Date(currentLoanModel.getDay_25()),
                currentLoanModel.getAmountPaid_25(),
                currentLoanModel.getToCollect_25(),
                currentLoanModel.getProofPayment_25(),
                holder.day25, holder.date25, holder.payment25);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_26(),
                new Date(currentLoanModel.getDay_26()),
                currentLoanModel.getAmountPaid_26(),
                currentLoanModel.getToCollect_26(),
                currentLoanModel.getProofPayment_26(),
                holder.day26, holder.date26, holder.payment26);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_27(),
                new Date(currentLoanModel.getDay_27()),
                currentLoanModel.getAmountPaid_27(),
                currentLoanModel.getToCollect_27(),
                currentLoanModel.getProofPayment_27(),
                holder.day27, holder.date27, holder.payment27);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_28(),
                new Date(currentLoanModel.getDay_28()),
                currentLoanModel.getAmountPaid_28(),
                currentLoanModel.getToCollect_28(),
                currentLoanModel.getProofPayment_28(),
                holder.day28, holder.date28, holder.payment28);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_29(),
                new Date(currentLoanModel.getDay_29()),
                currentLoanModel.getAmountPaid_29(),
                currentLoanModel.getToCollect_29(),
                currentLoanModel.getProofPayment_29(),
                holder.day29, holder.date29, holder.payment29);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_30(),
                new Date(currentLoanModel.getDay_30()),
                currentLoanModel.getAmountPaid_30(),
                currentLoanModel.getToCollect_30(),
                currentLoanModel.getProofPayment_30(),
                holder.day30, holder.date30, holder.payment30);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_31(),
                new Date(currentLoanModel.getDay_31()),
                currentLoanModel.getAmountPaid_31(),
                currentLoanModel.getToCollect_31(),
                currentLoanModel.getProofPayment_31(),
                holder.day31, holder.date31, holder.payment31);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_32(),
                new Date(currentLoanModel.getDay_32()),
                currentLoanModel.getAmountPaid_32(),
                currentLoanModel.getToCollect_32(),
                currentLoanModel.getProofPayment_32(),
                holder.day32, holder.date32, holder.payment32);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_33(),
                new Date(currentLoanModel.getDay_33()),
                currentLoanModel.getAmountPaid_33(),
                currentLoanModel.getToCollect_33(),
                currentLoanModel.getProofPayment_33(),
                holder.day33, holder.date33, holder.payment33);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_34(),
                new Date(currentLoanModel.getDay_34()),
                currentLoanModel.getAmountPaid_34(),
                currentLoanModel.getToCollect_34(),
                currentLoanModel.getProofPayment_34(),
                holder.day34, holder.date34, holder.payment34);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_35(),
                new Date(currentLoanModel.getDay_35()),
                currentLoanModel.getAmountPaid_35(),
                currentLoanModel.getToCollect_35(),
                currentLoanModel.getProofPayment_35(),
                holder.day35, holder.date35, holder.payment35);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_36(),
                new Date(currentLoanModel.getDay_36()),
                currentLoanModel.getAmountPaid_36(),
                currentLoanModel.getToCollect_36(),
                currentLoanModel.getProofPayment_36(),
                holder.day36, holder.date36, holder.payment36);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_37(),
                new Date(currentLoanModel.getDay_37()),
                currentLoanModel.getAmountPaid_37(),
                currentLoanModel.getToCollect_37(),
                currentLoanModel.getProofPayment_37(),
                holder.day37, holder.date37, holder.payment37);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_38(),
                new Date(currentLoanModel.getDay_38()),
                currentLoanModel.getAmountPaid_38(),
                currentLoanModel.getToCollect_38(),
                currentLoanModel.getProofPayment_38(),
                holder.day38, holder.date38, holder.payment38);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_39(),
                new Date(currentLoanModel.getDay_39()),
                currentLoanModel.getAmountPaid_39(),
                currentLoanModel.getToCollect_39(),
                currentLoanModel.getProofPayment_39(),
                holder.day39, holder.date39, holder.payment39);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_40(),
                new Date(currentLoanModel.getDay_40()),
                currentLoanModel.getAmountPaid_40(),
                currentLoanModel.getToCollect_40(),
                currentLoanModel.getProofPayment_40(),
                holder.day40, holder.date40, holder.payment40);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_41(),
                new Date(currentLoanModel.getDay_41()),
                currentLoanModel.getAmountPaid_41(),
                currentLoanModel.getToCollect_41(),
                currentLoanModel.getProofPayment_41(),
                holder.day41, holder.date41, holder.payment41);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_42(),
                new Date(currentLoanModel.getDay_42()),
                currentLoanModel.getAmountPaid_42(),
                currentLoanModel.getToCollect_42(),
                currentLoanModel.getProofPayment_42(),
                holder.day42, holder.date42, holder.payment42);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_43(),
                new Date(currentLoanModel.getDay_43()),
                currentLoanModel.getAmountPaid_43(),
                currentLoanModel.getToCollect_43(),
                currentLoanModel.getProofPayment_43(),
                holder.day43, holder.date43, holder.payment43);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_44(),
                new Date(currentLoanModel.getDay_44()),
                currentLoanModel.getAmountPaid_44(),
                currentLoanModel.getToCollect_44(),
                currentLoanModel.getProofPayment_44(),
                holder.day44, holder.date44, holder.payment44);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_45(),
                new Date(currentLoanModel.getDay_45()),
                currentLoanModel.getAmountPaid_45(),
                currentLoanModel.getToCollect_45(),
                currentLoanModel.getProofPayment_45(),
                holder.day45, holder.date45, holder.payment45);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_46(),
                new Date(currentLoanModel.getDay_46()),
                currentLoanModel.getAmountPaid_46(),
                currentLoanModel.getToCollect_46(),
                currentLoanModel.getProofPayment_46(),
                holder.day46, holder.date46, holder.payment46);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_47(),
                new Date(currentLoanModel.getDay_47()),
                currentLoanModel.getAmountPaid_47(),
                currentLoanModel.getToCollect_47(),
                currentLoanModel.getProofPayment_47(),
                holder.day47, holder.date47, holder.payment47);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_48(),
                new Date(currentLoanModel.getDay_48()),
                currentLoanModel.getAmountPaid_48(),
                currentLoanModel.getToCollect_48(),
                currentLoanModel.getProofPayment_48(),
                holder.day48, holder.date48, holder.payment48);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_49(),
                new Date(currentLoanModel.getDay_49()),
                currentLoanModel.getAmountPaid_49(),
                currentLoanModel.getToCollect_49(),
                currentLoanModel.getProofPayment_49(),
                holder.day49, holder.date49, holder.payment49);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_50(),
                new Date(currentLoanModel.getDay_50()),
                currentLoanModel.getAmountPaid_50(),
                currentLoanModel.getToCollect_50(),
                currentLoanModel.getProofPayment_50(),
                holder.day50, holder.date50, holder.payment50);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_51(),
                new Date(currentLoanModel.getDay_51()),
                currentLoanModel.getAmountPaid_51(),
                currentLoanModel.getToCollect_51(),
                currentLoanModel.getProofPayment_51(),
                holder.day51, holder.date51, holder.payment51);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_52(),
                new Date(currentLoanModel.getDay_52()),
                currentLoanModel.getAmountPaid_52(),
                currentLoanModel.getToCollect_52(),
                currentLoanModel.getProofPayment_52(),
                holder.day52, holder.date52, holder.payment52);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_53(),
                new Date(currentLoanModel.getDay_53()),
                currentLoanModel.getAmountPaid_53(),
                currentLoanModel.getToCollect_53(),
                currentLoanModel.getProofPayment_53(),
                holder.day53, holder.date53, holder.payment53);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_54(),
                new Date(currentLoanModel.getDay_54()),
                currentLoanModel.getAmountPaid_54(),
                currentLoanModel.getToCollect_54(),
                currentLoanModel.getProofPayment_54(),
                holder.day54, holder.date54, holder.payment54);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_55(),
                new Date(currentLoanModel.getDay_55()),
                currentLoanModel.getAmountPaid_55(),
                currentLoanModel.getToCollect_55(),
                currentLoanModel.getProofPayment_55(),
                holder.day55, holder.date55, holder.payment55);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_56(),
                new Date(currentLoanModel.getDay_56()),
                currentLoanModel.getAmountPaid_56(),
                currentLoanModel.getToCollect_56(),
                currentLoanModel.getProofPayment_56(),
                holder.day56, holder.date56, holder.payment56);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_57(),
                new Date(currentLoanModel.getDay_57()),
                currentLoanModel.getAmountPaid_57(),
                currentLoanModel.getToCollect_57(),
                currentLoanModel.getProofPayment_57(),
                holder.day57, holder.date57, holder.payment57);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_58(),
                new Date(currentLoanModel.getDay_58()),
                currentLoanModel.getAmountPaid_58(),
                currentLoanModel.getToCollect_58(),
                currentLoanModel.getProofPayment_58(),
                holder.day58, holder.date58, holder.payment58);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_59(),
                new Date(currentLoanModel.getDay_59()),
                currentLoanModel.getAmountPaid_59(),
                currentLoanModel.getToCollect_59(),
                currentLoanModel.getProofPayment_59(),
                holder.day59, holder.date59, holder.payment59);

        setDayDetails(holder, currentLoanModel, currentLoanModel.getPaidOrNot_60(),
                new Date(currentLoanModel.getDay_60()),
                currentLoanModel.getAmountPaid_60(),
                currentLoanModel.getToCollect_60(),
                currentLoanModel.getProofPayment_60(),
                holder.day60, holder.date60, holder.payment60);



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void setDayDetails(HomeRecentViewHolder holder, CollectorDailyCollectionModel currentLoanModel, String paidOrNot, Date day, double amountPaid, double toCollect, String proofPayment, View dayView, TextView dateView, TextView paymentView) {
        if ("yes".equalsIgnoreCase(paidOrNot)) {
            dayView.setVisibility(View.VISIBLE);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(day);
            String formattedPayment = String.format("%.2f", amountPaid);
            String formattedNeedPayment = String.format("%.2f", toCollect);

            dateView.setText(formattedDate);
            paymentView.setText(formattedPayment);

            dayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HomeBorrowerDetail.class);
                    // Put the values you want to pass to the next activity
                    intent.putExtra("formattedDate", formattedDate);
                    intent.putExtra("formattedPayment", formattedPayment);
                    intent.putExtra("proofPayment", proofPayment); // Use the passed proofPayment
                    intent.putExtra("needToPay", formattedNeedPayment);
                    // Start the new activity
                    context.startActivity(intent);
                }
            });
        }
    }




}
class HomeRecentViewHolder extends RecyclerView.ViewHolder{
    TextView payment1, date1;
    TextView payment2, date2;
    TextView payment3, date3;
    TextView payment4, date4;
    TextView payment5, date5;
    TextView payment6, date6;
    TextView payment7, date7;
    TextView payment8, date8;
    TextView payment9, date9;
    TextView payment10, date10;
    TextView payment11, date11;
    TextView payment12, date12;
    TextView payment13, date13;
    TextView payment14, date14;
    TextView payment15, date15;
    TextView payment16, date16;
    TextView payment17, date17;
    TextView payment18, date18;
    TextView payment19, date19;
    TextView payment20, date20;
    TextView payment21, date21;
    TextView payment22, date22;
    TextView payment23, date23;
    TextView payment24, date24;
    TextView payment25, date25;
    TextView payment26, date26;
    TextView payment27, date27;
    TextView payment28, date28;
    TextView payment29, date29;
    TextView payment30, date30;
    TextView payment31, date31;
    TextView payment32, date32;
    TextView payment33, date33;
    TextView payment34, date34;
    TextView payment35, date35;
    TextView payment36, date36;
    TextView payment37, date37;
    TextView payment38, date38;
    TextView payment39, date39;
    TextView payment40, date40;
    TextView payment41, date41;
    TextView payment42, date42;
    TextView payment43, date43;
    TextView payment44, date44;
    TextView payment45, date45;
    TextView payment46, date46;
    TextView payment47, date47;
    TextView payment48, date48;
    TextView payment49, date49;
    TextView payment50, date50;
    TextView payment51, date51;
    TextView payment52, date52;
    TextView payment53, date53;
    TextView payment54, date54;
    TextView payment55, date55;
    TextView payment56, date56;
    TextView payment57, date57;
    TextView payment58, date58;
    TextView payment59, date59;
    TextView payment60, date60;
    LinearLayout day1,day2,day3,day4,day5,day6,day7,day8,day9,day10;
    LinearLayout day11,day12,day13,day14,day15,day16,day17,day18,day19,day20;
    LinearLayout day21, day22, day23, day24, day25, day26, day27, day28, day29, day30;
    LinearLayout day31, day32, day33, day34, day35, day36, day37, day38, day39, day40;
    LinearLayout day41, day42, day43, day44, day45, day46, day47, day48, day49, day50;
    LinearLayout day51, day52, day53, day54, day55, day56, day57, day58, day59, day60;

    public HomeRecentViewHolder(@NonNull View itemView) {
        super(itemView);
        date1 = itemView.findViewById(R.id.date1);
        day1 = itemView.findViewById(R.id.day1);
        payment1 = itemView.findViewById(R.id.payment1);

        payment2 = itemView.findViewById(R.id.payment2);
        date2 = itemView.findViewById(R.id.date2);
        day2 = itemView.findViewById(R.id.day2);

        date3 = itemView.findViewById(R.id.date3);
        day3 = itemView.findViewById(R.id.day3);
        payment3 = itemView.findViewById(R.id.payment3);

        date4 = itemView.findViewById(R.id.date4);
        day4 = itemView.findViewById(R.id.day4);
        payment4 = itemView.findViewById(R.id.payment4);

        date5 = itemView.findViewById(R.id.date5);
        day5 = itemView.findViewById(R.id.day5);
        payment5 = itemView.findViewById(R.id.payment5);

        date6 = itemView.findViewById(R.id.date6);
        day6 = itemView.findViewById(R.id.day6);
        payment6 = itemView.findViewById(R.id.payment6);

        date7 = itemView.findViewById(R.id.date7);
        day7 = itemView.findViewById(R.id.day7);
        payment7 = itemView.findViewById(R.id.payment7);

        date8 = itemView.findViewById(R.id.date8);
        day8 = itemView.findViewById(R.id.day8);
        payment8 = itemView.findViewById(R.id.payment8);

        date9 = itemView.findViewById(R.id.date9);
        day9 = itemView.findViewById(R.id.day9);
        payment9 = itemView.findViewById(R.id.payment9);

        date10= itemView.findViewById(R.id.date10);
        day10 = itemView.findViewById(R.id.day10);
        payment10 = itemView.findViewById(R.id.payment10);

        date11 = itemView.findViewById(R.id.date11);
        day11 = itemView.findViewById(R.id.day11);
        payment11 = itemView.findViewById(R.id.payment11);

        date12 = itemView.findViewById(R.id.date12);
        day12 = itemView.findViewById(R.id.day12);
        payment12 = itemView.findViewById(R.id.payment12);

        date13 = itemView.findViewById(R.id.date13);
        day13 = itemView.findViewById(R.id.day13);
        payment13 = itemView.findViewById(R.id.payment13);

        date14 = itemView.findViewById(R.id.date14);
        day14 = itemView.findViewById(R.id.day14);
        payment14 = itemView.findViewById(R.id.payment14);

        date15 = itemView.findViewById(R.id.date15);
        day15 = itemView.findViewById(R.id.day15);
        payment15 = itemView.findViewById(R.id.payment15);

        date16 = itemView.findViewById(R.id.date16);
        day16 = itemView.findViewById(R.id.day16);
        payment16 = itemView.findViewById(R.id.payment16);

        date17 = itemView.findViewById(R.id.date17);
        day17 = itemView.findViewById(R.id.day17);
        payment17 = itemView.findViewById(R.id.payment17);

        date18 = itemView.findViewById(R.id.date18);
        day18 = itemView.findViewById(R.id.day18);
        payment18 = itemView.findViewById(R.id.payment18);

        date19 = itemView.findViewById(R.id.date19);
        day19 = itemView.findViewById(R.id.day19);
        payment19 = itemView.findViewById(R.id.payment19);

        payment20 = itemView.findViewById(R.id.payment20);
        date20 = itemView.findViewById(R.id.date20);
        day20 = itemView.findViewById(R.id.day20);

        payment21 = itemView.findViewById(R.id.payment21);
        date21 = itemView.findViewById(R.id.date21);
        day21 = itemView.findViewById(R.id.day21);

        payment22 = itemView.findViewById(R.id.payment22);
        date22 = itemView.findViewById(R.id.date22);
        day22 = itemView.findViewById(R.id.day22);

        payment23 = itemView.findViewById(R.id.payment23);
        date23 = itemView.findViewById(R.id.date23);
        day23 = itemView.findViewById(R.id.day23);

        payment24 = itemView.findViewById(R.id.payment24);
        date24 = itemView.findViewById(R.id.date24);
        day24 = itemView.findViewById(R.id.day24);

        payment25 = itemView.findViewById(R.id.payment25);
        date25 = itemView.findViewById(R.id.date25);
        day25 = itemView.findViewById(R.id.day25);

        payment26 = itemView.findViewById(R.id.payment26);
        date26 = itemView.findViewById(R.id.date26);
        day26 = itemView.findViewById(R.id.day26);

        payment27 = itemView.findViewById(R.id.payment27);
        date27 = itemView.findViewById(R.id.date27);
        day27 = itemView.findViewById(R.id.day27);

        payment28 = itemView.findViewById(R.id.payment28);
        date28 = itemView.findViewById(R.id.date28);
        day28 = itemView.findViewById(R.id.day28);

        payment29 = itemView.findViewById(R.id.payment29);
        date29 = itemView.findViewById(R.id.date29);
        day29 = itemView.findViewById(R.id.day29);

        payment30 = itemView.findViewById(R.id.payment30);
        date30 = itemView.findViewById(R.id.date30);
        day30 = itemView.findViewById(R.id.day30);

        payment31 = itemView.findViewById(R.id.payment31);
        date31 = itemView.findViewById(R.id.date31);
        day31 = itemView.findViewById(R.id.day31);

        payment32 = itemView.findViewById(R.id.payment32);
        date32 = itemView.findViewById(R.id.date32);
        day32 = itemView.findViewById(R.id.day32);

        payment33 = itemView.findViewById(R.id.payment33);
        date33 = itemView.findViewById(R.id.date33);
        day33 = itemView.findViewById(R.id.day33);

        payment34 = itemView.findViewById(R.id.payment34);
        date34 = itemView.findViewById(R.id.date34);
        day34 = itemView.findViewById(R.id.day34);

        payment35 = itemView.findViewById(R.id.payment35);
        date35 = itemView.findViewById(R.id.date35);
        day35 = itemView.findViewById(R.id.day35);

        payment36 = itemView.findViewById(R.id.payment36);
        date36 = itemView.findViewById(R.id.date36);
        day36 = itemView.findViewById(R.id.day36);

        payment37 = itemView.findViewById(R.id.payment37);
        date37 = itemView.findViewById(R.id.date37);
        day37 = itemView.findViewById(R.id.day37);

        payment38 = itemView.findViewById(R.id.payment38);
        date38 = itemView.findViewById(R.id.date38);
        day38 = itemView.findViewById(R.id.day38);

        payment39 = itemView.findViewById(R.id.payment39);
        date39 = itemView.findViewById(R.id.date39);
        day39 = itemView.findViewById(R.id.day39);

        payment40 = itemView.findViewById(R.id.payment40);
        date40 = itemView.findViewById(R.id.date40);
        day40 = itemView.findViewById(R.id.day40);

        payment41 = itemView.findViewById(R.id.payment41);
        date41 = itemView.findViewById(R.id.date41);
        day41 = itemView.findViewById(R.id.day41);

        payment42 = itemView.findViewById(R.id.payment42);
        date42 = itemView.findViewById(R.id.date42);
        day42 = itemView.findViewById(R.id.day42);

        payment43 = itemView.findViewById(R.id.payment43);
        date43 = itemView.findViewById(R.id.date43);
        day43 = itemView.findViewById(R.id.day43);

        payment44 = itemView.findViewById(R.id.payment44);
        date44 = itemView.findViewById(R.id.date44);
        day44 = itemView.findViewById(R.id.day44);

        payment45 = itemView.findViewById(R.id.payment45);
        date45 = itemView.findViewById(R.id.date45);
        day45 = itemView.findViewById(R.id.day45);

        payment46 = itemView.findViewById(R.id.payment46);
        date46 = itemView.findViewById(R.id.date46);
        day46 = itemView.findViewById(R.id.day46);

        payment47 = itemView.findViewById(R.id.payment47);
        date47 = itemView.findViewById(R.id.date47);
        day47 = itemView.findViewById(R.id.day47);

        payment48 = itemView.findViewById(R.id.payment48);
        date48 = itemView.findViewById(R.id.date48);
        day48 = itemView.findViewById(R.id.day48);

        payment49 = itemView.findViewById(R.id.payment49);
        date49 = itemView.findViewById(R.id.date49);
        day49 = itemView.findViewById(R.id.day49);

        payment50 = itemView.findViewById(R.id.payment50);
        date50 = itemView.findViewById(R.id.date50);
        day50 = itemView.findViewById(R.id.day50);

        payment51 = itemView.findViewById(R.id.payment51);
        date51 = itemView.findViewById(R.id.date51);
        day51 = itemView.findViewById(R.id.day51);

        payment52 = itemView.findViewById(R.id.payment52);
        date52 = itemView.findViewById(R.id.date52);
        day52 = itemView.findViewById(R.id.day52);

        payment53 = itemView.findViewById(R.id.payment53);
        date53 = itemView.findViewById(R.id.date53);
        day53 = itemView.findViewById(R.id.day53);

        payment54 = itemView.findViewById(R.id.payment54);
        date54 = itemView.findViewById(R.id.date54);
        day54 = itemView.findViewById(R.id.day54);

        payment55 = itemView.findViewById(R.id.payment55);
        date55 = itemView.findViewById(R.id.date55);
        day55 = itemView.findViewById(R.id.day55);

        payment56 = itemView.findViewById(R.id.payment56);
        date56 = itemView.findViewById(R.id.date56);
        day56 = itemView.findViewById(R.id.day56);

        payment57 = itemView.findViewById(R.id.payment57);
        date57 = itemView.findViewById(R.id.date57);
        day57 = itemView.findViewById(R.id.day57);

        payment58 = itemView.findViewById(R.id.payment58);
        date58 = itemView.findViewById(R.id.date58);
        day58 = itemView.findViewById(R.id.day58);

        payment59 = itemView.findViewById(R.id.payment59);
        date59 = itemView.findViewById(R.id.date59);
        day59 = itemView.findViewById(R.id.day59);

        payment60 = itemView.findViewById(R.id.payment60);
        date60 = itemView.findViewById(R.id.date60);
        day60 = itemView.findViewById(R.id.day60);


    }
}


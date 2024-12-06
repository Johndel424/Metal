package com.example.metal.Borrower.Home_Feature;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.R;
import com.squareup.picasso.Picasso;

public class HomeBorrowerDetail extends AppCompatActivity {
    TextView amountCollected, amountShouldBeCollect, formattedDateTextView,statusPaymentTextView;
    ImageView paymentProof;
    ImageButton back;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_borrower_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        statusPaymentTextView = findViewById(R.id.statusPaymentTextView);
        paymentProof = findViewById(R.id.paymentProof);
        amountCollected = findViewById(R.id.amountToBeCollected);
        amountShouldBeCollect = findViewById(R.id.amountShouldBeCollect);
        back = findViewById(R.id.backSecond);
        formattedDateTextView = findViewById(R.id.formattedDateTextView);
        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();

        // Extract the data passed with the intent
        String formattedDate = intent.getStringExtra("formattedDate");
        String formattedPayment = intent.getStringExtra("formattedPayment");
        String proofPayment = intent.getStringExtra("proofPayment");
        String needToPay = intent.getStringExtra("needToPay");
        String nname = intent.getStringExtra("nname");

        amountShouldBeCollect.setText(needToPay);
        amountCollected.setText(formattedPayment);
        formattedDateTextView.setText(formattedDate);
        statusPaymentTextView.setText("Paid");

        Picasso.get().load(proofPayment).into(paymentProof);

    }
}
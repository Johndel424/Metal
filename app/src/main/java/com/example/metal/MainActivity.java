package com.example.metal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.metal.Borrower.Home;
import com.example.metal.Collector.HomeCollector;
import com.example.metal.Secretary.Secretary_Home.HomeSecretary;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    private int[] images = {R.drawable.gif1, R.drawable.gif2, R.drawable.gif3};
    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler();
    private RelativeLayout splashScreen, gifScreen;
    private ViewFlipper viewFlipper;
    private TextView getStartedButton, signIn,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        mAuth = FirebaseAuth.getInstance();
        // Initialize the ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Check if user is already logged in
        if (mAuth.getCurrentUser() != null) {
            // Show the loading indicator
            progressDialog.show();

            String userId = mAuth.getCurrentUser().getUid();
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Dismiss the loading indicator
                    progressDialog.dismiss();

                    if (snapshot.exists()) {
                        String userType = snapshot.child("usertype").getValue(String.class);
                        if (userType != null) {
                            if ("borrower".equals(userType)) {
                                startActivity(new Intent(MainActivity.this, Home.class));
                            } else if ("secretary".equals(userType)) {
                                startActivity(new Intent(MainActivity.this, HomeSecretary.class));
                            } else if ("collector".equals(userType)) {
                                startActivity(new Intent(MainActivity.this, HomeCollector.class));
                            }
                            finish();
                        } else {
                            // Handle case where userType is null
                            Toast.makeText(MainActivity.this, "User type not found. Please contact support.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle case where snapshot does not exist
                        Toast.makeText(MainActivity.this, "User data not found. Please contact support.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Dismiss the loading indicator and handle possible errors
                    progressDialog.dismiss();
                    // Show a toast with the error message
                    Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle case where user is not logged in
            Toast.makeText(MainActivity.this, "User is not logged in.", Toast.LENGTH_SHORT).show();
        }


        //splash screen
        splashScreen = findViewById(R.id.splash);
        getStartedButton = findViewById(R.id.getStartedButton);
        viewFlipper = findViewById(R.id.viewFlipper);
        gifScreen = findViewById(R.id.gifScreen);

        //navigate to register
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gumawa ng Intent para mag-navigate sa SecondActivity
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);  // I-start ang SecondActivity
            }
        });
        //navigate to login
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gumawa ng Intent para mag-navigate sa SecondActivity
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);  // I-start ang SecondActivity
            }
        });
        //hide gif page
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the RelativeLayout
                gifScreen.setVisibility(View.GONE);
            }
        });
        // Start flipping
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3050); // 3 seconds per slide

        //Make the splashScreen gone
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashScreen.setVisibility(View.GONE);
            }
        }, 5000);

        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ImageAdapter adapter = new ImageAdapter(this, images);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Walang laman ang tab icons dahil ginagamit lang ito bilang dot indicator
        }).attach();

        // Simulan ang auto-slide
        sliderHandler.postDelayed(sliderRunnable, 3000); // 3 seconds delay

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Pagkatapos ng page transition, maghintay ng 3 segundo bago mag-move to the next slide
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            int nextItem = currentItem + 1;

            if (nextItem >= images.length) {
                nextItem = 0; // Bumalik sa unang image kung naabot na ang huling image
            }

            viewPager.setCurrentItem(nextItem, true);
            sliderHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable); // Itigil ang auto-slide kapag naka-pause ang activity
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000); // Ipagpatuloy ang auto-slide kapag bumalik ang activity
    }
}
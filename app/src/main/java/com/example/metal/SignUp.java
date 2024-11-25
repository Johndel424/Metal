package com.example.metal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.metal.Borrower.Borrower_PinRegistration;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText phoneNumberEditText;
    //private EditText otpEditText;
    String digi1,digi2,digi3,digi4,digi5,digi6;
    private TextView sendOTPButton;
    private TextView verifyOTPButton;
    private String verificationId;
    private ProgressDialog progressDialog;
    private ImageButton back;
    String phoneNumber;
    TextView resendOtp;
    private EditText digit1, digit2, digit3, digit4, digit5, digit6;
    private LinearLayout OtpVerify;
    RelativeLayout SendOtp;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int PERMISSION_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //linearid
        OtpVerify = findViewById(R.id.otpVerify);
        SendOtp = findViewById(R.id.sentOtp);
        back = findViewById(R.id.back);
        resendOtp = findViewById(R.id.resendOtp);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOtp.setVisibility(View.VISIBLE);
                OtpVerify.setVisibility(View.GONE);
            }
        });
        ImageView gif2 = findViewById(R.id.gif2);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gifsent) // replace with your GIF file
                .into(gif2);
        ImageView gifImageView = findViewById(R.id.logo);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gifregis) // replace with your GIF file
                .into(gifImageView);

        //show otp
        digit1 = findViewById(R.id.digit1);
        digit2 = findViewById(R.id.digit2);
        digit3 = findViewById(R.id.digit3);
        digit4 = findViewById(R.id.digit4);
        digit5 = findViewById(R.id.digit5);
        digit6 = findViewById(R.id.digit6);

        addTextWatcher(digit1, digit2);
        addTextWatcher(digit2, digit3);
        addTextWatcher(digit3, digit4);
        addTextWatcher(digit4, digit5);
        addTextWatcher(digit5, digit6);

        TextView textView2 = findViewById(R.id.privacyPolicy);
        String text = "By signing up, you agree to Metal 5's Privacy Policy.";

        // Create a SpannableString
        SpannableString spannableString = new SpannableString(text);

        // Define the start and end index of "Privacy Policy"
        int start = text.indexOf("Privacy Policy");
        int end = start + "Privacy Policy".length();

        // Apply the underline and color span
        spannableString.setSpan(new UnderlineSpan(), start, end, 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, 0);

        // Add a ClickableSpan to handle the click event
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Navigate to policy (class)
                Intent intent = new Intent(getApplicationContext(), Policy.class);
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true); // This keeps the underline for the link
            }
        };

        // Set the ClickableSpan
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Enable clickable links in TextView
        textView2.setMovementMethod(LinkMovementMethod.getInstance());

        // Set the spannable string to the TextView
        textView2.setText(spannableString);


        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        //otpEditText = findViewById(R.id.otpEditText);
        sendOTPButton = findViewById(R.id.sendOTPButton);
        verifyOTPButton = findViewById(R.id.verifyOTPButton);

        sendOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberEditText.getText().toString().trim();

                // Check if phone number is not empty
                if (phoneNumber.isEmpty()) {
                    phoneNumberEditText.setError("Phone number is required");
                    return;
                }

                // Siguraduhin na ang `+63` ay kasama sa unahan
                if (!phoneNumber.startsWith("+63")) {
                    phoneNumber = "+63" + phoneNumber;
                }

                //update the ui of phone number
                TextView numberSent = findViewById(R.id.numberSent);
                String num = "We sent it to the number <b>" + phoneNumber + "</b>";
                numberSent.setText(Html.fromHtml(num));

                sendOTP(phoneNumber);
            }
        });
        // Initially disable the image clickability
        resendOtp.setEnabled(false);
        // Set click listener on the image
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call resendOTP when the image is clicked
                resendOTP(phoneNumber); // Ensure phoneNumber is the current phone number
            }
        });
        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from each EditText
                digi1 = digit1.getText().toString().trim();
                digi2 = digit2.getText().toString().trim();
                digi3 = digit3.getText().toString().trim();
                digi4 = digit4.getText().toString().trim();
                digi5 = digit5.getText().toString().trim();
                digi6 = digit6.getText().toString().trim();

                // Concatenate the digits to form the OTP
                String otp = digi1 + digi2 + digi3 + digi4+ digi5 + digi6;

                // Check if OTP is not empty or incomplete
                if (otp.length() < 6) {
                    Toast.makeText(SignUp.this, "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyOTP(otp);
            }
        });


        // Check for notification permission and request if not granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
        }
        // Check for notification channel and create if not exists
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "Default Channel";
            String channelDescription = "This is the default notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void sendOTP(String phoneNumber) {
        // Initialize progress dialog
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Sending OTP...");
        progressDialog.setCancelable(false); // prevent dismiss by tapping outside of the dialog
        progressDialog.show();
        SendOtp.setVisibility(View.GONE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // Dismiss progress dialog
                        progressDialog.dismiss();
                        SendOtp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUp.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        // Dismiss progress dialog
                        progressDialog.dismiss();
                        OtpVerify.setVisibility(View.VISIBLE);

                        // Set the initial countdown time (60 seconds)
                        int countdownTime = 60;

                        // Create a countdown timer
                        new CountDownTimer(countdownTime * 1000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                // Update the TextView with the remaining time in seconds
                                resendOtp.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds");
                            }

                            public void onFinish() {
                                // Enable the clickability and update the TextView
                                resendOtp.setEnabled(true);
                                resendOtp.setText("Resend OTP");
                            }
                        }.start();

                        // Start a 60-second timer to enable the clickability
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Enable the image clickability after 60 seconds
                                resendOtp.setEnabled(true);
                            }
                        }, 60000); // 60000 milliseconds = 60 seconds

                        SignUp.this.verificationId = verificationId;
                        // Show OTP entry UI if needed
                    }
                }
        );
    }
    // Method to resend OTP
    private void resendOTP(String phoneNumber) {
        // Initialize progress dialog
        progressDialog.setMessage("Resending OTP...");
        progressDialog.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // Dismiss progress dialog
                        progressDialog.dismiss();

                        Toast.makeText(SignUp.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        // Dismiss progress dialog
                        progressDialog.dismiss();
                        OtpVerify.setVisibility(View.VISIBLE);

                        // Save the new verification ID and token
                        SignUp.this.verificationId = verificationId;
                        SignUp.this.resendToken = token;

                        // Show OTP entry UI if needed
                    }
                },
                resendToken // This is the key part: passing the resend token to request a new OTP
        );
    }
    private void verifyOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        // Initialize progress dialog
//        progressDialog = new ProgressDialog(SignUp.this);
//        progressDialog.setMessage("Verifying OTP...");
//        progressDialog.setCancelable(false); // prevent dismiss by tapping outside of the dialog
//        progressDialog.show();
//
//        // Perform sign-in with the credential
//        FirebaseAuth.getInstance().signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        // Dismiss progress dialog
//                        progressDialog.dismiss();
//
//                        if (task.isSuccessful()) {
//                            // Get the phone number from EditText
//                            String phoneNumber = phoneNumberEditText.getText().toString();
//
//                            // Check if the phone number doesn't already start with "+63"
//                            if (!phoneNumber.startsWith("+63")) {
//                                phoneNumber = "+63" + phoneNumber;
//                            }
//
//                            // Get the current user's UID
//                            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                            // Reference to the 'users' node in Firebase Realtime Database
//                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
//
//                            int creditScore = 0; // Default credit score
//                            int currentLoan = 0; // Default current loan
//
//                            // Create a map for the user data
//                            Map<String, Object> userUpdates = new HashMap<>();
//                            userUpdates.put("phoneNumber", phoneNumber);
//                            userUpdates.put("uid", userUid);  // Save the UID
//                            userUpdates.put("usertype", "borrower");  // Set default usertype
//                            userUpdates.put("accountStatus", "pending");
//                            userUpdates.put("accountDetailsComplete", "no");
//                            userUpdates.put("accountDocumentsComplete", "no");
//                            userUpdates.put("creditScore", creditScore);  // Save default credit score
//                            userUpdates.put("currentLoan", currentLoan);  // Save default current loan
//
//                            // Update the database with the user data
//                            String finalPhoneNumber = phoneNumber;
//                            databaseReference.child(userUid).updateChildren(userUpdates)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                // Create a new Intent
//                                                Intent intent = new Intent(SignUp.this, Borrower_PinRegistration.class);
//
//                                                // Include the phoneNumber value in the Intent
//                                                intent.putExtra("phoneNumber", finalPhoneNumber);
//
//                                                // Start the new activity with the Intent including phoneNumber
//                                                startActivity(intent);
//
//                                                // Show notification
//                                                showNotification("Otp Verification Successful");
//
//                                                // Proceed with the login flow
//                                                finish(); // Finish the current activity to prevent the user from going back to the sign-up screen
//                                            } else {
//                                                Toast.makeText(getApplicationContext(), "Failed to save user details", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Otp Verification Failed", Toast.LENGTH_SHORT).show();
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                Toast.makeText(getApplicationContext(), "Invalid Otp", Toast.LENGTH_SHORT).show();
//
//                                // Delay execution by 2 seconds (2000 milliseconds)
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // Clear the content of the EditTexts after 2 seconds
//                                        digit1.setText("");
//                                        digit2.setText("");
//                                        digit3.setText("");
//                                        digit4.setText("");
//                                        digit5.setText("");
//                                        digit6.setText("");
//                                    }
//                                }, 2000); // 2000 milliseconds = 2 seconds
//                            }
//                        }
//                    }
//                });
//    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        // Initialize progress dialog
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setMessage("Verifying OTP...");
        progressDialog.setCancelable(false); // prevent dismiss by tapping outside of the dialog
        progressDialog.show();

        // Perform sign-in with the credential
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Dismiss progress dialog
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Get the phone number from EditText
                            String phoneNumber = phoneNumberEditText.getText().toString();

                            // Check if the phone number doesn't already start with "+63"
                            if (!phoneNumber.startsWith("+63")) {
                                phoneNumber = "+63" + phoneNumber;
                            }

                            // Reference to the 'users' node in Firebase Realtime Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

                            // Check if phone number already exists in the database
                            Query query = databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber);
                            String finalPhoneNumber = phoneNumber;
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Phone number already exists, show a message
                                        Toast.makeText(SignUp.this, "Phone number is already registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Proceed with saving the user details if the phone number doesn't exist
                                        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        int creditScore = 0; // Default credit score
                                        int currentLoan = 0; // Default current loan

                                        // Create a map for the user data
                                        Map<String, Object> userUpdates = new HashMap<>();
                                        userUpdates.put("phoneNumber", finalPhoneNumber);
                                        userUpdates.put("uid", userUid);  // Save the UID
                                        userUpdates.put("usertype", "borrower");  // Set default usertype
                                        userUpdates.put("accountStatus", "pending");
                                        userUpdates.put("accountDetailsComplete", "no");
                                        userUpdates.put("accountDocumentsComplete", "no");
                                        userUpdates.put("creditScore", creditScore);  // Save default credit score
                                        userUpdates.put("currentLoan", currentLoan);  // Save default current loan

                                        databaseReference.child(userUid).updateChildren(userUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Create a new Intent
                                                            Intent intent = new Intent(SignUp.this, Borrower_PinRegistration.class);

                                                            // Include the phoneNumber value in the Intent
                                                            intent.putExtra("phoneNumber", finalPhoneNumber);

                                                            // Start the new activity with the Intent including phoneNumber
                                                            startActivity(intent);

                                                            // Show notification
                                                            showNotification("Otp Verification Successful");

                                                            // Proceed with the login flow
                                                            finish(); // Finish the current activity to prevent the user from going back to the sign-up screen
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Failed to save user details", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(SignUp.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Otp Verification Failed", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Invalid Otp", Toast.LENGTH_SHORT).show();

                                // Delay execution by 2 seconds (2000 milliseconds)
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Clear the content of the EditTexts after 2 seconds
                                        digit1.setText("");
                                        digit2.setText("");
                                        digit3.setText("");
                                        digit4.setText("");
                                        digit5.setText("");
                                        digit6.setText("");
                                    }
                                }, 2000); // 2000 milliseconds = 2 seconds
                            }
                        }
                    }
                });
    }


    private void showNotification(String message) {
        String channelId = "default"; // Tiyakin na ito ay katulad sa ID na iyong nilikha
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Phone Number Verified")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Check for notification permission explicitly
        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        } else {
            // Handle case where permission is not granted
            // For example, show a message to the user or request permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Notification permission granted, proceed with showing notifications
                } else {
                    // Notification permission denied, handle accordingly (e.g., show message to user)
                }
                break;
        }
    }
    //for update ng otp
    private void addTextWatcher(final EditText currentDigit, final EditText nextDigit) {
        currentDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (currentDigit.getText().toString().length() == 1) {
                    nextDigit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
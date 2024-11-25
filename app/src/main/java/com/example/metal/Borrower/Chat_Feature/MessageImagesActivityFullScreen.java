package com.example.metal.Borrower.Chat_Feature;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.metal.R;

public class MessageImagesActivityFullScreen extends AppCompatActivity {
    private ProgressDialog progressDialog;
    ImageButton back;
    private RelativeLayout floatingLayout;
    ImageView fullscreenImageView;
    private boolean isFront = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_images_full_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.back);
        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Get the image URL from the Intent
        String imageUrl = getIntent().getStringExtra("image_url");
        back.setOnClickListener(v -> onBackPressed());

        // Find the ImageView and load the image using Glide
        fullscreenImageView = findViewById(R.id.fullscreen_image_view);
        floatingLayout = findViewById(R.id.floatingLayout);

        fullscreenImageView.setOnClickListener(new View.OnClickListener() {
            boolean elevated = false;

            @Override
            public void onClick(View v) {
                if (elevated) {
                    // Balik sa original elevation
                    fullscreenImageView.setElevation(-100);
                    floatingLayout.setElevation(100);
                } else {
                    // Set custom elevation
                    fullscreenImageView.bringToFront();
                    fullscreenImageView.setElevation(100);
                    floatingLayout.setElevation(-100);
                }

                elevated = !elevated;  // Toggle the state
            }
        });

        Glide.with(this)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressDialog.dismiss(); // Dismiss the dialog if loading fails
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressDialog.dismiss(); // Dismiss the dialog if loading succeeds
                        return false;
                    }
                })
                .into(fullscreenImageView);// Set OnClickListener for back button


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
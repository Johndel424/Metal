package com.example.metal.Borrower.UploadDocuments_Feature;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//public class FaceScanner extends AppCompatActivity {
//    private PreviewView previewView;
//    private TextView statusTextView;
//    private Button uploadButton;
//    private ExecutorService cameraExecutor;
//
//    private Bitmap detectedFaceBitmap; // Store the detected face bitmap
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_face_scanner);
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        previewView = findViewById(R.id.previewView);
//        statusTextView = findViewById(R.id.statusTextView);
//        uploadButton = findViewById(R.id.uploadButton);
//
//        cameraExecutor = Executors.newSingleThreadExecutor();
//
//        // Disable the upload button initially
//        uploadButton.setEnabled(false);
//
//        // Set click listener for the upload button
//        uploadButton.setOnClickListener(v -> {
//            Bitmap screenshot = captureScreenshot();  // Capture screenshot of preview view
//            if (screenshot != null) {
//                Log.d("UploadButton", "Starting upload...");
//                uploadImageToFirebase(screenshot);  // Upload the screenshot
//            } else {
//                Log.e("UploadButton", "Screenshot capture failed!");
//                Toast.makeText(this, "Failed to capture screenshot!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        startCamera();
//    }
//
//    private void startCamera() {
//        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//
//        cameraProviderFuture.addListener(() -> {
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                CameraSelector cameraSelector = new CameraSelector.Builder()
//                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
//                        .build();
//
//                Preview preview = new Preview.Builder().build();
//                preview.setSurfaceProvider(previewView.getSurfaceProvider());
//
//                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                        .setTargetResolution(new Size(1280, 720))
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build();
//
//                imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeImage);
//
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
//            } catch (Exception e) {
//                Log.e("CameraX", "Error starting camera: " + e.getMessage());
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }
//
//    private void analyzeImage(ImageProxy imageProxy) {
//        try {
//            @SuppressWarnings("UnsafeOptInUsageError")
//            InputImage inputImage = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
//
//            FaceDetectorOptions options = new FaceDetectorOptions.Builder()
//                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
//                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
//                    .build();
//
//            FaceDetector detector = FaceDetection.getClient(options);
//
//            detector.process(inputImage)
//                    .addOnSuccessListener(faces -> {
//                        if (!faces.isEmpty()) {
//                            for (Face face : faces) {
//                                Rect bounds = face.getBoundingBox();
//                                if (bounds.left > 0 && bounds.top > 0 &&
//                                        bounds.right < previewView.getWidth() &&
//                                        bounds.bottom < previewView.getHeight()) {
//
//                                    detectedFaceBitmap = getBitmapFromImage(imageProxy); // Store the detected face
//                                    runOnUiThread(() -> {
//                                        statusTextView.setText("Face Detected!");
//                                        uploadButton.setEnabled(true); // Enable the upload button
//                                    });
//                                } else {
//                                    runOnUiThread(() -> {
//                                        statusTextView.setText("Adjust your face for full detection.");
//                                        uploadButton.setEnabled(false); // Disable the upload button
//                                    });
//                                }
//                            }
//                        } else {
//                            runOnUiThread(() -> {
//                                statusTextView.setText("No Face Detected.");
//                                uploadButton.setEnabled(false); // Disable the upload button
//                            });
//                        }
//                    })
//                    .addOnFailureListener(e -> Log.e("FaceDetection", "Detection failed: " + e.getMessage()))
//                    .addOnCompleteListener(task -> imageProxy.close());
//        } catch (Exception e) {
//            imageProxy.close();
//            Log.e("ImageAnalysis", "Error analyzing image: " + e.getMessage());
//        }
//    }
//
//    private Bitmap getBitmapFromImage(ImageProxy imageProxy) {
//        Image image = imageProxy.getImage();
//        if (image != null) {
//            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        }
//        return null;
//    }
//
//    private Bitmap captureScreenshot() {
//        Bitmap screenshot = null;
//        try {
//            // Create a bitmap with the same width and height as the previewView
//            screenshot = Bitmap.createBitmap(previewView.getWidth(), previewView.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(screenshot);
//            previewView.draw(canvas);  // Draw the content of the previewView onto the bitmap
//        } catch (Exception e) {
//            Log.e("Screenshot", "Error capturing screenshot: " + e.getMessage());
//        }
//        return screenshot;
//    }
//
//private void uploadImageToFirebase(Bitmap bitmap) {
//    // Show loading dialog
//    ProgressDialog progressDialog = new ProgressDialog(this);
//    progressDialog.setMessage("Uploading image...");
//    progressDialog.setCancelable(false);
//    progressDialog.show();
//
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//    byte[] data = baos.toByteArray();
//
//    FirebaseStorage storage = FirebaseStorage.getInstance();
//    StorageReference storageRef = storage.getReference();
//
//    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//    if (user == null) {
//        Log.e("Firebase", "User not authenticated.");
//        progressDialog.dismiss();
//        return;
//    }
//
//    String userUid = user.getUid();
//    StorageReference profileImageRef = storageRef.child("profile_images/" + userUid + ".jpg");
//
//    UploadTask uploadTask = profileImageRef.putBytes(data);
//
//    uploadTask.addOnSuccessListener(taskSnapshot -> {
//        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//            String imageUrl = uri.toString();
//            Log.d("Firebase", "Image uploaded successfully. URL: " + imageUrl);
//            updateProfileImageInDatabase(userUid, imageUrl);
//
//            // Hide loading dialog
//            progressDialog.dismiss();
//
//            // Show success Toast
//            Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
//
//            // Navigate to another activity and pass userUid
//            Intent intent = new Intent(this, Borrower_CompleteProfile.class);
//            intent.putExtra("userUid", userUid);  // Pass userUid as an extra
//            startActivity(intent);
//        }).addOnFailureListener(e -> {
//            Log.e("Firebase", "Failed to get download URL: " + e.getMessage());
//            progressDialog.dismiss();
//        });
//    }).addOnFailureListener(e -> {
//        Log.e("Firebase", "Image upload failed: " + e.getMessage());
//        progressDialog.dismiss();
//    });
//}
//
//    private void updateProfileImageInDatabase(String userUid, String imageUrl) {
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//
//        database.child("users").child(userUid).child("ProfileImage").setValue(imageUrl)
//                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Profile image successfully updated in database."))
//                .addOnFailureListener(e -> Log.e("Firebase", "Database write failed: " + e.getMessage()));
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cameraExecutor.shutdown();
//    }
//}
public class FaceScanner extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String userUid; // UID of the currently logged-in user
    private RelativeLayout initialLayout;  // Custom layout that shows the OK button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_face_scanner);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialLayout = findViewById(R.id.initialLayout);  // Custom layout with the "OK" button

        // Get the current user's UID (you can get it via FirebaseAuth)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userUid = user.getUid(); // Get user UID
        }

        // Set up the "OK" button click listener
        TextView okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            // Hide the initial layout and show the camera
            initialLayout.setVisibility(View.GONE);
            openFrontCamera();  // Open the camera to take the selfie
        });
    }

    // Open the front camera to take a selfie
    private void openFrontCamera() {
        // Create an intent to launch the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if a camera app is available
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Set the front camera using extra information
            takePictureIntent.putExtra("android.intent.extra.CAMERA_FACING", 1);  // 1 for front camera

            // Start the camera intent without showing a preview
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the captured image as a bitmap
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Upload the image to Firebase directly without showing a preview
            uploadImageToFirebase(imageBitmap);
        }
    }

    // Upload the selfie to Firebase Storage
    private void uploadImageToFirebase(Bitmap imageBitmap) {
        // Convert the bitmap to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Create a reference to Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("selfies/" + userUid + "_" + UUID.randomUUID().toString());

        // Upload the image to Firebase Storage
        UploadTask uploadTask = storageReference.putBytes(imageData);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();  // The download URL

                // Update the user's profile image in Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userUid);
                Map<String, Object> updates = new HashMap<>();
                updates.put("ProfileImage", imageUrl);  // Set the ProfileImage URL in the database

                // Update the user data in the database
                databaseReference.updateChildren(updates)
                        .addOnSuccessListener(aVoid -> {
                            // Handle success
                            Toast.makeText(FaceScanner.this, "Selfie uploaded successfully", Toast.LENGTH_SHORT).show();

                            // After upload, move to another activity and pass the UID
                            Intent intent = new Intent(FaceScanner.this, Borrower_CompleteProfile.class);
                            intent.putExtra("userUid", userUid);  // Pass the user UID
                            startActivity(intent);
                            finish();  // Close the current activity
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                            Toast.makeText(FaceScanner.this, "Failed to upload selfie", Toast.LENGTH_SHORT).show();
                        });
            });
        }).addOnFailureListener(e -> {
            // Handle failure during upload
            Toast.makeText(FaceScanner.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }
}

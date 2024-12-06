package com.example.metal.Borrower.UploadDocuments_Feature;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.Borrower.Home_Feature.HomeBorrower;
import com.example.metal.FullScreen;
import com.example.metal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Borrower_UploadDocuments2 extends AppCompatActivity {
    private ImageView imageView3, imageView4, imageView5;
    String imageUri3,imageUri4,imageUri5;
    private static final int REQUEST_IMAGE_CAPTURE_3 = 3;
    private static final int REQUEST_IMAGE_CAPTURE_4 = 4;
    private static final int REQUEST_IMAGE_CAPTURE_5 = 5;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_PICK_3 = 13;
    private static final int REQUEST_IMAGE_PICK_4 = 14;
    private static final int REQUEST_IMAGE_PICK_5 = 15;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageRef;
    String userUid;
    ProgressDialog progressDialog;
    int uploadsInProgress = 0;
    int totalUploads = 3;
    Handler handler;
    String[] ellipsisArray = {".", "..", "...", "...."};
    int ellipsisIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrower_upload_documents2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userUid = currentUser.getUid();
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users").child(userUid);

        // Initialize Handler
        handler = new Handler();

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        TextView cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the URI from the ImageView tag
                imageUri3 = (String) imageView3.getTag();

                // Check if the image URI is null or empty
                if (imageUri3 != null && !imageUri3.isEmpty()) {
                    Intent intent = new Intent(Borrower_UploadDocuments2.this, FullScreen.class);
                    intent.putExtra("imageUri", imageUri3);
                    startActivity(intent);
                } else {
                    // If the image URI is null or empty, show a toast message
                    Toast.makeText(Borrower_UploadDocuments2.this, "No image loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the URI from the ImageView tag
                imageUri4 = (String) imageView4.getTag();

                // Check if the image URI is null or empty
                if (imageUri4 != null && !imageUri4.isEmpty()) {
                    Intent intent = new Intent(Borrower_UploadDocuments2.this, FullScreen.class);
                    intent.putExtra("imageUri", imageUri4);
                    startActivity(intent);
                } else {
                    // If the image URI is null or empty, show a toast message
                    Toast.makeText(Borrower_UploadDocuments2.this, "No image loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the URI from the ImageView tag
                imageUri5 = (String) imageView5.getTag();

                // Check if the image URI is null or empty
                if (imageUri5 != null && !imageUri5.isEmpty()) {
                    Intent intent = new Intent(Borrower_UploadDocuments2.this, FullScreen.class);
                    intent.putExtra("imageUri", imageUri5);
                    startActivity(intent);
                } else {
                    // If the image URI is null or empty, show a toast message
                    Toast.makeText(Borrower_UploadDocuments2.this, "No image loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });


        TextView captureButton3 = findViewById(R.id.button_camera3);
        TextView captureButton4 = findViewById(R.id.button_camera4);
        TextView captureButton5 = findViewById(R.id.button_camera5);

        TextView button_gallery3 = findViewById(R.id.button_gallery3);
        TextView button_gallery4 = findViewById(R.id.button_gallery4);
        TextView button_gallery5 = findViewById(R.id.button_gallery5);


        button_gallery3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery(REQUEST_IMAGE_PICK_3);
            }
        });button_gallery4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery(REQUEST_IMAGE_PICK_4);
            }
        });button_gallery5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery(REQUEST_IMAGE_PICK_5);
            }
        });

        captureButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_3);
                } else {
                    requestCameraPermission();
                }
            }
        });

        captureButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_4);
                } else {
                    requestCameraPermission();
                }
            }
        });

        captureButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_5);
                } else {
                    requestCameraPermission();
                }
            }
        });
        // Find Button
        TextView saveButton = findViewById(R.id.submit);

        // Set OnClickListener for the button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if each ImageView has a valid image
                if (!isValidImage(imageView3) || !isValidImage(imageView4) ||
                        !isValidImage(imageView5)) {
                    Toast.makeText(Borrower_UploadDocuments2.this, "Please ensure all Documents are filled", Toast.LENGTH_SHORT).show();
                } else {
                    // Show progress dialog
                    progressDialog = new ProgressDialog(Borrower_UploadDocuments2.this);
                    progressDialog.setMessage("Uploading 0/" + totalUploads);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Reset the counter for uploads in progress
                    uploadsInProgress = 0;

                    // Start updating the progress dialog message
                    startUpdatingProgressDialogMessage();

                    checkAndUploadImageView(imageView3, "Img Meralco Bill");
                    checkAndUploadImageView(imageView4, "Img Water Bill");
                    checkAndUploadImageView(imageView5, "Img Internet Bill");
                }
            }
        });

    }
    private boolean isValidImage(ImageView imageView) {
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            // Check if the bitmap is not null and has valid dimensions
            return bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0;
        }
        return false; // No drawable or drawable is null
    }

    private void checkAndUploadImageView(ImageView imageView, String imageViewName) {
        if (imageView != null && imageView.getDrawable() != null) {
            // Get image data from ImageView (assuming the drawable is a BitmapDrawable)
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            // Check if the bitmap is not null and has valid dimensions
            if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                // Convert the Bitmap to a byte array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] byteArray = baos.toByteArray();

                // Create a reference to the file to upload
                StorageReference imageRef = storageRef.child("Borrower Documents/" + userUid + "/" + imageViewName + ".png");

                // Upload the byte array to Firebase Storage
                UploadTask uploadTask = imageRef.putBytes(byteArray);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    // Save the download URL to Firebase Realtime Database
                                    usersRef.child(imageViewName).setValue(downloadUri.toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Successfully saved image URL to database
                                                        //Toast.makeText(Borrower_UploadDocuments.this, imageViewName + " URL saved successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Failed to save image URL to database
                                                        Toast.makeText(Borrower_UploadDocuments2.this, "Failed to save " + imageViewName + " URL", Toast.LENGTH_SHORT).show();
                                                    }
                                                    checkIfAllUploadsCompleted();
                                                }
                                            });
                                } else {
                                    // Failed to get download URL
                                    Toast.makeText(Borrower_UploadDocuments2.this, "Failed to get download URL for " + imageViewName, Toast.LENGTH_SHORT).show();
                                    checkIfAllUploadsCompleted();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(Borrower_UploadDocuments2.this, "Failed to upload " + imageViewName, Toast.LENGTH_SHORT).show();
                        checkIfAllUploadsCompleted();
                    }
                });
            } else {
                // Handle case where the bitmap is null or has invalid dimensions
                Toast.makeText(Borrower_UploadDocuments2.this, "No valid image found in " + imageViewName, Toast.LENGTH_SHORT).show();
                checkIfAllUploadsCompleted();
            }
        } else {
            // Handle case where imageView is null or drawable is null
            Toast.makeText(Borrower_UploadDocuments2.this, "ImageView is empty or does not contain a drawable.", Toast.LENGTH_SHORT).show();
            checkIfAllUploadsCompleted();
        }
    }

//    private void checkIfAllUploadsCompleted() {
//        uploadsInProgress++;
//        updateProgressDialogMessage();
//
//        if (uploadsInProgress == totalUploads && progressDialog != null && progressDialog.isShowing()) {
//            // Update verificationStatus to "verifying" and add a timestamp
//            DatabaseReference accountDocumentsRef = usersRef.child("accountDocumentsComplete");
//            accountDocumentsRef.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        // Now update the timestamp
//                        usersRef.child("timestamp").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(Borrower_UploadDocuments2.this, "All documents uploaded successfully! Verification in progress.", Toast.LENGTH_LONG).show();
//                                    // Navigate to another page (e.g., MainActivity2)
//                                    Intent intent = new Intent(Borrower_UploadDocuments2.this, HomeBorrower.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(Borrower_UploadDocuments2.this, "Failed to update timestamp", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    } else {
//                        Toast.makeText(Borrower_UploadDocuments2.this, "Failed to update verification status", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            progressDialog.dismiss();
//        }
//    }
private void checkIfAllUploadsCompleted() {
    uploadsInProgress++;  // Increment the number of uploads that have been completed
    updateProgressDialogMessage();  // Update the progress dialog message

    // Check if all uploads are complete (3 uploads in this case)
    if (uploadsInProgress == 3 && progressDialog != null && progressDialog.isShowing()) {
        // Update the verificationStatus to "verifying" and add a timestamp
        DatabaseReference accountDocumentsRef = usersRef.child("accountDocumentsComplete");
        accountDocumentsRef.setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Now update the timestamp
                    usersRef.child("timestamp").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Show success message
                                Toast.makeText(Borrower_UploadDocuments2.this, "All documents uploaded successfully! Verification in progress.", Toast.LENGTH_LONG).show();

                                // Navigate to another page (e.g., HomeBorrower)
                                Intent intent = new Intent(Borrower_UploadDocuments2.this, HomeBorrower.class);
                                startActivity(intent);
                                finish();  // Finish the current activity
                            } else {
                                // Handle failure to update timestamp
                                Toast.makeText(Borrower_UploadDocuments2.this, "Failed to update timestamp", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Handle failure to update verification status
                    Toast.makeText(Borrower_UploadDocuments2.this, "Failed to update verification status", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Dismiss the progress dialog
        progressDialog.dismiss();
    }
}

    private void updateProgressDialogMessage() {
        if (progressDialog != null && progressDialog.isShowing()) {
            String ellipsis = ellipsisArray[ellipsisIndex % ellipsisArray.length];
            progressDialog.setMessage("Uploading " + uploadsInProgress + "/" + totalUploads + "  " + ellipsis);
            ellipsisIndex++;
        }
    }

    private void startUpdatingProgressDialogMessage() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateProgressDialogMessage();
                if (progressDialog != null && progressDialog.isShowing()) {
                    handler.postDelayed(this, 500); // Update every 500 milliseconds
                }
            }
        }, 500);
    }
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }
    private void pickImageFromGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }
    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, requestCode);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            Uri imageUri = null;

            if (data.getData() != null) {
                // Handling gallery pick
                imageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Handling image capture
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageUri = saveImageToInternalStorage(imageBitmap);
            }

            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE_3:
                case REQUEST_IMAGE_PICK_3:
                    imageView3.setImageBitmap(imageBitmap);
                    imageView3.setTag(imageUri.toString());
                    break;
                case REQUEST_IMAGE_CAPTURE_4:
                case REQUEST_IMAGE_PICK_4:
                    imageView4.setImageBitmap(imageBitmap);
                    imageView4.setTag(imageUri.toString());
                    break;
                case REQUEST_IMAGE_CAPTURE_5:
                case REQUEST_IMAGE_PICK_5:
                    imageView5.setImageBitmap(imageBitmap);
                    imageView5.setTag(imageUri.toString());
                    break;
                default:
                    break;
            }
        }
    }
    private Uri saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String fileName = "captured_image_" + System.currentTimeMillis() + ".png";
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Uri.fromFile(mypath);
    }
}
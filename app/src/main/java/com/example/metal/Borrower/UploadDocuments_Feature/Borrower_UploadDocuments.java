package com.example.metal.Borrower.UploadDocuments_Feature;

//import android.Manifest;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.metal.Borrower.Home_Feature.HomeBorrower;
//import com.example.metal.FullScreen;
//import com.example.metal.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ServerValue;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class Borrower_UploadDocuments extends AppCompatActivity {
//    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
//    String imageUri1,imageUri2,imageUri3,imageUri4,imageUri5;
//    private static final int REQUEST_IMAGE_CAPTURE_1 = 1;
//    private static final int REQUEST_IMAGE_CAPTURE_2 = 2;
//    private static final int REQUEST_IMAGE_CAPTURE_3 = 3;
//    private static final int REQUEST_IMAGE_CAPTURE_4 = 4;
//    private static final int REQUEST_IMAGE_CAPTURE_5 = 5;
//    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
//    private static final int REQUEST_IMAGE_PICK_1 = 11;
//    private static final int REQUEST_IMAGE_PICK_2 = 12;
//    private static final int REQUEST_IMAGE_PICK_3 = 13;
//    private static final int REQUEST_IMAGE_PICK_4 = 14;
//    private static final int REQUEST_IMAGE_PICK_5 = 15;
//    FirebaseDatabase database;
//    DatabaseReference usersRef;
//    FirebaseAuth auth;
//    FirebaseStorage storage;
//    StorageReference storageRef;
//    String userUid;
//    ProgressDialog progressDialog;
//    int uploadsInProgress = 0;
//    int totalUploads = 5;
//    Handler handler;
//    String[] ellipsisArray = {".", "..", "...", "...."};
//    int ellipsisIndex = 0;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_borrower_upload_documents);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        // Initialize Firebase Auth
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            userUid = currentUser.getUid();
//        }
//
//        // Initialize Firebase Database
//        database = FirebaseDatabase.getInstance();
//        usersRef = database.getReference("users").child(userUid);
//
//        // Initialize Handler
//        handler = new Handler();
//
//        // Initialize Firebase Storage
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
//
//        imageView1 = findViewById(R.id.imageView1);
//        imageView2 = findViewById(R.id.imageView2);
//        imageView3 = findViewById(R.id.imageView3);
//        imageView4 = findViewById(R.id.imageView4);
//        imageView5 = findViewById(R.id.imageView5);
//        TextView cancel = findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        imageView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retrieve the URI from the ImageView tag
//                imageUri1 = (String) imageView1.getTag();
//
//                // Check if the image URI is null or empty
//                if (imageUri1 != null && !imageUri1.isEmpty()) {
//                    Intent intent = new Intent(Borrower_UploadDocuments.this, Borrower_CompleteProfile.class);
//                    intent.putExtra("imageUri", imageUri1);
//                    startActivity(intent);
//                } else {
//                    // If the image URI is null or empty, show a toast message
//                    Toast.makeText(Borrower_UploadDocuments.this, "No image loaded", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retrieve the URI from the ImageView tag
//                imageUri2 = (String) imageView2.getTag();
//
//                // Check if the image URI is null or empty
//                if (imageUri2 != null && !imageUri2.isEmpty()) {
//                    Intent intent = new Intent(Borrower_UploadDocuments.this, FullScreen.class);
//                    intent.putExtra("imageUri", imageUri2);
//                    startActivity(intent);
//                } else {
//                    // If the image URI is null or empty, show a toast message
//                    Toast.makeText(Borrower_UploadDocuments.this, "No image loaded", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        imageView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retrieve the URI from the ImageView tag
//                imageUri3 = (String) imageView3.getTag();
//
//                // Check if the image URI is null or empty
//                if (imageUri3 != null && !imageUri3.isEmpty()) {
//                    Intent intent = new Intent(Borrower_UploadDocuments.this, FullScreen.class);
//                    intent.putExtra("imageUri", imageUri3);
//                    startActivity(intent);
//                } else {
//                    // If the image URI is null or empty, show a toast message
//                    Toast.makeText(Borrower_UploadDocuments.this, "No image loaded", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        imageView4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retrieve the URI from the ImageView tag
//                imageUri4 = (String) imageView4.getTag();
//
//                // Check if the image URI is null or empty
//                if (imageUri4 != null && !imageUri4.isEmpty()) {
//                    Intent intent = new Intent(Borrower_UploadDocuments.this, FullScreen.class);
//                    intent.putExtra("imageUri", imageUri4);
//                    startActivity(intent);
//                } else {
//                    // If the image URI is null or empty, show a toast message
//                    Toast.makeText(Borrower_UploadDocuments.this, "No image loaded", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        imageView5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retrieve the URI from the ImageView tag
//                imageUri5 = (String) imageView5.getTag();
//
//                // Check if the image URI is null or empty
//                if (imageUri5 != null && !imageUri5.isEmpty()) {
//                    Intent intent = new Intent(Borrower_UploadDocuments.this, FullScreen.class);
//                    intent.putExtra("imageUri", imageUri5);
//                    startActivity(intent);
//                } else {
//                    // If the image URI is null or empty, show a toast message
//                    Toast.makeText(Borrower_UploadDocuments.this, "No image loaded", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        TextView captureButton1 = findViewById(R.id.button_camera1);
//        TextView captureButton2 = findViewById(R.id.button_camera2);
//        TextView captureButton3 = findViewById(R.id.button_camera3);
//        TextView captureButton4 = findViewById(R.id.button_camera4);
//        TextView captureButton5 = findViewById(R.id.button_camera5);
//
//        TextView button_gallery1 = findViewById(R.id.button_gallery1);
//        TextView button_gallery2 = findViewById(R.id.button_gallery2);
//        TextView button_gallery3 = findViewById(R.id.button_gallery3);
//        TextView button_gallery4 = findViewById(R.id.button_gallery4);
//        TextView button_gallery5 = findViewById(R.id.button_gallery5);
//
//        button_gallery1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImageFromGallery(REQUEST_IMAGE_PICK_1);
//            }
//        });
//        button_gallery2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImageFromGallery(REQUEST_IMAGE_PICK_2);
//            }
//        });button_gallery3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImageFromGallery(REQUEST_IMAGE_PICK_3);
//            }
//        });button_gallery4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImageFromGallery(REQUEST_IMAGE_PICK_4);
//            }
//        });button_gallery5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickImageFromGallery(REQUEST_IMAGE_PICK_5);
//            }
//        });
//
//        captureButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkCameraPermission()) {
//                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_1);
//                } else {
//                    requestCameraPermission();
//                }
//            }
//        });
//
//        captureButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkCameraPermission()) {
//                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_2);
//                } else {
//                    requestCameraPermission();
//                }
//            }
//        });
//
//        captureButton3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkCameraPermission()) {
//                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_3);
//                } else {
//                    requestCameraPermission();
//                }
//            }
//        });
//
//        captureButton4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkCameraPermission()) {
//                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_4);
//                } else {
//                    requestCameraPermission();
//                }
//            }
//        });
//
//        captureButton5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkCameraPermission()) {
//                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_5);
//                } else {
//                    requestCameraPermission();
//                }
//            }
//        });
//        // Find Button
//        TextView saveButton = findViewById(R.id.submit);
//
//        // Set OnClickListener for the button
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Check if each ImageView has a valid image
//                if (!isValidImage(imageView1) || !isValidImage(imageView2) ||
//                        !isValidImage(imageView3) || !isValidImage(imageView4) ||
//                        !isValidImage(imageView5)) {
//                    Toast.makeText(Borrower_UploadDocuments.this, "Please ensure all Documents are filled", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Show progress dialog
//                    progressDialog = new ProgressDialog(Borrower_UploadDocuments.this);
//                    progressDialog.setMessage("Uploading 0/" + totalUploads);
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//
//                    // Reset the counter for uploads in progress
//                    uploadsInProgress = 0;
//
//                    // Start updating the progress dialog message
//                    startUpdatingProgressDialogMessage();
//
//                    // Check and upload each ImageView
//                    checkAndUploadImageView(imageView1, "Img Valid ID");
//                    checkAndUploadImageView(imageView2, "Img Permit");
//                    checkAndUploadImageView(imageView3, "Img Meralco Bill");
//                    checkAndUploadImageView(imageView4, "Img Water Bill");
//                    checkAndUploadImageView(imageView5, "Img Internet Bill");
//                }
//            }
//        });
//
//    }
//    private boolean isValidImage(ImageView imageView) {
//        if (imageView.getDrawable() != null) {
//            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//            // Check if the bitmap is not null and has valid dimensions
//            return bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0;
//        }
//        return false; // No drawable or drawable is null
//    }
//
//    private void checkAndUploadImageView(ImageView imageView, String imageViewName) {
//        if (imageView != null && imageView.getDrawable() != null) {
//            // Get image data from ImageView (assuming the drawable is a BitmapDrawable)
//            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//
//            // Check if the bitmap is not null and has valid dimensions
//            if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
//                // Convert the Bitmap to a byte array
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte[] byteArray = baos.toByteArray();
//
//                // Create a reference to the file to upload
//                StorageReference imageRef = storageRef.child("Borrower Documents/" + userUid + "/" + imageViewName + ".png");
//
//                // Upload the byte array to Firebase Storage
//                UploadTask uploadTask = imageRef.putBytes(byteArray);
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get the download URL of the uploaded image
//                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Uri> task) {
//                                if (task.isSuccessful()) {
//                                    Uri downloadUri = task.getResult();
//                                    // Save the download URL to Firebase Realtime Database
//                                    usersRef.child(imageViewName).setValue(downloadUri.toString())
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        // Successfully saved image URL to database
//                                                        //Toast.makeText(Borrower_UploadDocuments.this, imageViewName + " URL saved successfully", Toast.LENGTH_SHORT).show();
//                                                    } else {
//                                                        // Failed to save image URL to database
//                                                        Toast.makeText(Borrower_UploadDocuments.this, "Failed to save " + imageViewName + " URL", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                    checkIfAllUploadsCompleted();
//                                                }
//                                            });
//                                } else {
//                                    // Failed to get download URL
//                                    Toast.makeText(Borrower_UploadDocuments.this, "Failed to get download URL for " + imageViewName, Toast.LENGTH_SHORT).show();
//                                    checkIfAllUploadsCompleted();
//                                }
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        Toast.makeText(Borrower_UploadDocuments.this, "Failed to upload " + imageViewName, Toast.LENGTH_SHORT).show();
//                        checkIfAllUploadsCompleted();
//                    }
//                });
//            } else {
//                // Handle case where the bitmap is null or has invalid dimensions
//                Toast.makeText(Borrower_UploadDocuments.this, "No valid image found in " + imageViewName, Toast.LENGTH_SHORT).show();
//                checkIfAllUploadsCompleted();
//            }
//        } else {
//            // Handle case where imageView is null or drawable is null
//            Toast.makeText(Borrower_UploadDocuments.this, "ImageView is empty or does not contain a drawable.", Toast.LENGTH_SHORT).show();
//            checkIfAllUploadsCompleted();
//        }
//    }
//
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
//                                    Toast.makeText(Borrower_UploadDocuments.this, "All documents uploaded successfully! Verification in progress.", Toast.LENGTH_LONG).show();
//                                    // Navigate to another page (e.g., MainActivity2)
//                                    Intent intent = new Intent(Borrower_UploadDocuments.this, HomeBorrower.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(Borrower_UploadDocuments.this, "Failed to update timestamp", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    } else {
//                        Toast.makeText(Borrower_UploadDocuments.this, "Failed to update verification status", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            progressDialog.dismiss();
//        }
//    }
//
//    private void updateProgressDialogMessage() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            String ellipsis = ellipsisArray[ellipsisIndex % ellipsisArray.length];
//            progressDialog.setMessage("Uploading " + uploadsInProgress + "/" + totalUploads + "  " + ellipsis);
//            ellipsisIndex++;
//        }
//    }
//
//    private void startUpdatingProgressDialogMessage() {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateProgressDialogMessage();
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    handler.postDelayed(this, 500); // Update every 500 milliseconds
//                }
//            }
//        }, 500);
//    }
//    private boolean checkCameraPermission() {
//        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestCameraPermission() {
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.CAMERA},
//                CAMERA_PERMISSION_REQUEST_CODE);
//    }
//    private void pickImageFromGallery(int requestCode) {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, requestCode);
//    }
//    private void dispatchTakePictureIntent(int requestCode) {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, requestCode);
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            Bitmap imageBitmap = null;
//            Uri imageUri = null;
//
//            if (data.getData() != null) {
//                // Handling gallery pick
//                imageUri = data.getData();
//                try {
//                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                // Handling image capture
//                Bundle extras = data.getExtras();
//                imageBitmap = (Bitmap) extras.get("data");
//                imageUri = saveImageToInternalStorage(imageBitmap);
//            }
//
//            switch (requestCode) {
//                case REQUEST_IMAGE_CAPTURE_1:
//                case REQUEST_IMAGE_PICK_1:
//                    imageView1.setImageBitmap(imageBitmap);
//                    imageView1.setTag(imageUri.toString());
//                    break;
//                case REQUEST_IMAGE_CAPTURE_2:
//                case REQUEST_IMAGE_PICK_2:
//                    imageView2.setImageBitmap(imageBitmap);
//                    imageView2.setTag(imageUri.toString());
//                    break;
//                case REQUEST_IMAGE_CAPTURE_3:
//                case REQUEST_IMAGE_PICK_3:
//                    imageView3.setImageBitmap(imageBitmap);
//                    imageView3.setTag(imageUri.toString());
//                    break;
//                case REQUEST_IMAGE_CAPTURE_4:
//                case REQUEST_IMAGE_PICK_4:
//                    imageView4.setImageBitmap(imageBitmap);
//                    imageView4.setTag(imageUri.toString());
//                    break;
//                case REQUEST_IMAGE_CAPTURE_5:
//                case REQUEST_IMAGE_PICK_5:
//                    imageView5.setImageBitmap(imageBitmap);
//                    imageView5.setTag(imageUri.toString());
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//    private Uri saveImageToInternalStorage(Bitmap bitmap) {
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
//        String fileName = "captured_image_" + System.currentTimeMillis() + ".png";
//        File mypath = new File(directory, fileName);
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return Uri.fromFile(mypath);
//    }
//}
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
import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//public class Borrower_UploadDocuments extends AppCompatActivity {
//    private TextView textViewValidId, textViewPermitId;
//    private ImageView imageViewValidId, imageViewPermitId;
//    private Button captureButtonValidId, captureButtonPermitId, selectButtonValidId, selectButtonPermitId;
//
//    private String currentUserUid;
//    private Uri photoUri;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_borrower_upload_documents);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//        // Initialize views
//        textViewValidId = findViewById(R.id.textViewValidId);
//        textViewPermitId = findViewById(R.id.textViewPermitId);
//        imageViewValidId = findViewById(R.id.imageViewValidId);
//        imageViewPermitId = findViewById(R.id.imageViewPermitId);
//        captureButtonValidId = findViewById(R.id.captureButtonValidId);
//        captureButtonPermitId = findViewById(R.id.captureButtonPermitId);
//        selectButtonValidId = findViewById(R.id.selectButtonValidId);
//        selectButtonPermitId = findViewById(R.id.selectButtonPermitId);
//
//        // Firebase user UID
//        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Initialize a URI for saving captured images
//        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
//
//        // Launchers for capturing and selecting images
//        ActivityResultLauncher<Uri> takePictureLauncherValidId = registerForActivityResult(
//                new ActivityResultContracts.TakePicture(),
//                success -> handleCapture(success, imageViewValidId, textViewValidId, "ValidId")
//        );
//
//        ActivityResultLauncher<Uri> takePictureLauncherPermitId = registerForActivityResult(
//                new ActivityResultContracts.TakePicture(),
//                success -> handleCapture(success, imageViewPermitId, textViewPermitId, "PermitId")
//        );
//
//        ActivityResultLauncher<String> selectImageLauncherValidId = registerForActivityResult(
//                new ActivityResultContracts.GetContent(),
//                uri -> handleSelection(uri, imageViewValidId, textViewValidId, "ValidId")
//        );
//
//        ActivityResultLauncher<String> selectImageLauncherPermitId = registerForActivityResult(
//                new ActivityResultContracts.GetContent(),
//                uri -> handleSelection(uri, imageViewPermitId, textViewPermitId, "PermitId")
//        );
//
//        // Set up button listeners
//        captureButtonValidId.setOnClickListener(v -> takePictureLauncherValidId.launch(photoUri));
//        selectButtonValidId.setOnClickListener(v -> selectImageLauncherValidId.launch("image/*"));
//        captureButtonPermitId.setOnClickListener(v -> takePictureLauncherPermitId.launch(photoUri));
//        selectButtonPermitId.setOnClickListener(v -> selectImageLauncherPermitId.launch("image/*"));
//    }
//
//    private void handleCapture(boolean success, ImageView imageView, TextView textView, String imageKey) {
//        if (success) {
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
//                imageView.setImageBitmap(bitmap);
//                processImage(bitmap, textView, imageKey);
//            } catch (IOException e) {
//                Toast.makeText(this, "Error loading captured image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void handleSelection(Uri uri, ImageView imageView, TextView textView, String imageKey) {
//        if (uri != null) {
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                imageView.setImageBitmap(bitmap);
//                processImage(bitmap, textView, imageKey);
//            } catch (IOException e) {
//                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void processImage(Bitmap bitmap, TextView textView, String imageKey) {
//        if (bitmap == null) {
//            Toast.makeText(this, "No image provided", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        InputImage image = InputImage.fromBitmap(bitmap, 0);
//        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
//
//        recognizer.process(image)
//                .addOnSuccessListener(visionText -> {
//                    extractIdNumber(visionText, textView);
//                    uploadImageToFirebase(bitmap, imageKey);
//                })
//                .addOnFailureListener(e -> Toast.makeText(this, "Text recognition failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
//    }
//
//    private void extractIdNumber(Text visionText, TextView textView) {
//        String detectedText = visionText.getText();
//        Pattern idPattern = Pattern.compile("\\d{8,}"); // Adjust regex as needed
//        Matcher matcher = idPattern.matcher(detectedText);
//
//        if (matcher.find()) {
//            String idNumber = matcher.group();
//            textView.setText("ID Number: " + idNumber);
//        } else {
//            textView.setText("No ID Number found.");
//        }
//    }
//
//    private void uploadImageToFirebase(Bitmap bitmap, String imageKey) {
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("user_images/" + currentUserUid + "/" + imageKey + ".jpg");
//
//        // Convert bitmap to byte array
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        // Upload image
//        UploadTask uploadTask = storageReference.putBytes(data);
//        uploadTask.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
//                    if (uriTask.isSuccessful()) {
//                        String downloadUrl = uriTask.getResult().toString();
//                        saveImageUrlToDatabase(imageKey, downloadUrl);
//                    }
//                });
//            } else {
//                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveImageUrlToDatabase(String imageKey, String downloadUrl) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserUid).child(imageKey);
//        databaseReference.setValue(downloadUrl).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "Image URL saved successfully", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Failed to save image URL", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
public class Borrower_UploadDocuments extends AppCompatActivity {
    private TextView textViewValidId, textViewPermitId;
    private ImageView imageViewValidId, imageViewPermitId;
    private TextView captureButtonValidId, captureButtonPermitId, selectButtonValidId, selectButtonPermitId, uploadButton;

    private String currentUserUid;
    private Uri photoUri;

    private boolean isValidIdScanned = false, isPermitIdScanned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrower_upload_documents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        textViewValidId = findViewById(R.id.textViewValidId);
        textViewPermitId = findViewById(R.id.textViewPermitId);
        imageViewValidId = findViewById(R.id.imageViewValidId);
        imageViewPermitId = findViewById(R.id.imageViewPermitId);
        captureButtonValidId = findViewById(R.id.captureButtonValidId);
        captureButtonPermitId = findViewById(R.id.captureButtonPermitId);
        selectButtonValidId = findViewById(R.id.selectButtonValidId);
        selectButtonPermitId = findViewById(R.id.selectButtonPermitId);
        uploadButton = findViewById(R.id.uploadButton);

        // Firebase user UID
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Initialize a URI for saving captured images
        photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());

        // Launchers for capturing and selecting images
        ActivityResultLauncher<Uri> takePictureLauncherValidId = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> handleCapture(success, imageViewValidId, textViewValidId, "ValidId")
        );

        ActivityResultLauncher<Uri> takePictureLauncherPermitId = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> handleCapture(success, imageViewPermitId, textViewPermitId, "PermitId")
        );

        ActivityResultLauncher<String> selectImageLauncherValidId = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> handleSelection(uri, imageViewValidId, textViewValidId, "ValidId")
        );

        ActivityResultLauncher<String> selectImageLauncherPermitId = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> handleSelection(uri, imageViewPermitId, textViewPermitId, "PermitId")
        );

        // Set up button listeners
        captureButtonValidId.setOnClickListener(v -> takePictureLauncherValidId.launch(photoUri));
        selectButtonValidId.setOnClickListener(v -> selectImageLauncherValidId.launch("image/*"));
        captureButtonPermitId.setOnClickListener(v -> takePictureLauncherPermitId.launch(photoUri));
        selectButtonPermitId.setOnClickListener(v -> selectImageLauncherPermitId.launch("image/*"));

        // Upload button listener
        uploadButton.setOnClickListener(v -> {
            if (!isValidIdScanned || !isPermitIdScanned) {
                Toast.makeText(this, "Please ensure both images are scanned with ID numbers.", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadBothImages();
        });
    }

    private void handleCapture(boolean success, ImageView imageView, TextView textView, String imageKey) {
        if (success) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                imageView.setImageBitmap(bitmap);
                processImage(bitmap, textView, imageKey);
            } catch (IOException e) {
                Toast.makeText(this, "Error loading captured image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSelection(Uri uri, ImageView imageView, TextView textView, String imageKey) {
        if (uri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                processImage(bitmap, textView, imageKey);
            } catch (IOException e) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processImage(Bitmap bitmap, TextView textView, String imageKey) {
        if (bitmap == null) {
            Toast.makeText(this, "No image provided", Toast.LENGTH_SHORT).show();
            return;
        }

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    extractIdNumber(visionText, textView, imageKey);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Text recognition failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void extractIdNumber(Text visionText, TextView textView, String imageKey) {
        String detectedText = visionText.getText();
        Pattern idPattern = Pattern.compile("\\d{8,}"); // Adjust regex as needed
        Matcher matcher = idPattern.matcher(detectedText);

        if (matcher.find()) {
            String idNumber = matcher.group();
            textView.setText("ID Number: " + idNumber);
            if ("ValidId".equals(imageKey)) {
                isValidIdScanned = true;
            } else if ("PermitId".equals(imageKey)) {
                isPermitIdScanned = true;
            }
        } else {
            textView.setText("No ID Number found.");
            if ("ValidId".equals(imageKey)) {
                isValidIdScanned = false;
            } else if ("PermitId".equals(imageKey)) {
                isPermitIdScanned = false;
            }
        }
    }

    private void uploadBothImages() {
        // Validate images
        BitmapDrawable validIdDrawable = (BitmapDrawable) imageViewValidId.getDrawable();
        BitmapDrawable permitIdDrawable = (BitmapDrawable) imageViewPermitId.getDrawable();

        if (validIdDrawable == null || permitIdDrawable == null) {
            Toast.makeText(this, "Please select both images before uploading.", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap validIdBitmap = validIdDrawable.getBitmap();
        Bitmap permitIdBitmap = permitIdDrawable.getBitmap();

        uploadImageToFirebase(validIdBitmap, "Img Permit");
        uploadImageToFirebase(permitIdBitmap, "Img Valid ID");
    }
//
//    private void uploadImageToFirebase(Bitmap bitmap, String imageKey) {
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference("user_images/" + currentUserUid + "/" + imageKey + ".jpg");
//
//        // Convert bitmap to byte array
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        // Upload image
//        UploadTask uploadTask = storageReference.putBytes(data);
//        uploadTask.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
//                    if (uriTask.isSuccessful()) {
//                        String downloadUrl = uriTask.getResult().toString();
//                        saveImageUrlToDatabase(imageKey, downloadUrl);
//                    }
//                });
//            } else {
//                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
private void uploadImageToFirebase(Bitmap bitmap, String imageKey) {
    // Show progress dialog
    ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Uploading image...");
    progressDialog.setCancelable(false);  // Prevent cancellation
    progressDialog.show();

    // Create a reference to Firebase Storage
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("user_images/" + currentUserUid + "/" + imageKey + ".jpg");

    // Convert bitmap to byte array
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] data = baos.toByteArray();

    // Upload the image to Firebase Storage
    UploadTask uploadTask = storageReference.putBytes(data);
    uploadTask.addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            // Get the download URL after the upload is successful
            storageReference.getDownloadUrl().addOnCompleteListener(uriTask -> {
                if (uriTask.isSuccessful()) {
                    String downloadUrl = uriTask.getResult().toString();
                    saveImageUrlToDatabase(imageKey, downloadUrl);

                    // Dismiss the progress dialog
                    progressDialog.dismiss();

                    // Navigate to another page after successful upload
                    Intent intent = new Intent(this, Borrower_UploadDocuments2.class); // Replace with your next activity
                    intent.putExtra("imageUrl", downloadUrl); // Pass the download URL to the next activity
                    startActivity(intent);
                }
            });
        } else {
            // If upload fails, dismiss the progress dialog and show a failure message
            progressDialog.dismiss();
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
        }
    });
}


    private void saveImageUrlToDatabase(String imageKey, String downloadUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserUid).child(imageKey);
        databaseReference.setValue(downloadUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Image URL saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save image URL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

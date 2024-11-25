package com.example.metal.Collector;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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

import com.example.metal.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CollectorDailyCollectionDetail extends AppCompatActivity implements OnMapReadyCallback
{
    private static final int CAMERA_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;
    private static final int CAMERA_PERMISSION_CODE = 101;
    GoogleMap mMap;
    FrameLayout map;
    TextView distanceTextView, addressTextView, nameTextView, phoneNumberTextView, needToPayTodayTextView, amountToBeCollected, formattedDateTextView, statusPaymentTextView;
    ImageView profile1, paymentProof;
    Intent intent;
    ImageButton back, backSecond;
    SeekBar slideButton;
    RelativeLayout secondPageLayout, firstPageLayout;
    String userId, id, proofPaymentBorrowerString, name, profile, address, phoneNumber, needToPayToday, amountPaidbyBorrower, markBorrower, formattedDate, paymentStatusBorrower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_collector_daily_collection_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        secondPageLayout = findViewById(R.id.secondPageLayout);
        firstPageLayout = findViewById(R.id.firstPageLayout);

        slideButton = findViewById(R.id.slide_seekbar);
        distanceTextView = findViewById(R.id.distanceTextView);
        addressTextView = findViewById(R.id.addressTextView);
        paymentProof = findViewById(R.id.paymentProof);
        nameTextView = findViewById(R.id.nameTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumber);
        profile1 = findViewById(R.id.profile1);
        back = findViewById(R.id.back);
        backSecond = findViewById(R.id.backSecond);
        needToPayTodayTextView = findViewById(R.id.needToPayTodayTextView);
        amountToBeCollected = findViewById(R.id.amountToBeCollected);
        formattedDateTextView = findViewById(R.id.formattedDateTextView);
        statusPaymentTextView = findViewById(R.id.statusPaymentTextView);
        // Set OnClickListener for back button
        back.setOnClickListener(v -> onBackPressed());

        backSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondPageLayout.setVisibility(View.GONE);
                firstPageLayout.setVisibility(View.VISIBLE);
            }
        });


        slideButton.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    // Check if permission is granted
                    if (ContextCompat.checkSelfPermission(CollectorDailyCollectionDetail.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        // Permission is granted, open the camera
                        openCamera();
                    } else {
                        // Request camera permission
                        ActivityCompat.requestPermissions(CollectorDailyCollectionDetail.this,
                                new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Handle touch start if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 100) {
                    seekBar.setProgress(0); // Reset to 0 if not at the end
                }
            }
        });
        // Retrieve the intent that started this activity
        intent = getIntent();

        //update borrowers payment
        TextView updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = getIntent().getStringExtra("loanId");
                userId = getIntent().getStringExtra("userUid");
                updateAmountInFirebase(id,userId);
            }
        });


        //status if bayad or not
        markBorrower = getIntent().getStringExtra("markBorrower");

        // Extract the data passed with the intent
        name = intent.getStringExtra("Name");
        profile = intent.getStringExtra("Profile");
        address = intent.getStringExtra("Address");
        phoneNumber = intent.getStringExtra("PhoneNumber");
        needToPayToday = intent.getStringExtra("needToPayToday");
        amountPaidbyBorrower = intent.getStringExtra("amountPaidbyBorrower");
        formattedDate = intent.getStringExtra("dateBorrowerValue");
        paymentStatusBorrower = intent.getStringExtra("paymentStatusBorrower");
        proofPaymentBorrowerString = intent.getStringExtra("proofPaymentBorrower");


        nameTextView.setText(name);
        addressTextView.setText(address);
        phoneNumberTextView.setText(phoneNumber);
        needToPayTodayTextView.setText(needToPayToday);
        amountToBeCollected.setText(needToPayToday);
        formattedDateTextView.setText(formattedDate);
        statusPaymentTextView.setText(paymentStatusBorrower);

        Picasso.get().load(profile).into(profile1);

        // Retrieve latitude and longitude from the intent
        if (getIntent().hasExtra("Latitude") && getIntent().hasExtra("Longitude")) {
            latitude = getIntent().getDoubleExtra("Latitude", 0.0); // Default value if not found
            longitude = getIntent().getDoubleExtra("Longitude", 0.0); // Default value if not found
        }
        // Set up the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Permissions already granted, set up the map
            setupMap();
        }





    }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera and setup the map
                openCamera();
                setupMap();
            } else {
                // Permission denied, check if the user denied without selecting "Don't ask again"
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    // Show rationale and ask for permission again
                    new AlertDialog.Builder(this)
                            .setTitle("Camera Permission Needed")
                            .setMessage("This app requires camera permission to take pictures. Please allow it.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Ask for permission again
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                } else {
                    // Permission denied and user selected "Don't ask again", show a message
                    Toast.makeText(this, "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Call the method to add markers and draw the route
        addMarkersAndDrawRoute();
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    //    private void addMarkersAndDrawRoute() {
//        // Add a marker from the intent location
//        LatLng intentLocation = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(intentLocation).title("Borrower Location"));
//
//        // Move camera to the intent location
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intentLocation, 15));
//
//        // Get the user's current location
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        fusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, location -> {
//                    if (location != null) {
//                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Current Location"));
//
//
//                        // Calculate and display the distance
//                        calculateAndDisplayDistance(intentLocation, userLocation);
//
//                        // Optionally, move the camera to the user's location as well
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
//                    }
//                });
//    }
    private void addMarkersAndDrawRoute() {
        // Add a marker from the intent location
        LatLng intentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(intentLocation).title("Borrower Location"));

        // Move camera to the intent location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(intentLocation, 15));

        // Get the user's current location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Current Location"));

                        // Calculate and display the distance
                        calculateAndDisplayDistance(intentLocation, userLocation);

                        // Optionally, move the camera to the user's location as well
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                        // Draw the route between user location and intent location
                        drawRoute(userLocation, intentLocation);
                    }
                });
    }

    private void drawRoute(LatLng userLocation, LatLng intentLocation) {
        String accessToken = "pk.eyJ1Ijoiam9obmRlbCIsImEiOiJjbTIzaHJmczUwNXphMmpvY3N4czVpbW1wIn0.y_bc0lgqDY5NAdM5VO-D3w";
        String url = "https://api.mapbox.com/directions/v5/mapbox/driving/"
                + userLocation.longitude + "," + userLocation.latitude + ";"
                + intentLocation.longitude + "," + intentLocation.latitude
                + "?geometries=geojson&access_token=" + accessToken;

        // Create a new thread to make the network request
        new Thread(() -> {
            try {
                // Send a GET request to the Mapbox Directions API
                URL directionUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) directionUrl.openConnection();
                urlConnection.setRequestMethod("GET");

                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response to get the coordinates
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray routes = jsonResponse.getJSONArray("routes");
                JSONObject route = routes.getJSONObject(0);
                JSONArray coordinates = route.getJSONObject("geometry").getJSONArray("coordinates");

                // Prepare the Polyline options
                PolylineOptions polylineOptions = new PolylineOptions().color(Color.BLUE).width(5);

                // Add the coordinates to the Polyline
                for (int i = 0; i < coordinates.length(); i++) {
                    JSONArray point = coordinates.getJSONArray(i);
                    LatLng latLng = new LatLng(point.getDouble(1), point.getDouble(0)); // Latitude, Longitude
                    polylineOptions.add(latLng);
                }

                // Add the Polyline to the map
                runOnUiThread(() -> mMap.addPolyline(polylineOptions));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void calculateAndDisplayDistance(LatLng start, LatLng end) {
        // Create Location objects for distance calculation
        Location startLocation = new Location("start");
        startLocation.setLatitude(start.latitude);
        startLocation.setLongitude(start.longitude);

        Location endLocation = new Location("end");
        endLocation.setLatitude(end.latitude);
        endLocation.setLongitude(end.longitude);

        // Calculate the distance in meters
        float distanceInMeters = startLocation.distanceTo(endLocation);
        // Convert distance to kilometers (optional)
        float distanceInKilometers = distanceInMeters / 1000;

        // Display the distance in the TextView
        distanceTextView.setText(String.format("%.2f km", distanceInKilometers));
    }

    //    public void updateAmountInFirebase(String id, String userId) {
//        // Find the EditText by its ID
//        EditText editTextAmount = findViewById(R.id.editTextAmount);
//        ImageView paymentProof = findViewById(R.id.paymentProof); // Assuming you have an ImageView for the payment proof
//
//        // Get the text from the EditText and convert it to a String
//        String amountText = editTextAmount.getText().toString();
//
//        // Initialize ProgressDialog
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Uploading payment...");
//        progressDialog.setCancelable(false); // Prevent the dialog from being dismissed by back key
//
//        // Check if the input is not empty and if the ImageView has a bitmap
//        if (!amountText.isEmpty() && paymentProof.getDrawable() != null) {
//            // Show the loading dialog
//            progressDialog.show();
//
//            // Parse the String to a double
//            double amount = Double.parseDouble(amountText);
//
//            // Reference to the specific node in repaymentDetails based on the id
//            DatabaseReference repaymentReference = FirebaseDatabase.getInstance().getReference("repaymentDetails").child(id);
//
//            // Reference to the user's node to get and update prevLoan
//            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("previousLoanBalance");
//
//            // Create a map to hold the updates (amountPaid, status, and other values)
//            Map<String, Object> updates = new HashMap<>();
//            updates.put(amountPaidbyBorrower, amount); // Update the amountPaid
//            updates.put(markBorrower, "yes"); // Update the status to "yes"
//
//            // Retrieve the user's prevLoan
//            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // Get the current prevLoan value
//                        double prevLoan = dataSnapshot.getValue(Double.class);
//
//                        // Subtract the amount from prevLoan
//                        double updatedPrevLoan = prevLoan - amount;
//
//                        // Update the prevLoan in the users node
//                        userReference.setValue(updatedPrevLoan);
//
//                        // Convert the image to a Bitmap
//                        paymentProof.setDrawingCacheEnabled(true);
//                        paymentProof.buildDrawingCache();
//                        Bitmap bitmap = paymentProof.getDrawingCache();
//
//                        // Save the image to Firebase Storage
//                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("paymentProofs/" + id + proofPaymentBorrowerString + ".jpg"); // Use a unique path for each image
//
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] data = baos.toByteArray();
//
//                        UploadTask uploadTask = storageReference.putBytes(data);
//                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                // Get the download URL
//                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        // Add the image URL to the updates map
//                                        updates.put(proofPaymentBorrowerString, uri.toString()); // Assuming you want to store it under "proofPayment"
//
//                                        // Update the repaymentDetails node with the new values
//                                        repaymentReference.updateChildren(updates)
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//                                                        // Successfully updated the repayment details and prevLoan
//                                                        Toast.makeText(getApplicationContext(), "Payment Uploaded Successfully", Toast.LENGTH_SHORT).show();
//
//                                                        // Dismiss the ProgressDialog
//                                                        progressDialog.dismiss();
//
//                                                        // Finish the activity
//                                                        finish();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        // Failed to update the repayment details
//                                                        Toast.makeText(getApplicationContext(), "Failed to update repayment details for ID: " + id + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                                        // Dismiss the ProgressDialog
//                                                        progressDialog.dismiss();
//                                                    }
//                                                });
//                                    }
//                                });
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Failed to upload the image
//                                Toast.makeText(getApplicationContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                // Dismiss the ProgressDialog
//                                progressDialog.dismiss();
//                            }
//                        });
//                    } else {
//                        // Handle case where prevLoan does not exist
//                        Toast.makeText(getApplicationContext(), "Previous loan information not found for user.", Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle possible errors
//                    Toast.makeText(getApplicationContext(), "Failed to retrieve user loan information.", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            });
//        } else {
//            // Handle empty input or missing image
//            if (amountText.isEmpty()) {
//                Toast.makeText(getApplicationContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
//            }
//            if (paymentProof.getDrawable() == null) {
//                Toast.makeText(getApplicationContext(), "Please upload an image proof", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    public void updateAmountInFirebase(String id, String userId) {
        // Find the EditText by its ID
        EditText editTextAmount = findViewById(R.id.editTextAmount);
        ImageView paymentProof = findViewById(R.id.paymentProof); // Assuming you have an ImageView for the payment proof

        // Get the text from the EditText and convert it to a String
        String amountText = editTextAmount.getText().toString();

        // Initialize ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading payment...");
        progressDialog.setCancelable(false); // Prevent the dialog from being dismissed by back key

        // Check if the input is not empty and if the ImageView has a bitmap
        if (!amountText.isEmpty() && paymentProof.getDrawable() != null) {
            // Parse the String to a double
            double amount = Double.parseDouble(amountText);

            // Check if the amount is greater than zero
            if (amount > 0) {
                // Show the loading dialog
                progressDialog.show();

                // Reference to the specific node in repaymentDetails based on the id
                DatabaseReference repaymentReference = FirebaseDatabase.getInstance().getReference("repaymentDetails").child(id);

                // Reference to the user's node to get and update prevLoan
                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("previousLoanBalance");

                // Create a map to hold the updates (amountPaid, status, and other values)
                Map<String, Object> updates = new HashMap<>();
                updates.put(amountPaidbyBorrower, amount); // Update the amountPaid
                updates.put(markBorrower, "yes"); // Update the status to "yes"
                savePayment(amount);
                // Retrieve the user's prevLoan
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Get the current prevLoan value
                            double prevLoan = dataSnapshot.getValue(Double.class);

                            // Subtract the amount from prevLoan
                            double updatedPrevLoan = prevLoan - amount;

                            // Update the prevLoan in the users node
                            userReference.setValue(updatedPrevLoan);

                            // Convert the image to a Bitmap
                            paymentProof.setDrawingCacheEnabled(true);
                            paymentProof.buildDrawingCache();
                            Bitmap bitmap = paymentProof.getDrawingCache();

                            // Save the image to Firebase Storage
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference("paymentProofs/" + id + proofPaymentBorrowerString + ".jpg"); // Use a unique path for each image

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = storageReference.putBytes(data);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get the download URL
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Add the image URL to the updates map
                                            updates.put(proofPaymentBorrowerString, uri.toString()); // Assuming you want to store it under "proofPayment"

                                            // Update the repaymentDetails node with the new values
                                            repaymentReference.updateChildren(updates)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Successfully updated the repayment details and prevLoan
                                                            Toast.makeText(getApplicationContext(), "Payment Uploaded Successfully", Toast.LENGTH_SHORT).show();

                                                            // Dismiss the ProgressDialog
                                                            progressDialog.dismiss();

                                                            // Finish the activity
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Failed to update the repayment details
                                                            Toast.makeText(getApplicationContext(), "Failed to update repayment details for ID: " + id + " - " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                                            // Dismiss the ProgressDialog
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to upload the image
                                    Toast.makeText(getApplicationContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    // Dismiss the ProgressDialog
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            // Handle case where prevLoan does not exist
                            Toast.makeText(getApplicationContext(), "Previous loan information not found for user.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors
                        Toast.makeText(getApplicationContext(), "Failed to retrieve user loan information.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            } else {
                // Handle invalid amount input (0 or negative)
                Toast.makeText(getApplicationContext(), "Please enter a positive amount greater than zero.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle empty input or missing image
            if (amountText.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            }
            if (paymentProof.getDrawable() == null) {
                Toast.makeText(getApplicationContext(), "Please upload an image proof", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void savePayment(double amount) {
        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("payments");

        // Generate a timestamp as ID
        String timestampId = String.valueOf(new Date().getTime());

        // Prepare data to save
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("amount", amount);
        paymentData.put("timestamp", timestampId);

        // Save the data to Firebase under the 'payments' node
        databaseRef.child(timestampId).setValue(paymentData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Handle successful save (e.g., show success message)
                Toast.makeText(this, "Payment saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                // Handle failed save (e.g., show error message)
                Toast.makeText(this, "Failed to save payment.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            paymentProof.setImageBitmap(imageBitmap);

            secondPageLayout.setVisibility(View.VISIBLE);
            firstPageLayout.setVisibility(View.GONE);
            slideButton.setProgress(0);
        }
    }
}
package com.example.metal.Borrower.UploadDocuments_Feature;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.metal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Borrower_CompleteProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 100;
    private ImageView imageView;
    private Uri imageUri;
    Spinner provinceSpinner, citySpinner, spinnerLineOfWork, spinnerMaritalStatus;
    private String userUid;
    double totalBillMonthly, totalIncomeMonthly;
    EditText editName,editAge,editBrgy,editStreet,editWork,editMonthlyIncome,editMeralcoBill,editWaterBill,editInternetBill,editPadala,editHouseBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrower_complete_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //1st page
        // Get the user UID from the Intent
        Intent intent = getIntent();
        userUid = getIntent().getStringExtra("userUid");

        if (userUid == null) {
            // Handle the case where userUid is null
            Toast.makeText(this, "Error: User UID is not available", Toast.LENGTH_SHORT).show();
            // Optionally, you can finish the activity or disable functionalities
            finish();
            return;
        }

        //ui
        citySpinner = findViewById(R.id.editTextCity);
        provinceSpinner = findViewById(R.id.editTextProvince);
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editBrgy = findViewById(R.id.editBrgy);
        editStreet = findViewById(R.id.editStreet);
        editWork = findViewById(R.id.editWork);
        editMonthlyIncome = findViewById(R.id.editMonthlyIncome);
        editMeralcoBill = findViewById(R.id.editMeralcoBill);
        editWaterBill = findViewById(R.id.editWaterBill);
        editInternetBill = findViewById(R.id.editInternetBill);
        editPadala = findViewById(R.id.editPadala);
        editHouseBill= findViewById(R.id.editHouseBill);

        //populate the spinner
        // Create array adapters for category and subcategory spinners
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,
                R.array.province_array, android.R.layout.simple_spinner_item);

        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(provinceAdapter);

        // Set listener for category spinner to update subcategory spinner
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected category
                String selectedCategory = parent.getItemAtPosition(position).toString();

                // Determine subcategories based on selected category
                ArrayAdapter<CharSequence> cityAdapter;
                if (selectedCategory.equals("Abra")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Abra_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Agusan del Norte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Agusan_del_Norte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Agusan del Sur")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Agusan_del_Sur_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Aklan")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Aklan_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Albay")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Albay_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Antique")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Antique_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Apayao")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Apayao_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Aurora")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Aurora_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Basilan")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Basilan_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Bataan")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Bataan_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Batanes")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Batanes_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Batangas")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Batangas_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Benguet")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Benguet_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Biliran")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.Biliran_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Bohol")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.bohol_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Bulacan")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.bulacan_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Cagayan")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.cagayan_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Camarines Norte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.camarines_norte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Camarines Sur")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.camarines_sur_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Capiz")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.capiz_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Catanduanes")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.catanduanes_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Cavite")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.cavite_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Cebu")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.cebu_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Compostela Valley")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.compostela_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Cotabato")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.cotabato_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Davao del Norte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.davao_del_norte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Davao del Sur")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.davao_del_sur_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Davao Occidental")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.davao_occidental_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Davao Oriental")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.davao_occidental_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Eastern Samar")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.eastern_samar_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Guimaras")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.guimaras_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Ifugao")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.ifugao_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Ilocos Norte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.ilocos_norte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Ilocos Sur")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.ilocos_sur_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Iloilo")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.iloilo_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Isabela")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.isabela_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Kalinga")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.kalinga_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("La Union")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.la_union_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Laguna")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.laguna_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Lanao del Norte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.lanao_del_norte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Lanao del Sur")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.lanao_del_sur_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Leyte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.leyte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Maguindanao")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.maguindanao_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Marinduque")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.marinduque_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Masbate")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.masbate_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Misamis Occidental")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.misamis_occidental_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Misamis Oriental")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.misamis_oriental_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Mountain Province")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.mountain_province_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Negros Occidental")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.negros_occidental_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Negros Oriental")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.negros_oriental_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Nueva Ecija")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.nueva_ecija_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Nueva Vizcaya")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.nueva_vizcaya_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Occidental Mindoro")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.occidental_mindoro_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Oriental Mindoro")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.oriental_mindoro_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Palawan")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.palawan_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Pampanga")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.pampanga_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Pangasinan")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.pangasinan_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Quezon")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.quezon_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Quirino")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.quirino_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Rizal")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.rizal_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Romblon")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.romblon_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Samar")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.samar_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Sarangani")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.sarangani_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Siargao")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.siargao_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Siquijor")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.siquijor_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Sorsogon")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.sorsogon_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("South Cotabato")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.south_cotabato_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Southern Leyte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.southern_leyte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Sultan Kudarat")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.sultan_kudarat_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Sulu")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.sulu_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Surigao del Norte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.surigao_del_norte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Surigao del Sur")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.surigao_del_sur_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Tarlac")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.tarlac_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Tawi-Tawi")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.tawi_tawi_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Zambales")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.zambales_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Zamboanga del Norte")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.zamboanga_del_norte_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Zamboanga del Sur")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.zamboanga_del_sur_cities, android.R.layout.simple_spinner_item);
                } else if (selectedCategory.equals("Zamboanga Sibugay")) {
                    cityAdapter = ArrayAdapter.createFromResource(Borrower_CompleteProfile.this,
                            R.array.zamboanga_sibugay_cities, android.R.layout.simple_spinner_item);
                }
                else {
                    cityAdapter = new ArrayAdapter<>(Borrower_CompleteProfile.this,
                            android.R.layout.simple_spinner_item);
                }
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        //line of work
        spinnerLineOfWork = findViewById(R.id.spinnerLineOfWork);
        ArrayAdapter<CharSequence> adapterLineOfWork = ArrayAdapter.createFromResource(this,
                R.array.line_of_work_array, android.R.layout.simple_spinner_item);
        adapterLineOfWork.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLineOfWork.setAdapter(adapterLineOfWork);
        //matiral status
        spinnerMaritalStatus = findViewById(R.id.spinnerMaritalStatus);
        ArrayAdapter<CharSequence> adapterMaritalStatus = ArrayAdapter.createFromResource(this,
                R.array.marital_status_array, android.R.layout.simple_spinner_item);
        adapterMaritalStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaritalStatus.setAdapter(adapterMaritalStatus);


        // Set up the button
        TextView submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });
        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebase(final ImageUploadCallback callback) {
        if (imageUri != null) {
            // Firebase Storage reference
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference fileReference = storageReference.child("Profile Images/" + System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    callback.onUploadSuccess(imageUrl);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Borrower_CompleteProfile.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    callback.onUploadFailure(e);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Borrower_CompleteProfile.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            callback.onUploadFailure(e);
                        }
                    });
        } else {
            callback.onUploadFailure(new Exception("Image URI is null"));
        }
        //end of 1st page

    }
    private void saveDetails() {
        if (userUid == null) {
            // Handle the case where userUid is still null
            Toast.makeText(this, "Error: User UID is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected items from the spinners
        String selectedProvince = provinceSpinner.getSelectedItem().toString();
        String selectedCity = citySpinner.getSelectedItem().toString();
        String selectedLineOfWork = spinnerLineOfWork.getSelectedItem().toString();
        String selectedMaritalStatus = spinnerMaritalStatus.getSelectedItem().toString();

        // Get the values from the EditText fields
        String name = editName.getText().toString().trim();
        String age = editAge.getText().toString().trim();
        String brgy = editBrgy.getText().toString().trim();
        String street = editStreet.getText().toString().trim();
        String work = editWork.getText().toString().trim();
        String monthlyIncome = editMonthlyIncome.getText().toString().trim();
        String meralcoBill = editMeralcoBill.getText().toString().trim();
        String waterBill = editWaterBill.getText().toString().trim();
        String internetBill = editInternetBill.getText().toString().trim();
        String houseBill = editHouseBill.getText().toString().trim();
        String padala = editPadala.getText().toString().trim();

        // Compute monthly bills and income
        double meralcoBillDbl = 0.0, waterBillDbl = 0.0, internetBillDbl = 0.0, houseBillDbl = 0.0, monthlyIncomeDbl = 0.0, monthlyPadalaDbl = 0.0;
        try {
            meralcoBillDbl = Double.parseDouble(meralcoBill);
            waterBillDbl = Double.parseDouble(waterBill);
            internetBillDbl = Double.parseDouble(internetBill);
            houseBillDbl = Double.parseDouble(houseBill);
            monthlyIncomeDbl = Double.parseDouble(monthlyIncome);
            monthlyPadalaDbl = Double.parseDouble(padala);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please enter valid numbers for bills", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate total bill and income
        double totalBillMonthly = meralcoBillDbl + waterBillDbl + internetBillDbl + houseBillDbl;
        double totalIncomeMonthly = monthlyIncomeDbl + monthlyPadalaDbl;

        // Check if any field is empty
        if (name.isEmpty() || age.isEmpty() || brgy.isEmpty() || street.isEmpty() || work.isEmpty() || monthlyIncome.isEmpty() || meralcoBill.isEmpty() || waterBill.isEmpty() || internetBill.isEmpty() || houseBill.isEmpty() || padala.isEmpty() || selectedProvince.isEmpty() || selectedCity.isEmpty() || selectedLineOfWork.isEmpty() || selectedMaritalStatus.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {

            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(this);
            View customView = inflater.inflate(R.layout.dialog_confirm_details, null);


            TextView tvname = customView.findViewById(R.id.name);
            TextView tvage = customView.findViewById(R.id.age);
            TextView tvaddress = customView.findViewById(R.id.address);
            TextView tvworkk = customView.findViewById(R.id.workk);
            TextView tvmaritalStatus = customView.findViewById(R.id.maritalStatus);
            TextView tvwork = customView.findViewById(R.id.work);
            TextView tvmonthly = customView.findViewById(R.id.monthly);
            TextView tvmeralco = customView.findViewById(R.id.meralco);
            TextView tvwater = customView.findViewById(R.id.water);
            TextView tvinternet = customView.findViewById(R.id.internet);
            TextView tvhouse = customView.findViewById(R.id.house);
            TextView tvpadala = customView.findViewById(R.id.padala);
            TextView tvtotal = customView.findViewById(R.id.total);
            TextView tvtotal2 = customView.findViewById(R.id.total2);

            tvname.setText(name);
            tvage.setText(age);
            tvaddress.setText(" " + brgy + " " + selectedCity + " " + selectedProvince);
            tvworkk.setText(selectedLineOfWork);
            tvmaritalStatus.setText(selectedMaritalStatus);
            tvwork.setText(work);
            tvmonthly.setText(" " + monthlyIncome);
            tvmeralco.setText(" " + meralcoBill);
            tvwater.setText(" " + waterBill);
            tvinternet.setText(" " + internetBill);
            tvhouse.setText(" " + houseBill);
            tvpadala.setText(" " + padala);
            tvtotal.setText(" " + totalBillMonthly);
            tvtotal2.setText(" " + totalIncomeMonthly);


// Create the AlertDialog using custom layout
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(customView)  // Set the custom layout
                    .create();

// Handle the Cancel button click
            TextView btnCancel = customView.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();  // Close the dialog
                }
            });

// Handle the Confirm button click
            TextView btnConfirm = customView.findViewById(R.id.btnConfirm);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show loading indicator
                    ProgressDialog progressDialog = new ProgressDialog(Borrower_CompleteProfile.this);
                    progressDialog.setMessage("Saving details, please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // Handle data saving and uploading to Firebase
                    if (imageUri != null) {
                        uploadImageToFirebase(new ImageUploadCallback() {
                            @Override
                            public void onUploadSuccess(String imageUrl) {
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                                // Save user data to Firebase
                                database.child("users").child(userUid).child("Province").setValue(selectedProvince);
                                database.child("users").child(userUid).child("City").setValue(selectedCity);
                                database.child("users").child(userUid).child("lineOfWork").setValue(selectedLineOfWork);
                                database.child("users").child(userUid).child("MaritalStatus").setValue(selectedMaritalStatus);
                                database.child("users").child(userUid).child("ProfileImage").setValue(imageUrl);
                                database.child("users").child(userUid).child("Name").setValue(name);
                                database.child("users").child(userUid).child("Age").setValue(age);
                                database.child("users").child(userUid).child("Brgy").setValue(brgy);
                                database.child("users").child(userUid).child("Street").setValue(street);
                                database.child("users").child(userUid).child("Work").setValue(work);
                                database.child("users").child(userUid).child("MonthlyIncome").setValue(monthlyIncome);
                                database.child("users").child(userUid).child("MeralcoBill").setValue(meralcoBill);
                                database.child("users").child(userUid).child("WaterBill").setValue(waterBill);
                                database.child("users").child(userUid).child("HouseBill").setValue(houseBill);
                                database.child("users").child(userUid).child("InternetBill").setValue(internetBill);
                                database.child("users").child(userUid).child("Padala").setValue(padala);
                                database.child("users").child(userUid).child("accountStatus").setValue("pending");
                                database.child("users").child(userUid).child("totalBillMonthly").setValue(totalBillMonthly);
                                database.child("users").child(userUid).child("totalIncomeMonthly").setValue(totalIncomeMonthly);

                                // Add the timestamp when saving the data
                                database.child("users").child(userUid).child("timestamp").setValue(ServerValue.TIMESTAMP);

                                // Update account details completion
                                database.child("users").child(userUid).child("accountDetailsComplete").setValue("yes");

                                // Dismiss the loading indicator
                                progressDialog.dismiss();

                                // Show a confirmation message
                                Toast.makeText(getApplicationContext(), "Personal Details Updated Successfully!", Toast.LENGTH_SHORT).show();

                                // Navigate to another activity
                                Intent intent = new Intent(Borrower_CompleteProfile.this, Borrower_UploadDocuments.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onUploadFailure(Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(Borrower_CompleteProfile.this, "Failed to upload image: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Please select an image to upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            dialog.show();

        }
    }

    interface ImageUploadCallback {
        void onUploadSuccess(String imageUrl);
        void onUploadFailure(Exception exception);
    }
}
package com.example.ecocity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Change extends AppCompatActivity {

    String[] location_items ={"Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan", "Melaka", "Negeri Sembilan", "Pahang", "Perak", "Pulau Pinang", "Putrajaya", "Sabah", "Sarawak"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    String[] type_items ={"Brand new", "Like new", "Lightly used"};
    AutoCompleteTextView autoCompleteTextView_brand;
    ArrayAdapter<String> adapterItems_brand;


    // Declare Firebase Storage and Database references
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICK_IMAGE_REQUEST = 123;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    // Declare ImageView at the class level
    private ImageView imageView;

    // Use ActivityResultLauncher for image picking
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri imageUri;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        username = getIntent().getStringExtra("username");


        imageView = findViewById(R.id.IVImage);

        // Initialize Firebase Storage and Database references
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference("resources_images/");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker
                        .with(Change.this)
                        .crop() // Crop image (optional)
                        .compress(1024) // Compression (optional)
                        .maxResultSize(1080, 1080) // Max result size (optional)
                        .start();
            }
        });

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_location, location_items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        autoCompleteTextView_brand = findViewById(R.id.auto_complete_txt_condition);
        adapterItems_brand = new ArrayAdapter<String>(this, R.layout.list_type, type_items);
        autoCompleteTextView_brand.setAdapter(adapterItems_brand);
        autoCompleteTextView_brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        Button btnSubmit = findViewById(R.id.BSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to handle the submission
                handleSubmitButtonClick();
            }
        });

        // Handle back icon click
        ImageView backIcon = findViewById(R.id.IVArrowBack);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            uploadImageToFirebase();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    String downloadUri;

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            String timestamp = String.valueOf(System.currentTimeMillis());
            StorageReference fileReference = mStorageReference.child(username + "_" + timestamp + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Pass the URI to handle UI updates
                                    handleUploadSuccess(uri);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Change.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    String imageKey;
    // Method to handle UI updates after successful upload
    private void handleUploadSuccess(Uri uri) {
        // Use downloadUrl as needed
        downloadUri = uri.toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Resources").child(username);
        imageKey = databaseReference.push().getKey();

        Map<String, Object> imageData = new HashMap<>();
        imageData.put("resourceUrl", downloadUri);

        databaseReference.child(imageKey).updateChildren(imageData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Change.this, "Upload successful", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Change.this, "Failed to update data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Assuming imageView is a member variable of the Change class
        Glide.with(Change.this).load(downloadUri).into(imageView);
    }


    private void handleSubmitButtonClick() {
        // Get the values from the AutoCompleteTextViews
        String selectedCondition = autoCompleteTextView_brand.getText().toString();
        String selectedLocation = autoCompleteTextView.getText().toString();

        // Get the values from the TextInputEditTexts
        TextInputEditText resourceTitleEditText = findViewById(R.id.TIETResourceTitle);
        String resourceTitle = resourceTitleEditText.getText().toString();

        // Get the values from the TextInputEditText for notes on location
        TextInputEditText notesOnLocationEditText = findViewById(R.id.notes_on_location_edit_text);
        String notesOnLocation = notesOnLocationEditText.getText().toString();

        // Get the values from the TextInputEditText for notes on condition
        TextInputEditText notesOnConditionEditText = findViewById(R.id.notes_on_condition_edit_text);
        String notesOnCondition = notesOnConditionEditText.getText().toString();

        // Get the values from the TextInputEditText for description on resource
        TextInputEditText descriptionOnResourceEditText = findViewById(R.id.description_on_resource_edit_text);
        String descriptionOnResource = descriptionOnResourceEditText.getText().toString();

        // Perform validation
        if (selectedCondition.isEmpty() || selectedLocation.isEmpty() || resourceTitle.isEmpty()) {
            // If any of the fields are empty, show an error message
            Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_SHORT).show();
        } else {
            // Save the details to Firebase
            saveDetailsToFirebase(downloadUri.toString(),selectedCondition, selectedLocation, resourceTitle,
                    notesOnLocation, notesOnCondition, descriptionOnResource);
        }
    }

    private void saveDetailsToFirebase(String downloadUri, String selectedCondition, String selectedLocation, String resourceTitle,
                                       String notesOnLocation, String notesOnCondition, String descriptionOnResource) {
        // Create an instance of your model class and set the values
        ResourceDetails resourceDetails = new ResourceDetails(downloadUri.toString(),selectedCondition, selectedLocation,
                resourceTitle, notesOnLocation, notesOnCondition, descriptionOnResource);

        // Get the Firebase Database reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Resources").child(username); // Change this to your actual Firebase node


        // Push the data to Firebase
        //String key = mDatabaseReference.push().getKey();
        mDatabaseReference.child(imageKey).setValue(resourceDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data saved successfully
                        Toast.makeText(Change.this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                        // Navigate back to the Daily_Discover activity
//                        Intent intent = new Intent(Change.this, ResourceMainPage.class);
//                        TextView tvAlison = findViewById(R.id.TVAlison);
//                        tvAlison.setText(username);
//
//
//                        startActivity(intent);

                        // Finish the current activity to remove it from the back stack
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Toast.makeText(Change.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

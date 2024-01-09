package com.example.ecocity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadCV extends AppCompatActivity {

    Button btnUploadCV;
    Button btnSubmit;
    ImageButton btnBack;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    // Value (of USERNAME) to put in Intent
    private String username;
    private String activityKey;
    private String organizerName;

    private String fileName; // self-custom filename


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_cv);


        // Get username using Intent
        username = getIntent().getStringExtra("USERNAME");
        activityKey = getIntent().getStringExtra("ACTIVITY_KEY");
        organizerName = getIntent().getStringExtra("ORGANIZER_NAME");


        // Select and Upload CV
        storageReference = FirebaseStorage.getInstance().getReference("cv_file/");
        databaseReference = FirebaseDatabase.getInstance().getReference("Application");
        btnUploadCV = findViewById(R.id.BtnUploadCV);
        btnSubmit = findViewById(R.id.BtnSubmitCV);
        btnBack = findViewById(R.id.BtnBackUploadCV);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UploadCV.this, VolunteerPostUser.class);
//                intent.putExtra("ACTIVITY_KEY", activityKey);
//                intent.putExtra("USERNAME", username); //Pass username & activityKey back to Volunteer Post page
//                intent.putExtra("ORGANIZER_NAME",organizerName);
//                startActivity(intent);
                finish();
            }
        });
        btnUploadCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("application/pdf"));
            }
        });

        // if haven't uploaded CV and click submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadCV.this,"Please select a PDF File",Toast.LENGTH_LONG).show();
            }
        });

    }

    // Select PDF
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {

                // Generate a unique name using the current timestamp
                fileName = "upload_CV_" + System.currentTimeMillis() + ".pdf";

                btnUploadCV.setText(fileName);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Upload PDF
                        uploadPDFFileFirebase(data.getData());
                    }
                });
            }
        }
    });


    // Upload PDF
    private void uploadPDFFileFirebase(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        StorageReference reference = storageReference.child(fileName);

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        String pdfName = fileName;
                        String status = "Pending";

                        // Put value into UploadCVHelper
                        UploadCVHelper UploadCVHelper = new UploadCVHelper(activityKey, status, pdfName, uri.toString());
                        databaseReference.child(username).child(databaseReference.push().getKey()).setValue(UploadCVHelper);
                        Toast.makeText(UploadCV.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        addPointsToUser(username, activityKey);

//                        Intent intent = new Intent(UploadCV.this, VolunteerPostUser.class);
//                        intent.putExtra("ACTIVITY_KEY", activityKey);
//                        intent.putExtra("USERNAME", username); //Pass username & activityKey back to Volunteer Post page
//                        intent.putExtra("ORGANIZER_NAME",organizerName);
//                        intent.putExtra("finishUpload","true");
//                        startActivity(intent);

//                        finish();
//                        startActivity(getIntent());

                        finish();
                        Toast.makeText(UploadCV.this, "Applied successfully", Toast.LENGTH_LONG).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("File Uploading: " + (int) progress + "%");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadCV.this, "Unable to Upload File", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

//                        Intent intent = new Intent(UploadCV.this, VolunteerPostUser.class);
//                        intent.putExtra("ACTIVITY_KEY", activityKey);
//                        intent.putExtra("USERNAME", username);
//                        intent.putExtra("ORGANIZER_NAME",organizerName);
//                        startActivity(intent);
                        finish();
                    }
                });
    }

        private void addPointsToUser(String username, String activityKey) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(username);
            DatabaseReference activityReference = FirebaseDatabase.getInstance().getReference("Activities").child(organizerName).child(activityKey);

            // Retrieve points to add from the activity in the database
            activityReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String pointsToAddString = dataSnapshot.child("points").getValue(String.class);
                        int pointsToAdd = Integer.parseInt(pointsToAddString);

                        // Update user points in the database
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                if (userSnapshot.exists()) {
                                    int currentPoints = userSnapshot.child("point").getValue(Integer.class);
                                    int newPoints = currentPoints + pointsToAdd;

                                    // Update user points in the database
                                    userReference.child("point").setValue(newPoints).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Points added successfully
                                                Toast.makeText(UploadCV.this, "Points added to user", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Handle error
                                                Toast.makeText(UploadCV.this, "Failed to add points", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
}
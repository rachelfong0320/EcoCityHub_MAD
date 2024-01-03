package com.example.madassignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class OrganizerProfile extends AppCompatActivity {
    TextView TVUsername, TVBio;
    AppCompatButton BTEditOrgProfile;
    MaterialButton BTProfileBack;
    ImageView IVEditProfile;
    ShapeableImageView IVProfile;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mValueEventListener;
    private boolean shouldCheckData = true;
    private static final int PICK_IMAGE_REQUEST = 123;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_profile);
        TVUsername = findViewById(R.id.TVUsername);
        TVBio = findViewById(R.id.TVBio);
        BTEditOrgProfile = findViewById(R.id.BTEditOrgProfile);
        BTProfileBack = findViewById(R.id.BTProfileBack);
        IVEditProfile = findViewById(R.id.IVEditProfile);
        IVProfile = findViewById(R.id.IVProfile);

        String username = getIntent().getStringExtra("username");

        if (username != null){
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference().child("Organizer").child(username);
            attachDatabaseReadListener();
        }else{
            Toast.makeText(OrganizerProfile.this, "Invalid activity key. Unable to load profile.", Toast.LENGTH_SHORT).show();
            shouldCheckData = false;
        }

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();

        IVEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(OrganizerProfile.this)
                        .crop() // Crop image(Optional), Check Customization for more option
                        .compress(1024) // Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080) // Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        BTEditOrgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditOrganizerProfile.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        BTProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerProfile.this, OrganizerMainPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            final String username = getIntent().getStringExtra("username");
            StorageReference fileReference = mStorageReference.child("profile_images").child(username + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Organizer").child(username);
                                    databaseReference.child("profileImageUrl").setValue(uri.toString());
                                    Toast.makeText(OrganizerProfile.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    Glide.with(OrganizerProfile.this).load(uri).into(IVProfile);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OrganizerProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        String bio = snapshot.child("bio").getValue(String.class);
                        String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                        TVUsername.setText(username);
                        TVBio.setText(bio);
                        if (profileImageUrl != null) {
                            Glide.with(OrganizerProfile.this).load(profileImageUrl).into(IVProfile);
                        }
                    } else {
                        Toast.makeText(OrganizerProfile.this, "The Organizer profile doesn't exist. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Read Error", error.toString());
                }
            };
            mDatabaseReference.addValueEventListener(mValueEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mValueEventListener != null) {
            mDatabaseReference.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }
}
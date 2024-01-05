package com.example.ecocity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class RewardsUploadPic extends AppCompatActivity {
    private StorageReference storageReference;
    private LinearProgressIndicator progress;
    private Uri image;
    private MaterialButton selectImage, uploadImage;
    private ImageView imageView;
    DatabaseReference reference;
    FirebaseDatabase database;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        image = result.getData().getData();
                        uploadImage.setEnabled(true);
                        Glide.with(getApplicationContext()).load(image).into(imageView);
                    }
                } else {
                    Toast.makeText(RewardsUploadPic.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewards_upload_pic);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Reward");

        FirebaseApp.initializeApp(RewardsUploadPic.this);
        storageReference = FirebaseStorage.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progress = findViewById(R.id.progress);
        imageView = findViewById(R.id.imageView);
        selectImage = findViewById(R.id.selectImage);
        uploadImage = findViewById(R.id.uploadImage);

        selectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });

    uploadImage.setOnClickListener(v -> {
        // Check if an image is selected
        if (image != null) {
            uploadImage(image);
        } else {
            Toast.makeText(RewardsUploadPic.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    });
}

    private void uploadImage(Uri image) {
        StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
        reference.putFile(image).addOnSuccessListener(taskSnapshot -> {
            reference.getDownloadUrl().addOnSuccessListener(downloadUri -> {
            });

            Toast.makeText(RewardsUploadPic.this, "Image successfully uploaded!", Toast.LENGTH_SHORT).show();
        }).addOnProgressListener(snapshot -> {
            progress.setMax(Math.toIntExact(snapshot.getTotalByteCount()));
            progress.setProgress(Math.toIntExact(snapshot.getBytesTransferred()));
        });
    }
}

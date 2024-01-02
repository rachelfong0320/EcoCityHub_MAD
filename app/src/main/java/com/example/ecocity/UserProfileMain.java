package com.example.ecocity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserProfileMain extends AppCompatActivity {
    TextView TitleUsername, UserPoint;
    ConstraintLayout myConstraintLayout, AboutUsConstraint, supportLayout, PonitLayout, PrivacyLayout,FeedbackLayout, RatingLayout;
    ImageView imageView,imageViewButton, buttonBack;
    Button buttonLogOut;
    FirebaseStorage mFirebaseStorage;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser currentUser;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TitleUsername = findViewById(R.id.UserName);
        UserPoint= findViewById(R.id.textView3);
        showUserData();

        //profile picture
        imageView = findViewById(R.id.imageView);
        imageViewButton = findViewById(R.id.imageView4);
        buttonBack=findViewById(R.id.imageView28);


        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();


        RatingLayout=findViewById(R.id.RatingLayout);
        RatingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileMain.this, Rating.class));
            }
        });

        // setting profile picture
        imageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserProfileMain.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(UserProfileMain.this, UserProfileMain.class));
            }
        });

        buttonLogOut= findViewById(R.id.button);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfileMain.this,User_login.class));
                finish();
            }
        });

        myConstraintLayout = findViewById(R.id.editProfile);
        myConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileMain.this, EditProfile.class);
                startActivity(intent);
                passUserData();
            }
        });

        PonitLayout = findViewById(R.id.PonitLayout);
        PonitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For accumulated point
            }
        });

        AboutUsConstraint = findViewById(R.id.AboutUsLayout);
        AboutUsConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileMain.this, AboutUs.class);
                startActivity(intent);
            }
        });

        supportLayout= findViewById(R.id.SupportLayout);
        supportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileMain.this, ReportProb.class);
                startActivity(intent);
            }
        });

        PrivacyLayout = findViewById(R.id.PrivacyLayout);
        PrivacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid= getIntent().getStringExtra("uid");
                Intent intent = new Intent(UserProfileMain.this, AccPrivacy.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        FeedbackLayout=findViewById(R.id.FeedbackLayout);
        FeedbackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileMain.this,Feedback.class ));
            }
        });

        RatingLayout=findViewById(R.id.RatingLayout);
        RatingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileMain.this, Rating.class));
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
            final String uid = getIntent().getStringExtra("uid");
            StorageReference fileReference = mStorageReference.child("USER_profile_images").child(uid + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                                    databaseReference.child("profileImageUrl").setValue(uri.toString());
                                    Toast.makeText(UserProfileMain.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    Glide.with(UserProfileMain.this).load(uri).into(imageView);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserProfileMain.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // retrieve data from firebase and display at user profile
    public void showUserData() {
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("username");
        TitleUsername.setText(nameUser);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("username");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve the profile image URL and privacy setting
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                    boolean isPrivate = snapshot.child("privacy").getValue(Boolean.class);

                    // Load the profile image using Glide if the account is public
                    if (!isPrivate && profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(UserProfileMain.this).load(Uri.parse(profileImageUrl)).into(imageView);
                    } else {
                        // Account is private, set a default drawable or image
                        imageView.setImageResource(R.drawable.user_icon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }




    // To pass data to edit profile
    public void passUserData(){
        String userUsername = TitleUsername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUsersDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String usernameFromBB = snapshot.child(userUsername).child("username").getValue(String.class);
                    String genderFromBB = snapshot.child(userUsername).child("gender").getValue(String.class);
                    String contNumFromBB = snapshot.child(userUsername).child("contNum").getValue(String.class);
                    String emailFromBB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String addressFromBB = snapshot.child(userUsername).child("address").getValue(String.class);
                    String passFromBB = snapshot.child(userUsername).child("pass1").getValue(String.class);
                    String dateFromDB = snapshot.child(userUsername).child("date").getValue(String.class);

                    Intent intent = new Intent(UserProfileMain.this, EditProfile.class);
                    intent.putExtra("username", usernameFromBB);
                    intent.putExtra("gender", genderFromBB);
                    intent.putExtra("contNum", contNumFromBB);
                    intent.putExtra("email", emailFromBB);
                    intent.putExtra("address", addressFromBB);
                    intent.putExtra("password", passFromBB);
                    intent.putExtra("date",dateFromDB);

                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

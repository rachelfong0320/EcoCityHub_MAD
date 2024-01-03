package com.example.ecocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VolunteerPostUser extends AppCompatActivity {
    MaterialButton BtnVolPostBack;
    TextView TVVolPostTitle, TVVolPostDesc, TVVolPostPoints, TVVolPostDate, TVVolPostTime, TVVolPostLocation, TVVolPostAddress, TVAgeGroupValue, TVRequirementsValue, TVContactValue;
    AppCompatButton BtnVolApply;


    // Value to put in Intent
    private String username;
    private String activityKey;
    private String organizerName;

    // special case for back button
    //private String finishUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_post_user);
        BtnVolPostBack = findViewById(R.id.BtnVolPostBack);
        TVVolPostTitle = findViewById(R.id.TVVolPostTitle);
        TVVolPostDesc = findViewById(R.id.TVVolPostDesc);
        TVVolPostPoints = findViewById(R.id.TVVolPostPoints);
        TVVolPostDate = findViewById(R.id.TVVolPostDate);
        TVVolPostTime = findViewById(R.id.TVVolPostTime);
        TVVolPostLocation = findViewById(R.id.TVVolPostLocation);
        TVVolPostAddress = findViewById(R.id.TVVolPostAddress);
        TVAgeGroupValue = findViewById(R.id.TVAgeGroupValue);
        TVRequirementsValue = findViewById(R.id.TVRequirementsValue);
        TVContactValue = findViewById(R.id.TVContactValue);
        BtnVolApply = findViewById(R.id.BtnVolApply);

        // get value using Intent
        organizerName = getIntent().getStringExtra("ORGANIZER_NAME");
        activityKey = getIntent().getStringExtra("ACTIVITY_KEY");
        username = getIntent().getStringExtra("USERNAME");

        //special case for back button
//        if(getIntent().getStringExtra("finishUpload") != null){
//            finishUpload = getIntent().getStringExtra("finishUpload");
//        }else{
//            finishUpload = "";
//        }

        //test activityKey
//        Toast.makeText(VolunteerPostUser.this, activityKey, Toast.LENGTH_LONG).show();

        // test username
//        if (username == null) {
//            Toast.makeText(VolunteerPostUser.this, "Username is null 1", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(VolunteerPostUser.this, username, Toast.LENGTH_SHORT).show();
//        }

        // setText of BtnVolApply (Apply or Applied)  >>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // APPLICATIONS
        DatabaseReference appReference = FirebaseDatabase.getInstance().getReference("Application").child(username);


        //appReference = appReference.child(username);
        //Log.d("VolunteerPostUser", "has appReference.child(username): " + appReference.getKey().toString());

        if (username != null) {
            appReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot appSnapshot) {
                    if (appSnapshot.exists()) {
                        for (DataSnapshot applicationID : appSnapshot.getChildren()) {

                            // test application ID
                            // Log.d("VolunteerPostUser", "getKey: " + applicationID.getKey().toString()); //can get application ID

                            String databaseActivityID = applicationID.child("activityID").getValue(String.class);

                            if (databaseActivityID != null && databaseActivityID.equals(activityKey)) {
                                // addListenerForSingleValueEvent method is asynchronous method
                                BtnVolApply.setText(applicationID.child("status").getValue(String.class));
                                BtnVolApply.setEnabled(false);
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(VolunteerPostUser.this, "Failed to load applications details.", Toast.LENGTH_SHORT).show();
                }

            });
        }else {
            // Handle the case where username is null
            Toast.makeText(VolunteerPostUser.this, "Username is null", Toast.LENGTH_SHORT).show();
        }


        // Update frontend with database value >>>>>>>>>>>>
        // ACTIVITIES

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities").child(organizerName).child(activityKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                // setText to TextView and Button >>>>
                VolunteerPostUserHelper volPostHelper = snapshot.getValue(VolunteerPostUserHelper.class);

                if (volPostHelper != null){

                    TVVolPostTitle.setText(volPostHelper.getTitle());
                    TVVolPostDesc.setText(volPostHelper.getDescription());
                    TVVolPostPoints.setText(volPostHelper.getPoints() + " Points");
                    TVVolPostDate.setText(volPostHelper.getDateActivity());
                    TVVolPostTime.setText(volPostHelper.getStartTimeActivity() + " - " + volPostHelper.getEndTimeActivity());
                    TVVolPostLocation.setText(volPostHelper.getLocation());
                    TVVolPostAddress.setText(volPostHelper.getAddress());
                    TVAgeGroupValue.setText(volPostHelper.getMinimumAge() + " - " + volPostHelper.getMaximumAge() + " years old");
                    TVRequirementsValue.setText(volPostHelper.getRequirements());
                    TVContactValue.setText(volPostHelper.getContactNo());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerPostUser.this, "Failed to load activity details.", Toast.LENGTH_SHORT).show();
            }
        });

        BtnVolPostBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

//                Toast.makeText(VolunteerPostUser.this,"finishUpload: "+finishUpload,Toast.LENGTH_SHORT).show();
//
//                if(finishUpload.equals("true")){
//                    Intent intent = new Intent(VolunteerPostUser.this, VolunteerList.class);
//                    intent.putExtra("USERNAME", username);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    finish();
//                }

//                if(finishUpload.equals("true")){
//                    // If finishUpload is true, clear two activities from the back stack
//                    Intent intent = new Intent(VolunteerPostUser.this, VolunteerList.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //has Bug
//                    intent.putExtra("USERNAME", username);
//                    startActivity(intent);
//                    finish();
//                }else {
////                Intent intent = new Intent(VolunteerPostUser.this, VolunteerList.class);
//////                intent.putExtra("ORGANIZER_NAME", organizerName);
//////                intent.putExtra("ACTIVITY_KEY", activityKey);
////                intent.putExtra("USERNAME", username); //Pass username back
////                startActivity(intent);
//                    finish();
//                }
            }
        });

        BtnVolApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VolunteerPostUser.this, UploadCV.class);
                intent.putExtra("ACTIVITY_KEY", activityKey);
                intent.putExtra("USERNAME", username); //Pass username & activityKey to Upload CV
                intent.putExtra("ORGANIZER_NAME",organizerName);
                startActivity(intent);
            }
        });
    }
}

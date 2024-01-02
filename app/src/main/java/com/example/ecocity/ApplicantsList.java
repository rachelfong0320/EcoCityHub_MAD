package com.example.ecocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplicantsList extends AppCompatActivity {
    ArrayList <ApplicantsListHelper> applicants;
    RecyclerView applicant_recycler_list;
    ApplicantsListAdapter applicantsAdapter;
    MaterialButton BTDashboardBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants_list);
        applicant_recycler_list = findViewById(R.id.applicant_recycler_list);
        BTDashboardBack = findViewById(R.id.BTDashboardBack);

        String activityKey = getIntent().getStringExtra("activityKey");

        BTDashboardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityKey != null){
                    Intent intent = new Intent(ApplicantsList.this, VolunteerActivityPost.class);
                    intent.putExtra("activityKey", activityKey);
                    startActivity(intent);
                    finish();
                }
            }
        });

        applicant_recycler_list.setHasFixedSize(true);
        applicant_recycler_list.setLayoutManager(new LinearLayoutManager(this));

        applicants = new ArrayList<ApplicantsListHelper>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Application");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicants.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()){
                    for (DataSnapshot applicationSnapshot : userSnapshot.getChildren()){
                        String username = userSnapshot.getKey();
                        String url = applicationSnapshot.child("url").getValue(String.class);
                        String status = applicationSnapshot.child("status").getValue(String.class);
                        String actKey = applicationSnapshot.child("activityID").getValue(String.class);
                        String appKey = applicationSnapshot.getKey();

                        if (activityKey.equals(actKey)){
                            ApplicantsListHelper applicant = new ApplicantsListHelper(username, url, status, appKey);
                            applicants.add(applicant);
                        }
                    }
                }

                if (applicantsAdapter == null){
                    applicantsAdapter = new ApplicantsListAdapter(applicants, new ApplicantsListAdapter.ClickListener() {
                        @Override
                        public void onClick(String url) {
                            Log.d("ApplicantsList", "Clicked on: " + url);
                        }
                    });
                    applicant_recycler_list.setAdapter(applicantsAdapter);
                }else{
                    applicantsAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ApplicantsList", "Failed to load applicants.", error.toException());
            }
        });
    }
}
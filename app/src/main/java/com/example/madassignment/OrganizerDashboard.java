package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrganizerDashboard extends AppCompatActivity {
    ArrayList <ActivityHelper> activity_list;
    MaterialButton BTDashboardBack;
    RecyclerView recyclerView;
    ActivityAdapter activityAdapter;
    FloatingActionButton FOBAddActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);
        BTDashboardBack = findViewById(R.id.BTDashboardBack);

        FOBAddActivities = findViewById(R.id.FOBAddActivities);

        FOBAddActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerDashboard.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.activity_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activity_list = new ArrayList<ActivityHelper>();

        activityAdapter = new ActivityAdapter(activity_list, key -> {
            Intent intent = new Intent(OrganizerDashboard.this, VolunteerActivityPost.class);
            intent.putExtra("activityKey", key);
            startActivity(intent);
        });
        recyclerView.setAdapter(activityAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username.isEmpty()) {
            Toast.makeText(this, "No username found. Please log in again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LogInOrganizer.class);
            startActivity(intent);
            finish();
            return;
        }

        DatabaseReference activitiesRef = FirebaseDatabase.getInstance().getReference("Activities").child(username);

        if (activitiesRef != null){
            activitiesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    activity_list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ActivityHelper activity = dataSnapshot.getValue(ActivityHelper.class);
                        if (activity != null) {
                            activity.setKey(dataSnapshot.getKey());
                            activity_list.add(activity);
                        }
                    }
                    activityAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(OrganizerDashboard.this, "Failed to load activities.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        FOBAddActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerDashboard.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        BTDashboardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerDashboard.this, OrganizerMainPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }
}
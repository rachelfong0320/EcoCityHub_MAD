package com.example.ecocity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VolunteerList extends AppCompatActivity {

    ArrayList <VolListHelper> activity_list;
    RecyclerView recyclerView;

    // Value (of USERNAME) to put in Intent
    private String username; // TODO: Intent Value
    VolListAdapter volListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_list);

        // Get username using intent
        username = getIntent().getStringExtra("username"); // getIntent
        //Toast.makeText(VolunteerList.this,username,Toast.LENGTH_SHORT).show(); //Toast: Testing username



        // Back Button
        ImageButton btnVolBack = findViewById(R.id.BtnVolBack);
        btnVolBack.setOnClickListener(v -> {
            finish();
        });


        // RecyclerView setup
        recyclerView = findViewById(R.id.RecycleViewVolunteer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set Adapter
        activity_list = new ArrayList<VolListHelper>();

        volListAdapter = new VolListAdapter(activity_list, (key, organizerName, username) -> {
            Intent intent = new Intent(VolunteerList.this, VolunteerPostUser.class);
            intent.putExtra("ACTIVITY_KEY", key);
            intent.putExtra("ORGANIZER_NAME", organizerName);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        recyclerView.setAdapter(volListAdapter);

        // Fetch activity data from Firebase
        DatabaseReference activitiesRef = FirebaseDatabase.getInstance().getReference("Activities");

        activitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activity_list.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        VolListHelper helper = dataSnapshot2.getValue(VolListHelper.class);
                        if (helper != null) {
                            helper.setKey(dataSnapshot2.getKey());
                            helper.setOrganizerName(dataSnapshot1.getKey());
                            helper.setUsername(username);
                            activity_list.add(helper);
                        }
                    }
                }
                volListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerList.this, "Failed to load activities.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

package com.example.ecocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizerMainPage extends AppCompatActivity {
    MaterialCardView MCProfile, MCDashboard;
    TextView usernameOrgMainPage;
    ImageView IVLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_main_page);

        MCProfile = findViewById(R.id.MCProfile);
        MCDashboard = findViewById(R.id.MCDashboard);
        usernameOrgMainPage = findViewById(R.id.usernameOrgMainPage);
        IVLogOut = findViewById(R.id.IVLogOut);

        String key = getIntent().getStringExtra("username");

        MCProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerMainPage.this, OrganizerProfile.class);
                intent.putExtra("username", key);
                startActivity(intent);
            }
        });

        MCDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerMainPage.this, OrganizerDashboard.class);
                intent.putExtra("username", key);
                startActivity(intent);
            }
        });

        IVLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerMainPage.this, LogInOrganizer.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Organizer").child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String username = snapshot.child("username").getValue(String.class);
                    usernameOrgMainPage.setText(username);
                }else{
                    Toast.makeText(OrganizerMainPage.this, "The Organizer profile doesn't exist. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
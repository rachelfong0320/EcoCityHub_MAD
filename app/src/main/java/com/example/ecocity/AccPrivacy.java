package com.example.ecocity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccPrivacy extends AppCompatActivity {
    private Switch privacySwitch;
    private String username;
    private DatabaseReference userReference;
    private SharedPreferences sharedPreferences;
    ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_privacy);

        privacySwitch = findViewById(R.id.switch1);
        sharedPreferences=getSharedPreferences("com.example.ecocity", MODE_PRIVATE);
        buttonBack=findViewById(R.id.imageView28);


        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        String gender = intent.getStringExtra("gender");
        String contNum = intent.getStringExtra("contNum");
        String email = intent.getStringExtra("email");
        String address = intent.getStringExtra("address");
        String password =intent.getStringExtra("password");
        String date = intent.getStringExtra("date");

        userReference= FirebaseDatabase.getInstance().getReference("Users").child(username);

        boolean isPrivate = sharedPreferences.getBoolean("isPrivate", false);
        privacySwitch.setChecked(isPrivate);

        // Set a listener for the switch state change
        privacySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the privacy setting in the SharedPreference
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isPrivate", isChecked);
                editor.apply();

                // Update the privacy setting in the Database
                userReference.child("privacy").setValue(isChecked);

                Toast.makeText(AccPrivacy.this, "Privacy setting updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccPrivacy.this, UserProfileMain.class);
                intent.putExtra("username",username);
                intent.putExtra("gender", gender);
                intent.putExtra("contNum", contNum);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                intent.putExtra("password", password);
                intent.putExtra("date", date);
                startActivity(intent);

            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
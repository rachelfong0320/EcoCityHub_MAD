package com.example.ecocity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

public class UserHomePage extends AppCompatActivity {

    MaterialCardView MCUserProfile, MCVolunteer, MCResource, MCReward;
    TextView usernameText;
    String username,gender, contNum,email,address,password,date;
    ImageView IVLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        usernameText = findViewById(R.id.username);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        gender=intent.getStringExtra("gender");
        contNum=intent.getStringExtra("contNum");
        email=intent.getStringExtra("email");
        address=intent.getStringExtra("address");
        password=intent.getStringExtra("password");
        date=intent.getStringExtra("date");
        usernameText.setText(username);

        IVLogOut=findViewById(R.id.IVLogOut);
        IVLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHomePage.this,User_login.class));
                startActivity(intent);
            }
        });

        MCUserProfile=findViewById(R.id.MCUserProfile);
        MCUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomePage.this, UserProfileMain.class);
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
    }
}
package com.example.ecocity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class leaderboardMainPage extends AppCompatActivity {
    String username,gender, contNum,email,address,password,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_main_page);

        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        gender=intent.getStringExtra("gender");
        contNum=intent.getStringExtra("contNum");
        email=intent.getStringExtra("email");
        address=intent.getStringExtra("address");
        password=intent.getStringExtra("password");
        date=intent.getStringExtra("date");

        ImageButton arrowButton = findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NewPageActivity when the button is clicked
                Intent intent = new Intent(leaderboardMainPage.this, Leaderboard.class);

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

        ImageButton Guideline1_btn = findViewById(R.id.Guideline1_btn);
        Guideline1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NewPageActivity when the button is clicked
                Intent intent = new Intent(leaderboardMainPage.this, Guideline1.class);
                startActivity(intent);
            }
        });

        ImageButton backButtonLeaderboard = findViewById(R.id.backButtonLeaderboard);
        backButtonLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NewPageActivity when the button is clicked
                Intent intent = new Intent();
                intent.putExtra("username",username);
                intent.putExtra("gender", gender);
                intent.putExtra("contNum", contNum);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                intent.putExtra("password", password);
                intent.putExtra("date", date);
                startActivity(intent);
                finish();
            }
        });


    }
}
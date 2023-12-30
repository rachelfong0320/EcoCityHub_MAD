package com.example.ecocity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Feedback extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    String category;
    ImageView selectedImageView, buttonBack;
    String FeedbackKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        EditText feedbackForm = findViewById(R.id.editTextTextMultiLine);
        Button btnSubmit = findViewById(R.id.button4);
        ImageView profile = findViewById(R.id.imageView16);
        ImageView volunteer = findViewById(R.id.imageView19);
        ImageView exchange = findViewById(R.id.imageView20);
        ImageView reward = findViewById(R.id.imageView26);
        selectedImageView = null;
        buttonBack= findViewById(R.id.imageView28);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //check for category
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "profile";
                setBorder(profile);
            }
        });

        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "volunteer";
                setBorder(volunteer);
            }
        });

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "exchange";
                setBorder(exchange);
            }
        });

        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "reward";
                setBorder(reward);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Feedback");
                FeedbackKey=reference.push().getKey();

                String feedback = feedbackForm.getText().toString().trim();

                if(! feedbackForm.getText().toString().isEmpty()){
                    String message = "We appreciate your feedback, we'll try hard to it!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    FeedbackHelperClass helperClass = new FeedbackHelperClass(feedback);
                    reference.child(category).child(FeedbackKey).setValue(helperClass);
                    Intent intent = new Intent(Feedback.this, UserProfileMain.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Feedback.this, "Please leave your feedback here!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void setBorder(ImageView imageView) {
        // Reset the border for the previously selected ImageView
        if (selectedImageView != null) {
            selectedImageView.setBackgroundResource(0); // Remove the border
        }

        // Set the border for the clicked ImageView
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setShape(GradientDrawable.RECTANGLE);
        borderDrawable.setStroke(5, Color.BLUE); // Set border width and color
        imageView.setBackground(borderDrawable);

        // Update the currently selected ImageView
        selectedImageView = imageView;
    }

}
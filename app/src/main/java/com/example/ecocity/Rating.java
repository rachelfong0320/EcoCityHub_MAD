package com.example.ecocity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Rating extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseDatabase database;
    Float rateProfileSystem, rateVolunteer, rateResource, ratePoint, rateOverall;
    ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        RatingBar RateBarProfile = findViewById(R.id.ratingBar);
        RatingBar RateBarVolunteer = findViewById(R.id.ratingBar2);
        RatingBar RateBarResource = findViewById(R.id.ratingBar3);
        RatingBar RateBarPoint = findViewById(R.id.ratingBar4);
        RatingBar RateBarOverall = findViewById(R.id.ratingBar5);
        buttonBack= findViewById(R.id.imageView28);


        LayerDrawable stars = (LayerDrawable) RateBarProfile.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#966fd6"), PorterDuff.Mode.SRC_ATOP);


        Button button = findViewById(R.id.buttonSubmit);

        TextView OverallRateCount = findViewById(R.id.textViewOverall);
        TextView ProfileRateCount = findViewById(R.id.textViewProfile);
        TextView ResourceRateCount = findViewById(R.id.textViewResource);
        TextView VolunteerRateCount = findViewById(R.id.textViewVolunteer);
        TextView PointRateCount = findViewById(R.id.textViewPoint);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("Rating");
                String RatingKey = reference.push().getKey();

                RatingHelperClass helperClass = new RatingHelperClass(rateProfileSystem, rateVolunteer, rateResource, ratePoint, rateOverall);
                reference.child(RatingKey).setValue(helperClass);

                String message = "We appreciated you rating! It means a lot to us.";
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Rating.this, UserProfileMain.class);
                startActivity(intent);


            }
        });

        RateBarProfile.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ProfileRateCount.setText("You have rated " + rating);
                rateProfileSystem= rating;
            }
        });

        RateBarVolunteer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                VolunteerRateCount.setText("You have rated " + rating);
                rateVolunteer= rating;
            }
        });

        RateBarPoint.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                PointRateCount.setText("You have rated " + rating);
                ratePoint=rating;
            }
        });

        RateBarOverall.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                OverallRateCount.setText("You have rated " + rating);
                rateOverall = rating;
            }
        });

        RateBarResource.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ResourceRateCount.setText("You have rated " + rating);
                rateResource=rating;
            }
        });
    }
}
package com.example.ecocity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RewardsMainPage extends AppCompatActivity {

    RecyclerView recyclerView;
    RewardsMainAdapter mainAdapter;
    DatabaseReference reference;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewards_main_page);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Reward");

        Intent intent=getIntent();
        String username =intent.getStringExtra("username");

        String loggedInUsername =  username;



        FirebaseRecyclerOptions<RewardsMainModel> options =
                new FirebaseRecyclerOptions.Builder<RewardsMainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Reward"), RewardsMainModel.class)
                        .build();

        mainAdapter = new RewardsMainAdapter(options, loggedInUsername);

        // Check if data already exists before adding sample data
        checkForExistingData();

        recyclerView.setAdapter(mainAdapter);

        ImageButton backButton = findViewById(R.id.backButton4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NewPageActivity when the button is clicked
                Intent intent = new Intent(RewardsMainPage.this,UserHomePage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    private void checkForExistingData() {
        // Check if data already exists based on some criteria
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Add sample data only if the "Reward" node is empty
                    addSampleData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
    private void addSampleData() {

        String[] names = {
                "5% off Zus Coffee",
                "RM5 off Jaya Grocer",
                "RM20 off Watsons",
                "25% off Seoul Garden Hotpot"
        };

        String[] descriptions = {
                "Enjoy 5% off when you treat yourself to a cup of Zus Coffee! Savor the rich flavors and aromatic blends while indulging in a special offer. Simply make your purchase and relish the savings. Cheers to a delightful coffee experience with Zus!",
                "Get RM5 off your purchase at Jaya Grocer. Treat yourself and simply make your way to Jaya Grocer and savor the extra savings on your shopping. Don't miss out on this opportunity to enjoy more for less!",
                "Indulge in a shopping spree and enjoy a fabulous RM20 off your purchase. Whether you're stocking up on beauty essentials, wellness products, or personal care items, this exclusive discount is your ticket to more for less.  Don’t miss the boat!",
                "Enjoy a fantastic 25% off at Seoul Garden Hotpot. Dive into a world of delectable hotpot delights while relishing the savings. Gather your friends and family for a memorable dining experience without breaking the bank. Hurry, let the feast begin!"
        };

        int[] points = {50, 100, 400, 1000};

//        List<RewardHelperClass> rewards = new ArrayList<>();
//        String RewardKey = null;

        for (int i = 0; i < names.length; i++) {
            String RewardKey = reference.push().getKey();
            RewardsHelperClass helperClass = new RewardsHelperClass(names[i], descriptions[i], points[i]);
            //rewards.add(helperClass);
            reference.child(RewardKey).setValue(helperClass);
        }

    }

}

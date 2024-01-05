package com.example.ecocity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard extends AppCompatActivity {

    String username,gender, contNum,email,address,password,date;

    TextView user, user_point, user_rank, total_user;

    private ArrayList<leaderboardHelper> rankingsList;
    private RecyclerView usersView;
    private leaderboardAdapter rankingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        gender=intent.getStringExtra("gender");
        contNum=intent.getStringExtra("contNum");
        email=intent.getStringExtra("email");
        address=intent.getStringExtra("address");
        password=intent.getStringExtra("password");
        date=intent.getStringExtra("date");

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users");

        user = findViewById(R.id.user);
        user_point= findViewById(R.id.user_point);
        user_rank= findViewById(R.id.user_rank);
        total_user = findViewById(R.id.total_user);

        // Initialize RecyclerView and Adapter
        usersView = findViewById(R.id.recyclerView);
        usersView.setHasFixedSize(true);
        usersView.setLayoutManager(new LinearLayoutManager(this));

        rankingsList = new ArrayList<>();

        rankingAdapter = new leaderboardAdapter(rankingsList, null);

        usersView.setAdapter(rankingAdapter);

        //Fetch Data from Firebase
        Query query = databaseReference.orderByChild("point");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rankingsList.clear();
                int currentRank = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    leaderboardHelper helper = snapshot.getValue(leaderboardHelper.class);
                    if (helper != null) {
                        helper.setRanking(String.valueOf(currentRank++));
                        rankingsList.add(helper);
                    }
                }

                // Sort the data
                Collections.sort(rankingsList, new Comparator<leaderboardHelper>() {
                    @Override
                    public int compare(leaderboardHelper user1, leaderboardHelper user2) {
                        // Sort in descending order based on points
                        int pointsComparison = Integer.compare(user2.getPoint(), user1.getPoint());
                        if (pointsComparison != 0) {
                            return pointsComparison;
                        } else {
                            // If points are the same, use another attribute for tie-breaker
                            return user1.getUsername().compareTo(user2.getUsername());
                        }
                    }
                });

                // Assign ranks
                int rank = 1;
                for (int i = 0; i < rankingsList.size(); i++) {
                    leaderboardHelper currentUser = rankingsList.get(i);
                    leaderboardHelper previousUser = (i > 0) ? rankingsList.get(i - 1) : null;

                    if (previousUser == null || currentUser.getPoint() < previousUser.getPoint()) {
                        // If the current user has fewer points than the previous user, assign a new rank
                        currentUser.setRanking(String.valueOf(rank));
                    } else {
                        // If the current user has the same points as the previous user, use the same rank
                        currentUser.setRanking(previousUser.getRanking());
                    }

                    rank++;
                }

                rankingAdapter.notifyDataSetChanged();

                // Fetch and display logged-in user data
                showUserData();
                total_user.setText("Total Users: " + rankingsList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(Leaderboard.this, "Error fetching data from Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Error fetching data from Firebase", databaseError.toException());
            }
        });


        // Back Button Click Listener
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private int getUserRank(String username) {
        for (int i = 0; i < rankingsList.size(); i++) {
            leaderboardHelper currentUser = rankingsList.get(i);
            if (currentUser.getUsername().equals(username)) {
                // Return the rank of the user
                return Integer.parseInt(currentUser.getRanking());
            }
        }
        // Return -1 if the user is not found (you can handle this case accordingly)
        return -1;
    }

    public void showUserData() {
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("username");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(nameUser);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Retrieve user data and update UI
                if (snapshot.exists()) {
                    leaderboardHelper userData = snapshot.getValue(leaderboardHelper.class);
                    if (userData != null) {
                        user.setText(userData.getUsername() + "(You)");
                        user_point.setText("Point: " + userData.getPoint());
                        user_rank.setText("Rank: " + getUserRank(userData.getUsername()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(Leaderboard.this, "Error fetching user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Error fetching user data", error.toException());
            }
        });
    }
}

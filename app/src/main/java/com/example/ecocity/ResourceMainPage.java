package com.example.ecocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResourceMainPage extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DailyDiscoverItem> dailyDiscoverItemList;
    private Daily_Discover_Adapter dailyDiscoverAdapter;
    private String username, receiverUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_main_page);

        username = getIntent().getStringExtra("username");

        TextView tvAlison = findViewById(R.id.TVAlison);

        if (username != null) {
            tvAlison.setText(username);
        } else {
            Toast.makeText(ResourceMainPage.this, "Username is null", Toast.LENGTH_SHORT).show();
        }

        recyclerView = findViewById(R.id.Listings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        dailyDiscoverItemList = new ArrayList<>();
        dailyDiscoverAdapter = new Daily_Discover_Adapter(this, dailyDiscoverItemList, username, receiverUsername);
        recyclerView.setAdapter(dailyDiscoverAdapter);


        ImageView profileImageView = findViewById(R.id.IVImageProfile);
        fetchImageURL(username, profileImageView);


        // Retrieve and put data to frontend (Daily Discover)
        DatabaseReference resourcesRef = FirebaseDatabase.getInstance().getReference("Resources");
        resourcesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyDiscoverItemList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        DailyDiscoverItem item = dataSnapshot1.getValue(DailyDiscoverItem.class);
                        String postUsername = dataSnapshot.getKey();
                        if (item != null) {
                            item.setUsername(postUsername);
                            dailyDiscoverItemList.add(item);
                        }
                    }
                }
                // Notify the adapter that the data has changed
                dailyDiscoverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResourceMainPage.this, "Failed to load resources item.", Toast.LENGTH_SHORT).show();
            }
        });

        // search method
        SearchView searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        // IVBack icon
        ImageView backIcon = findViewById(R.id.IVArrowBack);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Button +change
        Button staticButton = findViewById(R.id.staticButton);
        staticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePageActivity();
            }
        });

        // IVReport icon
        ImageView report = findViewById(R.id.IVReport);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReportPageActivity();
            }
        });


        String loggedInUsername = tvAlison.getText().toString();
        Daily_Discover_Adapter dailyDiscoverAdapter = new Daily_Discover_Adapter(this, dailyDiscoverItemList, loggedInUsername, receiverUsername);
    }

    private void openReportPageActivity() {
        Intent intent = new Intent(this, ReportProb.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    private void openChangePageActivity() {
        Intent intent = new Intent(this, Change.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openMessagePageActivity() {
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void searchList(String text){
        ArrayList<DailyDiscoverItem> searchList = new ArrayList<>();
        for(DailyDiscoverItem dailyDiscoverItem : dailyDiscoverItemList){
            if (dailyDiscoverItem.getselectedLocation().toLowerCase().contains(text.toLowerCase()) ||
                    dailyDiscoverItem.getUsername().toLowerCase().contains(text.toLowerCase()) ||
                    dailyDiscoverItem.getselectedCondition().toLowerCase().contains(text.toLowerCase()) ||
                    dailyDiscoverItem.getresourceTitle().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dailyDiscoverItem);
            }
            dailyDiscoverAdapter.searchResourceList(searchList);
        }
    }


    private void fetchImageURL(String username, ImageView imageView) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                    // Load the image using Glide or any other image loading library
                    Glide.with(ResourceMainPage.this)  // Use ResourceMainPage.this as the context
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_background)
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }
}
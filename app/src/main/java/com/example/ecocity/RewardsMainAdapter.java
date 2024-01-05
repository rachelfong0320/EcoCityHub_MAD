package com.example.ecocity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RewardsMainAdapter extends FirebaseRecyclerAdapter<RewardsMainModel,RewardsMainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String rewardKey;
    private String loggedInUsername;

    String headerFromDB, desFromDB, pointFromDB;

    public RewardsMainAdapter(@NonNull FirebaseRecyclerOptions<RewardsMainModel> options, String loggedInUsername) {
        super(options);
        this.loggedInUsername = loggedInUsername;
        this.rewardKey = rewardKey;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull RewardsMainModel model) {
        // Bind data and pass the key for the current item
        holder.bindData(getRef(position).getKey());
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, description, point;
        Button redeem;

        DatabaseReference reference;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.Reward_name);
            description = (TextView) itemView.findViewById(R.id.Re_Desc);
            point = (TextView) itemView.findViewById(R.id.Reward_point);
            redeem = (Button) itemView.findViewById(R.id.redeem_button);
            reference = FirebaseDatabase.getInstance().getReference("Reward");
        }

        public void bindData (String rewardKey){
            // Use the provided key to fetch data for the current item
            reference.child(rewardKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Retrieve data specific to the current item
                    RewardsHelperClass model = snapshot.getValue(RewardsHelperClass.class);
                    RewardsMainModel model1 = snapshot.getValue(RewardsMainModel.class);

                    if (model != null) {
                        name.setText(model.getName());
                        description.setText(model.getDescription());
                        point.setText(String.valueOf(model.getPoint()));

                        Glide.with(img.getContext())
                                .load(model1.getImage())
                                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                                .error(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark_normal)
                                .into(img);

                        // Add any additional logic related to the redeemed button, if needed
                        redeem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle button click
                                redeemReward(rewardKey, model.getPoint());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors here
                }
            });

        }
        private void redeemReward(String rewardKey, int rewardPoints) {

            // Get the current user's ID (you can replace this with your actual user ID retrieval logic)
            String userName = loggedInUsername;

            // Get the reference to the user's totalPoints in the database
            DatabaseReference userPointsRef = FirebaseDatabase.getInstance().getReference("Users").child(userName).child("point");

            // Read the user's current total points
            userPointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int currentPoints = dataSnapshot.getValue(Integer.class);

                        // Check if the user has enough points to redeem the reward
                        if (currentPoints >= rewardPoints) {
                            // Deduct the points
                            int newTotalPoints = currentPoints - rewardPoints;

                            // Update the user's total points in the database
                            userPointsRef.setValue(newTotalPoints);

                            // Inform the user about successful redemption
                            Toast.makeText(itemView.getContext(), "Reward redeemed successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            // User doesn't have enough points to redeem the reward
                            // Display a message to the user
                            Toast.makeText(itemView.getContext(), "Insufficient points to redeem this reward.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }


    }
}
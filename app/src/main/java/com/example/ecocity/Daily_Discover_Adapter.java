package com.example.ecocity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Daily_Discover_Adapter extends RecyclerView.Adapter<Daily_Discover_Adapter.MyViewHolder> {

    private ArrayList<DailyDiscoverItem> resourceList;
    private ArrayList<Model> image;
    private Context context;
    private String loggedInUsername;
    private String receiverUsername;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public Daily_Discover_Adapter(Context context, ArrayList<DailyDiscoverItem> resourceList, String loggedInUsername, String receiverUsername) {
        this.context = context;
        this.resourceList = resourceList;
        this.loggedInUsername = loggedInUsername;
        this.receiverUsername = receiverUsername;
    }

    public void searchResourceList(ArrayList<DailyDiscoverItem> searchList) {
        resourceList = searchList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dailydiscover_list, parent, false);
        return new MyViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindData(resourceList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public interface OnItemClickListener {
        void onButtonClick(int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Daily_Discover_Adapter adapter;
        private ShapeableImageView profileImageView;
        private TextView usernameTextView;
        private TextView locationTextView;
        private TextView brandTypeTextView;
        private ImageView resourceItemImageView;
        private TextView resourceItemDescription;
        private MaterialButton exchangeButton;
        private ImageView messageImageView;
        private ImageView receiverMessageImageView;
        private String notesOnCondition;
        private String notesOnLocation;
        private String descriptionOnResource;

        public MyViewHolder(View itemView, Daily_Discover_Adapter adapter) {
            super(itemView);
            this.adapter = adapter;
            profileImageView = itemView.findViewById(R.id.profile_picture_id);
            usernameTextView = itemView.findViewById(R.id.username_id);
            locationTextView = itemView.findViewById(R.id.place_id);
            brandTypeTextView = itemView.findViewById(R.id.type_id);
            resourceItemImageView = itemView.findViewById(R.id.item_id);
            resourceItemDescription = itemView.findViewById(R.id.item_description_id);
            exchangeButton = itemView.findViewById(R.id.BExchange);
            messageImageView = itemView.findViewById(R.id.IVSenderMessage);
            //receiverMessageImageView = itemView.findViewById(R.id.IVReceiverMessage);
            setListeners();
        }

        private void setListeners() {
            exchangeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showExchangeAlertDialog(loggedInUsername, adapter.resourceList.get(getAdapterPosition()).getUsername(), getAdapterPosition());
                }
            });

//
        }


        private void showExchangeAlertDialog(String loggedInUsername, String uploaderUsername, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Confirm Exchange");
            builder.setMessage("Are you sure you want to exchange resources?");

            builder.setPositiveButton("Exchange", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showPointsAlertDialog(loggedInUsername, uploaderUsername, position);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(itemView.getContext(), "Exchange cancelled", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        private void showPointsAlertDialog(String loggedInUsername, String uploaderUsername, int position) {
            AlertDialog.Builder pointsBuilder = new AlertDialog.Builder(itemView.getContext());
            pointsBuilder.setTitle("Bravo!");
            pointsBuilder.setMessage("You've successfully exchanged the resources \n\n" +
                    "Nah, 30 points are in your hand! \uD83C\uDF33\uD83C\uDF1F");

            uploaderUsername = usernameTextView.getText().toString();
            DatabaseReference uploaderRef = FirebaseDatabase.getInstance().getReference("Users").child(uploaderUsername);
            DatabaseReference uploaderPointsRef = uploaderRef.child("point");

            DatabaseReference loggedInUserRef = FirebaseDatabase.getInstance().getReference("Users").child(loggedInUsername);
            DatabaseReference loggedInUserPointsRef = loggedInUserRef.child("point");

            uploaderPointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer currentOwnerPoints = snapshot.getValue(Integer.class);
                    if (currentOwnerPoints == null) {
                        currentOwnerPoints = 0;
                    }
                    int newOwnerPoints = currentOwnerPoints + 30;
                    uploaderRef.child("point").setValue(newOwnerPoints);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error if needed
                }
            });

            loggedInUserPointsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer currentLoggedInUserPoints = snapshot.getValue(Integer.class);
                    if (currentLoggedInUserPoints == null) {
                        currentLoggedInUserPoints = 0;
                    }
                    int newLoggedInUserPoints = currentLoggedInUserPoints + 30;
                    loggedInUserRef.child("point").setValue(newLoggedInUserPoints);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error if needed
                }
            });

            pointsBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.removeResource(position);
                }
            });

            AlertDialog pointsAlertDialog = pointsBuilder.create();
            pointsAlertDialog.show();
        }

        private void bindData(DailyDiscoverItem currentItem, int position) {
            usernameTextView.setText(currentItem.getUsername());
            locationTextView.setText(currentItem.getselectedLocation());
            brandTypeTextView.setText(currentItem.getselectedCondition());
            resourceItemDescription.setText(currentItem.getresourceTitle());


            notesOnCondition = currentItem.getNotesOnCondition();
            notesOnLocation = currentItem.getNotesOnLocation();
            descriptionOnResource = currentItem.getDescriptionOnResource();

            String imageUrl = currentItem.getresourceUrl();
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .into(resourceItemImageView);

            boolean isUploader = loggedInUsername.equals(currentItem.getUsername());
            Log.d("Debug", "Username in bindData: " + currentItem.getUsername());

            exchangeButton.setVisibility(isUploader ? View.INVISIBLE : View.VISIBLE);
            messageImageView.setVisibility(isUploader ? View.INVISIBLE : View.VISIBLE);

            fetchImageURL(currentItem.getUsername(), profileImageView);

            messageImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to start the Chat activity
                    Intent intent = new Intent(itemView.getContext(), Chat.class);

                    // Pass the username as an extra to the Intent
                    intent.putExtra("receiverUsername", currentItem.getUsername());


                    Log.d("Debug", "To pass Username in bindData: " + currentItem.getUsername());


                    // Start the Chat activity
                    itemView.getContext().startActivity(intent);
                }
            });




            resourceItemImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && adapter.onItemClickListener != null) {
                        adapter.onItemClickListener.onButtonClick(position);
                    }

                    // Move this part outside the if statement
                    // Create an Intent to start the Chat activity
                    Intent intent = new Intent(itemView.getContext(), ItemDescription.class);

                    // Pass the username as an extra to the Intent
                    intent.putExtra("receiverUsername", currentItem.getUsername());
                    intent.putExtra("resourceItemTitle", currentItem.getresourceTitle());
                    intent.putExtra("location", currentItem.getselectedLocation());
                    intent.putExtra("condition", currentItem.getselectedCondition());
                    intent.putExtra("resourceUrl", currentItem.getresourceUrl());

                    intent.putExtra("notesOnCondition", notesOnCondition);
                    intent.putExtra("notesOnLocation", notesOnLocation);
                    intent.putExtra("resourceDescription", descriptionOnResource);

                    context.startActivity(intent);
                }
            });


            resourceItemDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && adapter.onItemClickListener != null) {
                        adapter.onItemClickListener.onButtonClick(position);
                    }

                    // Move this part outside the if statement
                    // Create an Intent to start the Chat activity
                    Intent intent = new Intent(itemView.getContext(), ItemDescription.class);

                    // Pass the username as an extra to the Intent
                    intent.putExtra("receiverUsername", currentItem.getUsername());
                    intent.putExtra("resourceItemTitle", currentItem.getresourceTitle());
                    intent.putExtra("location", currentItem.getselectedLocation());
                    intent.putExtra("condition", currentItem.getselectedCondition());
                    intent.putExtra("resourceUrl", currentItem.getresourceUrl());

                    intent.putExtra("notesOnCondition", notesOnCondition);
                    intent.putExtra("notesOnLocation", notesOnLocation);
                    intent.putExtra("resourceDescription", descriptionOnResource);

                    context.startActivity(intent);
                }
            });

        }
    }

    public void removeResource(int position) {
        // in app
        DailyDiscoverItem removedItem = resourceList.remove(position);
        String removedResourceID = removedItem.getResourceId();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, resourceList.size());
        Toast.makeText(context, "The resource item is removed", Toast.LENGTH_SHORT).show();

        // in firebase
        DatabaseReference removedResourceRef = FirebaseDatabase.getInstance().getReference("Resources");

        removedResourceRef.child(removedItem.getUsername())  // Assuming getUsername() returns the username
                .orderByChild("resourceId").equalTo(removedResourceID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot resourceSnapshot : snapshot.getChildren()) {
                            // Log the removed resourceId
                            Log.d("Firebase", "Removed resourceId: " + removedResourceID);

                            // Remove the specific resource under the user
                            resourceSnapshot.getRef().removeValue();
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                        Log.e("Firebase", "Error during removal", error.toException());
                    }
                });
    }



    private void fetchImageURL(String username, ImageView imageView) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                    boolean isPrivate = snapshot.child("privacy").getValue(Boolean.class);

                    if (!isPrivate && imageUrl != null && !imageUrl.isEmpty()) {
                        // Load the image using Glide or any other image loading library
                        Glide.with(context)
                                .load(imageUrl)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.baseline_person_24)
                                .into(imageView);
                    }else{
                        // Account is private, set a default drawable or image
                        imageView.setImageResource(R.drawable.baseline_person_24);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }

}

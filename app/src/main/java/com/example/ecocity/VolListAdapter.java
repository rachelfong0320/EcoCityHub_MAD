package com.example.ecocity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VolListAdapter extends RecyclerView.Adapter<VolListAdapter.ActivityViewHolder> {

    private List<VolListHelper> activities;
    private View.OnClickListener clickListener;


    public VolListAdapter(ArrayList<VolListHelper> activities, ClickListener clickListener) {
        this.activities = activities;
        this.clickListener = view -> {
            // Retrieving the custom object from the tag
            TagData tagData = (TagData) view.getTag();

            // Checking for null to avoid potential issues
            if (tagData != null) {
                // Invoking the onClick method with both key and organizerName
                clickListener.onClick(tagData.getKey(), tagData.getOrganizerName(), tagData.getUsername());
            }
        };
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_volunteer_card, parent, false);
        ActivityViewHolder viewHolder = new ActivityViewHolder(view);
        viewHolder.seeMore.setOnClickListener(clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        VolListHelper model = activities.get(position);
        holder.title.setText(model.getTitle());
        holder.location.setText(model.getLocation());
        holder.date.setText(model.getDateActivity());
        holder.time.setText(model.getStartTimeActivity());

        // Creating a custom object to hold both key and organizerName
        TagData tagData = new TagData(model.getKey(), model.getOrganizerName(), model.getUsername());

        // Setting the custom object as the tag
        holder.seeMore.setTag(tagData);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView location;
        TextView date;
        TextView time;
        AppCompatButton seeMore;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.TVVolListTitle);
            location = itemView.findViewById(R.id.TVVolListLocation);
            date = itemView.findViewById(R.id.TVVolListDate);
            time = itemView.findViewById(R.id.TVVolListTime);
            seeMore = itemView.findViewById(R.id.BtnSeeMore);
            seeMore.setOnClickListener(clickListener);
        }
    }

    public interface ClickListener {
        void onClick(String key, String organizerName, String username);
    }


    // Custom class to hold multiple values for Tag
    private static class TagData {
        private String key;
        private String organizerName;
        private String username;

        public TagData(String key, String organizerName, String username) {
            this.key = key;
            this.organizerName = organizerName;
            this.username = username;
        }

        public String getKey() {
            return key;
        }

        public String getOrganizerName() {
            return organizerName;
        }
        public String getUsername() {
            return username;
        }
    }

}


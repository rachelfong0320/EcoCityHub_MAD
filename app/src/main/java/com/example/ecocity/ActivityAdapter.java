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

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<ActivityHelper> activities;
    private View.OnClickListener clickListener;


    public ActivityAdapter(ArrayList<ActivityHelper> activities, ClickListener clickListener) {
        this.activities = activities;
        this.clickListener = view -> clickListener.onClick((String) view.getTag());
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_post_card, parent, false);
        ActivityViewHolder viewHolder = new ActivityViewHolder(view);
        viewHolder.seeMore.setOnClickListener(clickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityHelper model = activities.get(position);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getLocation());
        String dateAndTime = model.getDateActivity() + " " + model.getStartTimeActivity();
        holder.date.setText(dateAndTime);
        holder.seeMore.setTag(model.getKey());
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        AppCompatButton seeMore;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.activity_title);
            description = itemView.findViewById(R.id.activity_description);
            date = itemView.findViewById(R.id.activity_date);
            seeMore = itemView.findViewById(R.id.DetailButtonCV);
            seeMore.setOnClickListener(clickListener);
        }
    }

    public interface ClickListener {
        void onClick(String key);
    }
}

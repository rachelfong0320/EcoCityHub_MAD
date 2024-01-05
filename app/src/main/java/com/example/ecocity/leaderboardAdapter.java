package com.example.ecocity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class leaderboardAdapter extends RecyclerView.Adapter<leaderboardAdapter.ActivityViewHolder> {

    private List<leaderboardHelper> rankings;
    private OnItemClickListener clickListener;

    public leaderboardAdapter (ArrayList <leaderboardHelper> rankings, OnItemClickListener clickListener){
        this.rankings = rankings;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_card, parent, false);
        ActivityViewHolder viewHolder = new ActivityViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        leaderboardHelper model = rankings.get(position);
        holder.bindData(model);
    }

    public int getItemCount(){
        return rankings.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView username, point, ranking;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            point = itemView.findViewById(R.id.points);
            ranking = itemView.findViewById(R.id.ranking);

        }

        public void bindData(leaderboardHelper model) {
            username.setText(model.getUsername());
            point.setText(String.valueOf(model.getPoint()));
            ranking.setText(model.getRanking());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(model.getUsername()); // Pass the username as a key, or use another identifier
                }
            });

        }
    }


    public interface OnItemClickListener{
        void onClick(String key);
    }
}

package com.example.ecocity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> messageList;
//    private List<Message> dateList;
//    private List<Message> timeList;


    public MessageAdapter(List<Message> messages, List<Message> dates, List<Message> times) {
        this.messageList = messages;
//        this.dateList = dates;
//        this.timeList = times;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message);
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }
}


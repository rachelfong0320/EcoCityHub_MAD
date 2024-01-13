package com.example.ecocity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView message;
    TextView date;
    TextView time;


    //pass data to recycler view
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message_id);
        date= itemView.findViewById(R.id.date_picker_actions);
        time = itemView.findViewById(R.id.material_timepicker_view);
    }

    public void bind(Message message) {
        // Bind the message data to the TextViews
        // Ensure message is not null before setting text
        if (message != null) {
            this.message.setText(message.getText());
        }

        // Similarly, set date and time if available
        if (date != null) {
            date.setText(message.getDate());
        }

        if (time != null) {
            time.setText(message.getTime());
        }
    }
}


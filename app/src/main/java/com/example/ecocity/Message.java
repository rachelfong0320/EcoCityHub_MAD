package com.example.ecocity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private String senderID;
    private String text;
    private long timestamp;
    private String time;

    public Message() {
        // Required empty constructor for Firebase
    }

    public Message(String senderID, String text, long timestamp) {
        this.senderID = senderID;
        this.text = text;
        this.timestamp = timestamp;
        this.time = time;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getDate() {
        // Format the timestamp to date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }

    public String getTime() {
        // Format the timestamp to time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(new Date(timestamp));
    }
}

package com.example.ecocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chat extends AppCompatActivity {

    private String username;
    private EditText etMessage;
    private ImageView ivSend;
    private RecyclerView recyclerView;
    private TextView dateTextView;
    private TextView timeTextView;
    private String currenttime;
    private List<Message> dateList;
    private List<Message> timeList;
    private List<Message> messageList;
    private MessageAdapter adapter;
    private DatabaseReference messagesRef;
    private ValueEventListener messagesListener;
    String senderUsername;
    String receiverUsername;

    private TextView TVUsername;
    private ImageView IVReceiverMessage;
    private ImageView IVSenderMessage;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Retrieve the username passed through the Intent
        username = getIntent().getStringExtra("username");
        senderUsername = username;

        receiverUsername = getIntent().getStringExtra("receiverUsername");


        TVUsername = findViewById(R.id.TVUsername);
        IVReceiverMessage = findViewById(R.id.IVReceiverMessage);
        TVUsername.setText(receiverUsername);


        ivSend = findViewById(R.id.IVSend);
        etMessage = findViewById(R.id.etMessage);


        dateTextView = findViewById(R.id.date_picker_actions);
        timeTextView = findViewById(R.id.material_timepicker_view);

        // firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("Chat");

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null) {
                        Log.d("FirebaseData", "Message: " + message.getText());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error reading data from Firebase", databaseError.toException());
            }
        });

        // recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the messageList and adapter
        messageList = new ArrayList<>();
        dateList = new ArrayList<>();
        timeList = new ArrayList<>();

        adapter = new MessageAdapter(messageList, dateList, timeList);
        recyclerView.setAdapter(adapter);

        // Handle send message image view
        ImageView ivSend = findViewById(R.id.IVSend);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the Daily_Discover page
                sendMessage();
            }
        });

        // Handle back icon click
        ImageView backIcon = findViewById(R.id.IVArrowBack);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for data changes
        messagesListener = new ValueEventListener() {
            private boolean initialLoad = true; // Flag for initial load
            private long initialLoadTimestamp = System.currentTimeMillis(); // Timestamp of initial load

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (initialLoad) {
                    // Skip updating adapter during initial load
                    initialLoad = false;
                    return;
                }

                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message != null && message.getTimestamp() > initialLoadTimestamp) {
                        messageList.add(message);
                    }
                }

                // Log the messages
                for (Message message : messageList) {
                    Log.d("FirebaseData", "Message: " + message.getText());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        messagesRef.addValueEventListener(messagesListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (messagesListener != null) {
            messagesRef.removeEventListener(messagesListener);
        }
    }

    private void sendMessage() {
        // Retrieve the TextInputLayout
        TextInputLayout messageInputLayout = findViewById(R.id.messageInputLayout);

        // Retrieve the EditText from the TextInputLayout
        etMessage = messageInputLayout.getEditText();

        if (etMessage != null) {
            // EditText is not null, proceed with retrieving text
            String messageText = etMessage.getText().toString().trim();

            if (!TextUtils.isEmpty(messageText)) {
                // Create a Chat object with the message, sender, and timestamp
                Date date = new Date();

                // Correct the typos here
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                //currenttime = simpleDateFormat.format(new Date());

                Message message = new Message(username, messageText, date.getTime());

                // Generate a unique key for the new message
                String chatId = messagesRef.push().getKey(); // Save the generated key

                // Push the message to Firebase with the generated key
                if (chatId != null) {
                    messagesRef.child(chatId).setValue(message);
                }

                // Clear the input field
                etMessage.setText("");

                // Show a toast message indicating that the message is sent
                Toast.makeText(Chat.this, "Message Sent", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the case where the EditText is null
                Toast.makeText(Chat.this, "Enter message first", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
package com.example.ecocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInOrganizer extends AppCompatActivity {

    TextInputEditText ETUsernameEmail, ETPassword;
    AppCompatButton logInButton;
    DatabaseReference mDatabase;
    TextView createAccount, forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_organizer);

        ETUsernameEmail = findViewById(R.id.ETUsernameEmail);
        ETPassword = findViewById(R.id.ETPassword);
        logInButton = findViewById(R.id.logInButton);
        createAccount = findViewById(R.id.createAccount);
        forgot_password = findViewById(R.id.forgot_password);
        mDatabase = FirebaseDatabase.getInstance().getReference("Organizer");

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameEmail = ETUsernameEmail.getText().toString().trim();
                String password = ETPassword.getText().toString().trim();

                if(TextUtils.isEmpty(usernameEmail)){
                    ETUsernameEmail.setError("Username or email is required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    ETPassword.setError("Password is required.");
                    return;
                }

                logInOrganizer(usernameEmail,password);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInOrganizer.this, SignUpOrganizer.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void logInOrganizer(String usernameOrEmail, String password){
        DatabaseReference userRef = mDatabase.child(usernameOrEmail);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("password")){
                    String storedPassword = snapshot.child("password").getValue(String.class);
                    if (password.equals(storedPassword)){
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", usernameOrEmail);
                        editor.apply();

                        Intent intent = new Intent(LogInOrganizer.this, OrganizerMainPage.class);
                        intent.putExtra("username", usernameOrEmail);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LogInOrganizer.this, "Logging in...", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LogInOrganizer.this, "Incorrect password. Please try again. ", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LogInOrganizer.this, "No user found with this username. ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInOrganizer.this, "Error logging in. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
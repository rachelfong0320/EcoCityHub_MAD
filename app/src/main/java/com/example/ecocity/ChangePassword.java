package com.example.ecocity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {
    String username;
    TextView textView24;
    EditText editText1Password, editText2Password, editText3Password;
    DatabaseReference databaseReference;
    String currentPassword, newPassword1, newPassword2;
    Button button4;
    ImageView buttonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        textView24= findViewById(R.id.textView24);
        showData();

        editText1Password= findViewById(R.id.editText1Password);
        editText2Password= findViewById(R.id.editText2Password);
        editText3Password= findViewById(R.id.editText3Password);
        button4= findViewById(R.id.button4);
        buttonBack=findViewById(R.id.imageView28);

        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(username);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentPassword = snapshot.child("pass1").getValue(String.class);
                if (currentPassword != null) {
                    currentPassword = currentPassword.trim(); // Trim leading and trailing whitespaces
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndUpdatePassword();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showData(){
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        textView24.setText(username);
    }

    private void checkAndUpdatePassword() {
        String enteredCurrentPassword = editText1Password.getText().toString().trim();
        newPassword1 = editText2Password.getText().toString().trim();
        newPassword2 = editText3Password.getText().toString().trim();

        if (currentPassword != null && currentPassword.equals(enteredCurrentPassword)) {
            if (newPassword1.equals(newPassword2)) {
                updatePasswordInDatabase(newPassword1);
            } else {
                editText3Password.setError("Please confirm the new password.");
            }
        } else {
            editText1Password.setError("Incorrect current password.");
        }
    }

    private void updatePasswordInDatabase(String newPassword) {
        databaseReference.child("pass1").setValue(newPassword);
        currentPassword = newPassword;
        Toast.makeText(ChangePassword.this, "Password Updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

}
package com.example.ecocity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class forgetPassword extends AppCompatActivity {
    EditText editText, editText2Password, editText3Password;
    Button button4;
    FirebaseAuth fAuth;
    String strEmail;
    ImageView buttonBack;
    FirebaseDatabase database;
    DatabaseReference reference;
    String newPassword1, newPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        editText= findViewById(R.id.editText);
        button4 = findViewById(R.id.button4);
        fAuth = FirebaseAuth.getInstance();
        buttonBack = findViewById(R.id.imageView28);
        database= FirebaseDatabase.getInstance();
        reference=database.getReference("Users");
        editText2Password=findViewById(R.id.editText2Password);
        editText3Password= findViewById(R.id.editText3Password);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(strEmail)){
                    ResetPassword();
                }else{
                    editText.setError("Email field cannot be empty");
                }
            }
        });
    }

    public void ResetPassword() {
        button4.setVisibility(View.INVISIBLE);

        Query checkUserDatabase = reference.orderByChild("username").equalTo(strEmail);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UpdatePassword();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void UpdatePassword(){
        newPassword1 = editText2Password.getText().toString().trim();
        newPassword2 = editText3Password.getText().toString().trim();
        if (newPassword1.equals(newPassword2)) {
            updatePasswordInDatabase(newPassword1);
        } else {
            editText3Password.setError("Please confirm the new password.");
        }
    }

    private void updatePasswordInDatabase(String newPassword) {
        // Assuming the unique identifier for each user is the username
        final String enteredUsername = editText.getText().toString().trim();

        Query checkUserDatabase = reference.orderByChild("username").equalTo(enteredUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Assuming there's only one user with the specified username
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String userId = userSnapshot.getKey();

                    // Update the password for the specific user
                    reference.child(userId).child("pass1").setValue(newPassword);

                    Toast.makeText(forgetPassword.this, "Password Updated!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }

}
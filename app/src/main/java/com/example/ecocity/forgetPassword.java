package com.example.ecocity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {
    EditText editText;
    Button button4;
    FirebaseAuth fAuth;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        editText= findViewById(R.id.editText);
        button4 = findViewById(R.id.button4);
        fAuth = FirebaseAuth.getInstance();

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

    public void ResetPassword(){
        button4.setVisibility(View.INVISIBLE);

        fAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(forgetPassword.this, "Reset Password link has been sent to your registered email.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(forgetPassword.this, User_login.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(forgetPassword.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.ecocity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class User_login extends AppCompatActivity {
    private EditText usernameOrEmailEditText,passwordEditText;
    private ImageButton togglePasswordButton;
    private FirebaseAuth fAuth;
    private Button loginButton;
    private TextView createAcc, forgot_password;
    AppCompatButton organizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        System.out.println("c");
        usernameOrEmailEditText = findViewById(R.id.editTextUsernameOrEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        loginButton= findViewById(R.id.log_in_button);
        fAuth = FirebaseAuth.getInstance();
        createAcc = findViewById(R.id.createAcc);
        organizer = findViewById(R.id.organizer);

        organizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_login.this, LogInOrganizer.class);
                startActivity(intent);
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_login.this,Register.class);
                startActivity(intent);
            }
        });

        forgot_password= findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_login.this, forgetPassword.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameOrEmail = usernameOrEmailEditText.getText().toString().trim();
                String pass1 = passwordEditText.getText().toString().trim();

                if(TextUtils.isEmpty(usernameOrEmail)){
                    usernameOrEmailEditText.setError("Username or email is required.");
                    return;
                }

                if(TextUtils.isEmpty(pass1)){
                    passwordEditText.setError("Password is required.");
                    return;
                }

                // authenticate the user
                fAuth.signInWithEmailAndPassword(usernameOrEmail,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) { // check by Authentication
                            Toast.makeText(User_login.this, "LogIn Successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), UserProfileMain.class);
                            intent.putExtra("username",usernameOrEmail);
                            startActivity(intent);
                        } else if(!task.isSuccessful()){ // check by username
                            checkUser();
                            Toast.makeText(User_login.this, "LogIn Successfully.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(User_login.this, "Error!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });




        // Set click listener for the user_button
        AppCompatButton user_button = findViewById(R.id.user_button);
        user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the NewPageActivity when the button is clicked
                //Intent intent = new Intent(User_login.this, NewPageActivity.class);
                //startActivity(intent);
            }
        });
    }



     public void togglePasswordVisibility(View view) {
         // Toggle password visibility
         if (passwordEditText.getTransformationMethod() instanceof PasswordTransformationMethod) {
               passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
               togglePasswordButton.setImageResource(R.drawable.baseline_visibility_24); // Change to visible icon
         } else {
              passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
              togglePasswordButton.setImageResource(R.drawable.baseline_visibility_off_24); // Change to hidden icon
         }


         // Move the cursor to the end of the text
         passwordEditText.setSelection(passwordEditText.getText().length());
    }


    public void checkUser() {
        String usernameOrEmail = usernameOrEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        // Check if the input looks like an email
        if (usernameOrEmail.contains("@")) {
            // User is logging in with an email
            fAuth.signInWithEmailAndPassword(usernameOrEmail, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Query checkUserDatabase = reference.orderByChild("username").orderByChild("email").equalTo(usernameOrEmail);
                                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                                // User authenticated successfully, proceed to the next activity
                                Toast.makeText(User_login.this, "LogIn Successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(User_login.this, UserProfileMain.class);
                                startActivity(intent);
                            } else {
                                // Authentication failed, display an error message
                                Toast.makeText(User_login.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // User is logging in with a username
            Query checkUserDatabase = reference.orderByChild("username").equalTo(usernameOrEmail);

            checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // User with the given username exists in the database
                        String username = snapshot.getChildren().iterator().next().getKey();
                        String passwordFormDB = snapshot.child(username).child("pass1").getValue(String.class);

                        if (passwordFormDB != null && passwordFormDB.equals(password)) {
                            //Pass the data using intent
                            String usernameFromBB = snapshot.child(username).child("username").getValue(String.class);
                            String genderFromBB = snapshot.child(username).child("gender").getValue(String.class);
                            String contNumFromBB = snapshot.child(username).child("contNum").getValue(String.class);
                            String emailFromBB = snapshot.child(username).child("email").getValue(String.class);
                            String addressFromBB = snapshot.child(username).child("address").getValue(String.class);
                            String passFromBB = snapshot.child(username).child("pass1").getValue(String.class);
                            String dateFromDB = snapshot.child(username).child("date").getValue(String.class);

                            Intent intent = new Intent(User_login.this, UserProfileMain.class);
                            intent.putExtra("username", usernameFromBB);
                            intent.putExtra("gender", genderFromBB);
                            intent.putExtra("contNum", contNumFromBB);
                            intent.putExtra("email", emailFromBB);
                            intent.putExtra("address", addressFromBB);
                            intent.putExtra("password", passFromBB);
                            intent.putExtra("date", dateFromDB);

                            startActivity(intent);

                        } else {
                            // Password is incorrect, display an error message
                            passwordEditText.setError("Password is incorrect.");
                            passwordEditText.requestFocus();
                        }
                    } else {
                        // User with the given username does not exist in the database
                        usernameOrEmailEditText.setError("User does not exist");
                        usernameOrEmailEditText.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });
        }
    }

}

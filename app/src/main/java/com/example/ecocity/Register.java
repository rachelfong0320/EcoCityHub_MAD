package com.example.ecocity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    EditText editUsername, editTextDate,editConNum, editTextEmail, editTextAdd, editTextPass, editTextPass2;
    Button button4;
    Spinner editGender;
    FirebaseAuth fAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressBar progressBar;
    ImageView buttonBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername= findViewById(R.id.editText1);
        editGender=findViewById(R.id.editText2);
        editTextDate= findViewById(R.id.editTextDate);
        editConNum = findViewById(R.id.editText4);
        editTextEmail= findViewById(R.id.editTextEmail);
        editTextAdd = findViewById(R.id.editText);
        editTextPass= findViewById(R.id.editTextPass);
        editTextPass2 = findViewById(R.id.editTextPass2);
        button4 = findViewById(R.id.button4);
        buttonBack = findViewById(R.id.imageView28);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        //Date picker button
       editTextDate.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               final int DRAWABLE_RIGHT=2;

               if(event.getAction()== MotionEvent.ACTION_UP){
                   if(event.getRawX()>= (editTextDate.getRight()-editTextDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                       //Clicked on drawableRight
                       showDate();
                       return true;
                   }
               }
               return false;
           }
       });

       buttonBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

        //When REGISTER button is clicked
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Users");

                String username = editUsername.getText().toString().trim();
                String gender = editGender.getSelectedItem().toString().trim();
                String date = editTextDate.getText().toString().trim();
                String contNum = editConNum.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String address = editTextAdd.getText().toString().trim();
                String pass1 = editTextPass.getText().toString().trim();
                String pass2 = editTextPass2.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    editUsername.setError("Username is required.");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    editTextEmail.setError("Email is required.");
                    return;
                }

                if(TextUtils.isEmpty(pass1)){
                    editTextPass.setError("Password is required.");
                    return;
                }

                if(TextUtils.isEmpty(pass2)){
                    editTextPass2.setError("Confirm Password is required.");
                    return;
                }

                if(!pass1.equals(pass2)){
                    editTextPass.setError("Your password is not same");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                // Register the user into firebase realtime Database
                HelperClass helperClass = new HelperClass(username,gender,date,contNum, email,address,pass1, false,20);
                reference.child(username).setValue(helperClass);


                // Register the user into firebase Authorization
                fAuth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created Successfully.", Toast.LENGTH_SHORT).show();
                            Intent profileIntent = new Intent(Register.this, User_login.class);
                            profileIntent.putExtra("username", username);
                            startActivity(profileIntent);
                        }else{
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    private void showDate() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Handle the selected date
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextDate.setText(selectedDate);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }
}
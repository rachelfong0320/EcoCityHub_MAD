package com.example.ecocity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditProfile extends AppCompatActivity {
    EditText editUsername,editTextDate, editContNum, editTextEmail,editAddress;
    Button button4;
    Spinner editGender;
    String username, gender, dateOfBirth, contNum, email, address, password;
    DatabaseReference reference;
    ImageView buttonBack;

    ConstraintLayout constraintLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        reference= FirebaseDatabase.getInstance().getReference("Users");

        editUsername=findViewById(R.id.editText1);
        editGender = findViewById(R.id.editText3);
        editTextDate = findViewById(R.id.editTextDate);
        editContNum = findViewById(R.id.editText4);
        editTextEmail = findViewById(R.id.editTextEmail);
        editAddress = findViewById(R.id.editText);
        button4=findViewById(R.id.button4);
        buttonBack=findViewById(R.id.imageView28);



        showData();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean dataChanged = false;

                if (isUserNameChange()) {
                    dataChanged = true;
                }

                if (isGenderChange()) {
                    dataChanged = true;
                }

                if (isDateOfBirthChange()) {
                    dataChanged = true;
                }

                if (isEmailChange()) {
                    dataChanged = true;
                }

                if (isContNumChange()) {
                    dataChanged = true;
                }

                if (isAddressChange()) {
                    dataChanged = true;
                }

                if (dataChanged) {
                    Toast.makeText(EditProfile.this, "All your data has been saved!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfile.this, UserProfileMain.class);
                    startActivity(intent);
                    // Add any additional logic or navigation code here
                }else{
                    Toast.makeText(EditProfile.this, "Nothing has changed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


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

        constraintLayout3= findViewById(R.id.constraintLayout3);
        constraintLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, ChangePassword.class);
                intent.putExtra("username", username);
                intent.putExtra("password",password);
                startActivity(intent);

            }
        });

    }

    public boolean isUserNameChange(){
        if (!username.equals(editUsername.getText().toString())){
            reference.child(username).child("username").setValue(editUsername.getText().toString());
            username=editUsername.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    public boolean isGenderChange(){
        if (!gender.equals(editGender.getSelectedItem().toString())){
            reference.child(username).child("gender").setValue(editGender.getSelectedItem().toString());
            gender=editGender.getSelectedItem().toString();
            return true;
        }else{
            return false;
        }
    }

    public boolean isDateOfBirthChange() {
        if (!dateOfBirth.equals(editTextDate.getText().toString())) {
            reference.child(username).child("date").setValue(editTextDate.getText().toString());
            dateOfBirth = editTextDate.getText().toString();
            return true;
        } else {
            return false;
        }
    }


    public boolean isEmailChange(){
        if (!email.equals(editTextEmail.getText().toString())){
            reference.child(username).child("email").setValue(editTextEmail.getText().toString());
            email=editTextEmail.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    public boolean isContNumChange(){
        if (!contNum.equals(editContNum.getText().toString())){
            reference.child(username).child("contNum").setValue(editContNum.getText().toString());
            contNum=editContNum.getText().toString();
            return true;
        }else{
            return false;
        }
    }

    public boolean isAddressChange(){
        if (!address.equals(editAddress.getText().toString())){
            reference.child(username).child("address").setValue(editAddress.getText().toString());
            address=editAddress.getText().toString();
            return true;
        }else{
            return false;
        }
    }
    public void showData(){
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        gender = intent.getStringExtra("gender");
        dateOfBirth = intent.getStringExtra("date");
        contNum= intent.getStringExtra("contNum");
        email = intent.getStringExtra("email");
        address = intent.getStringExtra("address");
        password = intent.getStringExtra("password");


        editUsername.setText(username);
        // Set the selected item in the gender Spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editGender.setAdapter(genderAdapter);

        if (gender != null) {
            int genderPosition = genderAdapter.getPosition(gender);
            editGender.setSelection(genderPosition);
        }
        editTextDate.setText(dateOfBirth);
        editContNum.setText(contNum);
        editTextEmail.setText(email);
        editAddress.setText(address);

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
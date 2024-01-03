package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SignUpOrganizer extends AppCompatActivity {
    AutoCompleteTextView genderMenu;
    TextInputEditText usernameEditText, bioEditText, EditTextDOB, phoneEditText, emailEditText, addressEditText, passwordEditText;
    AppCompatButton OrgSignUpBT;
    FirebaseDatabase database;
    DatabaseReference reference;
    MaterialButton BTOrgSignUpBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_organizer);
        genderMenu = findViewById(R.id.genderMenu);
        usernameEditText = findViewById(R.id.usernameEditText);
        bioEditText = findViewById(R.id.bioEditText);
        EditTextDOB = findViewById(R.id.EditTextDOB);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        OrgSignUpBT = findViewById(R.id.OrgSignUpBT);
        BTOrgSignUpBack = findViewById(R.id.BTOrgSignUpBack);

        List<String> gender = Arrays.asList("Male", "Female", "Prefer not to say");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, gender);
        genderMenu.setAdapter(adapter);

        EditTextDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpOrganizer.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        EditTextDOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }}, year, month, day);
                    datePickerDialog.show();
            }
        });

        BTOrgSignUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpOrganizer.this, LogInOrganizer.class);
                startActivity(intent);
                finish();
            }
        });

        OrgSignUpBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Organizer");

                String username, bio, gender, dateOfBirth, contactNo, email, address, password;
                username = usernameEditText.getText().toString();
                bio = bioEditText.getText().toString();
                gender = genderMenu.getText().toString();
                dateOfBirth = EditTextDOB.getText().toString();
                contactNo = phoneEditText.getText().toString();
                email = emailEditText.getText().toString();
                address = addressEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(bio) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(dateOfBirth) || TextUtils.isEmpty(contactNo) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address) || TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpOrganizer.this, "Please fill in all the credentials!", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkUser(username, email, contactNo, new FirebaseOperationCallback() {
                    @Override
                    public void onSuccess(boolean isUnique) {
                        if (isUnique) {
                            // Proceed with creating the new user
                            SignUpOrganizerHelper organizerHelper = new SignUpOrganizerHelper(username, bio, gender, dateOfBirth, contactNo, email, address, password);
                            reference.child(username).setValue(organizerHelper);

                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username); //
                            editor.apply();

                            Toast.makeText(SignUpOrganizer.this, "You have successfully signed up as an Organizer!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpOrganizer.this, LogInOrganizer.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show error message that the username, email, or contact number is not unique
                            Toast.makeText(SignUpOrganizer.this, "Username, email, or contact number already exists!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void checkUser(String username, String email, String contactNo, FirebaseOperationCallback callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Organizer");
        Query checkUsername = reference.orderByChild("username").equalTo(username);
        Query checkEmail = reference.orderByChild("email").equalTo(email);
        Query checkContactNo = reference.orderByChild("contactNo").equalTo(contactNo);

        checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onSuccess(false); // Username is not unique
                } else {
                    checkEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                callback.onSuccess(false); // Email is not unique
                            } else {
                                checkContactNo.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            callback.onSuccess(false); // Contact number is not unique
                                        } else {
                                            callback.onSuccess(true); // All checks passed, data is unique
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(SignUpOrganizer.this, "Failed to check contact number availability.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SignUpOrganizer.this, "Failed to check email availability.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUpOrganizer.this, "Failed to check username availability.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
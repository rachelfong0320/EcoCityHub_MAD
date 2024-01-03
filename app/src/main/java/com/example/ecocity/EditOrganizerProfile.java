package com.example.ecocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditOrganizerProfile extends AppCompatActivity {

    AutoCompleteTextView genderMenu;
    TextInputEditText usernameEditText, bioEditText, EditTextDOB, phoneEditText, emailEditText, addressEditText, passwordEditText;
    AppCompatButton BTUpdateOrgProfile;
    MaterialButton BTEditOrgProfileBack;
    String profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_organizer_profile);
        genderMenu = findViewById(R.id.genderMenu);
        usernameEditText = findViewById(R.id.usernameEditText);
        bioEditText = findViewById(R.id.bioEditText);
        EditTextDOB = findViewById(R.id.EditTextDOB);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        BTUpdateOrgProfile = findViewById(R.id.BTUpdateOrgProfile);
        BTEditOrgProfileBack = findViewById(R.id.BTEditOrgProfileBack);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditOrganizerProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                EditTextDOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }}, year, month, day);
                datePickerDialog.show();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", "");

        BTEditOrgProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditOrganizerProfile.this, OrganizerProfile.class);
                intent.putExtra("username", currentUsername);
                startActivity(intent);
            }
        });

        if (currentUsername.isEmpty()){
            Toast.makeText(this, "No username found. Please log in again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LogInOrganizer.class);
            startActivity(intent);
            finish();
            return;
        }

        DatabaseReference organizerRef = FirebaseDatabase.getInstance().getReference("Organizer").child(currentUsername);

        ValueEventListener organizerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                EditProfileHelper organizerHelper = snapshot.getValue(EditProfileHelper.class);
                if (organizerHelper != null) {
                    genderMenu.setText(organizerHelper.getGender());
                    usernameEditText.setText(organizerHelper.getUsername());
                    bioEditText.setText(organizerHelper.getBio());
                    EditTextDOB.setText(organizerHelper.getDateOfBirth());
                    phoneEditText.setText(organizerHelper.getContactNo());
                    emailEditText.setText(organizerHelper.getEmail());
                    addressEditText.setText(organizerHelper.getAddress());
                    passwordEditText.setText(organizerHelper.getPassword());
                    profileImageUrl = organizerHelper.getProfileImageUrl();
                }
                organizerRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditOrganizerProfile.this, "Failed to load profile details.", Toast.LENGTH_SHORT).show();
            }
        };

        organizerRef.addListenerForSingleValueEvent(organizerListener);

        BTUpdateOrgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername, bio, gender, dateOfBirth, contactNo, email, address, password;
                newUsername = usernameEditText.getText().toString();
                bio = bioEditText.getText().toString();
                gender = genderMenu.getText().toString();
                dateOfBirth = EditTextDOB.getText().toString();
                contactNo = phoneEditText.getText().toString();
                email = emailEditText.getText().toString();
                address = addressEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (!currentUsername.equals(newUsername)){
                    organizerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            SignUpOrganizerHelper organizerHelper = snapshot.getValue(SignUpOrganizerHelper.class);
                            if (organizerHelper != null){
                                organizerHelper.setUsername(newUsername);
                                organizerHelper.setBio(bio);
                                organizerHelper.setGender(gender);
                                organizerHelper.setDateOfBirth(dateOfBirth);
                                organizerHelper.setContactNo(contactNo);
                                organizerHelper.setEmail(email);
                                organizerHelper.setAddress(address);
                                organizerHelper.setPassword(password);


                                DatabaseReference newUserRef = FirebaseDatabase.getInstance().getReference("Organizer").child(newUsername);
                                newUserRef.setValue(organizerHelper).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Delete the old organizer profile
                                        organizerRef.removeValue();

                                        // Update the username in SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("username", newUsername);
                                        editor.apply();

                                        // Update the username in each associated activity
                                        updateActivitiesUsername(currentUsername, newUsername, bio, gender, dateOfBirth, contactNo, email, address, password);

                                        Toast.makeText(EditOrganizerProfile.this, "Username updated successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditOrganizerProfile.this, OrganizerProfile.class);
                                        intent.putExtra("username", newUsername);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(EditOrganizerProfile.this, "Failed to update username.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(EditOrganizerProfile.this, "Failed to update username.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    updateProfileDetails(newUsername, currentUsername, bio, gender, dateOfBirth, contactNo, email, address, password, profileImageUrl);
                }
            }
        });
    }

    private void updateProfileDetails(String newUsername, String currentUsername, String bio, String gender, String dateOfBirth, String contactNo, String email, String address, String password, String profileImageUrl) {
        DatabaseReference oldOrganizerRef = FirebaseDatabase.getInstance().getReference("Organizer").child(currentUsername);
        DatabaseReference newOrganizerRef = FirebaseDatabase.getInstance().getReference("Organizer").child(newUsername);
        EditProfileHelper updatedOrganizer = new EditProfileHelper(newUsername, bio, gender, dateOfBirth, contactNo, email, address, password, profileImageUrl);
        oldOrganizerRef.removeValue(); // Remove the old organizer details
        newOrganizerRef.setValue(updatedOrganizer).addOnCompleteListener(task -> { // Save the new organizer details
            if (task.isSuccessful()) {
                Toast.makeText(EditOrganizerProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditOrganizerProfile.this, OrganizerProfile.class);
                intent.putExtra("username", newUsername);
                startActivity(intent);
            } else {
                Toast.makeText(EditOrganizerProfile.this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateActivitiesUsername(String currentUsername, String newUsername, String bio, String gender, String dateOfBirth, String contactNo, String email, String address, String password) {
        DatabaseReference oldActivitiesRef = FirebaseDatabase.getInstance().getReference("Activities").child(currentUsername);
        DatabaseReference newActivitiesRef = FirebaseDatabase.getInstance().getReference("Activities").child(newUsername);

        oldActivitiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
                    // Copy each activity to the new username
                    newActivitiesRef.child(activitySnapshot.getKey()).setValue(activitySnapshot.getValue());
                }
                // Remove the old activities
                oldActivitiesRef.removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditOrganizerProfile.this, "Failed to migrate activities.", Toast.LENGTH_SHORT).show();
            }
        });
        updateProfileDetails(newUsername, currentUsername, bio, gender, dateOfBirth, contactNo, email, address, password, profileImageUrl);
    }
}
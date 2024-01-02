package com.example.madassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    TextInputEditText titleEditText, descriptionEditText, EditTextDateActivity, EditTextStartTime, EditTextEndTime, EditTextLocation, EditTextActivityAdd, ETMinimumAge, ETMaximumAge, ETRequirements, ETContactNo, ETPoints;
    AppCompatButton BTChangeActivity;
    MaterialButton BTEditActivityBack;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        EditTextDateActivity = findViewById(R.id.EditTextDateActivity);
        EditTextStartTime = findViewById(R.id.EditTextStartTime);
        EditTextEndTime = findViewById(R.id.EditTextEndTime);
        EditTextLocation = findViewById(R.id.EditTextLocation);
        EditTextActivityAdd = findViewById(R.id.EditTextActivityAdd);
        ETMinimumAge = findViewById(R.id.ETMinimumAge);
        ETMaximumAge = findViewById(R.id.ETMaximumAge);
        ETRequirements = findViewById(R.id.ETRequirements);
        ETContactNo = findViewById(R.id.ETContactNo);
        ETPoints = findViewById(R.id.ETPoints);
        BTEditActivityBack = findViewById(R.id.BTEditActivityBack);

        BTChangeActivity = findViewById(R.id.BTChangeActivity);

        String activityKey = getIntent().getStringExtra("activityKey");

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username.isEmpty()) {
            Toast.makeText(this, "No username found. Please log in again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LogInOrganizer.class);
            startActivity(intent);
            finish();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activities").child(username).child(activityKey);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ActivityHelperClass activityHelper = snapshot.getValue(ActivityHelperClass.class);

                if (activityHelper != null){
                    titleEditText.setText(activityHelper.getTitle());
                    descriptionEditText.setText(activityHelper.getDescription());
                    EditTextDateActivity.setText(activityHelper.getDateActivity());
                    EditTextStartTime.setText(activityHelper.getStartTimeActivity());
                    EditTextEndTime.setText(activityHelper.getEndTimeActivity());
                    EditTextLocation.setText(activityHelper.getLocation());
                    EditTextActivityAdd.setText(activityHelper.getAddress());
                    ETMinimumAge.setText(activityHelper.getMinimumAge());
                    ETMaximumAge.setText(activityHelper.getMaximumAge());
                    ETRequirements.setText(activityHelper.getRequirements());
                    ETContactNo.setText(activityHelper.getContactNo());
                    ETPoints.setText(activityHelper.getPoints());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditActivity.this, "Failed to load activity details.", Toast.LENGTH_SHORT).show();
            }
        });

        BTEditActivityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, OrganizerDashboard.class);
                startActivity(intent);
            }
        });

        EditTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12){
                            amPm = "PM";
                        } else{
                            amPm = "AM";
                        }
                        EditTextStartTime.setText(String.format("%2d:%02d", hourOfDay, minute) + amPm);
                    }
                }, hour, minute, false);
                mTimePicker.show();
            }
        });

        EditTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12){
                            amPm = "PM";
                        } else{
                            amPm = "AM";
                        }
                        EditTextEndTime.setText(String.format("%2d:%02d", hourOfDay, minute) + amPm);
                    }
                }, hour, minute, false);
                mTimePicker.show();
            }
        });

        EditTextDateActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                EditTextDateActivity.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        BTChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title, description, dateActivity, startTimeActivity, endTimeActivity, locationActivity, addressActivity, minimumAge, maximumAge, requirements, contactNo, points;
                title = titleEditText.getText().toString();
                description = descriptionEditText.getText().toString();
                dateActivity = EditTextDateActivity.getText().toString();
                startTimeActivity = EditTextStartTime.getText().toString();
                endTimeActivity = EditTextEndTime.getText().toString();
                locationActivity = EditTextLocation.getText().toString();
                addressActivity = EditTextActivityAdd.getText().toString();
                minimumAge = ETMinimumAge.getText().toString();
                maximumAge = ETMaximumAge.getText().toString();
                requirements = ETRequirements.getText().toString();
                contactNo = ETContactNo.getText().toString();
                points = ETPoints.getText().toString();

                ActivityHelperClass updatedActivity = new ActivityHelperClass(
                        title, description, dateActivity, startTimeActivity, endTimeActivity,
                        locationActivity, addressActivity, minimumAge, maximumAge,
                        requirements, contactNo, points, username
                );
                reference.setValue(updatedActivity).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditActivity.this, "Activity updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditActivity.this, OrganizerDashboard.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(EditActivity.this, "Failed to update activity. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
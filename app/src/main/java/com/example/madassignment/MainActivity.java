package com.example.madassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    Spinner statusSpinner1, statusSpinner2, statusSpinner3, statusSpinner4, statusSpinner5;
//    TextInputEditText editTextDOB;
//    TextInputLayout genderContainer;
//    AutoCompleteTextView genderMenu;

    // ---------------------------------------------------------------------------------------------------------------------
//    TextInputEditText EditTextStartTime, EditTextEndTime, EditTextDateActivity;
//    MaterialButton BTNextCreate;

    Button CreateActivityBT, OrganizerProfileBT, EditOrganizerBT, BTApplicantsList, BTOrganizerDashboard, BTSignUpOrganizer, BTLogInOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateActivityBT = findViewById(R.id.CreateActivityBT);
        OrganizerProfileBT = findViewById(R.id.OrganizerProfileBT);
        EditOrganizerBT = findViewById(R.id.EditOrganizerBT);
        BTApplicantsList = findViewById(R.id.BTApplicantsList);
        BTOrganizerDashboard = findViewById(R.id.BTOrganizerDashboard);
        BTSignUpOrganizer = findViewById(R.id.BTSignUpOrganizer);
        BTLogInOrganizer = findViewById(R.id.BTLogInOrganizer);

        BTOrganizerDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrganizerDashboard.class);
                startActivity(intent);
            }
        });


        BTApplicantsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ApplicantsList.class);
                startActivity(intent);
            }
        });



        CreateActivityBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
//
        OrganizerProfileBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrganizerProfile.class);
                startActivity(intent);
            }
        });

        EditOrganizerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditOrganizerProfile.class);
                startActivity(intent);
            }
        });

        BTSignUpOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpOrganizer.class);
                startActivity(intent);
            }
        });

        BTLogInOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LogInOrganizer.class);
                startActivity(intent);
            }
        });


//        EditTextStartTime = findViewById(R.id.EditTextStartTime);
//        EditTextEndTime = findViewById(R.id.EditTextEndTime);
//        EditTextDateActivity = findViewById(R.id.EditTextDateActivity);
//        BTNextCreate = findViewById(R.id.BTNextCreate);
//
//        BTNextCreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CreateActivity2.class);
//                startActivity(intent);
//            }
//        });
//
//        EditTextStartTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar mCurrentTime = Calendar.getInstance();
//                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mCurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        EditTextStartTime.setText(hourOfDay + ":" + minute);
//                    }
//                }, hour, minute, true);
//                mTimePicker.show();
//            }
//        });
//
//        EditTextEndTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar mCurrentTime = Calendar.getInstance();
//                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mCurrentTime.get(Calendar.MINUTE);
//
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        EditTextEndTime.setText(hourOfDay + ":" + minute);
//                    }
//                }, hour, minute, true);
//                mTimePicker.show();
//            }
//        });
//
//        EditTextDateActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        MainActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        EditTextDateActivity.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
//                    }
//                },
//                        year, month, day);
//                datePickerDialog.show();
//            }
//        });
        // --------------------------------------------------------------------------------------------------------------------
//        editTextDOB = findViewById(R.id.EditTextDOB);
//        genderMenu = findViewById(R.id.genderMenu);
//        genderContainer =findViewById(R.id.genderContainer);
//
//        editTextDOB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int year = c.get(Calendar.YEAR);
//                int month = c.get(Calendar.MONTH);
//                int day = c.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        MainActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        editTextDOB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
//                    }
//                },
//                        year, month, day);
//                datePickerDialog.show();
//            }
//        });
//
//        List <String> gender = Arrays.asList("Male", "Female", "Prefer not to say");
//        ArrayAdapter <String> adapter = new ArrayAdapter<>(this, R.layout.gender_menu, gender);
//        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) genderContainer.getEditText();
//        autoCompleteTextView.setAdapter(adapter);

//        statusSpinner1 = findViewById(R.id.statusSpinner1);
//        statusSpinner2 = findViewById(R.id.statusSpinner2);
//        statusSpinner3 = findViewById(R.id.statusSpinner3);
//        statusSpinner4 = findViewById(R.id.statusSpinner4);
//        statusSpinner5 = findViewById(R.id.statusSpinner5);
//        List <String> mList = Arrays.asList("Pending", "Accepted", "Rejected");
//
//        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list, mList);
//        mArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
//
//        statusSpinner1.setAdapter(mArrayAdapter);
//        statusSpinner2.setAdapter(mArrayAdapter);
//        statusSpinner3.setAdapter(mArrayAdapter);
//        statusSpinner4.setAdapter(mArrayAdapter);
//        statusSpinner5.setAdapter(mArrayAdapter);
    }
}
package com.example.ecocity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportProb extends AppCompatActivity {

    EditText editTextTextMultiLine,editTextTextMultiLine1;
    Button button4;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView buttonBack;
    String reportKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_prob);

        editTextTextMultiLine1=findViewById(R.id.editTextTextMultiLine1);
        editTextTextMultiLine=findViewById(R.id.editTextTextMultiLine);
        button4=findViewById(R.id.button4);
        buttonBack= findViewById(R.id.imageView28);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Report");
                reportKey= reference.push().getKey();

                String problem = editTextTextMultiLine.getText().toString().trim();
                String des = editTextTextMultiLine1.getText().toString().trim();

                if(! editTextTextMultiLine.getText().toString().isEmpty()){
                    String message = "We receive your report, we'll try hard to it!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    ReportHelperClass helperClass = new ReportHelperClass(des, problem);
                    reference.child(reportKey).setValue(helperClass);
                    Intent intent = new Intent(ReportProb.this, UserProfileMain.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ReportProb.this, "Please leave your feedback here!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
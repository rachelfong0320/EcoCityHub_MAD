package com.example.ecocity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportProb extends AppCompatActivity {

    EditText editTextTextMultiLine,editTextTextMultiLine1;
    Button button4;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_prob);

        editTextTextMultiLine1=findViewById(R.id.editTextTextMultiLine1);
        editTextTextMultiLine=findViewById(R.id.editTextTextMultiLine);
        button4=findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Report");

                String problem = editTextTextMultiLine.getText().toString().trim();
                String des = editTextTextMultiLine1.getText().toString().trim();

                if(! editTextTextMultiLine.getText().toString().isEmpty()){
                    String message = "We receive your report, we'll try hard to it!";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    ReportHelperClass helperClass = new ReportHelperClass(des);
                    reference.child(problem).setValue(helperClass);
                    Intent intent = new Intent(ReportProb.this, UserProfileMain.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ReportProb.this, "Please leave your feedback here!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
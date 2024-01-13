package com.example.ecocity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ItemDescription extends AppCompatActivity {

    String username;
    String title;
    String location;
    String condition;
    String resourceUrl;
    TextView resourceItemTitle;
    TextView selectedLocation;
    TextView selectedCondition;
    ImageView resourceItem;

    TextView resourceDescription;
    String descriptionOnResource;
    TextView locationNotes;
    TextView conditionNotes;


    String notesOnLocation;

    String notesOnCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        username = getIntent().getStringExtra("username");

        resourceItemTitle = findViewById(R.id.TVItemTitle);
        title = getIntent().getStringExtra("resourceItemTitle");
        resourceItemTitle.setText(handleNull(title));

        resourceDescription = findViewById(R.id.TVItemDescriptionAnswer);
        descriptionOnResource = getIntent().getStringExtra("resourceDescription");
        resourceDescription.setText(handleNull(descriptionOnResource));

        selectedLocation = findViewById(R.id.TVLocationAnswer);
        location = getIntent().getStringExtra("location");
        selectedLocation.setText(handleNull(location));

        locationNotes = findViewById(R.id.TVNotesOnLocationAnswer);
        notesOnLocation = getIntent().getStringExtra("notesOnLocation");
        locationNotes.setText(handleNull(notesOnLocation));

        selectedCondition = findViewById(R.id.TVConditionAnswer);
        condition = getIntent().getStringExtra("condition");
        selectedCondition.setText(handleNull(condition));

        conditionNotes = findViewById(R.id.TVNotesOnConditionAnswer);
        notesOnCondition = getIntent().getStringExtra("notesOnCondition");
        conditionNotes.setText(handleNull(notesOnCondition));


        resourceItem = findViewById(R.id.IVItem);
        resourceUrl = getIntent().getStringExtra("resourceUrl");
        Glide.with(this)
                .load(resourceUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(resourceItem);

        // IVBack icon
        ImageView backIcon = findViewById(R.id.IVArrowBack);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String handleNull(String value) {
        return (value != null && !value.trim().isEmpty()) ? value : "No";
    }
}
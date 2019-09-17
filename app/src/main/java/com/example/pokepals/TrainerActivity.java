package com.example.pokepals;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class TrainerActivity extends AppCompatActivity {

    // Creating interactive/display objects
    private TextView trainerName;
    private ImageView trainerImg;
    private Button genTeamBtn;

    // Creating variable to hold genderID
    private int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_card);

        // Setting objects to appropriate items
        Intent taData = getIntent();
        final Intent taIntent = new Intent(this, TeamActivity.class);
        trainerName = findViewById(R.id.trainerName);
        trainerImg = findViewById(R.id.trainerImg);
        genTeamBtn = findViewById(R.id.genTeamBtn);

        // Getting and setting the trainer's name
        String name = taData.getStringExtra("NAME");
        trainerName.setText(name);

        // Getting the trainer's gender and sets the appropriate image on the trainer card
        gender = taData.getIntExtra("GENDER", 0);
        if (gender == 0)
            trainerImg.setImageResource(R.drawable.male_trainer);
        else
            trainerImg.setImageResource(R.drawable.female_trainer);

        // The click method will take us to the next activity which is the work horse of the whole operation
        genTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(taIntent);
            }
        });
    }
}
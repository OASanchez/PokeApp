package com.example.pokepals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    // Creating objects
    private EditText trainerName;
    private Button generateBtn;
    private RadioButton girlRb;
    private int gender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        // Setting objects to appropriate items
        trainerName = findViewById(R.id.nameEntryTxt);
        generateBtn = findViewById(R.id.generateTrainerBtn);
        girlRb = findViewById(R.id.girlRB);
        final Intent maIntent = new Intent(this, TrainerActivity.class);

        // When the button is clicked, it checks for the gender and name, if the user decides to have one
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (girlRb.isChecked())
                {
                    gender = 1;
                }
                else
                {
                    gender = 0;
                }

                // Passing intent of the gender and chosen name to a new activity
                maIntent.putExtra("GENDER", gender);
                maIntent.putExtra("NAME", trainerName.getText().toString());
                startActivity(maIntent);
            }
        });
    }
}

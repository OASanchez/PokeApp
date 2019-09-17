package com.example.pokepals;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AllPokesActivity extends AppCompatActivity {
    DatabaseHelper pokeDBHelper;
    private ListView pokeList;
    private Button rerollBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Setting the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pokes_history);

        pokeDBHelper = new DatabaseHelper(this);

        // Creating and populating the list view
        pokeList = findViewById(R.id.teamList);
        populateListView();

        // Creating our button and it's function to take us back to the team view to get a new team
        rerollBtn = findViewById(R.id.rerollBtn);
        final Intent rerollIntent = new Intent(this, TeamActivity.class);
        rerollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(rerollIntent);
            }
        });

    }

    // Populates the list with the Pokemon names of the past using a simple list item
    private void populateListView()
    {
        //Getting data and adding to list
        Cursor data = pokeDBHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext())
        {
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        pokeList.setAdapter(adapter);
    }
}


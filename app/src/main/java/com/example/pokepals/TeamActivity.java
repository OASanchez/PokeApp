package com.example.pokepals;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TeamActivity extends AppCompatActivity {

    // Variables to hold data
    private int [] pokeIntArr;
    private String [] pokeStringArr;
    private int count;
    DatabaseHelper mDatabaseHelper;

    // Actual objects from the layout
    BasicPokeFragment poke1, poke2, poke3, poke4, poke5, poke6, currentFragment;
    BasicPokeFragment[] allPokeFragments;
    Button viewAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_layout);
        // We'll need this to add our pokemon history to our database
        mDatabaseHelper = new DatabaseHelper(this);

        //  Individually creating our fragments just in case
        poke1 = (BasicPokeFragment) getSupportFragmentManager().findFragmentById(R.id.pokemon1);
        poke2 = (BasicPokeFragment) getSupportFragmentManager().findFragmentById(R.id.pokemon2);
        poke3 = (BasicPokeFragment) getSupportFragmentManager().findFragmentById(R.id.pokemon3);
        poke4 = (BasicPokeFragment) getSupportFragmentManager().findFragmentById(R.id.pokemon4);
        poke5 = (BasicPokeFragment) getSupportFragmentManager().findFragmentById(R.id.pokemon5);
        poke6 = (BasicPokeFragment) getSupportFragmentManager().findFragmentById(R.id.pokemon6);

        // Populating our fragment array
        allPokeFragments = new BasicPokeFragment[6];
        allPokeFragments[0] = poke1;
        allPokeFragments[1] = poke2;
        allPokeFragments[2] = poke3;
        allPokeFragments[3] = poke4;
        allPokeFragments[4] = poke5;
        allPokeFragments[5] = poke6;
        // Setting the current fragment to the first one so that it can be the first one updated
        currentFragment = poke1;

        // Creating the arrays which will hold the 6 random pokemon
        pokeIntArr = new int[6];
        pokeStringArr = new String[6];
        for(int i = 0; i < pokeIntArr.length; i++)
        {
            pokeIntArr[i] = (int) (Math.random() * 807) + 1;
            pokeStringArr[i] = Integer.toString(pokeIntArr[i]);
        }

        // The base URL string for the PokeAPI
        String base = "https://pokeapi.co/api/v2/pokemon/";

        // One more loop which will call the api 6 times, each time using the next pokemon to append the base URL
        // Each loop it will call the WebServiceTask with the new URL and set the base URL back to its original state
        for (int i = 0; i < pokeStringArr.length; i++) {
            URL url = null;
            base += pokeStringArr[i];
            try {
                url = new URL(base);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // Executing web services
            WebServiceTask webTask = new WebServiceTask();
            webTask.execute(url);

            base = "https://pokeapi.co/api/v2/pokemon/";
        }

        // Creating our button to take us to the next activity
        viewAllBtn = findViewById(R.id.viewAllBtn);
        final Intent viewAllIntent = new Intent(this, AllPokesActivity.class);

        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(viewAllIntent);
            }
        });
    }


    public class WebServiceTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), R.string.read_error, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                } else {
                    Toast.makeText(getApplicationContext(), R.string.connect_error, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.connect_error, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                connection.disconnect(); // close the HttpURLConnection
            }
            return null;
        }

        // process JSON response and update
        @Override
        protected void onPostExecute(JSONObject response) {
            convertJSONtoArrayList(response);
        }

        // Where the actual execution for displaying the results will be shown
        private void convertJSONtoArrayList(JSONObject response) {
            try {
                String[] m = workHorse(response);
                changeFragment(getFragment(), m[0], m[1], m[2]);
                setCount(getCount() + 1);
                if (getCount() == 6) { }
                else
                setFragment(allPokeFragments[getCount()]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // The "workhorse" method which will do all the hard work and return their results
        private String[] workHorse(JSONObject response) throws JSONException {
            // This is what will be returned and hold our three values
            String[] master = new String[3];

            // Getting the name
            String name = response.getString("name");
            master[0] = name;
            mDatabaseHelper.addData(name);

            // THIS IS HOW YOU GET THE TYPES PROPERLY
            // Getting the type array, because pokemon can have 1 or 2 types
            JSONArray typesArray = response.getJSONArray("types");
            // Getting the first object within the types array, since there will always be at least 1
            JSONObject typesObj = typesArray.getJSONObject(0);
            // The types array has slot and type{}, this actually gets the type{} object
            JSONObject actualTypeObj = typesObj.getJSONObject("type");
            // Getting the actual first type and setting it to a string
            String type1 = actualTypeObj.getString("name");

            String allTypes = type1;
            // This will check the length of the types array, if it equal to 2, then we will also get the second type, which is technically the primary type
            int arrL = typesArray.length();
            if (arrL == 2) {
                // This is the shorter way of doing the above, but can't work the first time because we have to get the array individually first
                JSONObject typesObj1 = (JSONObject) response.getJSONArray("types").get(1);
                JSONObject actualTypeObj2 = typesObj1.getJSONObject("type");
                String type2 = actualTypeObj2.getString("name");
                allTypes = type2 + " " + type1;
            }

            master[1] = allTypes;

            // Getting the sprite
            JSONObject spriteObj = response.getJSONObject("sprites");
            String spriteLink = spriteObj.getString("front_default");
            master[2] = spriteLink;

            return master;
        }

        private void changeFragment(BasicPokeFragment myPokemon, String name, String type, String spriteLink)
        {
            myPokemon.changeName(name);
            myPokemon.changeType(type);
            myPokemon.changeSprite(spriteLink);
        }
    }

    private void setFragment(BasicPokeFragment myFragment)
    {
        currentFragment = myFragment;
    }
    private BasicPokeFragment getFragment()
    {
        return currentFragment;
    }

    private void setCount(int count)
    {
     this.count = count;
    }

    private int getCount()
    {
        return count;
    }

}

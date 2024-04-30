package com.example.gcuweather.View;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gcuweather.Controller.GradientTextHelper;
import com.example.gcuweather.R;
import com.example.gcuweather.Controller.WeatherInfoExtractor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class CityWeatherDetails extends AppCompatActivity {

    private  TextView conditionTextView;
    private  TextView temperatureTextView;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @SuppressLint("WrongConstant")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                // Handle no internet connection
                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();            }
        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            // Show appropriate UI to inform user about no internet connection
            Toast.makeText(this,"No internet connection", Toast.LENGTH_LONG).show();
        }
        NetworkUtils.registerNetworkChangeReceiver(this, networkChangeReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkUtils.unregisterNetworkChangeReceiver(this, networkChangeReceiver);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather_details);

        // Retrieve data from intent
        Intent intent = getIntent();
        String location = intent.getStringExtra("City");
        String temperature = intent.getStringExtra("Temperature");
        String condition = intent.getStringExtra("Condition");
        String date= intent.getStringExtra("Date");
        String iconName = intent.getStringExtra("WeatherIcon");
        String description=intent.getStringExtra("Description");

        // Find views in the layout
        TextView locationTextView = findViewById(R.id.locationTextView);
        ImageView weatherIcon = findViewById(R.id.weatherIcon);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        Button threedayforecast=findViewById(R.id.threedayforecast);
        ImageView backIcon= findViewById(R.id.backIcon);
        TextView dateTextView=findViewById(R.id.dateTextView);
        TextView speedOfWind=findViewById(R.id.speedOfWind);
        TextView  directionOfWind=findViewById(R.id.directionOfWind);
        TextView humidText=findViewById(R.id.humidText);
        TextView pressureText=findViewById(R.id.pressureText);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigationView);


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity and go back
            }
        });

        threedayforecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threedayforecast.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.teal)));

                Intent inta=new Intent(v.getContext(), ThreedayForecast.class );
                inta.putExtra("City",location );
                inta.putExtra("Temperature", temperature);
                inta.putExtra("Condition",condition);
                inta.putExtra("IconName",iconName);
                inta.putExtra("Date",date);
                startActivity(inta);
            }
        });

        // Set data to views
        Map<String, String> weatherInfo = WeatherInfoExtractor.extractWeatherInfo(description);

        int startColor = getResources().getColor(R.color.white);
        int endColor = getResources().getColor(R.color.white_transparent);
        locationTextView.setText(location);
        temperatureTextView.setText(temperature);
        conditionTextView.setText(condition);
        dateTextView.setText(date);
        speedOfWind.setText(weatherInfo.get("Wind Speed"));
        directionOfWind.setText(weatherInfo.get("Wind Direction"));
        humidText.setText(weatherInfo.get("Humidity"));
        pressureText.setText(weatherInfo.get("Pressure"));
        GradientTextHelper.applyGradientToText(temperatureTextView, startColor, endColor);

        // Load the appropriate weather icon based on the condition
        // For now, Using a placeholder
        int iconResourceId = getResources().getIdentifier(iconName, "drawable", getPackageName());
        if (iconResourceId != 0) {
          weatherIcon.setImageResource(iconResourceId);
        } else {
            // If the resource is not found, you can set a default image or handle the case accordingly
            weatherIcon.setImageResource(R.drawable.ic_weather);
        }

        LinearLayout observations = findViewById(R.id.observations);
        registerForContextMenu(observations);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.observations_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Check if the selected item is the "Recommendations" item
        if (item.getItemId() == R.id.recommendations) {
            // Extract condition and temperature from observations
            String condition = conditionTextView.getText().toString(); // Correctly extract text from TextView
            String temperature = temperatureTextView.getText().toString(); // Correctly extract text from TextView

            // Construct the query
            if ("Not available".equals(condition)) {
                String query = "Make dressing and safety recommendations for a day at " + temperature + " temperature";
                fetchRecommendations(query);
                return true;
            } else {
                String query = "Make dressing and safety recommendations for a " + condition + " day at " + temperature + " temperature";
                fetchRecommendations(query);
                return true;
            }
        }
        // If the selected item is not the "Recommendations" item, call the superclass implementation
        return super.onContextItemSelected(item);
    }




    private String parseRecommendations(String response) {
        try {
            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);

            // Extract the recommendations from the response
            String recommendations = jsonResponse.getString("response");

            // Return the recommendations
            return "Recommendations:\n" + recommendations;
        } catch (Exception e) {
            // Handle any exceptions that occur during parsing
            e.printStackTrace();
            return "Error parsing recommendations.";
        }
    }

    private void displayRecommendations(String recommendations) {
        // Create a new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_recommendations, null);

        // Find the TextView and Button in the layout
        TextView recommendationsTextView = dialogLayout.findViewById(R.id.recommendationsTextView);
        Button closeButton = dialogLayout.findViewById(R.id.closeButton);

        // Set the recommendations text
        recommendationsTextView.setText(recommendations);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        // Set the custom view to the AlertDialog
        dialog.setView(dialogLayout);

        // Set the onClickListener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when the "Close" button is clicked
                dialog.dismiss();
            }
        });

        // Show the AlertDialog
        dialog.show();
    }
    private void fetchRecommendations(String query) {
        new Thread(() -> {
            try {
                // URL for the API request
                URL url = new URL("https://ai-content-genius-unleash-googles-language-model-power.p.rapidapi.com/google-chat");

                // Open a connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                connection.setRequestMethod("POST");

                // Set the request headers
                connection.setRequestProperty("content-type", "application/json");
                connection.setRequestProperty("X-RapidAPI-Key", "a4674df18dmshddc32ea7ed9250ap11720ejsnc8bcf2840b5f");
                connection.setRequestProperty("X-RapidAPI-Host", "ai-content-genius-unleash-googles-language-model-power.p.rapidapi.com");

                // Enable input and output streams
                connection.setDoOutput(true);
                connection.setDoInput(true);

                // Write the request body
                String jsonInputString = "{\"messages\":[{\"role\":\"user\",\"parts\":[{\"text\":\"" + query + "\"}]}]}";
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    StringBuilder response = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                        String responseLine = null;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                    }

                    // Parse the response to extract the recommendations
                    final String recommendations = parseRecommendations(response.toString());

                    // Since we're on a background thread, use runOnUiThread to update the UI
                    runOnUiThread(() -> {
                        // Display the recommendations
                        displayRecommendations(recommendations);
                    });
                } else {
                    // Handle the error response
                    runOnUiThread(() -> {
                        System.out.println("Error: " + responseCode);
                    });
                }
            } catch (Exception e) {
                // Handle any exceptions that occur during the request
                runOnUiThread(() -> {
                    e.printStackTrace();
                });
            }
        }).start();
    }

}
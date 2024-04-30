package com.example.gcuweather.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gcuweather.Controller.DownloadForecast;
import com.example.gcuweather.Model.WeatherForecast;
import com.example.gcuweather.Model.WeatherIconMapper;
import com.example.gcuweather.R;
import com.example.gcuweather.Controller.WeatherForecastAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

public class ThreedayForecast extends AppCompatActivity implements WeatherForecastAdapter.OnWeatherDayClickListener {


    private TextView locationada, temperatureTextView, dateTextView, conditionTextView;
    private ImageView backIcon, weatherIcon;
    private RecyclerView recyclerView;
    private WeatherForecastAdapter weatherForecastAdapter;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                // Handle no internet connection
                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            // Show appropriate UI to inform user about no internet connection
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
        NetworkUtils.registerNetworkChangeReceiver(this, networkChangeReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkUtils.unregisterNetworkChangeReceiver(this, networkChangeReceiver);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threeday_forecast);
        locationada = findViewById(R.id.locationada);
        weatherIcon = findViewById(R.id.iconWeather);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        dateTextView = findViewById(R.id.dateTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        Intent intent = getIntent();
        String location = intent.getStringExtra("City");
        String temperature = intent.getStringExtra("Temperature");
        String condition = intent.getStringExtra("Condition");
        String date = intent.getStringExtra("Date");
        String iconName = intent.getStringExtra("IconName");
        locationada.setText(location);
        conditionTextView.setText(condition);
        temperatureTextView.setText(temperature);
        dateTextView.setText(date);

        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity and go back
            }
        });
        int iconResourceId = getResources().getIdentifier(iconName, "drawable", getPackageName());
        if (iconResourceId != 0) {
            weatherIcon.setImageResource(iconResourceId);
        } else {
            // If the resource is not found, you can set a default image or handle the case accordingly
            weatherIcon.setImageResource(R.drawable.ic_weather);
        }
        recyclerView = findViewById(R.id.recyclerView);
        weatherForecastAdapter = new WeatherForecastAdapter(this, new ArrayList<>());
        weatherForecastAdapter.setOnWeatherDayClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Adjust the spanCount as needed
        recyclerView.setAdapter(weatherForecastAdapter);
        String cityName = locationada.getText().toString();
        ProgressBar progressBar = findViewById(R.id.progressBar);
        DownloadForecast downloadForecast = new DownloadForecast(weatherForecastAdapter,progressBar);
        downloadForecast.execute(cityName);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigationView);

    }

    @Override
    public void onWeatherDayClick(WeatherForecast.WeatherDay weatherDay) {

        if (weatherDay != null) {
            String title = weatherDay.getTitle();
            String[] titleParts = title.split(":", 2);

            String dayText;
            String conditionText;

            if (titleParts.length == 2) {
                dayText = titleParts[0].trim();
                String[] detailsParts = titleParts[1].split(",");
                if (detailsParts.length > 0) {
                    conditionText = detailsParts[0].trim();
                } else {
                    conditionText = "Unknown";
                }
            } else {
                dayText = "Unknown";
                conditionText = "Unknown";
            }
            String iconName = WeatherIconMapper.getIconName(conditionText);

            // Update the TextViews with the new data
            temperatureTextView.setText(WeatherForecastAdapter.extractAndFormatTemperatures(title));
            conditionTextView.setText(conditionText);
            dateTextView.setText(dayText);

            // If you have an ImageView to display the weather icon, you can update it here as well
            // For example, if WeatherDay has a method getIconName() that returns the name of the icon resource
            int iconResourceId = getResources().getIdentifier(iconName, "drawable", getPackageName());
            if (iconResourceId != 0) {
                weatherIcon.setImageResource(iconResourceId);
            } else {
                // If the resource is not found, you can set a default image or handle the case accordingly
                weatherIcon.setImageResource(R.drawable.ic_weather); // Replace with your default icon resource
            }
        }


    }
}
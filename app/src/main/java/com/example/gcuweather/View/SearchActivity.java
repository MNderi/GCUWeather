package com.example.gcuweather.View;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gcuweather.Controller.DownloadXmlTask;
import com.example.gcuweather.Model.WeatherData;
import com.example.gcuweather.R;
import com.example.gcuweather.Controller.WeatherAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;
    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private List<WeatherData> weatherDataList;

    private EditText searchBar;

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
        setContentView(R.layout.activity_search);

        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        weatherDataList = new ArrayList<>(); // Initialize with an empty list
        weatherAdapter = new WeatherAdapter(weatherDataList);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(weatherAdapter);

        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                weatherAdapter.filterList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Instantiate and execute DownloadXmlTask
        DownloadXmlTask downloadXmlTask = new DownloadXmlTask(weatherAdapter);
        downloadXmlTask.execute();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationHelper.setupBottomNavigation(this, bottomNavigationView);


        FrameLayout fragmentContainer = findViewById(R.id.mapContainer);

        if (fragmentContainer != null && savedInstanceState == null) {
            // Create a new instance of MapFragment
            MapFragment mapFragment = new MapFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mapContainer, mapFragment);
            transaction.commit();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backIcon) {
            goBack();
        }
    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

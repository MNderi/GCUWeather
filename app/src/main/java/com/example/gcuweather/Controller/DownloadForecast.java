package com.example.gcuweather.Controller;
// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________



import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.gcuweather.Model.CityDictionary;
import com.example.gcuweather.Model.WeatherForecast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DownloadForecast extends AsyncTask<String, Void, WeatherForecast> {

    private final String BASE_URL = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/";
    private WeatherForecastAdapter forecastAdapter;
    private ProgressBar progressBar;

    public DownloadForecast(WeatherForecastAdapter forecastAdapter, ProgressBar progressBar) {
        this.forecastAdapter = forecastAdapter;
        this.progressBar=progressBar;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE); // Show the ProgressBar
    }

    /** @noinspection deprecation*/
    @Override
    protected WeatherForecast doInBackground(String... cityNames) {
        if (cityNames.length == 0) {
            return null;
        }

        String cityName = cityNames[0];
        String areaCode = null;

        try {
            // First, try to get the area code from the CityDictionary
            areaCode = CityDictionary.getCityAreaCode(cityName);
            if (areaCode == null) {
                // If not found, fetch the area code from the API
                areaCode = CityDictionary.getGeonameIdFromAPIResponse(cityName);
                if (areaCode == null) {
                    Log.e("DownloadForecast", "City not found in dictionary and API");
                    return null;
                }
            }


            URL url = new URL(BASE_URL + areaCode);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            WeatherForecast weatherForecast = ForecastParser.parse(inputStream);

            return weatherForecast != null ? weatherForecast : null;


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DownloadForecast", "Error downloading data for " + cityName, e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(WeatherForecast result) {
        if (result != null) {
            progressBar.setVisibility(View.GONE);
            Log.d("DownloadForecast", result.toString());
            Log.d("WeatherForecast", "Title: " + result.getTitle());

            List<WeatherForecast.WeatherDay> forecastDays = result.getForecast();
            if (forecastDays != null) {
                for (WeatherForecast.WeatherDay day : forecastDays) {
                    Log.d("WeatherData", "Day: " + day.getTitle() + ", Description: " + day.getDescription());
                }
                // Update adapter data here
                forecastAdapter.setWeatherDays(forecastDays);
            } else {
                Log.e("WeatherData", "Forecast list is null");
            }

            if (forecastAdapter != null) {
                forecastAdapter.notifyDataSetChanged();
            }
        } else {
            Log.e("DownloadForecast", "Parsing failed or data is null");
        }
    }




}

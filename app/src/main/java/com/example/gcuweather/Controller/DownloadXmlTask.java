package com.example.gcuweather.Controller;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________


import android.os.AsyncTask;
import android.util.Log;

import com.example.gcuweather.Model.CityDictionary;
import com.example.gcuweather.Model.WeatherData;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** @noinspection ALL*/
public class DownloadXmlTask extends AsyncTask<Void, Void, List<WeatherData>> {

    private final String BASE_URL = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/";
    private WeatherAdapter weatherAdapter;

    public DownloadXmlTask(WeatherAdapter weatherAdapter) {
        this.weatherAdapter = weatherAdapter;
    }

    @Override
    protected List<WeatherData> doInBackground(Void... voids) {
        List<WeatherData> allWeatherData = new ArrayList<>();

        Map<String, String> cityDictionary = CityDictionary.getCityDictionary();

        for (Map.Entry<String, String> entry : cityDictionary.entrySet()) {
            String cityName = entry.getKey();
            String areaCode = entry.getValue();

            try {
                URL url = new URL(BASE_URL + areaCode);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();

                List<WeatherData> cityWeatherData = XmlParser.parse(inputStream, cityName);

                if (cityWeatherData != null) {
                    allWeatherData.addAll(cityWeatherData);
                    Log.d("DownloadXmlTask", "Downloaded weather data for " + cityName);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DownloadXmlTask", "Error downloading XML for " + cityName, e);
            }
        }

        return allWeatherData;
    }

    @Override
    protected void onPostExecute(List<WeatherData> weatherDataList) {
        // Process the combined weather data for all cities
        if (weatherDataList != null) {
            weatherAdapter.updateData(weatherDataList);

            for (WeatherData weatherData : weatherDataList) {
                Log.d("WeatherData", "Title: " + weatherData.getTitle());
                Log.d("WeatherData", "City: " + weatherData.getCityName());
                Log.d("WeatherData", "Link: " + weatherData.getLink());
                Log.d("WeatherData", "Description: " + weatherData.getDescription());
                Log.d("WeatherData", "PubDate: " + weatherData.getPubDate());
                Log.d("WeatherData", "Wind Direction: " + weatherData.getWindDirection());
                Log.d("WeatherData", "Temperature: " + weatherData.getTemperature());
                Log.d("WeatherData", "Humidity: " + weatherData.getHumidity());
                Log.d("WeatherData", "Pressure: " + weatherData.getPressure());
                Log.d("WeatherData", "Visibility: " + weatherData.getVisibility());
                Log.d("WeatherData", "--------------------------");
            }
        }
    }

    // Method to handle search queries
    public void searchWeatherData(String searchQuery) {
        new SearchTask().execute(searchQuery);
    }

    private class SearchTask extends AsyncTask<String, Void, List<WeatherData>> {

        @Override
        protected List<WeatherData> doInBackground(String... searchQueries) {
            List<WeatherData> searchWeatherData = new ArrayList<>();

            if (searchQueries.length > 0) {
                String searchQuery = searchQueries[0]; // Correctly extracting the search query
                String areaCode = null;

                try {
                    // First, try to get the area code from the CityDictionary
                    areaCode = CityDictionary.getCityAreaCode(searchQuery);
                    if (areaCode == null) {
                        // If not found, fetch the area code from the API
                        areaCode = CityDictionary.getGeonameIdFromAPIResponse(searchQuery);
                        if (areaCode == null) {
                            Log.e("DownloadXmlTask", "City not found in dictionary and API");
                            return null;
                        }
                    }

                    URL url = new URL(BASE_URL + areaCode);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();

                    List<WeatherData> cityWeatherData = XmlParser.parse(inputStream, searchQuery);

                    if (cityWeatherData != null) {
                        searchWeatherData.addAll(cityWeatherData);
                        Log.d("DownloadXmlTask", "Downloaded weather data for " + searchQuery);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("DownloadXmlTask", "Error downloading XML for " + searchQuery, e);
                }
            }

            return searchWeatherData;
        }

        @Override
        protected void onPostExecute(List<WeatherData> searchResults) {
            if (searchResults != null) {
                // Assuming you have a reference to the WeatherAdapter
                weatherAdapter.updateData(searchResults);
            }
        }
        }

    }

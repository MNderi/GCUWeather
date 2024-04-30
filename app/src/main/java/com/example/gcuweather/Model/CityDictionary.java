package com.example.gcuweather.Model;
// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CityDictionary {

    public static Map<String, String> getCityDictionary() {
        Map<String, String> cityDictionary = new HashMap<>();

        cityDictionary.put("Glasgow", "2648579");
        cityDictionary.put("London", "2643743");
        cityDictionary.put("New York", "5128581");
        cityDictionary.put("Oman", "287286");
        cityDictionary.put("Mauritius", "934154");
        cityDictionary.put("Bangladesh", "1185241");

        return cityDictionary;
    }

    public static String getCityAreaCode(String cityName) {
        Map<String, String> cityDictionary = getCityDictionary();

        // Check if the city name is in the dictionary
        if (cityDictionary.containsKey(cityName)) {
            // If the city name is found, return the area code from the dictionary
            return cityDictionary.get(cityName);
        } else {
            // If the city name is not found, return null
            return null;
        }
    }

    public static String getGeonameId(String cityName) {
        String areaCode = getCityAreaCode(cityName);
        if (areaCode != null) {
            // The city is found in the dictionary, return its area code
            return areaCode;
        } else {
            // The city is not found in the dictionary, return null
            return null;
        }
    }

    // Parse the JSON response from the API and return the Geoname ID for the specified city
    public static String getGeonameIdFromAPIResponse(String cityName) throws IOException, JSONException {
        String apiKey = "a4674df18dmshddc32ea7ed9250ap11720ejsnc8bcf2840b5f";
        Locale defaultLocale = Locale.getDefault();
        String language = defaultLocale.getLanguage();
        String baseUrl = "https://spott.p.rapidapi.com/places/autocomplete";
        String params = String.format("limit=%s&skip=%s&language=%s&q=%s&type=%s",
                "10", "0", language, cityName, "CITY");
        String url = baseUrl + "?" + params;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-RapidAPI-Key", apiKey);
        connection.setRequestProperty("X-RapidAPI-Host", "spott.p.rapidapi.com");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            Log.d("City JSON", "API response for city: " + cityName + "\n" + response.toString());

            // Parse the JSON array into a JSONArray
            JSONArray jsonArray = new JSONArray(response.toString());

            // Check if the array is not empty
            if (jsonArray.length() > 0) {
                // Get the first object from the array
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                // Extract the geonameId from the first object
                String geonameId = jsonObject.getString("geonameId");
                Log.d("City JSON", "City geoname ID retrieved from API: " + cityName + ", geonameId: " + geonameId);
                return geonameId;
            } else {
                Log.d("City JSON", "No results found for city: " + cityName);
            }
        }
        Log.e("City JSON", "Error fetching geoname ID for city: " + cityName + ", response code: " + responseCode);
        return null;
    }
}

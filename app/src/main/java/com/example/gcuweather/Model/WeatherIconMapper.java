package com.example.gcuweather.Model;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import java.util.Map;

import java.util.HashMap;
        import java.util.Map;

public class WeatherIconMapper {

    private static final Map<String, String> weatherIconMap = new HashMap<>();

    // Initialize the map with weather condition-icon mappings
    static {
        // Add mappings with variations of weather conditions and corresponding icon names
        weatherIconMap.put("clear sky", "ic_sunny");
        weatherIconMap.put("sunny", "ic_sunny");
        weatherIconMap.put("sunny intervals", "ic_sunny");
        weatherIconMap.put("sunny day", "ic_sunny");
        weatherIconMap.put("sunny and windy", "ic_sun_wind");
        weatherIconMap.put("cloud", "ic_cloudy");
        weatherIconMap.put("light cloud", "ic_cloudy");
        weatherIconMap.put("partly cloud", "ic_cloudy");
        weatherIconMap.put("mostly cloud", "ic_cloudy");
        weatherIconMap.put("light rain", "ic_rainy");
        weatherIconMap.put("heavy rain", "ic_rainy");
        weatherIconMap.put("drizzle", "ic_rainy");
        weatherIconMap.put("light rain showers", "ic_rainy");
        weatherIconMap.put("thundery showers","ic_thunder");
        weatherIconMap.put("lightning", "ic_lightning");
        weatherIconMap.put("wind", "ic_windy");
        weatherIconMap.put("fog", "ic_fog");
        weatherIconMap.put("hazy", "ic_fog");
        weatherIconMap.put("mist", "ic_fog");

    }

    // Method to get the icon name corresponding to a weather condition
    public static String getIconName(String weatherCondition) {
        String conditionLowercase = weatherCondition.toLowerCase();
        // Check if the weather condition is present in the map
        if (weatherIconMap.containsKey(conditionLowercase)) {
            // Return the corresponding icon name
            return weatherIconMap.get(conditionLowercase);
        } else {
            return "ic_weather";
        }
    }
}

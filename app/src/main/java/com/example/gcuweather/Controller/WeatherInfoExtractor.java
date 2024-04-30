package com.example.gcuweather.Controller;
// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import java.util.HashMap;
import java.util.Map;

public class WeatherInfoExtractor {

    public static Map<String, String> extractWeatherInfo(String description) {
        Map<String, String> weatherInfo = new HashMap<>();

        // Split the description into parts based on the comma delimiter
        String[] parts = description.split(",");

        // Iterate over each part to extract the required information
        for (String part : parts) {
            // Remove any leading or trailing whitespace
            part = part.trim();

            // Check if the part contains the required information
            if (part.startsWith("Temperature:")) {
                // Extract temperature value
                String temperature = part.split(":")[1].trim();
                weatherInfo.put("Temperature", temperature);
            } else if (part.startsWith("Wind Direction:")) {
                // Extract wind direction
                String windDirection = part.split(":")[1].trim();
                weatherInfo.put("Wind Direction", windDirection);
            } else if (part.startsWith("Wind Speed:")) {
                // Extract wind speed
                String windSpeed = part.split(":")[1].trim();
                weatherInfo.put("Wind Speed", windSpeed);
            } else if (part.startsWith("Humidity:")) {
                // Extract humidity
                String humidity = part.split(":")[1].trim();
                weatherInfo.put("Humidity", humidity);
            } else if (part.startsWith("Pressure:")) {
                // Extract pressure
                String pressure = part.split(":")[1].trim();
                weatherInfo.put("Pressure", pressure);
            } else if (part.startsWith("Visibility:")) {
                // Extract visibility
                String visibility = part.split(":")[1].trim();
                weatherInfo.put("Visibility", visibility);
            }
        }

        return weatherInfo;
    }

    public static void main(String[] args) {
        // Example description string
        String description = "Description: Temperature: 10°C (51°F), Wind Direction: Northerly, Wind Speed: 0mph, Humidity: 65%, Pressure: 1009mb, Rising, Visibility: Good";

        // Extract weather information
        Map<String, String> weatherInfo = extractWeatherInfo(description);

        // Print the extracted information
        for (Map.Entry<String, String> entry : weatherInfo.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}


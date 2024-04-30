package com.example.gcuweather.Model;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import java.util.ArrayList;
import java.util.List;

public class WeatherForecast {
    private String title;
    private String description;
    private List<WeatherDay> forecast;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WeatherDay> getForecast() {
        return forecast;
    }

    public void setForecast(List<WeatherDay> forecast) {
        this.forecast = forecast;
    }

    public static class WeatherDay {
        private String title;
        private String description;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "WeatherDay{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

}

package com.example.gcuweather.Model;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

public class WeatherData {
    private String title;
    private String cityName;

    private String link;
    private String description;
    private String pubDate;
    private String windDirection;
    private double temperature;
    private int humidity;
    private int pressure;
    private String visibility;

    public WeatherData() {
        // Default constructor
    }

    public WeatherData(String title, String cityName,String link, String description, String pubDate,
                       String windDirection, double temperature, int humidity,
                       int pressure, String visibility) {
        this.title = title;
        this.cityName = cityName;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.windDirection = windDirection;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "title='" + title + '\'' +
                ",cityName" +cityName+'\''+
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", windDirection='" + windDirection + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", visibility='" + visibility + '\'' +
                '}';
    }
}

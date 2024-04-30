package com.example.gcuweather.Controller;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import com.example.gcuweather.Model.WeatherData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public static List<WeatherData> parse(InputStream inputStream, String cityName) throws XmlPullParserException, IOException {
        XmlPullParser parser = org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);

        List<WeatherData> weatherDataList = new ArrayList<>();
        WeatherData currentWeatherData = null;

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("item".equals(tagName)) {
                        currentWeatherData = new WeatherData();
                        currentWeatherData.setCityName(cityName);
                    } else if (currentWeatherData != null) {
                        switch (tagName) {
                            case "title":
                                currentWeatherData.setTitle(parser.nextText());
                                break;
                            case "link":
                                currentWeatherData.setLink(parser.nextText());
                                break;
                            case "description":
                                currentWeatherData.setDescription(parser.nextText());
                                break;
                            case "pubDate":
                                currentWeatherData.setPubDate(parser.nextText());
                                break;
                            case "windDirection":
                                currentWeatherData.setWindDirection(parser.nextText());
                                break;
                            case "temperature":
                                currentWeatherData.setTemperature(Double.parseDouble(parser.nextText()));
                                break;
                            case "humidity":
                                currentWeatherData.setHumidity(Integer.parseInt(parser.nextText()));
                                break;
                            case "pressure":
                                currentWeatherData.setPressure(Integer.parseInt(parser.nextText()));
                                break;
                            case "visibility":
                                currentWeatherData.setVisibility(parser.nextText());
                                break;
                            // Add more cases for other properties if needed
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if ("item".equals(tagName) && currentWeatherData != null) {
                        weatherDataList.add(currentWeatherData);
                        currentWeatherData = null;
                    }
                    break;
            }

            eventType = parser.next();
        }

        return weatherDataList;
    }
}

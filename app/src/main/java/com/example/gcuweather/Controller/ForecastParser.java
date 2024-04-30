package com.example.gcuweather.Controller;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import com.example.gcuweather.Model.WeatherForecast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ForecastParser {
    public static WeatherForecast parse(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);

        WeatherForecast weatherForecast = new WeatherForecast();
        weatherForecast.setForecast(new ArrayList<>());

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("item".equals(tagName)) {
                        parseItem(parser, weatherForecast);
                    }
                    break;
            }

            eventType = parser.next();
        }

        return weatherForecast;
    }

    private static void parseItem(XmlPullParser parser, WeatherForecast weatherForecast) throws XmlPullParserException, IOException {
        WeatherForecast.WeatherDay currentWeatherDay = new WeatherForecast.WeatherDay();

        int eventType = parser.next();
        while (eventType != XmlPullParser.END_TAG) {
            String tagName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("title".equals(tagName)) {
                        currentWeatherDay.setTitle(parseText(parser));
                    } else if ("description".equals(tagName)) {
                        currentWeatherDay.setDescription(parseText(parser));
                    }
                    break;
            }
            eventType = parser.next();
        }

        List<WeatherForecast.WeatherDay> forecast = weatherForecast.getForecast();
        if (forecast != null) {
            forecast.add(currentWeatherDay);
        }
    }

    private static String parseText(XmlPullParser parser) throws XmlPullParserException, IOException {
        StringBuilder text = new StringBuilder();
        int eventType = parser.next();
        while (eventType != XmlPullParser.END_TAG) {
            if (eventType == XmlPullParser.TEXT) {
                text.append(parser.getText());
            }
            eventType = parser.next();
        }
        return text.toString().trim();
    }
}

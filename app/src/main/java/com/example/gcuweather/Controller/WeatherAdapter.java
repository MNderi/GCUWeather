package com.example.gcuweather.Controller;
// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gcuweather.Model.WeatherData;
import com.example.gcuweather.Model.WeatherIconMapper;
import com.example.gcuweather.R;
import com.example.gcuweather.View.CityWeatherDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<WeatherData> weatherDataList;
    private List<WeatherData> originalWeatherDataList;

    public WeatherAdapter(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
        this.originalWeatherDataList = new ArrayList<>(weatherDataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherData weatherData = weatherDataList.get(position);

        // Extract temperature from description
        String description = weatherData.getDescription();
        String temperature = extractTemperature(description);
        String title = weatherData.getTitle();
        String condition = extractCondition(title);
        String date=extractDate(weatherData.getPubDate());
        String iconName = WeatherIconMapper.getIconName(condition);
        int iconResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(iconName, "drawable", holder.itemView.getContext().getPackageName());

        // Bind data to the ViewHolder
        holder.locationTextView.setText(weatherData.getCityName());
        holder.temperatureTextView.setText(temperature);
        holder.conditionTextView.setText(condition);
        holder.dateTextView.setText(date);
        holder.weatherIcon.setImageResource(iconResourceId);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CityWeatherDetails.class);
                intent.putExtra("City", weatherData.getCityName());
                intent.putExtra("Temperature", temperature);
                intent.putExtra("Condition", condition);
                intent.putExtra("Date", date);
                intent.putExtra("Description", description);
                intent.putExtra("WeatherIcon", iconName);
                v.getContext().startActivity(intent);
            }
        });
    }


    // Method to extract temperature from description
    private String extractTemperature(String description) {
        // Define the regular expression pattern to match the temperature value
        Pattern pattern = Pattern.compile("Temperature:\\s*([0-9]+)°C");

        // Create a matcher object
        Matcher matcher = pattern.matcher(description);

        // Check if the pattern is found in the description
        if (matcher.find()) {
            // Extract the temperature value from the matched group
            return matcher.group(1)+"°C";
        }

        // Return an empty string if no match is found
        return "";
    }


    private String extractCondition(String title) {
        // Define the regex pattern to match text between colon and comma, where space is preceded by colon
        Pattern pattern = Pattern.compile(":\\s(.*?),");
        Matcher matcher = pattern.matcher(title);

        // Find the first match
        if (matcher.find()) {
            // Extract the matched text (excluding colon, space, and comma)
            String condition = matcher.group(1);
            return condition.trim(); // Trim any leading or trailing spaces
        }

        return "Unknown";
    }



    // Method to extract the day of the week and date from the PubDate string
    private String extractDate(String pubDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.ENGLISH);
        try {
            Date date = inputFormat.parse(pubDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnHoverListener {
        TextView locationTextView;
        TextView temperatureTextView;
        TextView conditionTextView;
        TextView dateTextView;
        ImageView weatherIcon;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            locationTextView = itemView.findViewById(R.id.locationTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            conditionTextView = itemView.findViewById(R.id.conditionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            weatherIcon = itemView.findViewById(R.id.weatherIcon);

            // Set the hover listener for the item view

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Change the background color of the item view when long pressed
                    Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.button_gradient);
                    itemView.setBackground(drawable);

                    // Return true to indicate that the long press event has been handled
                    return true;
                }
            });

            itemView.setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View v, MotionEvent event) {
                    // Change the background color of the item view when hovered
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_HOVER_ENTER:
                            Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.gradient_text);
                            itemView.setBackground(drawable);
                            break;

                        case MotionEvent.ACTION_HOVER_EXIT:
                            // Restore the original background color when hovering ends
                            Drawable defaultDrawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.rounded_card);
                            itemView.setBackground(defaultDrawable);
                            break;
                    }
                    return true;
                }
            });
        }


        @Override
        public boolean onHover(View v, MotionEvent event) {
            return false;
        }
    }
    public void updateData(List<WeatherData> newData) {
        weatherDataList.clear();
        weatherDataList.addAll(newData);
        notifyDataSetChanged();
    }


    public void filterList(String text) {
        List<WeatherData> filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            filteredList.addAll(originalWeatherDataList);
        } else {
            DownloadXmlTask downloadXmlTask = new DownloadXmlTask(this);
            downloadXmlTask.searchWeatherData(text);

        }
        // Update the adapter's data with the filtered list
        weatherDataList.clear();
        weatherDataList.addAll(filteredList);
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

}

package com.example.gcuweather.Controller;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gcuweather.Model.WeatherForecast;
import com.example.gcuweather.Model.WeatherIconMapper;
import com.example.gcuweather.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {

    private final List<WeatherForecast.WeatherDay> weatherDays;
    private final Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;

    private OnWeatherDayClickListener listener;
    CardView cardView;

    public void setWeatherDays(List<WeatherForecast.WeatherDay> weatherDays) {
        this.weatherDays.clear();
        this.weatherDays.addAll(weatherDays);
    }
    public WeatherForecastAdapter(Context context, List<WeatherForecast.WeatherDay> weatherDays) {
        this.context = context;
        this.weatherDays = weatherDays;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherForecast.WeatherDay currentWeatherDay = weatherDays.get(position);

        if (currentWeatherDay != null) {
            String title = currentWeatherDay.getTitle();
            String[] titleParts = title.split(":", 2);

            String dayText;
            String conditionText;

            if (titleParts.length == 2) {
                dayText = titleParts[0].trim();
                String[] detailsParts = titleParts[1].split(",");
                if (detailsParts.length > 0) {
                    conditionText = detailsParts[0].trim();
                } else {
                    conditionText = "Unknown";
                }
            } else {
                dayText = "Unknown";
                conditionText = "Unknown";
            }
            String iconName = WeatherIconMapper.getIconName(conditionText);
            int iconResourceId = holder.itemView.getContext().getResources()
                    .getIdentifier(iconName, "drawable", holder.itemView.getContext().getPackageName());
            holder.dayTextView.setText(dayText);
            holder.conditionTextView.setText(conditionText);
            holder.temperatureTextView.setText(extractAndFormatTemperatures(title));
            holder.weatherIcon.setImageResource(iconResourceId);

            // Logging
            Log.d("WeatherForecastAdapter", "Day: " + dayText);
            Log.d("WeatherForecastAdapter", "Condition: " + conditionText);
            Log.d("WeatherForecastAdapter", "Temperature: " + extractAndFormatTemperatures(title));
        }

        if (position == selectedPosition) {
            // Set the background color to yellow
            ((CardView) holder.itemView).setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavy_metal));
        } else {
            // Set the background color to the default color
            ((CardView) holder.itemView).setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onWeatherDayClick(currentWeatherDay);
                }
                // Check if another item was previously selected
                if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition != holder.getAdapterPosition()) {
                    // Reset the background color of the previously selected item
                    notifyItemChanged(selectedPosition);
                }
                // Update the selected position
                selectedPosition = holder.getAdapterPosition();
                // Change the background color of the clicked CardView to yellow
                ((CardView) holder.itemView).setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavy_metal));
            }
        });
    }
    public static String extractAndFormatTemperatures(String title) {
        String regexMin = "Minimum Temperature: (\\d+)°C";
        String regexMax = "Maximum Temperature: (\\d+)°C";

        Pattern patternMin = Pattern.compile(regexMin);
        Pattern patternMax = Pattern.compile(regexMax);

        Matcher matcherMin = patternMin.matcher(title);
        Matcher matcherMax = patternMax.matcher(title);

        String minTemperature = null;
        String maxTemperature = null;

        if (matcherMin.find()) {
            minTemperature = matcherMin.group(1);
        }

        if (matcherMax.find()) {
            maxTemperature = matcherMax.group(1);
        }

        if (minTemperature != null && maxTemperature != null) {
            return minTemperature + "/" + maxTemperature + " °C";
        } else if (minTemperature != null) {
            return minTemperature + " °C";
        } else if (maxTemperature != null) {
            return maxTemperature + " °C";
        }
        return null;
    }

    public interface OnWeatherDayClickListener {
        void onWeatherDayClick(WeatherForecast.WeatherDay weatherDay);
    }

    public void setOnWeatherDayClickListener(OnWeatherDayClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return weatherDays.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView, temperatureTextView, conditionTextView;
        ImageView weatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            conditionTextView = itemView.findViewById(R.id.conditionTextView);
            weatherIcon= itemView.findViewById(R.id.weatherIcon);
        }
    }
}

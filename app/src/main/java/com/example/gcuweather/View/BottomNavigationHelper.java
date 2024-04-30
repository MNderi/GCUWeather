package com.example.gcuweather.View;
// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

// BottomNavigationHelper.java
import android.app.Activity;
import android.content.Intent;

import com.example.gcuweather.R;
import com.example.gcuweather.View.MainActivity;
import com.example.gcuweather.View.SearchActivity;
import com.example.gcuweather.View.Settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHelper {

    public static void setupBottomNavigation(Activity activity, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                navigateToHome(activity);
                return true;
            } else if (itemId == R.id.navigation_settings) {
                navigateToSettings(activity);
                return true;
            } else if (itemId == R.id.navigation_more_info) {
                navigateToMoreInfo(activity);
                return true;
            }
            return false;
        });
    }


    private static void navigateToHome(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class); // Replace HomeActivity with your actual home activity
        activity.startActivity(intent);
    }

    private static void navigateToSettings(Activity activity) {
        Intent intent = new Intent(activity, Settings.class);
        activity.startActivity(intent);
    }

    private static void navigateToMoreInfo(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }
}



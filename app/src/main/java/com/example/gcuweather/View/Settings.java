
package com.example.gcuweather.View;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.gcuweather.R;

import java.util.Locale;

public class Settings extends AppCompatActivity {
    // Define keys for SharedPreferences

    private ImageView backIcon;
    private static final String PREF_NAME = "TranslationPrefs";
    private static final String LANGUAGE_KEY = "Language";
    private static final String THEME_KEY = "Theme";
    private boolean languageChanged = false;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                // Handle no internet connection
                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            // Show appropriate UI to inform user about no internet connection
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
        NetworkUtils.registerNetworkChangeReceiver(this, networkChangeReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkUtils.unregisterNetworkChangeReceiver(this, networkChangeReceiver);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        backIcon=findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity and go back
            }
        });
        // Initialize language spinner
        Spinner languageSpinner = findViewById(R.id.languageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Retrieve and set saved language preference
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedLanguage = prefs.getString(LANGUAGE_KEY, "");
        if (!savedLanguage.isEmpty()) {
            int spinnerPosition = adapter.getPosition(savedLanguage);
            languageSpinner.setSelection(spinnerPosition);
        }
        // Listen for language selection changes
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedLanguage = parentView.getItemAtPosition(position).toString();
                String currentLanguage = getCurrentLanguage();

                if (!selectedLanguage.equals(currentLanguage)) {
                    showLanguageChangeDialog(selectedLanguage);
                    languageChanged = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        Switch themeSwitch = findViewById(R.id.themeSwitch);
        String savedTheme = prefs.getString(THEME_KEY, "light"); // Default to light theme
        themeSwitch.setChecked("dark".equals(savedTheme)); // Set the switch state based on the saved preference
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = prefs.edit();
                if (isChecked) {
                    // Switch to dark theme
                    editor.putString(THEME_KEY, "dark");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Switch to light theme
                    editor.putString(THEME_KEY, "light");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                editor.apply();

                // Recreate the activity to apply the theme change
                recreate();
            }
        });


    }
    private void showLanguageChangeDialog(final String selectedLanguage) {
        if (languageChanged) { // Check if the language has been changed
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.change_language_title); // Use string resource for title
            builder.setMessage(getString(R.string.change_language_message, selectedLanguage)); // Use string resource for message

            // Set positive button with custom background color and text from string resource
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Proceed with language change logic
                    saveLanguagePreference(selectedLanguage);
                    String languageCode = getLanguageCode(selectedLanguage);
                    updateAppLanguage(languageCode);
                    Log.d("LanguageChange", "Language changed to: " + languageCode);
                    // Reset the flag after showing the dialog
                    Intent intent = new Intent(Settings.this, MainActivity.class); // Replace MainActivity.class with your app's main activity class
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    languageChanged = false;
                }
            });

            // Set negative button with custom background color and text from string resource
            builder.setNegativeButton(R.string.no, null);

            AlertDialog dialog = builder.create();

            // Set background color for positive button
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setBackgroundColor(getResources().getColor(R.color.bright_teal));
                }
            });

            // Show the dialog
            dialog.show();
        }
    }



    private void saveLanguagePreference(String language) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(LANGUAGE_KEY, language);
        editor.apply();
    }
    private String getCurrentLanguage() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPreferences.getString(LANGUAGE_KEY, ""); // Returns the currently saved language preference
    }

    private String getLanguageCode(String languageName) {
        switch (languageName) {
            // English
            case "Anglais": // French for English
            case "English": // English
            case "англиски":
            case "anglisht":
                return "en"; // Language: English (comment in English)

            // French
            case "Français": // French
            case "French": // English
            case "француски":
            case "francés":
                return "fr"; // Language: French (comment in English)

            // Bengali
            case "Bengali": // English
            case "bengali": // French
            case "Bengalí": // Albanian
            case "Бенгалски": // Macedonian
                return "bn"; // Language: Bengali (comment in English)

            // Macedonian
            case "Македонски": // Macedonian
            case "Macedonian": // English
            case "Macédonien": // French for Macedonian
            case "Maqedonas":// Macedonian
                return "mk"; // Language: Macedonian (comment in English)

            // Albanian
            case "Shqiptar": // Albanian
            case "Albanian": // English
            case "албански": // Macedonian for Albanian
                return "sq"; // Language: Albanian (comment in English)

            // Add more cases for other supported languages

            default:
                Log.e("LanguageChange", "Unsupported language: " + languageName);
                return "en"; // Default to English if the language is not supported
        }
    }



    private void updateAppLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Log.d("LanguageChange", "Language changed to: " + languageCode);

    }





}

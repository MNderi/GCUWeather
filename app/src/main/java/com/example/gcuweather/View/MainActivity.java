package com.example.gcuweather.View;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________



import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.gcuweather.R;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button onboardButton;

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
        setContentView(R.layout.activity_main);

        View rootView = findViewById(android.R.id.content);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade);
        fadeIn.setRepeatCount(0);
        rootView.startAnimation(fadeIn);


        startButton = findViewById(R.id.getStartedButton);
        onboardButton= findViewById(R.id.onboardButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity();
            }
        });
        onboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOnboardActivity();
            }
        });

        VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg);
        videoView.setVideoURI(videoUri);

        // Set the video to loop
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        // Start the video
        videoView.start();
    }

    private void openSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }
    private void openOnboardActivity(){
        Intent intent=new Intent(MainActivity.this, OnboardingActivity.class);
        startActivity(intent);
    }
}


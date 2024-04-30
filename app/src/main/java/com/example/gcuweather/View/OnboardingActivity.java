package com.example.gcuweather.View;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gcuweather.Controller.OnboardingPagerAdapter;
import com.example.gcuweather.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<String> mOnboardingSteps;
    private OnboardingPagerAdapter mAdapter;
    private GestureDetector mGestureDetector;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                // Handle no internet connection
                Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mViewPager = findViewById(R.id.viewPager);
        mOnboardingSteps = new ArrayList<>();
        mOnboardingSteps.add(getString(R.string.onboarding_step1));
        mOnboardingSteps.add(getString(R.string.onboarding_step2));
        mOnboardingSteps.add(getString(R.string.onboarding_step3));
        mOnboardingSteps.add(getString(R.string.onboarding_step4));

        mAdapter = new OnboardingPagerAdapter(this, mOnboardingSteps, mViewPager);
        mViewPager.setAdapter(mAdapter);

        // Find the ImageView
        ImageView swipeIcon = findViewById(R.id.swipeIcon);
        // Load the animation
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);

        mGestureDetector = new GestureDetector(this, new SwipeGestureListener());

        // Set onTouchListener for swipe gesture detection
        swipeIcon.setOnTouchListener(new View.OnTouchListener() {
            private float startX; // Initial touch X position
            private float moveX; // X position while moving

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Save the initial touch X position
                        startX = event.getRawX();
                        // Animation when touched
                        swipeIcon.startAnimation(pulseAnimation);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // Calculate the X position change while moving
                        moveX = event.getRawX();
                        float deltaX = moveX - startX;
                        // Implement movement animation here if desired
                        // For example, translate the icon based on deltaX
                        swipeIcon.setTranslationX(deltaX);
                        break;
                    case MotionEvent.ACTION_UP:
                        // Animation when touch released
                        swipeIcon.setTranslationX(0); // Reset translation
                        break;
                }
                return mGestureDetector.onTouchEvent(event);
            }
        });


        swipeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Advance to the next page
                int nextPage = mViewPager.getCurrentItem() + 1;
                if (nextPage < mAdapter.getCount()) {
                    mViewPager.setCurrentItem(nextPage, true);
                } else {
                    // If already on the last page, proceed to the next activity
                    Intent intent = new Intent(OnboardingActivity.this, SearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button onboardButton = findViewById(R.id.onboardButton);
        onboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proceed to the next activity
                Intent intent = new Intent(OnboardingActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start auto-swiping when activity is resumed
        mAdapter.startAutoSwipe();
        if (!NetworkUtils.isNetworkAvailable(this)) {
            // Show appropriate UI to inform user about no internet connection
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
        NetworkUtils.registerNetworkChangeReceiver(this, networkChangeReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop auto-swiping when activity is paused
        mAdapter.stopAutoSwipe();
        NetworkUtils.unregisterNetworkChangeReceiver(this, networkChangeReceiver);

    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Detect left to right swipe
            if (e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 100) {
                int nextPage = mViewPager.getCurrentItem() + 1;
                if (nextPage < mAdapter.getCount()) {
                    mViewPager.setCurrentItem(nextPage, true);
                    return true;
                }
            }
            // Detect right to left swipe
            else if (e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 100) {
                int previousPage = mViewPager.getCurrentItem() - 1;
                if (previousPage >= 0) {
                    mViewPager.setCurrentItem(previousPage, true);
                    return true;
                }
            }
            return false;
        }
    }
}


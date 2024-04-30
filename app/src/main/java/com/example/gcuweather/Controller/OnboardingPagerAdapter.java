package com.example.gcuweather.Controller;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.gcuweather.R;

import java.util.List;

public class OnboardingPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mOnboardingSteps;
    private ViewPager mViewPager;
    private Handler mHandler;
    private Runnable mRunnable;
    private static final long DELAY_MS = 6000; // Change delay as needed
    private static final long PERIOD_MS = 6000; // Change period as needed

    public OnboardingPagerAdapter(Context context, List<String> onboardingSteps, ViewPager viewPager) {
        mContext = context;
        mOnboardingSteps = onboardingSteps;
        mViewPager = viewPager;
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem < getCount() - 1) {
                    mViewPager.setCurrentItem(currentItem + 1, true);
                } else {
                    mViewPager.setCurrentItem(0, true);
                }
                mHandler.postDelayed(this, PERIOD_MS);
            }
        };
    }

    @Override
    public int getCount() {
        return mOnboardingSteps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.onboard_page, container, false);
        TextView textView = view.findViewById(R.id.onboardingText);
        textView.setText(mOnboardingSteps.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void startAutoSwipe() {
        mHandler.postDelayed(mRunnable, DELAY_MS);
    }

    public void stopAutoSwipe() {
        mHandler.removeCallbacks(mRunnable);
    }
}




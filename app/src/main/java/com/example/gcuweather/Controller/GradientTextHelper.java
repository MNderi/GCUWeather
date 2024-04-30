package com.example.gcuweather.Controller;

// Name                 _______Marthar Nderitu__________
// Student ID           _______S2110914__________
// Programme of Study   ___Computing______________

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.TextView;

public class GradientTextHelper {

    public static void applyGradientToText(TextView textView, int startColor, int endColor) {
        String text = textView.getText().toString();

        if (TextUtils.isEmpty(text)) {
            return;
        }

        // Create a LinearGradient
        Shader shader = new LinearGradient(0, 0, textView.getMeasuredWidth(), 0,
                startColor, endColor, Shader.TileMode.CLAMP);

        // Apply the gradient color to the text
        TextPaint paint = textView.getPaint();
        paint.setShader(shader);

        // Ensure that the text is redrawn to reflect the changes
        textView.invalidate();
    }
}



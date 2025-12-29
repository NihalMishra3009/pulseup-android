package com.example.martyapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class Splashpulse extends Activity {

    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pulsescren);


        // Load slide-out animation for splash screen exit
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splashpulse.this, dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, 2500); // 2.5 seconds delay

        TextView textView = findViewById(R.id.textView2);
        String text = "By continuing you agree that you have read and accepted our T&Cs and Privacy Policy";

        // Create a SpannableString
        SpannableString spannableString = new SpannableString(text);

// Set color for 'T&Cs'
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#175F75")),
                text.indexOf("T&Cs"),
                text.indexOf("T&Cs") + 4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set color for 'Privacy Policy'
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#175F75")),
                text.indexOf("Privacy Policy"),
                text.indexOf("Privacy Policy") + 14,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Apply black color to other text
        textView.setText(spannableString);
        textView.setTextColor(Color.parseColor("#979797"));
    }
}

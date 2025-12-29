package com.example.martyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ProgressBar;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class gw_da_j extends AppCompatActivity {

    private TextView daayy;
    private ProgressBar pb_daayy;

    private TextView startex1, startex2, startex3, startex4, startex5, startex6;

    private TextView wp1, wp2, wp3, wp4, wp5, wp6;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gw_da);

        daayy = findViewById(R.id.daayy);
        pb_daayy = findViewById(R.id.pb_daayy);

        // Start loading process
        pb_daayy.setVisibility(View.VISIBLE);
        daayy.setVisibility(View.INVISIBLE);

        // Get current day name
        String currentDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
        String displayText = "Today is " + currentDay + ", follow these exercises!";

        // Simulate a loading delay (optional)
        daayy.postDelayed(() -> {
            daayy.setText(displayText);
            pb_daayy.setVisibility(View.GONE);
            daayy.setVisibility(View.VISIBLE);
        }, 1000); // Delay for 1 second


        startex1 = findViewById(R.id.startex1);
        startex2 = findViewById(R.id.startex2);
        startex3 = findViewById(R.id.startex3);
        startex4 = findViewById(R.id.startex4);
        startex5 = findViewById(R.id.startex5);
        startex6 = findViewById(R.id.startex6);

        wp1 = findViewById(R.id.wp1);
        wp2 = findViewById(R.id.wp2);
        wp3 = findViewById(R.id.wp3);
        wp4 = findViewById(R.id.wp4);
        wp5 = findViewById(R.id.wp5);
        wp6 = findViewById(R.id.wp6);


        wp1.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Preview of exercise!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, pre_botharmdumblerow_j.class));
        });

        startex1.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Exercise started!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, botharmdumblerow_j.class));
        });

        wp2.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Preview of exercise!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, pre_chinups_j.class));
        });

        startex2.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Exercise started!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, chinups_j.class));
        });

        wp3.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Preview of exercise!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, pre_dumblelateralraises_j.class));
        });

        startex3.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Exercise started!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, dumblelateralraises_j.class));
        });

        wp4.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Preview of exercise!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, pre_dumbleoverheadsquats_j.class));
        });

        startex4.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Exercise started!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, dumbleoverheadsquats_j.class));
        });

        wp5.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Preview of exercise!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, pre_dumblechestpress_j.class));
        });

        startex5.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Exercise started!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, dumblechestpress_j.class));
        });

        wp6.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Preview of exercise!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, pre_overheadtriceppushdown_j.class));
        });

        startex6.setOnClickListener(view -> {
            Toast.makeText(gw_da_j.this, "Exercise started!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(gw_da_j.this, overheadtriceppushdown_j.class));
        });


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D"));
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        // Set dark navigation bar icons
        setDarkNavigationIcons(window);

        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle Back Button Click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    @SuppressLint("ObsoleteSdkInt")
    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

}

package com.example.martyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class stepcounting extends AppCompatActivity implements SensorEventListener {

    private TextView tvSteps;
    private Button btnStopResume, btnReset;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private boolean isCounting = true; // Step counter is active by default
    private int stepCount = 0;
    private int stepOffset = 0; // Offset to handle reset

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepcounting);

        tvSteps = findViewById(R.id.tv_steps);
        btnStopResume = findViewById(R.id.btn_stop_resume);
        btnReset = findViewById(R.id.btn_reset);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Check if the device has a step counter sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        } else {
            tvSteps.setText("Step Sensor Not Available!");
            return;
        }

        // Register listener for step counter
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);

        // Toggle Stop/Resume functionality
        btnStopResume.setOnClickListener(v -> {
            if (isCounting) {
                // Stop counting steps
                sensorManager.unregisterListener(stepcounting.this, stepSensor);
                btnStopResume.setText("Resume");
            } else {
                // Resume counting steps
                sensorManager.registerListener(stepcounting.this, stepSensor, SensorManager.SENSOR_DELAY_UI);
                btnStopResume.setText("Stop");
            }
            isCounting = !isCounting; // Toggle state
        });

        // Reset button functionality
        btnReset.setOnClickListener(v -> {
            stepOffset = 0;  // Reset offset
            stepCount = 0;
            tvSteps.setText(String.valueOf(stepCount));

        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (stepOffset == 0) {
                stepOffset = (int) event.values[0];  // Set offset on first detection
            }

            if (isCounting) {
                stepCount = (int) event.values[0] - stepOffset;  // Adjust for reset
                tvSteps.setText(String.valueOf(stepCount));

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

}

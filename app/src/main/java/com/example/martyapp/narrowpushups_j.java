package com.example.martyapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class narrowpushups_j extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView, stepHint;
    private TextView nextButton, prevButton;
    private int currentStep = 0;

    // Steps Data
    private int[] stepImages = {R.drawable.narrowpushups_s1, R.drawable.narrowpushups_s2, R.drawable.narrowpushups_s3};
    private String[] stepDescriptions = {
            "Lift your arms up straight.",
            "Bend your knees slightly.",
            "Return to the starting position."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.narrowpushups);

        // Toolbar Setup
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

        // Find Views
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        stepHint = findViewById(R.id.stepHint);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);

        // Next Button Click Listener
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep < stepImages.length - 1) {
                    currentStep++;
                    updateStep();
                }
            }
        });

        // Previous Button Click Listener
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStep > 0) {
                    currentStep--;
                    updateStep();
                }
            }
        });

        // Initialize UI
        updateStep();
    }

    // ✅ Updated Part
    private void updateStep() {
        imageView.setImageResource(stepImages[currentStep]);
        textView.setText(stepDescriptions[currentStep]);

        // Updated to show "Step X/Y"
        stepHint.setText("Step " + (currentStep + 1) + "/" + stepImages.length);

        // Show/Hide Buttons Based on Step Position
        if (currentStep == 0) {
            prevButton.setVisibility(View.GONE);  // Hide Previous Button on Step 1
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }

        if (currentStep == stepImages.length - 1) {
            nextButton.setVisibility(View.GONE);  // Hide Next Button on Last Step
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}

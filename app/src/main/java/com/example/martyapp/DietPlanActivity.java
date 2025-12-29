package com.example.martyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DietPlanActivity extends AppCompatActivity {

    private Button btnShred, btnBulk;
    private TextView[] mealButtons = new TextView[7];
    private SharedPreferences sharedPreferences;
    private String currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle Back Button Click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to previous activity
            }
        });


        // Status bar ko wapas laane ka code
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences("DietPrefs", MODE_PRIVATE);
        currentMode = sharedPreferences.getString("selectedMode", "shred"); // Default to shred mode

        // Initialize mode switch buttons
        btnShred = findViewById(R.id.btnShred);
        btnBulk = findViewById(R.id.btnBulk);

        // Initialize meal buttons
        mealButtons[0] = findViewById(R.id.but1);
        mealButtons[1] = findViewById(R.id.but2);
        mealButtons[2] = findViewById(R.id.but3);
        mealButtons[3] = findViewById(R.id.but4);
        mealButtons[4] = findViewById(R.id.but5);
        mealButtons[5] = findViewById(R.id.but6);
        mealButtons[6] = findViewById(R.id.but7);

        updateMealButtons(); // Set initial button IDs and text

        // Handle mode switching
        btnShred.setOnClickListener(view -> switchMode("shred"));
        btnBulk.setOnClickListener(view -> switchMode("bulk"));

        // Set click listeners for meal buttons
        for (int i = 0; i < mealButtons.length; i++) {
            final int mealIndex = i + 1;
            mealButtons[i].setOnClickListener(view -> openMealPage(mealIndex));
        }
    }

    private void switchMode(String mode) {
        currentMode = mode;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedMode", mode);
        editor.apply();
        updateMealButtons();
    }

    private void updateMealButtons() {
        // Update mode button colors
        if (currentMode.equals("shred")) {
            btnShred.setBackgroundColor(Color.parseColor("#175F75")); // Selected mode color
            btnShred.setTextColor(Color.WHITE);
            btnBulk.setBackgroundColor(Color.parseColor("#BCE2EF")); // Unselected mode color
            btnBulk.setTextColor(Color.parseColor("#175F75"));
        } else {
            btnBulk.setBackgroundColor(Color.parseColor("#175F75"));
            btnBulk.setTextColor(Color.WHITE);
            btnShred.setBackgroundColor(Color.parseColor("#BCE2EF"));
            btnShred.setTextColor(Color.parseColor("#175F75"));
        }

        // Update meal button texts
        for (int i = 0; i < mealButtons.length; i++) {
            String buttonText = "Meal " + (i + 1);
            mealButtons[i].setText(buttonText);
        }

        // Update meal info TextViews based on current mode
        TextView tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        TextView tv3 = findViewById(R.id.tv3);
        TextView tv4 = findViewById(R.id.tv4);
        TextView tv5 = findViewById(R.id.tv5);
        TextView tv6 = findViewById(R.id.tv6);
        TextView tv7 = findViewById(R.id.tv7);

        if (currentMode.equals("shred")) {
            tv1.setText("A healthy meal packed with high protein, low carbs, and moderate fat to keep you full and energized all morning.");
            tv2.setText("A light snack with very few carbs to keep you going without slowing you down.");
            tv3.setText("A balanced meal with lean protein and fiber-rich veggies to keep you full and energized.");
            tv4.setText("A small bite with some carbs and protein to fuel your workout.");
            tv5.setText("A high-protein meal with very few carbs to help your muscles recover.");
            tv6.setText("A low-carb, high-protein meal to end your day on a healthy note.");
            tv7.setText("A light protein snack that digests slowly and supports overnight recovery.");
        } else {
            tv1.setText("A protein-rich meal to kickstart your day with energy");
            tv2.setText("A small boost of protein and healthy nuts to keep you going.");
            tv3.setText("A filling mix of lean protein, good fats, and complex carbs for lasting energy.");
            tv4.setText("A quick bite with energy-boosting carbs and some protein.");
            tv5.setText("Fast-digesting protein and carbs to help your muscles recover.");
            tv6.setText("A balanced meal with protein, some carbs, and healthy fats to end the day right.");
            tv7.setText("Light protein that digests slowly to support your body while you sleep.");
        }
    }

    private void openMealPage(int mealNumber) {
        Class<?> targetActivity;

        if (currentMode.equals("shred")) {
            switch (mealNumber) {
                case 1:
                    targetActivity = Meal1ShredActivity.class;
                    break;
                case 2:
                    targetActivity = Meal2ShredActivity.class;
                    break;
                case 3:
                    targetActivity = Meal3ShredActivity.class;
                    break;
                case 4:
                    targetActivity = Meal4ShredActivity.class;
                    break;
                case 5:
                    targetActivity = Meal5ShredActivity.class;
                    break;
                case 6:
                    targetActivity = Meal6ShredActivity.class;
                    break;
                case 7:
                    targetActivity = Meal7ShredActivity.class;
                    break;
                default:
                    return;
            }
        } else { // Bulk Mode
            switch (mealNumber) {
                case 1:
                    targetActivity = Meal1BulkActivity.class;
                    break;
                case 2:
                    targetActivity = Meal2BulkActivity.class;
                    break;
                case 3:
                    targetActivity = Meal3BulkActivity.class;
                    break;
                case 4:
                    targetActivity = Meal4BulkActivity.class;
                    break;
                case 5:
                    targetActivity = Meal5BulkActivity.class;
                    break;
                case 6:
                    targetActivity = Meal6BulkActivity.class;
                    break;
                case 7:
                    targetActivity = Meal7BulkActivity.class;
                    break;
                default:
                    return;
            }
        }

        Intent intent = new Intent(DietPlanActivity.this, targetActivity);
        startActivity(intent);
    }
    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}

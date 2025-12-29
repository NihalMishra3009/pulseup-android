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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DietSelectionActivity extends AppCompatActivity {

    private Button btnShredMode, btnBulkMode;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("DietPrefs", MODE_PRIVATE);
        setContentView(R.layout.activity_diet_selection);

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


        // Check if mode is already selected
        if (sharedPreferences.contains("selectedMode")) {
            openDietPlan();
            return;
        }



        btnShredMode = findViewById(R.id.btnShredMode);
        btnBulkMode = findViewById(R.id.btnBulkMode);

        btnShredMode.setOnClickListener(view -> saveModeAndProceed("shred"));
        btnBulkMode.setOnClickListener(view -> saveModeAndProceed("bulk"));
    }

    private void saveModeAndProceed(String mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedMode", mode);
        editor.apply();
        openDietPlan();
    }

    private void openDietPlan() {
        Intent intent = new Intent(DietSelectionActivity.this, DietPlanActivity.class);
        startActivity(intent);
        finish();
    }
private void setDarkNavigationIcons(Window window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }
}
}

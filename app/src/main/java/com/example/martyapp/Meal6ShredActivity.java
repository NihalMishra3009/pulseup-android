package com.example.martyapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Meal6ShredActivity extends AppCompatActivity {

    private Button btnProteins, btnVegetables, btnHealthyfats;
    private ImageView imgMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meal6_shred);

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

        btnProteins = findViewById(R.id.btnProteins);
        btnVegetables = findViewById(R.id.btnVegetables);
        btnHealthyfats = findViewById(R.id.btnHealthyfats);
        imgMeal = findViewById(R.id.imgMeal);

        // Set default selection (Proteins)
        selectCategory("proteins");

        // Set click listeners for category buttons
        btnProteins.setOnClickListener(view -> selectCategory("proteins"));
        btnVegetables.setOnClickListener(view -> selectCategory("vegetables"));
        btnHealthyfats.setOnClickListener(view -> selectCategory("healthyfats"));
    }

    private void selectCategory(String category) {
        // Change image based on category
        switch (category) {
            case "proteins":
                imgMeal.setImageResource(R.drawable.sm_dinner_protein);
                setButtonColors(btnProteins);
                break;
            case "vegetables":
                imgMeal.setImageResource(R.drawable.sm_dinner_vegetables);
                setButtonColors(btnVegetables);
                break;
            case "healthyfats":
                imgMeal.setImageResource(R.drawable.sm_dinner_healthyfat);
                setButtonColors(btnHealthyfats);
                break;
        }
    }

    private void setButtonColors(Button selectedButton) {
        // Reset all buttons to default color
        btnProteins.setBackgroundColor(getResources().getColor(R.color.tott));
        btnProteins.setTextColor(getResources().getColor(R.color.tett));
        btnVegetables.setBackgroundColor(getResources().getColor(R.color.tott));
        btnVegetables.setTextColor(getResources().getColor(R.color.tett));
        btnHealthyfats.setBackgroundColor(getResources().getColor(R.color.tott));
        btnHealthyfats.setTextColor(getResources().getColor(R.color.tett));

        // Highlight the selected button
        selectedButton.setBackgroundColor(getResources().getColor(R.color.tett));
        selectedButton.setTextColor(getResources().getColor(R.color.white));
    }
    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}

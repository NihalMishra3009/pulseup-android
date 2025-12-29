package com.example.martyapp;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class mainten_calorie_j extends AppCompatActivity {

    private EditText etAge, etWeight, etHeight;
    private RadioGroup rgGender;
    private Spinner spActivityLevel;
    private TextView tvResult;
    private Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainten_calorie);

        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        rgGender = findViewById(R.id.rgGender);
        spActivityLevel = findViewById(R.id.spActivityLevel);
        tvResult = findViewById(R.id.tvResult);
        btnCalculate = findViewById(R.id.btnCalculate);

        // ✅ Fix Spinner Text Color, Background, and Arrow Color
        setupSpinner();

        // ✅ Status Bar & Navigation Bar Color
        setupSystemBars();

        // ✅ Toolbar Setup
        setupToolbar();

        // ✅ Button Click Listener
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMaintenanceCalories();
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this,
                R.layout.custom_spinner_item,
                getResources().getStringArray(R.array.activity_levels)
        ) {
            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView item = (TextView) super.getDropDownView(position, convertView, parent);

                // ✅ Text Size & Color
                item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                item.setTextColor(getResources().getColor(R.color.tett));

                // ✅ Background & Borders
                GradientDrawable background = new GradientDrawable();
                background.setColor(Color.WHITE); // Background color
                background.setCornerRadius(12); // Rounded corners
                background.setStroke(2, getResources().getColor(R.color.tett)); // Border color
                item.setBackground(background);

                // ✅ Partition Lines
                if (position < getCount() - 1) {
                    item.setPadding(16, 16, 16, 16);
                } else {
                    item.setPadding(16, 16, 16, 16);
                }

                return item;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActivityLevel.setAdapter(adapter);

        // ✅ Change Spinner Arrow Color
        spActivityLevel.setPopupBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_item_background));
    }

    private void setupSystemBars() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button in Toolbar
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
    }

    private void calculateMaintenanceCalories() {
        String ageStr = etAge.getText().toString();
        String weightStr = etWeight.getText().toString();
        String heightStr = etHeight.getText().toString();

        if (ageStr.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty() || rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        float weight = Float.parseFloat(weightStr);
        float height = Float.parseFloat(heightStr);
        int selectedGender = rgGender.getCheckedRadioButtonId();
        double bmr;

        if (selectedGender == R.id.rbMale) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        double activityFactor;
        switch (spActivityLevel.getSelectedItemPosition()) {
            case 0:
                activityFactor = 1.2;
                break;
            case 1:
                activityFactor = 1.375;
                break;
            case 2:
                activityFactor = 1.55;
                break;
            case 3:
                activityFactor = 1.725;
                break;
            default:
                activityFactor = 1.2;
                break;
        }

        double maintenanceCalories = bmr * activityFactor;
        double calorieDeficit = maintenanceCalories - 500;
        double calorieSurplus = maintenanceCalories + 500;

        // ✅ Center Align + Text Size Fix
        tvResult.setText(
                "Your Maintenance Calories: " + String.format("%.2f", maintenanceCalories) + " kcal/day\n\n" +
                        "For Weight Loss (Calorie Deficit): " + String.format("%.2f", calorieDeficit) + " kcal/day\n" +
                        "For Muscle Gain (Calorie Surplus): " + String.format("%.2f", calorieSurplus) + " kcal/day"
        );
        tvResult.setGravity(Gravity.CENTER);
        tvResult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}

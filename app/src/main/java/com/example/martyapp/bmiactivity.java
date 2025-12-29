package com.example.martyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class bmiactivity extends AppCompatActivity {
    Button mrecalculatebmi;
    TextView mbmidisplay, mbmicategory, mgender;
    ImageView mimageview;
    RelativeLayout mbackground;

    String height, weight, gender;
    float intheight, intweight, intbmi;
    String mbmi;

    @SuppressLint({"MissingInflatedId", "ObsoleteSdkInt", "SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bmiactivity);


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D")); // Replace with your hex color
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        // Set ActionBar properties
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='white'>Result</font>"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E1D1D")));
        }

        // Initialize UI elements
        mbmidisplay = findViewById(R.id.bmidisplay);
        mbmicategory = findViewById(R.id.bmicategory);
        mgender = findViewById(R.id.genderdisplay);
        mimageview = findViewById(R.id.resultimageview);
        mbackground = findViewById(R.id.main);
        mrecalculatebmi = findViewById(R.id.mrecalculatebmi);

        // Retrieve data from the Intent
        Intent intent = getIntent();
        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");
        gender = intent.getStringExtra("gender");

        // Handle null values
        if (height == null || height.isEmpty()) height = "170"; // Default height
        if (weight == null || weight.isEmpty()) weight = "60"; // Default weight
        if (gender == null || gender.isEmpty()) gender = "Not Specified";

        // Display gender
        mgender.setText(gender);

        try {
            // Convert height from cm to meters
            intheight = Float.parseFloat(height) / 100;
            intweight = Float.parseFloat(weight);

            // BMI Calculation
            intbmi = intweight / (intheight * intheight);
            mbmi = String.format("%.2f", intbmi); // Formatting to 2 decimal places
            mbmidisplay.setText(mbmi);

            // Determine BMI category and UI changes
            if (intbmi < 16) {
                mbmicategory.setText("Severely Underweight");
                mbackground.setBackgroundColor(Color.parseColor("#E68568"));// Orange color
                mimageview.setImageResource(R.drawable.cancel_icon);
            } else if (intbmi < 17) {
                mbmicategory.setText("Underweight");
                mbackground.setBackgroundColor(Color.parseColor("#EADA7F"));
                mimageview.setImageResource(R.drawable.exclamation_triangle_icon);
            } else if (intbmi < 18.5) {
                mbmicategory.setText("Mild Thinness");
                mbackground.setBackgroundColor(Color.parseColor("#EADA7F"));
                mimageview.setImageResource(R.drawable.exclamation_triangle_icon);
            } else if (intbmi < 25) {
                mbmicategory.setText("Normal Weight");
                mbackground.setBackgroundColor(Color.parseColor("#9CEA7F"));
                mimageview.setImageResource(R.drawable.confirm_icon);
            } else if (intbmi < 30) {
                mbmicategory.setText("Overweight");
                mbackground.setBackgroundColor(Color.parseColor("#EADA7F"));
                mimageview.setImageResource(R.drawable.exclamation_triangle_icon);
            } else {
                mbmicategory.setText("Obese");
                mbackground.setBackgroundColor(Color.parseColor("#E68568"));
                mimageview.setImageResource(R.drawable.cancel_icon);
            }
        } catch (NumberFormatException e) {
            mbmidisplay.setText("Error");
            mbmicategory.setText("Invalid input");
            mbackground.setBackgroundColor(Color.parseColor("#E68568"));
            mimageview.setImageResource(R.drawable.cancel_icon);
        }

        // Recalculate BMI Button
        mrecalculatebmi.setOnClickListener(v -> finish());


        }
    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}

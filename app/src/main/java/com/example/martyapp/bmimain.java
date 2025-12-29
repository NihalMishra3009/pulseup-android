package com.example.martyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class bmimain extends AppCompatActivity {
    android.widget.Button calculatebmi;
    EditText mcurrentheight, mcurrentage, mcurrentweight;
    ImageView mincrementage, mincrementweight, mdecrementage, mdecrementweight;
    SeekBar mseekbarforheight;
    RelativeLayout mmale, mfemale;

    int intweight = 55;
    int intage = 22;
    int currentprogress = 170;
    String typeofuser = "";  // Empty by default to check if user selects gender

    @SuppressLint({"DefaultLocale", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_calculate);

        // Initializing UI elements
        calculatebmi = findViewById(R.id.calculatebmi);
        mcurrentage = findViewById(R.id.currentage);
        mcurrentweight = findViewById(R.id.currentweight);
        mcurrentheight = findViewById(R.id.currentheight);
        mincrementage = findViewById(R.id.incrementage);
        mdecrementage = findViewById(R.id.decrementage);
        mincrementweight = findViewById(R.id.incrementweight);
        mdecrementweight = findViewById(R.id.decrementweight);
        mseekbarforheight = findViewById(R.id.seekbarforheight);
        mmale = findViewById(R.id.male);
        mfemale = findViewById(R.id.female);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        // Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle Back Button Click
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed(); // Go back to previous activity
        });

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D"));
        window.setNavigationBarColor(Color.parseColor("#FFFFFF"));

        setDarkNavigationIcons(window);

        // Gender selection
        mmale.setOnClickListener(v -> {
            mmale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalefocus));
            mfemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalenotfocus));
            typeofuser = "Male";
        });

        mfemale.setOnClickListener(v -> {
            mfemale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalefocus));
            mmale.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.malefemalenotfocus));
            typeofuser = "Female";
        });

        // SeekBar setup
        mseekbarforheight.setMax(250);  // Reasonable height limit
        mseekbarforheight.setProgress(currentprogress);
        mcurrentheight.setText(String.valueOf(currentprogress));

        mseekbarforheight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentprogress = Math.max(50, progress); // Prevent unrealistic values
                mcurrentheight.setText(String.valueOf(currentprogress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Ensure EditTexts remain editable
        mcurrentage.setText(String.valueOf(intage));
        mcurrentweight.setText(String.valueOf(intweight));

        // Fix cursor visibility issue
        setupCursorVisibility(mcurrentage);
        setupCursorVisibility(mcurrentweight);
        setupCursorVisibility(mcurrentheight);

        // Age increment & decrement
        mincrementage.setOnClickListener(v -> {
            intage = Math.min(intage + 1, 120); // Age limit
            mcurrentage.setText(String.valueOf(intage));
        });

        mdecrementage.setOnClickListener(v -> {
            if (intage > 1) {
                intage--;
                mcurrentage.setText(String.valueOf(intage));
            }
        });

        // Weight increment & decrement
        mincrementweight.setOnClickListener(v -> {
            intweight = Math.min(intweight + 1, 300); // Weight limit
            mcurrentweight.setText(String.valueOf(intweight));
        });

        mdecrementweight.setOnClickListener(v -> {
            if (intweight > 1) {
                intweight--;
                mcurrentweight.setText(String.valueOf(intweight));
            }
        });

        // BMI Calculation button
        calculatebmi.setOnClickListener(v -> {
            if (typeofuser.isEmpty()) {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
                return; // Prevent proceeding if gender isn't selected
            }

            Intent intent = new Intent(bmimain.this, bmiactivity.class);
            intent.putExtra("height", String.valueOf(currentprogress));
            intent.putExtra("weight", String.valueOf(intweight));
            intent.putExtra("age", String.valueOf(intage));
            intent.putExtra("gender", typeofuser);
            startActivity(intent);
        });
    }

    private void setupCursorVisibility(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                editText.setCursorVisible(true);
            }
        });
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}
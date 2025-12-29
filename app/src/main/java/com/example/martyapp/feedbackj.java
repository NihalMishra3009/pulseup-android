package com.example.martyapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class feedbackj extends AppCompatActivity {

    private ImageView emojiImage;
    private SeekBar seekBar;
    private TextView textHappy, textUnhappy, textConfused;
    private EditText feedbackInput, emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_pu);

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

        // Initialize Views
        emojiImage = findViewById(R.id.emojiImage);
        seekBar = findViewById(R.id.seekBar);
        textHappy = findViewById(R.id.textHappy);
        textUnhappy = findViewById(R.id.textUnhappy);
        textConfused = findViewById(R.id.textConfused);
        feedbackInput = findViewById(R.id.feedbackInput);
        emailInput = findViewById(R.id.emailInput);

        // Back Button Click Event

        // SeekBar Change Listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateEmojiAndText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Submit Button Click Event
        findViewById(R.id.submitButton).setOnClickListener(v -> submitFeedback());
    }

    private void updateEmojiAndText(int progress) {
        if (progress == 0) {
            emojiImage.setImageResource(R.drawable.sad_icon);
            textHappy.setTextColor(getResources().getColor(R.color.bad));
            textUnhappy.setTextColor(getResources().getColor(R.color.ttt));
            textConfused.setTextColor(getResources().getColor(R.color.ttt));
        } else if (progress == 1) {
            emojiImage.setImageResource(R.drawable.grinning_face_with_big_eyes_emoji_icon);
            textHappy.setTextColor(getResources().getColor(R.color.ttt));
            textUnhappy.setTextColor(getResources().getColor(R.color.good));
            textConfused.setTextColor(getResources().getColor(R.color.ttt));
        } else {
            emojiImage.setImageResource(R.drawable.grinning_face_with_smiling_eyes_emoji_icon);
            textHappy.setTextColor(getResources().getColor(R.color.ttt));
            textUnhappy.setTextColor(getResources().getColor(R.color.ttt));
            textConfused.setTextColor(getResources().getColor(R.color.excellent));
        }
    }

    private void submitFeedback() {
        String feedbackText = feedbackInput.getText().toString().trim();
        String emailText = emailInput.getText().toString().trim();

        if (feedbackText.isEmpty()) {
            Toast.makeText(this, "Please enter your feedback!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (emailText.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
            return;
        }

        sendEmail(emailText, feedbackText);
    }

    private void sendEmail(String userEmail, String feedback) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nihalcr72020@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "User Feedback");
        intent.putExtra(Intent.EXTRA_TEXT, "User Email: " + userEmail + "\nFeedback: " + feedback);

        try {
            startActivity(Intent.createChooser(intent, "Choose an email client"));
            Toast.makeText(this, "Opening Email Client", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No Email Clients Installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

}

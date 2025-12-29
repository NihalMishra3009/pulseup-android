package com.example.martyapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class medit_j extends AppCompatActivity {

    private ImageView backgroundImage;
    private Spinner spinnerMusic;
    private TextView tvTitle, tvTimer, durationText, themeText;
    private LinearLayout timerLayout1, timerLayout2;
    private Button btnStart;

    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private long timeLeftMillis;
    private int selectedMusicResId = 0;
    private int selectedTimerMillis = 0;
    private Button selectedTimerButton = null;

    private final HashMap<String, Integer> themeBackgrounds = new HashMap<String, Integer>() {{
        put("Forest Retreat", R.drawable.forestretreatnew);
        put("Beach Serenity", R.drawable.beachserenitynew);
        put("Mountain Bliss", R.drawable.mountainblissnew);
        put("Cosmic Calm", R.drawable.cosmiccalmnew);
    }};

    private final HashMap<String, Integer> themeMusic = new HashMap<String, Integer>() {{
        put("Forest Retreat", R.raw.forestaudio);
        put("Beach Serenity", R.raw.forestaudio);
        put("Mountain Bliss", R.raw.forestaudio);
        put("Cosmic Calm", R.raw.forestaudio);
    }};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medit_pulseup);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#0F3E4D"));
        window.setNavigationBarColor(Color.parseColor("#0F3E4D"));

        setDarkNavigationIcons(window);

        backgroundImage = findViewById(R.id.backgroundImage);
        spinnerMusic = findViewById(R.id.spinnerMusic);
        tvTitle = findViewById(R.id.tvTitle);
        tvTimer = findViewById(R.id.tvTimer);
        timerLayout1 = findViewById(R.id.timerLayout1);
        timerLayout2 = findViewById(R.id.timerLayout2);
        btnStart = findViewById(R.id.btnStart);
        durationText = findViewById(R.id.durationee);
        themeText = findViewById(R.id.themeii);

        // Apply Custom Spinner Style with Preselected First Item
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.theme_array));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerMusic.setAdapter(adapter);

        // Set the first item as the default selected item
        spinnerMusic.setSelection(0, false);

        // Set dropdown background with rounded corners
        Drawable backgroundDrawable = ContextCompat.getDrawable(this, R.drawable.spinner_bg);
        spinnerMusic.setPopupBackgroundDrawable(backgroundDrawable);

        spinnerMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerMusic.setSelection(0, false);
            }
        });

        // Timer buttons
        int[] timerButtonIds = {R.id.bt5min, R.id.bt10min, R.id.bt20min, R.id.bt30min, R.id.bt45min, R.id.bt60min};
        final int[] timerDurations = {5, 10, 20, 30, 45, 60};

        for (int i = 0; i < timerButtonIds.length; i++) {
            Button btn = findViewById(timerButtonIds[i]);
            final int duration = timerDurations[i] * 60 * 1000;
            btn.setOnClickListener(view -> selectTimer(btn, duration));
        }

        btnStart.setOnClickListener(view -> {
            if (countDownTimer == null) {
                startMeditation();
            } else {
                stopMeditation();
            }
        });
    }

    private void selectTimer(Button button, int duration) {
        if (selectedTimerButton != null) {
            selectedTimerButton.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        }
        selectedTimerButton = button;
        selectedTimerMillis = duration;
        selectedTimerButton.setBackgroundTintList(getResources().getColorStateList(R.color.tott));
    }

    private void startMeditation() {
        if (selectedTimerMillis == 0) {
            Toast.makeText(this, "Select timer!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnStart.setText("Stop");

        timerLayout1.setVisibility(View.GONE);
        timerLayout2.setVisibility(View.GONE);
        durationText.setVisibility(View.GONE);
        themeText.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        spinnerMusic.setVisibility(View.GONE);

        String selectedTheme = spinnerMusic.getSelectedItem().toString();

        if (themeBackgrounds.containsKey(selectedTheme)) {
            backgroundImage.setImageResource(themeBackgrounds.get(selectedTheme));
            backgroundImage.setAlpha(0.5f);
        }

        selectedMusicResId = themeMusic.getOrDefault(selectedTheme, 0);

        playMusic();

        countDownTimer = new CountDownTimer(selectedTimerMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                stopMeditation();
            }
        }.start();
    }

    private void stopMeditation() {
        btnStart.setText("Start");

        timerLayout1.setVisibility(View.VISIBLE);
        timerLayout2.setVisibility(View.VISIBLE);
        durationText.setVisibility(View.VISIBLE);
        themeText.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        spinnerMusic.setVisibility(View.VISIBLE);

        backgroundImage.setAlpha(0.2f);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        if (selectedTimerButton != null) {
            selectedTimerButton.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        }

        tvTimer.setText("00:00");
    }

    private void playMusic() {
        if (selectedMusicResId == 0) return;

        mediaPlayer = MediaPlayer.create(this, selectedMusicResId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            if (countDownTimer != null && timeLeftMillis > 0) {
                playMusic();
            }
        });
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftMillis / 1000) / 60;
        int seconds = (int) (timeLeftMillis / 1000) % 60;
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}

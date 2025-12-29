package com.example.martyapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.graphics.Color;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class schedules extends AppCompatActivity {
    private Button setTimetable1;
    private Button setTimetable2;
    private Button setTimetable3;

    private Button viewbut1, viewbut2, viewbut3;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "TimetablePrefs";
    private static final String ACTIVE_TIMETABLE = "active_timetable";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedulesui);

        setTimetable1 = findViewById(R.id.setTimetable1);
        setTimetable2 = findViewById(R.id.setTimetable2);
        setTimetable3 = findViewById(R.id.setTimetable3);

        viewbut1 = findViewById(R.id.viewbut1);
        viewbut2 = findViewById(R.id.viewbut2);
        viewbut3 = findViewById(R.id.viewbut3);

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

        viewbut1.setOnClickListener(view -> startActivity(new Intent(schedules.this, tt1j.class)));
        viewbut2.setOnClickListener(view -> startActivity(new Intent(schedules.this, tt2j.class)));
        viewbut3.setOnClickListener(view -> startActivity(new Intent(schedules.this, tt3j.class)));

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        int savedTimetable = preferences.getInt(ACTIVE_TIMETABLE, -1);
        if (savedTimetable != -1) {
            updateButtonState(savedTimetable, true);
        }

        setTimetable1.setOnClickListener(v -> toggleTimetable(1, setTimetable1));
        setTimetable2.setOnClickListener(v -> toggleTimetable(2, setTimetable2));
        setTimetable3.setOnClickListener(v -> toggleTimetable(3, setTimetable3));
    }

    private void toggleTimetable(int timetableNumber, Button clickedButton) {
        int activeTimetable = preferences.getInt(ACTIVE_TIMETABLE, -1);

        if (activeTimetable == timetableNumber) {
            cancelAlarms(timetableNumber);
            updateButtonState(timetableNumber, false);
            preferences.edit().putInt(ACTIVE_TIMETABLE, -1).apply();
            Toast.makeText(this, "Timetable " + timetableNumber + " alarms disabled!", Toast.LENGTH_SHORT).show();
        } else {
            if (!hasExactAlarmPermission()) {
                requestExactAlarmPermission();
                return;
            }

            if (activeTimetable != -1) {
                cancelAlarms(activeTimetable);
                updateButtonState(activeTimetable, false);
            }

            setAlarms(timetableNumber);
            updateButtonState(timetableNumber, true);
            preferences.edit().putInt(ACTIVE_TIMETABLE, timetableNumber).apply();
            Toast.makeText(this, "Timetable " + timetableNumber + " alarms set!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateButtonState(int timetableNumber, boolean isSet) {
        TextView textView = null;
        switch (timetableNumber) {
            case 1:
                textView = setTimetable1;
                break;
            case 2:
                textView = setTimetable2;
                break;
            case 3:
                textView = setTimetable3;
                break;
        }
        if (textView != null) {
            if (isSet) {
                textView.setText("Disable");
                textView.setBackgroundColor(Color.parseColor("#175F75"));
                textView.setTextColor(Color.WHITE);
            } else {
                textView.setText("Set");
                textView.setBackgroundColor(Color.parseColor("#CFF3FF"));
                textView.setTextColor(Color.parseColor("#175F75"));
            }
        }
    }

    private void setAlarms(int timetableNumber) {
        switch (timetableNumber) {
            case 1:
                AlarmHelper.setMultipleAlarms(this, 1,
                        new int[]{5, 5, 6, 7, 8, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20, 21, 22},
                        new int[]{0, 30, 30, 0, 0, 30, 0, 30, 30, 0, 0, 0, 0, 30, 30, 30, 30},
                        new int[]{R.raw.five_am_eb, R.raw.five_thirty_am_eb, R.raw.six_thirty_am_eb, R.raw.seven_am_eb, R.raw.eight_am_eb, R.raw.ten_thirty_am_eb, R.raw.eleven_am_eb, R.raw.twelve_thirty_pm_eb, R.raw.one_thirty_pm_eb, R.raw.two_pm_eb, R.raw.four_pm_eb, R.raw.five_pm_eb, R.raw.six_pm_eb, R.raw.seven_thirty_pm_eb, R.raw.eight_thirty_pm_eb, R.raw.nine_thirty_pm_eb, R.raw.ten_thirty_pm_eb}
                );
                break;

            case 2:
                AlarmHelper.setMultipleAlarms(this, 2,
                        new int[]{10, 10, 10, 11, 12, 13, 15, 16, 18, 19, 20, 21, 23, 0, 1},
                        new int[]{0, 15, 30, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        new int[]{R.raw.ten_am_no, R.raw.ten_fifteen_am_no, R.raw.ten_thirty_am_no, R.raw.eleven_am_no, R.raw.twelve_thirty_pm_no, R.raw.one_pm_no, R.raw.three_pm_no, R.raw.four_pm_no, R.raw.six_pm_no, R.raw.seven_pm_no, R.raw.eight_pm_no, R.raw.nine_pm_no, R.raw.eleven_pm_no, R.raw.twelve_am_no, R.raw.one_am_no}
                );
                break;

            case 3:
                AlarmHelper.setMultipleAlarms(this, 3,
                        new int[]{7, 7, 7, 8, 9, 9, 12, 12, 14, 16, 17, 18, 19, 21, 22, 23},
                        new int[]{0, 15, 30, 30, 0, 30, 0, 30, 0, 30, 0, 0, 30, 0, 0, 0},
                        new int[]{R.raw.seven_am_br, R.raw.seven_fifteen_am_br, R.raw.seven_thirty_am_br, R.raw.eight_thirty_am_br, R.raw.nine_am_br, R.raw.nine_thirty_am_br, R.raw.twelve_pm_br, R.raw.twelve_thirty_pm_br, R.raw.two_pm_br, R.raw.four_thirty_pm_br, R.raw.five_pm_br, R.raw.six_pm_br, R.raw.seven_thirty_pm_br, R.raw.nine_pm_br, R.raw.ten_pm_br, R.raw.eleven_pm_br}
                );
                break;
        }
    }

    private void cancelAlarms(int timetableNumber) {
        for (int requestCode = timetableNumber * 100; requestCode < (timetableNumber * 100 + 10); requestCode++) {
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    @SuppressLint("NewApi")
    private boolean hasExactAlarmPermission() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        return alarmManager != null && alarmManager.canScheduleExactAlarms();
    }

    private void requestExactAlarmPermission() {
        @SuppressLint("InlinedApi") Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        startActivity(intent);
        Toast.makeText(this, "Please grant exact alarm permission", Toast.LENGTH_LONG).show();
    }

    // Moved this method outside onCreate()
    private void setDarkNavigationIcons(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}

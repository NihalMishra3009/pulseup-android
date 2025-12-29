package com.example.martyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {
    private static final String PREFS_NAME = "TimetablePrefs";
    private static final String ACTIVE_TIMETABLE = "active_timetable";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if (preferences != null) {
                int savedTimetable = preferences.getInt(ACTIVE_TIMETABLE, -1);

                if (savedTimetable != -1) {
                    restoreAlarms(context, savedTimetable);
                }
            }
        }
    }

    private void restoreAlarms(Context context, int timetableNumber) {
        switch (timetableNumber) {
            case 1:
                AlarmHelper.setMultipleAlarms(context, 1,
                        new int[]{5, 5, 6, 7, 8, 10, 11, 12, 13, 14, 16, 17, 18, 19, 20, 21, 22},
                        new int[]{0, 30, 30, 0, 0, 30, 0, 30, 30, 0, 0, 0, 0, 30, 30, 30, 30},
                        new int[]{R.raw.five_am_eb, R.raw.five_thirty_am_eb, R.raw.six_thirty_am_eb, R.raw.seven_am_eb, R.raw.eight_am_eb, R.raw.ten_thirty_am_eb, R.raw.eleven_am_eb, R.raw.twelve_thirty_pm_eb, R.raw.one_thirty_pm_eb, R.raw.two_pm_eb, R.raw.four_pm_eb, R.raw.five_pm_eb, R.raw.six_pm_eb, R.raw.seven_thirty_pm_eb, R.raw.eight_thirty_pm_eb, R.raw.nine_thirty_pm_eb, R.raw.ten_thirty_pm_eb}
                );
                break;

            case 2:
                AlarmHelper.setMultipleAlarms(context, 2,
                        new int[]{10, 10, 10, 11, 12, 13, 15, 16, 18, 19, 20, 21, 23, 0, 1},
                        new int[]{0, 15, 30, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        new int[]{R.raw.ten_am_no, R.raw.ten_fifteen_am_no, R.raw.ten_thirty_am_no, R.raw.eleven_am_no, R.raw.twelve_thirty_pm_no, R.raw.one_pm_no, R.raw.three_pm_no, R.raw.four_pm_no, R.raw.six_pm_no, R.raw.seven_pm_no, R.raw.eight_pm_no, R.raw.nine_pm_no, R.raw.eleven_pm_no, R.raw.twelve_am_no, R.raw.one_am_no}
                );
                break;

            case 3:
                AlarmHelper.setMultipleAlarms(context, 3,
                        new int[]{7, 7, 7, 8, 9, 9, 12, 12, 14, 16, 17, 18, 19, 21, 22, 23},
                        new int[]{0, 15, 30, 30, 0, 30, 0, 30, 0, 30, 0, 0, 30, 0, 0, 0},
                        new int[]{R.raw.seven_am_br, R.raw.seven_fifteen_am_br, R.raw.seven_thirty_am_br, R.raw.eight_thirty_am_br, R.raw.nine_am_br, R.raw.nine_thirty_am_br, R.raw.twelve_pm_br, R.raw.twelve_thirty_pm_br, R.raw.two_pm_br, R.raw.four_thirty_pm_br, R.raw.five_pm_br, R.raw.six_pm_br, R.raw.seven_thirty_pm_br, R.raw.nine_pm_br, R.raw.ten_pm_br, R.raw.eleven_pm_br}
                );
                break;
        }
    }
}

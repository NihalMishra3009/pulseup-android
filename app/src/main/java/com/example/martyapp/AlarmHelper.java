package com.example.martyapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmHelper {

    @SuppressLint("NewApi")
    public static void setMultipleAlarms(Context context, int timetableNumber, int[] hours, int[] minutes, int[] soundIds) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) {
            Toast.makeText(context, "Alarm service unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Grant exact alarm permission in settings", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < hours.length; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hours[i]);
            calendar.set(Calendar.MINUTE, minutes[i]);
            calendar.set(Calendar.SECOND, 0);

            // Ensure alarm is always set for the future
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("sound_id", soundIds[i]);
            intent.putExtra("timetable_id", timetableNumber);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, timetableNumber * 100 + i, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            Log.d("AlarmHelper", "Setting alarm for timetable " + timetableNumber + " at " + calendar.getTime());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setRepeating(  // Legacy devices - Daily repeating alarm
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,  // Repeat every 24 hours
                        pendingIntent
                );
            }
        }

        Toast.makeText(context, "Alarms set for timetable " + timetableNumber, Toast.LENGTH_SHORT).show();
    }

    public static void cancelAlarms(Context context, int timetableNumber) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) {
            return;
        }

        for (int i = 0; i < 10; i++) {  // Assuming max 10 alarms per timetable
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, timetableNumber * 100 + i, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(context, "Alarms cancelled for timetable " + timetableNumber, Toast.LENGTH_SHORT).show();
    }
}

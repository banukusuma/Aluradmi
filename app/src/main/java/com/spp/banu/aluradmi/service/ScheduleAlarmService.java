package com.spp.banu.aluradmi.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.spp.banu.aluradmi.BootBroadcastReceiver;
import com.spp.banu.aluradmi.PeriodicTaskReceiver;
import com.spp.banu.aluradmi.SetupActivity;

import java.util.Date;

/**
 * Created by banu on 25/02/17.
 */

public class ScheduleAlarmService extends IntentService {
    private static final String TAG = "ScheduleAlarmService";
    public static final String KEY_PREF_MODE_ALARM = "com.spp.aluradmi.banu.pref.mode.alarm";
    public static int MODE_ALARM_SCHEDULE;
    SharedPreferences preferences;

    public ScheduleAlarmService() {
        super("ScheduleAlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        scheduleAlarm();
        Log.e(TAG, "onHandleIntent: schedule alarmservice dimulai" );
        //BootBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        preferences = getSharedPreferences(SetupActivity.KEY, Context.MODE_PRIVATE);
        MODE_ALARM_SCHEDULE = preferences.getInt(SetupActivity.KEY_MODE_ALARM, 1);
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), PeriodicTaskReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, PeriodicTaskReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        Log.e(TAG, "scheduleAlarm: system current time " + new Date(firstMillis) );
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        if (MODE_ALARM_SCHEDULE == 1){
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    AlarmManager.INTERVAL_DAY, pIntent);
            Log.e(TAG, "scheduleAlarm: sehari"  );
        }else if (MODE_ALARM_SCHEDULE == 2){
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    AlarmManager.INTERVAL_DAY * 7, pIntent);
            Log.e(TAG, "scheduleAlarm: seminggu"  );
        }else if (MODE_ALARM_SCHEDULE == 3){
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    AlarmManager.INTERVAL_DAY * 15, pIntent);
            Log.e(TAG, "scheduleAlarm: 15 hari"  );
        }else {
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    AlarmManager.INTERVAL_DAY * 30, pIntent);
            Log.e(TAG, "scheduleAlarm: 30 hari"  );
        }
    }

    public void writeSchedulePreferences(int mode){
        final SharedPreferences preferences = getSharedPreferences(SetupActivity.KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_PREF_MODE_ALARM, mode);
        editor.commit();
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), PeriodicTaskReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, PeriodicTaskReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}

package com.spp.banu.aluradmi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.spp.banu.aluradmi.service.ScheduleAlarmService;

import java.util.Date;

/**
 * Created by banu on 25/02/17.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";
    public static final String KEY_SETUP_ALARM = "com.spp.aluradmi.banu.setup.alarm.boot";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, ScheduleAlarmService.class);
        context.startService(startServiceIntent);
    }
}

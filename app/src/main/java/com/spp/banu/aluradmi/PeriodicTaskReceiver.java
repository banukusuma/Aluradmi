package com.spp.banu.aluradmi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.spp.banu.aluradmi.service.ScheduleAlarmService;

/**
 * Created by banu on 25/02/17.
 */

public class PeriodicTaskReceiver extends BroadcastReceiver {
    public final static int REQUEST_CODE = 7995;
    public final static String KEY_INTENT_PERIODIC = "com.spp.banu.aluradmi.periodic.task.syncronisation.receiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,SinkronisasiService.class);
        context.startService(intent1);
    }
}

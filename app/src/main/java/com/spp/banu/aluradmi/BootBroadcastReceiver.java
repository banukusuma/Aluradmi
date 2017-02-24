package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by banu on 25/02/17.
 */

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, PeriodicTaskReceiver.class);
        startWakefulService(context, startServiceIntent);
    }
}

package com.spp.banu.aluradmi.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.spp.banu.aluradmi.AlurListActivity;
import com.spp.banu.aluradmi.DatabaseHelper;
import com.spp.banu.aluradmi.KeteranganPagerActivity;
import com.spp.banu.aluradmi.MainActivity;
import com.spp.banu.aluradmi.MapsActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniAlur;
import com.spp.banu.aluradmi.httpcall.GetJsonData;

import java.net.ProtocolException;

/**
 * Created by banu on 19/02/17.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private String table;
    private int id;
    private String order;
    private String body;
    private Context broadcastContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        broadcastContext = context;
        table = intent.getStringExtra(MyFirebaseMessagingService.KEY_TABLE_NOTIF);
        id = Integer.parseInt(intent.getStringExtra(MyFirebaseMessagingService.KEY_ID_NOTIF));
        order = intent.getStringExtra(MyFirebaseMessagingService.KEY_ORDER_NOTIF);
        body = intent.getStringExtra(MyFirebaseMessagingService.KEY_BODY_NOTIF);
        //saveData();
        sendNotification();
    }

    private void saveData() {
        String idOrTimestamp = null;
        DatabaseHelper db = DatabaseHelper.getInstance(broadcastContext, true);
        switch (order){
            case "insert":
                idOrTimestamp = db.getMaxTimestamp(table);
                break;
            case "update":
                idOrTimestamp = Integer.toString(id);
                break;
            case "sorting":
                idOrTimestamp = db.getMaxTimestamp(table);
                break;
            case "delete":
                idOrTimestamp = Integer.toString(id);
                break;
            default:
                break;
        }
        GetJsonData getJsonData = new GetJsonData(order,table,idOrTimestamp, broadcastContext);
        getJsonData.execute();
    }

    private void sendNotification(){
        //String body = intent.getStringExtra(MyFirebaseMessagingService.KEY_BODY_NOTIF);
        Intent intent;
        // new Intent(context, MainActivity.class);
            //String table = intent.getStringExtra(MyFirebaseMessagingService.KEY_TABLE_NOTIF);
        switch (table) {
            case "kategori":
                intent = new Intent(broadcastContext, MainActivity.class);
                break;
            case "jurusan":
                intent = new Intent(broadcastContext, MainActivity.class);
                break;
            case "alur":
                SharedPreferences preferences = broadcastContext.getSharedPreferences(MainActivity.KEY_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(MainActivity.KEY_ID_KATEGORI, id);
                editor.commit();
                intent = new Intent(broadcastContext, AlurListActivity.class);
                break;
            case "keterangan":
                intent = KeteranganPagerActivity.newIntent(broadcastContext, id);
                break;
            case "berkas":
                intent = KeteranganPagerActivity.newIntent(broadcastContext, id);
                break;
            default:
                intent = new Intent(broadcastContext, MapsActivity.class);
                break;
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(broadcastContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //set sounds notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(broadcastContext);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You Have 1 Notification")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =  (NotificationManager) broadcastContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}

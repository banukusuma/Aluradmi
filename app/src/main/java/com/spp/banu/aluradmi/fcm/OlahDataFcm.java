package com.spp.banu.aluradmi.fcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.spp.banu.aluradmi.AlurListActivity;
import com.spp.banu.aluradmi.DatabaseHelper;
import com.spp.banu.aluradmi.KeteranganPagerActivity;
import com.spp.banu.aluradmi.MainActivity;
import com.spp.banu.aluradmi.MapsActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.httpcall.AluradmiRestClient;
import com.spp.banu.aluradmi.httpcall.GetJsonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by banu on 21/02/17.
 */

public class OlahDataFcm extends IntentService {
    private String table;
    private String id;
    private String order;
    private String body;
    private static final String TAG = "olahdatafcm";
    private DatabaseHelper db;

    public OlahDataFcm(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        table = intent.getStringExtra(MyFirebaseMessagingService.KEY_TABLE_NOTIF);
        id = intent.getStringExtra(MyFirebaseMessagingService.KEY_ID_NOTIF);
        order = intent.getStringExtra(MyFirebaseMessagingService.KEY_ORDER_NOTIF);
        body = intent.getStringExtra(MyFirebaseMessagingService.KEY_BODY_NOTIF);
        db = DatabaseHelper.getInstance(this, true);
        RequestParams params = new RequestParams();
        GetJsonData getJsonData = null;
        switch (order) {
            case "tambah":
                String ts = db.getMaxTimestamp(table);
                params.put("table", table);
                params.put("ts", ts);
                try {
                    getData(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "update":
                params.put("table", table);
                params.put("id", id);
                try {
                    getData(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "delete":
                deleteData(id);
                break;
            case "sorting":
                break;
            default:
                break;
        }
        if (table.equals("keterangan")){
            int id_parent = db.getIdParent("keterangan","alur", id);
            sendNotification(id_parent);
        }else if (table.equals("alur")){
            int id_parent = db.getIdParent("alur", "kategori", id);
            sendNotification(id_parent);
        }else if (table.equals("berkas")){
            int id_keterangan = db.getIdParent("berkas", "keterangan", id);
            int id_alur = db.getIdParent("keterangan", "alur", Integer.toString(id_keterangan));
            sendNotification(id_alur);
        }
        else {
            sendNotification(Integer.parseInt(id));
        }
    }


    private void getData(RequestParams params) throws JSONException{
        AluradmiRestClient.get(order, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, "onSuccess: " + response);
                JSONObject jsonObject = response;
                if (order.equals("tambah")){
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = jsonObject.getJSONArray(table);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonArray.length() > 0){
                        db.insertData(table,jsonArray);
                    }
                } else if (order.equals("update")){
                    String field_id = "id_" + table;
                    String id_baru = id;
                    db.updateData(table,field_id,id_baru,jsonObject);
                } else {
                    Log.e(TAG, "method diluar insert, update, delete");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, "onFailure: " + statusCode );
            }
        });
    }

    private void deleteData(String id){
        String field_id = "id_" + table;
        db.deleteData(table,field_id,id);
    }

    private void sendNotification(int id){
        Intent intent;
        switch (table) {
            case "kategori":
                intent = new Intent(this, MainActivity.class);
                break;
            case "jurusan":
                intent = new Intent(this, MainActivity.class);
                break;
            case "alur":
                SharedPreferences preferences = this.getSharedPreferences(MainActivity.KEY_PREFERENCE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(MainActivity.KEY_ID_KATEGORI, id);
                editor.commit();
                intent = new Intent(this, AlurListActivity.class);
                break;
            case "keterangan":
                intent = KeteranganPagerActivity.newIntent(this, id);
                break;
            case "berkas":
                intent = KeteranganPagerActivity.newIntent(this, id);
                break;
            default:
                intent = new Intent(this, MapsActivity.class);
                break;
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //set sounds notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("You Have 1 Notification")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =  (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}

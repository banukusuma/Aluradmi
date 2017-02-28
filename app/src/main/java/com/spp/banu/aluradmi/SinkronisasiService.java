package com.spp.banu.aluradmi;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.spp.banu.aluradmi.httpcall.AluradmiRestClient;
import com.spp.banu.aluradmi.httpcall.CheckNetwork;
import com.spp.banu.aluradmi.service.ScheduleAlarmService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by banu on 21/02/17.
 */

public class SinkronisasiService extends IntentService {
    private DatabaseHelper db;
    SharedPreferences preferences;
    public static final String TAG_INTENT_SINKRONISASI = "com.spp.banu.aluradmi.sinkronisasi.pesan.broadcast";
    private static final String TAG = "SinkronisasiService";
    public static final String KEY_INTERNET_CONNECTION = "com.spp.banu.aluradmi.koneksi.internet";
    public static final String KEY_IS_NEW_DATA = "com.spp.banu.aluradmi.is.new.data";
    public static final String KEY_IS_SUCCESS_UPDATE = "com.spp.banu.aluradmi.is.success.update";
    public static final String KEY_LIST_UPDATE = "com.spp.banu.aluradmi.list.update";
    private Intent broadcast_intent = new Intent(TAG_INTENT_SINKRONISASI);
    public SinkronisasiService() {
        super("SinkronisasiService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = getSharedPreferences(SetupActivity.KEY, Context.MODE_PRIVATE);
        db = DatabaseHelper.getInstance(this,true);
        Log.e(TAG, "onHandleIntent: memulai sinkronisasi intentService"  );
        CheckNetwork network = new CheckNetwork(this);
        if (network.isNetworkAvailable()){
            AluradmiRestClient.get("cekData", null, new JsonHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e(TAG, "onFailure: awal " + statusCode );
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.e(TAG, "onSuccess: respon " + response );
                    Iterator<String> keys = response.keys();
                    boolean[] cek_ke_broadcast = new boolean[8];
                    HashMap<String, Boolean> list_update = new HashMap<>();
                    int i = 0;
                    while (keys.hasNext()){
                        String table = keys.next();
                        Log.e(TAG, "onSuccess: key table awal " + table );
                        try {

                            JSONObject jml_dan_timestamp = response.getJSONObject(table);
                            boolean isHasNewData = isHasNewData(table, jml_dan_timestamp);
                            Log.e(TAG, "onSuccess: apakah ada data baru di  " + table +" " + isHasNewData );
                        /*
                        sementara di buat true untuk mengetes data baru
                        */

                            if (isHasNewData){
                                memulaiSinkronisasi(table);
                                list_update.put(table, true);
                                cek_ke_broadcast[i] = true;
                            }else {
                                list_update.put(table, true);
                                cek_ke_broadcast[i] = false;
                                Log.e(TAG, "onSuccess: data table " + table + " tidak ada perubahan" );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                    broadcast_intent.putExtra(KEY_LIST_UPDATE, list_update);
                    broadcast_intent.putExtra(KEY_IS_SUCCESS_UPDATE, true);
                    broadcast_intent.putExtra(KEY_IS_NEW_DATA, cek_ke_broadcast);
                    writeLastSyncPreferences();
                }
            });
        }else {
            broadcast_intent.putExtra(KEY_INTERNET_CONNECTION, false);

        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast_intent);
    }

    private void memulaiSinkronisasi(String table) {
        //table_class = table;
        List<Integer> listID = db.getListIdFromTable(table);
        for (Integer id : listID){
            RequestParams params = new RequestParams();
            params.put("table", table);
            params.put("id", id);
            AluradmiRestClient.get("isDataExist", params, new JsonHttpResponseHandler(){
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.e(TAG, "onFailure: memulai sinkronisasi " + statusCode );
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        JSONObject data = response.getJSONObject("data");
                        String table = data.getString("table");
                        String id = data.getString("id");
                        boolean ada = data.getBoolean("ada");
                        if (!ada){
                            Log.e(TAG, "onSuccess: check data exits table " + table + " id " + id + " "+ ada );
                            db.deleteData(table, "id_" +table, id);
                        }else {
                            Log.e(TAG, "onSuccess: check data exits table " + table + " id " + id + " "+ ada );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        String timestamp = db.getMaxTimestamp(table);
        RequestParams params = new RequestParams();
        params.put("table", table);
        params.put("timestamp", timestamp);
        AluradmiRestClient.get("getNewData", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(TAG, "onSuccess: memulai sinkronisasi memasukkan data atau mengupdate object " + response );
                Iterator<?> keys = response.keys();
                while (keys.hasNext()){
                    String table = (String) keys.next();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = response.getJSONArray(table);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if ((jsonArray != null ? jsonArray.length() : 0) > 0){
                        Log.e(TAG, "onSuccess: memulai sinkronisasi memasukkan data atau mengupdate " + jsonArray );
                        db.insertData(table,jsonArray);
                    }
                }
            }
        });
    }

    private boolean isHasNewData(String table, JSONObject object){
        String timestamp_client = db.getMaxTimestamp(table);
        int jumlah_Data_client = db.countDataInTable(table);
        Log.e(TAG, "isHasNewData: timestamp_client "+ timestamp_client);
        Log.e(TAG, "isHasNewData: jumlah_data client "+ jumlah_Data_client);
        try {
            String timestamp_server = object.getString("timestamp");
            int jumlah_data_server = object.getInt("jumlah");
            Log.e(TAG, "isHasNewData: timestamp_server "+ timestamp_server);
            Log.e(TAG, "isHasNewData: jumlah_data server "+ jumlah_data_server);
            Log.e(TAG, "isHasNewData: ts server equals ts client " +  timestamp_client.equals(timestamp_server));
            boolean jml_data_Sama = true;
            if (jumlah_Data_client != jumlah_data_server){
                jml_data_Sama = false;
            }

            Log.e(TAG, "isHasNewData: jumlah data server equals jumlah data client " +  jml_data_Sama);
            if (!timestamp_client.equals(timestamp_server) || jumlah_Data_client != jumlah_data_server){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void writeLastSyncPreferences(){
        Date currentDate = new Date();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(SetupActivity.KEY_DATE_SYNC, currentDate.getTime());
        editor.commit();
        Log.e(TAG, "writeLastSyncPreferences: " + currentDate );
    }
}

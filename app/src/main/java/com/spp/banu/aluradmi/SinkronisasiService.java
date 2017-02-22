package com.spp.banu.aluradmi;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.spp.banu.aluradmi.httpcall.AluradmiRestClient;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by banu on 21/02/17.
 */

public class SinkronisasiService extends IntentService {
    private DatabaseHelper db;
    private static final String TAG = "SinkronisasiService";
    public SinkronisasiService() {
        super("SinkronisasiService");
    }
    private String table_class;
    @Override
    protected void onHandleIntent(Intent intent) {
        db = DatabaseHelper.getInstance(this,true);
        Log.e(TAG, "onHandleIntent: memulai sinkronisasi intentService"  );
        AluradmiRestClient.get("cekData", null, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, "onFailure: awal " + statusCode );
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONObject jsonObject = response;
                Log.e(TAG, "onSuccess: respon " + response );
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()){
                    String table = keys.next();
                    Log.e(TAG, "onSuccess: key table awal " + table );
                    try {
                        JSONObject jml_dan_timestamp = jsonObject.getJSONObject(table);
                        boolean isHasNewData = isHasNewData(table, jml_dan_timestamp);
                        Log.e(TAG, "onSuccess: apakah data nya sama " + isHasNewData );
                        /*
                        if (!isHasNewData){
                            memulaiSinkronisasi(table);
                        }else {
                            Log.e(TAG, "onSuccess: data table " + table + " tidak ada perubahan" );
                        }
                        */
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void memulaiSinkronisasi(String table) {
        table_class = table;
        List<Integer> listID = db.getListIdFromTable(table);
        for (final Integer id : listID){
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
                    JSONObject hasil = response;
                    try {
                        boolean ada = hasil.getBoolean("ada");
                        if (!ada){
                            Log.e(TAG, "onSuccess: check data exits table " + table_class + " id " + id + " "+ ada );
                            //db.deleteData(table_class, "id_" +table_class, id.toString());
                        }else {
                            Log.e(TAG, "onSuccess: check data exits table " + table_class + " id " + id + " "+ ada );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
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
}

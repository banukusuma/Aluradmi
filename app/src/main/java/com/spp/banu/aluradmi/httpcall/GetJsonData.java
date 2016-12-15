package com.spp.banu.aluradmi.httpcall;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.spp.banu.aluradmi.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;


/**
 * Created by banu on 08/12/16.
 */

public class GetJsonData extends AsyncTask<Void, Void, Void> {
    private String url;
    private Context context;
    private String table;
    private String field_id;
    private String id;
    private String method;
    private SQLiteDatabase sqLiteDatabase;
    private String TAG = GetJsonData.class.getSimpleName();
    public GetJsonData(String url, String method, String table, String idOrTimestamp, Context context){
       if (method == "tambah"){
           this.method = method;
           this.table = table;
           this.context = context;
           this.url = url + method + "/table/" + table + "/ts/" + idOrTimestamp;
       }else if (method == "update"){
           this.method = method;
           this.table = table;
           this.context = context;
           this.field_id = "id_" + table;
           this.id = idOrTimestamp;
           this.url = url + method + "/table/" + table + "/id/" + idOrTimestamp;
       }else if (method == "delete"){
           this.field_id = "id_" + table;
           this.table = table;
           this.id = idOrTimestamp;
           this.context = context;
           this.method = method;
       }
    }
    @Override
    protected Void doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();
        DatabaseHelper db = new DatabaseHelper(context, true);
        if (method != "delete"){
            String jsonString = httpHandler.makeServiceCall(this.url);
            Log.d(TAG, "url: " + this.url);
            Log.e(TAG, "data json from server: " + jsonString );
            if (jsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (method == "tambah"){
                        JSONArray jsonArray = jsonObject.getJSONArray(table);
                        if (jsonArray.length() > 0){
                            db.insertData(table,jsonArray);
                        }
                    } else if (method == "update"){
                        db.updateData(table,field_id,id,jsonObject);
                    } else {
                        Log.e(TAG, "method diluar insert, update, delete");
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            }
            else {
                Log.e(TAG, "Couldn't get json from server.");
            }
        }else if (method == "delete"){
            db.deleteData(table,field_id,id);
        }else {
            Log.e(TAG, "method diluar insert, update, delete");
        }
    return null;
        }
    }


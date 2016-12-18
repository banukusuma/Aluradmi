package com.spp.banu.aluradmi.httpcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.spp.banu.aluradmi.AsyncBooleanListener;
import com.spp.banu.aluradmi.DatabaseHelper;
import com.spp.banu.aluradmi.MainActivity;
import com.spp.banu.aluradmi.SplashActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by banu on 10/12/16.
 */

public class GetAllData extends AsyncTask<Void, Void,Boolean> {
    private Context context;
    private TextView textView;
    private AsyncBooleanListener listener;
    private final String TAG = GetAllData.class.getSimpleName();
    private static final String url = "http://aluradmi.pe.hu/data/semua";
    public GetAllData(Context context, AsyncBooleanListener asyncBooleanListener, TextView textView) {
        this.context = context;
        this.listener = asyncBooleanListener;
        this.textView = textView;
        this.textView.setText("Sedang Mengunduh Data");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        listener.getResult(result);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();
        DatabaseHelper db =  DatabaseHelper.getInstance(context, false);
        String jsonstring = httpHandler.makeServiceCall(url);
        Log.e(TAG, "data json from server: " + jsonstring );
        if (jsonstring != null){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonstring);
                Iterator<?> keys = jsonObject.keys();
                while (keys.hasNext()){
                    String table = (String) keys.next();
                    JSONArray jsonArray = jsonObject.getJSONArray(table);
                    if (jsonArray.length() > 0){
                        db.insertData(table,jsonArray);
                    }
                }
                return true;
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage() );
            }
        }
        else{
            Log.e(TAG, "Couldn't get json from server.");
            return false;
        }
        return false;
    }
}

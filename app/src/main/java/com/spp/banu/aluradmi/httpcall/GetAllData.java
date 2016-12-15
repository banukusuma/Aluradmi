package com.spp.banu.aluradmi.httpcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


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

public class GetAllData extends AsyncTask<Void, Void,Void> {
    private Context context;
    public ProgressDialog dialog;
    private final String TAG = GetAllData.class.getSimpleName();
    private static final String url = "http://aluradmi.pe.hu/data/semua";
    public GetAllData(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
         dialog =  new ProgressDialog(context);
        dialog.setMessage("Sedang Mengunduh Data");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        context.startActivity(new Intent(context,MainActivity.class));
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();
        DatabaseHelper db = new DatabaseHelper(context, true);
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
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage() );
            }
        }
        else{
            Log.e(TAG, "Couldn't get json from server.");
        }
        return null;
    }
}

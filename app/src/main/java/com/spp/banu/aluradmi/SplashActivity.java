package com.spp.banu.aluradmi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;


import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.spp.banu.aluradmi.httpcall.CheckNetwork;

import com.spp.banu.aluradmi.httpcall.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class SplashActivity extends AppCompatActivity {
    static final String KEY_FIRST_TIME = "com.spp.aluradmi.first_time";
    static final String KEY = "com.spp.aluradmi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isFirstRun = getSharedPreferences(KEY, Context.MODE_PRIVATE).getBoolean(KEY_FIRST_TIME, true);
        if (isFirstRun){
            if (haveNetworkConnection()){
                new SetupData(SplashActivity.this).execute();

                getSharedPreferences(KEY, Context.MODE_PRIVATE).edit()
                        .putBoolean(KEY_FIRST_TIME, false).commit();
            } else {
                showNoConnectionDialog(SplashActivity.this);
            }


        }else{
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public static void showNoConnectionDialog(Context ctx1) {
        final Context ctx = ctx1;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage("Tidak ada koneksi internet");
        builder.setTitle("Need Internet Connection");
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }
    private class SetupData extends AsyncTask<Void, Void, Void>{
        private Context context;
        private final String TAG = SetupData.class.getSimpleName();
        private ProgressDialog dialog;
        private static final String url = "http://aluradmi.pe.hu/data/semua";
        public SetupData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Sedang mengunduh data");
            dialog.show();
            dialog.setCancelable(false);
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

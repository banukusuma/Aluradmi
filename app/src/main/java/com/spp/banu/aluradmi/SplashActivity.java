package com.spp.banu.aluradmi;

import android.app.AlarmManager;
import android.app.AlertDialog;

import android.app.PendingIntent;
import android.content.Context;


import android.content.DialogInterface;
import android.content.Intent;


import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.spp.banu.aluradmi.httpcall.AluradmiRestClient;
import com.spp.banu.aluradmi.httpcall.CheckNetwork;

import com.spp.banu.aluradmi.httpcall.GetAllData;
import com.spp.banu.aluradmi.service.ScheduleAlarmService;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;


public class SplashActivity extends AppCompatActivity {
    static SharedPreferences preferences;
    private static final String TAG = "splashactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(SetupActivity.KEY, Context.MODE_PRIVATE);
        Log.e("splashactivity", "onCreate: di splash");
        //scheduleAlarm();
        long millies = preferences.getLong(SetupActivity.KEY_DATE_SYNC, new Date().getTime());
        DateTime last_date_sync = new DateTime(millies);
        Log.e(TAG, "onCreate: last date sync " + last_date_sync );
        Log.e(TAG, "onCreate: beda hari with joda " +
                Days.daysBetween(last_date_sync.toLocalDate(), new DateTime().toLocalDate()).getDays());

        boolean isFirstRun = checkFirstRun();
        if (isFirstRun) {
            Intent intent2 = new Intent(SplashActivity.this, SetupActivity.class);
            startActivity(intent2);
            finish();
        } else {
            ReuniJurusan reuniJurusan = new ReuniJurusan(this);
            boolean isSelectedJurusan = reuniJurusan.isSelectedJurusan();
            if (isSelectedJurusan){
                startJurusanActivity();
            }else {
                startMainActivity();
            }

        }


    }

    public void startMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void startJurusanActivity(){
        Intent intent = new Intent(this, JurusanActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean checkFirstRun(){
        boolean isFirstRun = preferences.getBoolean(SetupActivity.KEY_FIRST_TIME, true);
        return isFirstRun;
    }

    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }


}

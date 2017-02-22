package com.spp.banu.aluradmi;

import android.app.AlertDialog;

import android.content.Context;


import android.content.DialogInterface;
import android.content.Intent;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.spp.banu.aluradmi.httpcall.CheckNetwork;

import com.spp.banu.aluradmi.httpcall.GetAllData;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

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


public class SplashActivity extends AppCompatActivity {
    static SharedPreferences preferences;
    private static final String TAG = "splashactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(SetupActivity.KEY, Context.MODE_PRIVATE);
        Log.e("splashactivity", "onCreate: di splash");
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d("splashactivity", "Key: " + key + " Value: " + value);
            }
        }
        long millies = preferences.getLong(SetupActivity.KEY_DATE_SYNC, new Date().getTime());
        Date date = new Date(millies);
        Log.e(TAG, "onCreate: date in date " + date);
        String lala = new SimpleDateFormat("dd-mm-yyyy").format(date);
        Log.e(TAG, "onCreate: date after format" + lala);
        boolean isFirstRun = checkFirstRun();
        if (isFirstRun) {
            Intent intent = new Intent(SplashActivity.this, SetupActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, SinkronisasiService.class);
            startService(intent);
            startMainActivity();
        }
        LocalDate localDate = new LocalDate(2016,11,10);
        DateTime dateTime1 = new DateTime(millies);
        Log.e(TAG, "onCreate: date in datetime joda " + localDate);
        Log.e(TAG, "onCreate: beda hari with joda " +
                Days.daysBetween(dateTime1.toLocalDate(), new DateTime().toLocalDate()).getDays());
    }

    public void startMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
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

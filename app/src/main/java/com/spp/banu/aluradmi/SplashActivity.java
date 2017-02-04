package com.spp.banu.aluradmi;

import android.app.AlertDialog;

import android.content.Context;


import android.content.DialogInterface;
import android.content.Intent;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.spp.banu.aluradmi.httpcall.CheckNetwork;

import com.spp.banu.aluradmi.httpcall.GetAllData;



public class SplashActivity extends AppCompatActivity {
    static final String KEY_FIRST_TIME = "com.spp.aluradmi.first_time";
    static final String KEY = "com.spp.aluradmi.first";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstRun = checkFirstRun();
        if (isFirstRun){
            Intent intent = new Intent(SplashActivity.this, SetupActivity.class);
            startActivity(intent);
            finish();
        }else{
            startMainActivity();
        }
    }

    public void startMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean checkFirstRun(){
        final SharedPreferences preferences = getSharedPreferences(KEY,Context.MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean(KEY_FIRST_TIME, true);

        if (isFirstRun){
            return true;
        }
        return false;
    }
}

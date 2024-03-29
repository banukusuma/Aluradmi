package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spp.banu.aluradmi.service.ScheduleAlarmService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 01/03/17.
 */

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_sinkronisasi;
    SharedPreferences preferences;
    private int mode_alarm;
    private  String select_text;
    int selected_last;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Pengaturan");
        preferences = getSharedPreferences(SetupActivity.KEY, Context.MODE_PRIVATE);
        int selected_from_pref = preferences.getInt(SetupActivity.KEY_MODE_ALARM, 1);
        selected_last = selected_from_pref - 1;
        List<String> periode = new ArrayList<>();
        periode.add("1 Hari");
        periode.add("7 Hari");
        periode.add("15 Hari");
        periode.add("30 Hari");
        ImageView logo_sync = (ImageView) findViewById(R.id.icon_setting_sinkronisasi);
        TextView tulisan_sync = (TextView) findViewById(R.id.ket_sinkronisasi_Setting);
        tulisan_sync.setText("Sinkronisasi dilakukan setiap : ");
        logo_sync.setImageResource(R.drawable.ic_sync_black_24dp);
        //Picasso.with(this).load(R.drawable.ic_sync_black_24dp).resize(56,56).into(logo_sync);
        spinner_sinkronisasi = (Spinner) findViewById(R.id.spinner_sinkronisasi_setting);
        spinner_sinkronisasi.setOnItemSelectedListener(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, periode);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sinkronisasi.setAdapter(arrayAdapter);
        spinner_sinkronisasi.setSelection(selected_last);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        select_text = adapterView.getItemAtPosition(i).toString();
        if (i != selected_last){
            selected_last = i;
            switch (select_text) {
                case "1 Hari":
                    mode_alarm = 1;
                    savePreferences();
                    break;
                case "7 Hari":
                    mode_alarm = 2;
                    savePreferences();
                    break;
                case "15 Hari":
                    mode_alarm = 3;
                    savePreferences();
                    break;
                case "30 Hari" :
                    mode_alarm = 4;
                    savePreferences();
                    break;
                default:
                    break;
            }
        }
        /*
        switch (select_text) {
            case "1 Hari":
                mode_alarm = 1;
                break;
            case "7 Hari":
                mode_alarm = 2;
                break;
            case "15 Hari":
                mode_alarm = 3;
                break;
            case "30 Hari" :
                mode_alarm = 4;
                break;
            default:
                break;
        }
        */
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void savePreferences(){
        Intent intent = new Intent(this, ScheduleAlarmService.class);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SetupActivity.KEY_MODE_ALARM, mode_alarm);
        editor.commit();
        startService(intent);
        Toast.makeText(this, "Sinkronisasi akan dilakukan setiap " + select_text + " sekali ", Toast.LENGTH_SHORT).show();
    }
}

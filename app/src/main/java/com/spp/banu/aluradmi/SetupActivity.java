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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spp.banu.aluradmi.httpcall.CheckNetwork;
import com.spp.banu.aluradmi.httpcall.GetAllData;
import com.spp.banu.aluradmi.service.ScheduleAlarmService;

import java.util.Date;

public class SetupActivity extends AppCompatActivity implements AsyncBooleanListener {
    private static final String TAG = "SetupActivity";
    private ProgressBar progressBar;
    private TextView textView;
    private Button button;
    private ImageView sad;
    private boolean isDialogOpen;
    public static final String KEY_FIRST_TIME = "com.spp.aluradmi.first_time";
    public static final String KEY_DATE_SYNC = "com.spp.aluradmi.date.sinkronisasi";
    public static final String KEY_MODE_ALARM = "com.spp.aluradmi.banu.mode.alarm.service";
    public static final String KEY = "com.spp.aluradmi.preferences.data.aplikasi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSetup);
        progressBar.setVisibility(View.VISIBLE);
        textView = (TextView) findViewById(R.id.download_textview);
        sad = (ImageView) findViewById(R.id.imageView_sad);
        button = (Button) findViewById(R.id.buttonRetry);
        isDialogOpen = false;
        button.setText(R.string.coba);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilData(SetupActivity.this);
                button.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void showNoConnectionDialog(Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(false);
        builder.setMessage("Setup Aplikasi Membutuhkan Koneksi Internet");
        builder.setTitle("Tidak Ada Koneksi Internet");
        builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Buka Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.show();
        isDialogOpen = true;
    }
    public void startMainActivity(){
        Intent intent = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("com.spp.aluradmi.Setup", "onStart");
        textView.setText(R.string.unduh_data_awal);
        CheckNetwork network = new CheckNetwork(this);
        if(!network.isNetworkAvailable()){
            if (!isDialogOpen){
                showNoConnectionDialog(this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        button.setVisibility(View.INVISIBLE);
        ambilData(this);
        Log.i("com.spp.aluradmi.Setup", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("com.spp.aluradmi.Setup", "onPause: ");
    }

    public void ambilData(final Context ctx){
        final Context context = ctx;
        progressBar.setVisibility(View.VISIBLE);
        sad.setVisibility(View.INVISIBLE);
            GetAllData getAllData = new GetAllData(context, this);
            getAllData.execute();

    }

    public void writeFirstRunPreferences(){
        Date currentDate = new Date();
        final SharedPreferences preferences = getSharedPreferences(KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_MODE_ALARM, 1);
        editor.putLong(KEY_DATE_SYNC, currentDate.getTime());
        editor.putBoolean(KEY_FIRST_TIME, false);
        editor.commit();
    }

    @Override
    public void getResult(boolean result) {
        if (result != false){
            writeFirstRunPreferences();
            Intent intent = new Intent(this, ScheduleAlarmService.class);
            startService(intent);
            startJurusanActivity();
        }else{
            button.setVisibility(View.VISIBLE);
            sad.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText(R.string.gagal_mengunduh);
        }


    }
    private void startJurusanActivity(){
        Intent intent = new Intent(this, JurusanActivity.class);
        startActivity(intent);
        finish();
    }

}

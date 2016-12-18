package com.spp.banu.aluradmi;

import android.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spp.banu.aluradmi.httpcall.CheckNetwork;
import com.spp.banu.aluradmi.httpcall.GetAllData;

public class SetupActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private Button button;
    static final String KEY_FIRST_TIME = "com.spp.aluradmi.first_time";
    static final String KEY = "com.spp.aluradmi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        progressBar = (ProgressBar) findViewById(R.id.progressBarSetup);
        progressBar.setVisibility(View.VISIBLE);
        textView = (TextView) findViewById(R.id.download_textview);

        button = (Button) findViewById(R.id.buttonRetry);

        button.setText("Retry");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilData(SetupActivity.this);
                button.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void showNoConnectionDialog(final Context ctx) {
        final Context context = ctx;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("Setup Aplikasi Membutuhkan Koneksi Internet");
        builder.setTitle("Tidak Ada Koneksi Internet");
        builder.setPositiveButton("Buka Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.show();
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
        textView.setText("Setup");
        CheckNetwork network = new CheckNetwork(this);
        if(!network.isNetworkAvailable()){
            showNoConnectionDialog(this);
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
            GetAllData getAllData = new GetAllData(context, new AsyncBooleanListener() {
                @Override
                public void getResult(boolean result) {
                    if (result != false){
                        writeFirstRunPreferences();
                        startMainActivity();
                    }else{
                        button.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setText("Gagal Mengunduh Data");
                    }
                }
            }, textView);
            getAllData.execute();

    }

    public void writeFirstRunPreferences(){
        final SharedPreferences preferences = getSharedPreferences(KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_TIME, false);
        editor.commit();
    }
}

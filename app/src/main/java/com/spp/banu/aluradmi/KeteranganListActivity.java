package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.spp.banu.aluradmi.SingleFragmentActivity;
import com.spp.banu.aluradmi.fragment.AlurListFragment;
import com.spp.banu.aluradmi.fragment.KeteranganListFragment;

/**
 * Created by banu on 05/12/16.
 */

public class KeteranganListActivity extends AppCompatActivity {
    public static final String EXTRA_ID_ALUR = "com.spp.aluradmi.keteranganIntent.id_alur";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new KeteranganListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext, int id_alur){
        Intent intent = new Intent(packageContext, KeteranganListActivity.class);
        intent.putExtra(EXTRA_ID_ALUR,id_alur);
        return intent;
    }
}

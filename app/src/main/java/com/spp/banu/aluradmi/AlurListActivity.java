package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.spp.banu.aluradmi.fragment.AlurListFragment;

/**
 * Created by banu on 05/12/16.
 */

public class AlurListActivity extends AppCompatActivity implements AlurListFragment.onAlurListSelected{
    public static final String EXTRA_ID_KATEGORI = "com.spp.banu.aluradmi.alurIntent.id_kategori";
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
            fragment = new AlurListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext,int id_kategori){
        Intent intent = new Intent(packageContext, AlurListActivity.class);
        intent.putExtra(EXTRA_ID_KATEGORI, id_kategori);
        return intent;
    }

    @Override
    public void onSelectAlur(int id_alur) {
        if (id_alur != 0 ){
            Intent intent = KeteranganPagerActivity.newIntent(this, id_alur);
            startActivity(intent);
        }
    }
}

package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.spp.banu.aluradmi.fragment.AlurListFragment;
import com.spp.banu.aluradmi.fragment.JurusanDialogFragment;

/**
 * Created by banu on 05/12/16.
 */

public class AlurListActivity extends AppCompatActivity implements AlurListFragment.onAlurListSelected{
    public static final String EXTRA_ID_KATEGORI = "com.spp.banu.aluradmi.alurIntent.id_kategori";
    private static final String ALUR_FRAGMENT_TAG = "alur_list_fragment";
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
                    .add(R.id.fragment_container, fragment,ALUR_FRAGMENT_TAG)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.alur_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_jurusan_alur){
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            JurusanDialogFragment dialogFragment = new JurusanDialogFragment();
            dialogFragment.show(fragmentManager, "jurusanDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

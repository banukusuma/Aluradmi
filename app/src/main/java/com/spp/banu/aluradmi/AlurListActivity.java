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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.fragment.AlurListFragment;
import com.spp.banu.aluradmi.fragment.JurusanDialogFragment;
import com.spp.banu.aluradmi.model.Alur;
import com.spp.banu.aluradmi.model.Jurusan;

import java.util.List;

/**
 * Created by banu on 05/12/16.
 */

public class AlurListActivity extends AppCompatActivity implements AlurListFragment.onAlurListSelected{
    private static final String ALUR_FRAGMENT_TAG = "alur_list_fragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView image_kosong = (ImageView) findViewById(R.id.imageView_empty_data);
        TextView text_kosong = (TextView) findViewById(R.id.textView_kosong);
        SharedPreferences preferences = getSharedPreferences(MainActivity.KEY_PREFERENCE, Context.MODE_PRIVATE);
        int id_kategori = preferences.getInt(MainActivity.KEY_ID_KATEGORI,0);
        ReuniAlur reuniAlur = new ReuniAlur(this);
        ReuniJurusan reuniJurusan = new ReuniJurusan(this);
        Jurusan jurusan = reuniJurusan.getSelectJurusan();
        List<Alur> alurList = reuniAlur.getAlurs(
                AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                        AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                new String[]{Integer.toString(id_kategori), Integer.toString(jurusan.getId_jurusan())}
        );
        ReuniKategori reuniKategori = new ReuniKategori(this);
        String title = reuniKategori.getKategori(id_kategori).getNama();
        if (title != null){
           this.setTitle("Alur " + title);
        } else {
            this.setTitle("Alur");
        }
        if (!alurList.isEmpty()){
            image_kosong.setVisibility(View.INVISIBLE);
            text_kosong.setVisibility(View.INVISIBLE);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            if (fragment == null) {
                fragment = new AlurListFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment,ALUR_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    @Override
    public void onSelectAlur(int id_alur) {
        if (id_alur != 0 ){
            Intent intent = KeteranganPagerActivity.newIntent(this, id_alur);
            startActivity(intent);
        }
    }

}

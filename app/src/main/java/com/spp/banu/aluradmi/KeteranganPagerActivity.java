package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.astuetz.PagerSlidingTabStrip;
import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;
import com.spp.banu.aluradmi.fragment.KeteranganFragment;
import com.spp.banu.aluradmi.model.Alur;
import com.spp.banu.aluradmi.model.Keterangan;

import java.util.List;

/**
 * Created by banu on 06/01/17.
 */

public class KeteranganPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Keterangan> keteranganList;
    private PagerAdapter pagerAdapter;
    private final static String TAG = "keteranganPagerActivity";
    private ImageView image_kosong;
    private TextView text_kosong;
    private static final String KEY_ID_KATEGORI = "com.spp.banu.aluradmi.key.id.kategori";
    private static final String KEY_PREFERENCE = "com.spp.banu.aluradmi.kategori.pref";
    private boolean canChecked;
    private static final String EXTRA_ID_ALUR = "com.spp.banu.aluradmi.keteranganIntent.id_alur";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keterangan_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image_kosong = (ImageView) findViewById(R.id.imageView_keterangan_kosong);
        text_kosong = (TextView) findViewById(R.id.textView_keterangan_kosong);
        Log.e(TAG, "onCreate: " + getIntent().getIntExtra(EXTRA_ID_ALUR, 0) );
         int id_alur = getIntent().getIntExtra(EXTRA_ID_ALUR, 0);
        ReuniAlur reuniAlur = new ReuniAlur(this);
        Alur alur = reuniAlur.getAlur(id_alur);
        getSupportActionBar().setTitle(alur.getNama());
        boolean progress = isBeforeAlurDone(alur);
        Log.e(TAG, "onCreate: boolean progress " + progress );
        if (progress || alur.getUrut() == 1){
            canChecked = true;
        } else {
            canChecked = false;
        }

        ReuniKeterangan reuniKeterangan = new ReuniKeterangan(this);
        keteranganList = reuniKeterangan.getKeteranganList(
                KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? ",
            new String[]{Integer.toString(id_alur)}
        );
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.keterangan_view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        if (keteranganList.isEmpty()){
            image_kosong.setVisibility(View.VISIBLE);
            text_kosong.setVisibility(View.VISIBLE);
        }else {
            image_kosong.setVisibility(View.INVISIBLE);
            text_kosong.setVisibility(View.INVISIBLE);
            viewPager.setAdapter(pagerAdapter);
            PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
            tabStrip.setViewPager(viewPager);
        }
    }

    public static Intent newIntent(Context packagecontext, int id_alur){
        Log.e(TAG, "newIntent: " + id_alur );
        Intent intent = new Intent(packagecontext, KeteranganPagerActivity.class);
        intent.putExtra(EXTRA_ID_ALUR, id_alur);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    private boolean isBeforeAlurDone(Alur alur){
        if (alur.getUrut() != 1){
            SharedPreferences preferences = this.getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
            int id_kategori = preferences.getInt(KEY_ID_KATEGORI,0);
            ReuniAlur reuniAlur = new ReuniAlur(this);
            ReuniJurusan reuniJurusan = new ReuniJurusan(this);
            List<Alur> alurList = reuniAlur.getAlurs(
                    AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                            AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                    new String[]{Integer.toString(id_kategori),
                    Integer.toString(reuniJurusan.getSelectJurusan().getId_jurusan())}
            );
            Alur alurBefore = new Alur();
            if (!alurList.isEmpty()){
                for (int i = 0; i < alurList.size();i++){
                    Log.e(TAG, "id_alur yang dilempar ke isbeforeAlur " + alur.getId_alur() );
                    Log.e(TAG, "list_alur_keterangan_pager " + alurList.get(i).getId_alur());
                    if (alurList.get(i).getId_alur() == alur.getId_alur()){
                        int j = i - 1;
                        Log.e(TAG, "alur sebelumnya : " + alurList.get(j));
                        alurBefore = alurList.get(j);
                        break;
                    }
                }
            }
            int progress = Math.round(alurBefore.getProgress());
            Log.e(TAG, "alur before " + alurBefore.getNama());
            Log.e(TAG, "progress alur before " + alurBefore.getProgress());
            if (progress == 100){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (viewPager != null){
            if (viewPager.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
            } else {
                // Otherwise, select the previous step.
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        }else {
            super.onBackPressed();
        }


    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Keterangan keterangan = keteranganList.get(position);
            return KeteranganFragment.newInstance(keterangan.getId_keterangan(), canChecked, keteranganList.size());
        }

        @Override
        public int getCount() {
            return keteranganList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return keteranganList.get(position).getNama();
        }
    }

}

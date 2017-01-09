package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.Intent;
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
    private final String TAG = "keteranganPagerActivity";
    private int id_alur;
    private boolean canChecked;
    private static final String EXTRA_ID_ALUR = "com.spp.banu.aluradmi.keteranganIntent.id_alur";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keterangan_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id_alur = getIntent().getIntExtra(EXTRA_ID_ALUR, 0);
        ReuniAlur reuniAlur = new ReuniAlur(this);
        Alur alur = reuniAlur.getAlur(id_alur);
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
        if (keteranganList.isEmpty()){
            Keterangan keterangan = new Keterangan();
            keterangan.setId_keterangan(0);
            keteranganList.add(keterangan);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.keterangan_view_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        viewPager.setAdapter(pagerAdapter);
    }

    public static Intent newIntent(Context packagecontext, int id_alur){
        Intent intent = new Intent(packagecontext, KeteranganPagerActivity.class);
        intent.putExtra(EXTRA_ID_ALUR, id_alur);
        return intent;
    }

    private boolean isBeforeAlurDone(Alur alur){
        if (alur.getUrut() != 1){
            ReuniAlur reuniAlur = new ReuniAlur(this);
            ReuniJurusan reuniJurusan = new ReuniJurusan(this);
            List<Alur> alurList = reuniAlur.getAlurs(
                    AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                            AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                    new String[]{Integer.toString(StorageId.id_kategori),
                    Integer.toString(reuniJurusan.getSelectJurusan().getId_jurusan())}
            );
            Alur alurBefore = new Alur();
            if (!alurList.isEmpty()){
                for (int i = 0; i < alurList.size();i++){
                    if (alurList.get(i).getId_alur() == alur.getId_alur()){
                        int j = i - 1;
                        alurBefore = alurList.get(j);
                    }
                }
            }
            int progress = Math.round(alurBefore.getProgress());
            Log.e(TAG, "alur before " + alurBefore.getNama());
            Log.e(TAG, "progress alur before" + alurBefore.getProgress());
            if (progress == 100){
                return true;
            }
        }
        return false;
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
            int posisi = position +1 ;
            return "Halaman " + posisi;
        }
    }

}

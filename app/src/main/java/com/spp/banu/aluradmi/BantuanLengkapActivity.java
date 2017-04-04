package com.spp.banu.aluradmi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.spp.banu.aluradmi.fragment.BantuanLengkapMaster;
import com.spp.banu.aluradmi.fragment.OpeningBantuanLengkap;

/**
 * Created by banu on 02/04/17.
 */

public class BantuanLengkapActivity extends AppCompatActivity implements OpeningBantuanLengkap.onBantuanLengkapSelected {
    FragmentManager fragmentManager;
    Fragment fragment;
    private int choose_fragment;
    private final static String KEY_CHOOSE_FRAGMENT = "choose_fragment_bantuan_lengkap";
    private final static String TAG_OPENING = "opening_bantuan";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_lengkap_baru);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Bantuan");
        fragmentManager = getSupportFragmentManager();

        if (fragment == null){
            fragment = new OpeningBantuanLengkap();
            addFragment(fragment, TAG_OPENING);
            choose_fragment = 0;
        }

        if (savedInstanceState != null){
            if (savedInstanceState.keySet().contains(KEY_CHOOSE_FRAGMENT)){
                choose_fragment = savedInstanceState.getInt(KEY_CHOOSE_FRAGMENT);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        needChangeFragment(choose_fragment);
    }

    private void replaceFragment (Fragment fragment, String fragmentTag){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_bantuan_baru, fragment, fragmentTag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
    private void addFragment(Fragment fragment, String fragmentTag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_bantuan_baru, fragment, fragmentTag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
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

    @Override
    public void onBackPressed() {
        OpeningBantuanLengkap bantuanLengkap = (OpeningBantuanLengkap) getSupportFragmentManager().findFragmentByTag(TAG_OPENING);
        if (bantuanLengkap != null && bantuanLengkap.isVisible()){
            super.onBackPressed();
        }else {
            fragment = new OpeningBantuanLengkap();
            replaceFragment(fragment, TAG_OPENING);
            choose_fragment = 0;
        }


    }
    @Override
    public void onSelectItemBantuan(int id_bantuan) {
        needChangeFragment(id_bantuan);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_CHOOSE_FRAGMENT,choose_fragment );
        super.onSaveInstanceState(outState);
    }

    private void needChangeFragment(int no_fragment){
            switch (no_fragment){
                case 0:
                    fragment = new OpeningBantuanLengkap();
                    replaceFragment(fragment, TAG_OPENING);
                    choose_fragment = 0;
                    break;
                case 1:
                    fragment =  BantuanLengkapMaster.newInstance(1);
                    replaceFragment(fragment,"bantuan_1");
                    choose_fragment = 1;
                    break;
                case 2:
                    fragment =  BantuanLengkapMaster.newInstance(2);
                    replaceFragment(fragment,"bantuan_2");
                    choose_fragment = 2;
                    break;
                case 3:
                    fragment =  BantuanLengkapMaster.newInstance(3);
                    replaceFragment(fragment,"bantuan_3");
                    choose_fragment = 3;
                    break;
                case 4:
                    fragment =  BantuanLengkapMaster.newInstance(4);
                    replaceFragment(fragment,"bantuan_4");
                    choose_fragment = 4;
                    break;
                case 5:
                    fragment =  BantuanLengkapMaster.newInstance(5);
                    replaceFragment(fragment,"bantuan_5");
                    choose_fragment = 5;
                    break;
                case 6:
                    fragment =  BantuanLengkapMaster.newInstance(6);
                    replaceFragment(fragment,"bantuan_6");
                    choose_fragment = 6;
                    break;
                case 7:
                    fragment =  BantuanLengkapMaster.newInstance(7);
                    replaceFragment(fragment,"bantuan_7");
                    choose_fragment = 7;
                    break;
                case 8:
                    fragment =  BantuanLengkapMaster.newInstance(8);
                    replaceFragment(fragment,"bantuan_8");
                    choose_fragment = 8;
                    break;
                case 9:
                    fragment =  BantuanLengkapMaster.newInstance(9);
                    replaceFragment(fragment,"bantuan_9");
                    choose_fragment = 9;
                    break;
            }
    }
}

package com.spp.banu.aluradmi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.spp.banu.aluradmi.fragment.BantuanLengkapMaster;
import com.spp.banu.aluradmi.fragment.EndingBantuanSingkatFragment;
import com.spp.banu.aluradmi.fragment.OpeningBantuanLengkap;
import com.spp.banu.aluradmi.fragment.OpeningBantuanSingkatFragment;

/**
 * Created by banu on 03/04/17.
 */

public class BantuanSingkatActivity extends AppCompatActivity implements EndingBantuanSingkatFragment.onEndingBantuanChoice {
    Fragment fragment;
    FragmentManager fragmentManager;
    private int choose_fragment;
    private final static String KEY_SELECT_FRAGMENT = "select_fragment_bantuan_singkat";
    private final static String TAG_OPENING = "opening_bantuan_singkat";
    private final static String TAG_ENDING = "ending_bantuan_singkat";
    Button next;
    Button prev;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_singkat);
        next = (Button) findViewById(R.id.button_selanjutnya);
        prev = (Button) findViewById(R.id.button_sebelumnya);
        fragmentManager = getSupportFragmentManager();
        if (fragment == null){
            fragment = new OpeningBantuanSingkatFragment();
            addFragment(fragment, TAG_OPENING);
            choose_fragment = 0;
        }
        if (savedInstanceState != null){
            if (savedInstanceState.keySet().contains(KEY_SELECT_FRAGMENT)){
                choose_fragment = savedInstanceState.getInt(KEY_SELECT_FRAGMENT);
            }

        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_fragment = choose_fragment + 1;
                needChangeFragment(choose_fragment);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_fragment = choose_fragment - 1;
                needChangeFragment(choose_fragment);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (choose_fragment == 0){
            super.onBackPressed();
        }else {
            choose_fragment = choose_fragment - 1;
            needChangeFragment(choose_fragment);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        needChangeFragment(choose_fragment);
    }

    private void replaceFragment (Fragment fragment, String fragmentTag){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.bantuan_singkat_framelayout, fragment, fragmentTag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
    private void addFragment(Fragment fragment, String fragmentTag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.bantuan_singkat_framelayout, fragment, fragmentTag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private void needChangeFragment(int no_fragment){
        switch (no_fragment){
            case 0:
                fragment = new OpeningBantuanSingkatFragment();
                replaceFragment(fragment, TAG_OPENING);
                choose_fragment = 0;
                prev.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 1:
                fragment =  BantuanLengkapMaster.newInstance(1);
                replaceFragment(fragment,"bantuan_1");
                choose_fragment = 1;
                prev.setVisibility(View.VISIBLE);
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
                next.setVisibility(View.VISIBLE);
                break;
            case 6:
                fragment =  new EndingBantuanSingkatFragment();
                replaceFragment(fragment,"ending_bantuan_singkat");
                choose_fragment = 6;
                next.setVisibility(View.INVISIBLE);
                break;

        }
    }

    @Override
    public void onTombolKlik(boolean hasil) {
        if (hasil){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            choose_fragment = 0 ;
            needChangeFragment(choose_fragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SELECT_FRAGMENT,choose_fragment );
        super.onSaveInstanceState(outState);
    }
}

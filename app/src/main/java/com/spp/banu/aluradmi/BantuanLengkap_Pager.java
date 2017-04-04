package com.spp.banu.aluradmi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.spp.banu.aluradmi.fragment.BantuanLengkapMaster;
import com.spp.banu.aluradmi.fragment.OpeningBantuanLengkap;

/**
 * Created by banu on 02/04/17.
 */

public class BantuanLengkap_Pager extends AppCompatActivity implements OpeningBantuanLengkap.onBantuanLengkapSelected{
    private ViewPager viewPager;
    private final static String TAG = "BantuanLengkap";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_lengkap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Bantuan");
        viewPager = (ViewPager) findViewById(R.id.bantuan_lengkap_view_pager);
        FragmentPagerAdapter pagerAdapter = new BantuanLengkapAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

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
        if (viewPager.getCurrentItem() == 0){
            super.onBackPressed();
        }else {
            viewPager.setCurrentItem(0);
        }

    }

    @Override
    public void onSelectItemBantuan(int id_bantuan) {
        Log.e(TAG, "onSelectItemBantuan: " + id_bantuan );
        viewPager.setCurrentItem(id_bantuan);
    }


    private static class BantuanLengkapAdapter extends FragmentPagerAdapter{
        private static final int JML = 10;
        public BantuanLengkapAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new OpeningBantuanLengkap();
                case 1:
                    return BantuanLengkapMaster.newInstance(1);
                case 2:
                    return BantuanLengkapMaster.newInstance(2);
                case 3:
                    return BantuanLengkapMaster.newInstance(3);
                case 4:
                    return BantuanLengkapMaster.newInstance(4);
                case 5:
                    return BantuanLengkapMaster.newInstance(5);
                case 6:
                    return BantuanLengkapMaster.newInstance(6);
                case 7:
                    return BantuanLengkapMaster.newInstance(7);
                case 8:
                    return BantuanLengkapMaster.newInstance(8);
                case 9:
                    return BantuanLengkapMaster.newInstance(9);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return JML;
        }
    }
}

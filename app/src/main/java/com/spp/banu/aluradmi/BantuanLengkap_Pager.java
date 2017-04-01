package com.spp.banu.aluradmi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.spp.banu.aluradmi.fragment.BantuanLengkapMaster;

/**
 * Created by banu on 02/04/17.
 */

public class BantuanLengkap_Pager extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_lengkap);
        viewPager = (ViewPager) findViewById(R.id.bantuan_lengkap_view_pager);
        FragmentPagerAdapter pagerAdapter = new BantuanLengkapAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

    }


    private static class BantuanLengkapAdapter extends FragmentPagerAdapter{
        private static final int JML = 9;
        public BantuanLengkapAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return BantuanLengkapMaster.newInstance(1);
                case 1:
                    return BantuanLengkapMaster.newInstance(2);
                case 2:
                    return BantuanLengkapMaster.newInstance(3);
                case 3:
                    return BantuanLengkapMaster.newInstance(4);
                case 4:
                    return BantuanLengkapMaster.newInstance(5);
                case 5:
                    return BantuanLengkapMaster.newInstance(6);
                case 6:
                    return BantuanLengkapMaster.newInstance(7);
                case 7:
                    return BantuanLengkapMaster.newInstance(8);
                case 8:
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

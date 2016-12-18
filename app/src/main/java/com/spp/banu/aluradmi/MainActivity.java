package com.spp.banu.aluradmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.spp.banu.aluradmi.fragment.JurusanDialogFragment;
import com.spp.banu.aluradmi.fragment.JurusanListFragment;
import com.spp.banu.aluradmi.fragment.KategoriListFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.content_main);
            if (fragment == null){
                fragment = new KategoriListFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.content_main, fragment)
                        .commit();
            }

        //menambahkan fragment listKategori diawal aplikasi
        //untuk cadangan kalau tidak bisa menambahkan lewat click navigation drawer
        //setelah navigation drawer dan seluruh fragment jadi
        // tolong cari cara lain agar fragment listkategori langsung ditambahkan






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_jurusan){
            FragmentManager fragmentManager = getSupportFragmentManager();
            JurusanDialogFragment dialogFragment = new JurusanDialogFragment();
            dialogFragment.show(fragmentManager, "jurusanDialog");
            dialogFragment.setCancelable(false);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_kategori) {
            // Handle navigation view item clicks here.
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.content_main);
            fragment = new KategoriListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
        } else if (id == R.id.nav_lokasi) {

        }  else if (id == R.id.nav_bantuan) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ReuniJurusan reuniJurusan = new ReuniJurusan(this);
        boolean isFirstRun = reuniJurusan.isSelectedJurusan();
        Log.i("" +this, "onResume: " + isFirstRun);
        if (isFirstRun){
            FragmentManager fragmentManager = getSupportFragmentManager();
            JurusanDialogFragment dialogFragment = new JurusanDialogFragment();
            dialogFragment.show(fragmentManager, "jurusanDialog");
            dialogFragment.setCancelable(false);
        }
    }
}

package com.spp.banu.aluradmi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.spp.banu.aluradmi.fragment.AlurListFragment;
import com.spp.banu.aluradmi.fragment.JurusanDialogFragment;
import com.spp.banu.aluradmi.fragment.JurusanListFragment;
import com.spp.banu.aluradmi.fragment.KategoriListFragment;
import com.spp.banu.aluradmi.fragment.KeteranganListFragment;
import com.spp.banu.aluradmi.fragment.LokasiFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , KategoriListFragment.onKategoriListSelectListener,
    AlurListFragment.onAlurListSelected   {
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = mDrawerTitle = getTitle();
        if (googleServiceAvailable()){
            Toast.makeText(this, "Start with Google Play Services", Toast.LENGTH_SHORT).show();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.content_main);
            if (fragment == null){
                fragment = new KategoriListFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.content_main, fragment)
                        .commit();
            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public boolean googleServiceAvailable(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        } else if (apiAvailability.isUserResolvableError(isAvailable)){
            Dialog dialog = apiAvailability.getErrorDialog(this,isAvailable,0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to Google Play Services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            toggle.setDrawerIndicatorEnabled(true);
        }

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
       if (id == R.id.menu_jurusan){
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.content_main);
        int id = item.getItemId();
        if (id == R.id.nav_kategori) {
            fragment = new KategoriListFragment();

        } else if (id == R.id.nav_lokasi) {
            fragment = new LokasiFragment();

        }  else if (id == R.id.nav_bantuan) {

        } else if (id == R.id.nav_about) {

        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.commit();
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

    @Override
    public void onKategoriSelected(int id_kategori) {
        AlurListFragment fragment = new AlurListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(fragment.ALUR_ARG_ID_KATEGORI, id_kategori);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    private void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        toggle.setDrawerIndicatorEnabled(false);
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_main, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onSelectAlur(int id_alur) {
        KeteranganListFragment fragment = new KeteranganListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(fragment.KETERANGAN_ARG_ID_ALUR, id_alur);
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    public void showUpButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void hideUpButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}

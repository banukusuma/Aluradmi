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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spp.banu.aluradmi.fragment.AlurListFragment;
import com.spp.banu.aluradmi.fragment.JurusanDialogFragment;
import com.spp.banu.aluradmi.fragment.JurusanListFragment;
import com.spp.banu.aluradmi.fragment.KategoriListFragment;
import com.spp.banu.aluradmi.fragment.KeteranganListFragment;
import com.spp.banu.aluradmi.fragment.LokasiFragment;
import com.spp.banu.aluradmi.model.Lokasi;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , KategoriListFragment.onKategoriListSelectListener{
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private final static String TAG_kategori_fragment = "kategori_fragment";
    private final static String TAG_bantuan_fragment = "bantuan_fragment";
    private final static String TAG_tentang_fragment = "tentang_fragment";
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle = mDrawerTitle = getTitle();
        FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.content_main);
            if (fragment == null){
                fragment = new KategoriListFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.content_main, fragment, "kategori_fragment")
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
        Log.e("MainActivity", "backstakecount" + getSupportFragmentManager().getBackStackEntryCount());
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_kategori) {
            Fragment fragment = new KategoriListFragment();
            replaceFragment(fragment, TAG_kategori_fragment);
        } else if (id == R.id.nav_lokasi) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
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
        googleServiceAvailable();
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
        new StorageId(id_kategori);
        Intent intent = new Intent(this, AlurListActivity.class);
        startActivity(intent);
    }

    private void replaceFragment (Fragment fragment, String fragmentTag){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_main, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

    }

}

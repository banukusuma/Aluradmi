package com.spp.banu.aluradmi;

import android.app.Dialog;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.spp.banu.aluradmi.fragment.AboutFragment;

import com.spp.banu.aluradmi.fragment.BantuanFragment;
import com.spp.banu.aluradmi.fragment.HomeFragment;
import com.spp.banu.aluradmi.fragment.JurusanDialogFragment;

import com.spp.banu.aluradmi.model.Kategori;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements Drawer.OnDrawerItemClickListener {


    private boolean isFirstRun;

    private final static String TAG_home_fragment = "home_fragment";
    private final static String TAG_kategori_fragment = "kategori_fragment";
    private final static String TAG_bantuan_fragment = "bantuan_fragment";
    private final static String TAG_tentang_fragment = "tentang_fragment";
    private static long choose_fragment;
    Drawer result;
    AccountHeader resultHeader;
    FragmentManager fragmentManager;
    Fragment fragment;
    private static final String KEY_ID_KATEGORI = "com.spp.banu.aluradmi.key.id.kategori";
    private static final String KEY_PREFERENCE = "com.spp.banu.aluradmi.kategori.pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.content_main);

        //Pembuatan Account Header
        resultHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesClickable(false)
                .addProfiles(new ProfileDrawerItem().withName("Aluradmi").withIcon(FontAwesome.Icon.faw_paper_plane_o))
                .build();
        //Penambahan Kategori
        ReuniKategori reuniKategori = new ReuniKategori(this);
        List<Kategori> kategoriList = reuniKategori.getKategoris(null,null);
        if (kategoriList.size() == 0){
            Kategori kategori = new Kategori();
            kategori.setId_kategori(0);
            kategori.setNama("Data Masih Kosong");
            kategoriList.add(kategori);
        }
        //membuat drawer item
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(100000).withName(R.string.home)
                .withIcon(GoogleMaterial.Icon.gmd_home);
        ExpandableDrawerItem kategori_expand = new ExpandableDrawerItem().withName(R.string.kategori)
                .withIcon(GoogleMaterial.Icon.gmd_receipt).withIdentifier(200000).withSelectable(false);
        PrimaryDrawerItem itemLokasi = new PrimaryDrawerItem().withIdentifier(300000).withName(R.string.lokasi)
                .withIcon(GoogleMaterial.Icon.gmd_place);
        PrimaryDrawerItem itemAbout = new PrimaryDrawerItem().withIdentifier(500000).withName(R.string.about)
                .withIcon(GoogleMaterial.Icon.gmd_info_outline);
        PrimaryDrawerItem itemBantuan = new PrimaryDrawerItem().withIdentifier(400000).withName(R.string.bantuan)
                .withIcon(GoogleMaterial.Icon.gmd_help_outline);

        List<IDrawerItem> kategori_item_list = new ArrayList<>();

        //perulangan untuk memasukkan kategori ke dalam expand drawer
        //belum dimasukkan apabila data kosong
        for (Kategori kategori : kategoriList){
            if (kategori.getId_kategori() != 0){
                kategori_item_list.add(new SecondaryDrawerItem().withName(kategori.getNama()).withIdentifier(kategori.getId_kategori())
                        .withIcon(GoogleMaterial.Icon.gmd_receipt));
            }
        }
        kategori_expand.withSubItems(kategori_item_list);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(resultHeader)
                .withOnDrawerItemClickListener(this)
                .addDrawerItems(
                        itemHome,
                        kategori_expand,
                        itemLokasi,
                        new SectionDrawerItem().withSelectable(false).withName(R.string.info),
                        itemBantuan,
                        itemAbout
                )
                .build();
        result.setSelection(100000, true);
        choose_fragment = 100000;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        MenuItem searchitem = menu.findItem(R.id.menu_search_main);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, AlurSearchActivity.class)));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        return true;
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
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_jurusan){
           menuJurusan();
            return true;
        }
        /*
        else if(id == R.id.menu_search_main){
            onSearchRequested();
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleServiceAvailable();
        Log.i("" +this, "onResume: " + isFirstRun);
        ReuniJurusan reuniJurusan = new ReuniJurusan(this);
        isFirstRun = reuniJurusan.isSelectedJurusan();
        if (isFirstRun){
            menuJurusan();
        }
        result.setSelection(choose_fragment);
    }


    private void replaceFragment (Fragment fragment, String fragmentTag){
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
    }
    private void addFragment(Fragment fragment, String fragmentTag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_main, fragment, fragmentTag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private void menuJurusan(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        JurusanDialogFragment dialogFragment = new JurusanDialogFragment();
        dialogFragment.show(fragmentManager, "jurusanDialog");
        if (isFirstRun){
            dialogFragment.setCancelable(false);
        } else {
            dialogFragment.setCancelable(true);
        }
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        long id = drawerItem.getIdentifier();
        if (id == 100000){
            if (fragment == null){
                fragment = new HomeFragment();
                addFragment(fragment, TAG_home_fragment);
            } else {
                fragment = new HomeFragment();
                replaceFragment(fragment, TAG_home_fragment);
            }
            choose_fragment = id;
        } else if (id == 200000){

        }else if (id == 300000){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }else if (id == 400000){
            choose_fragment = id;
            fragment = new BantuanFragment();
            replaceFragment(fragment, TAG_bantuan_fragment);
            Toast.makeText(this,"Bantuan di klik", Toast.LENGTH_SHORT).show();
        } else if (id == 500000){
            choose_fragment = id;
            fragment = new AboutFragment();
            replaceFragment(fragment, TAG_tentang_fragment);
            Toast.makeText(this,"Tentang di klik", Toast.LENGTH_SHORT).show();
        } else {
            int id_kategori = (int) id;
            SharedPreferences preferences = getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(KEY_ID_KATEGORI, id_kategori);
            editor.commit();
            Intent intent = new Intent(this, AlurListActivity.class);
            startActivity(intent);
        }
        return false;
    }

}

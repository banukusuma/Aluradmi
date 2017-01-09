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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialize.util.UIUtils;
import com.spp.banu.aluradmi.fragment.AlurListFragment;
import com.spp.banu.aluradmi.fragment.JurusanDialogFragment;
import com.spp.banu.aluradmi.fragment.JurusanListFragment;
import com.spp.banu.aluradmi.fragment.KategoriListFragment;
import com.spp.banu.aluradmi.fragment.KeteranganListFragment;
import com.spp.banu.aluradmi.fragment.LokasiFragment;
import com.spp.banu.aluradmi.model.Kategori;
import com.spp.banu.aluradmi.model.Lokasi;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements KategoriListFragment.onKategoriListSelectListener, Drawer.OnDrawerItemClickListener {
    //NavigationView.OnNavigationItemSelectedListener ,
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private final static String TAG_kategori_fragment = "kategori_fragment";
    private final static String TAG_bantuan_fragment = "bantuan_fragment";
    private final static String TAG_tentang_fragment = "tentang_fragment";
    private ActionBarDrawerToggle toggle;
    Drawer result;
    AccountHeader resultHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);
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
        resultHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(new ProfileDrawerItem().withName("Aluradmi").withIcon(FontAwesome.Icon.faw_paper_plane_o))
                .build();
        PrimaryDrawerItem itemHome = new PrimaryDrawerItem().withIdentifier(1).withName("Home").withIcon(GoogleMaterial.Icon.gmd_home);
        ReuniKategori reuniKategori = new ReuniKategori(this);
        List<Kategori> kategoriList = reuniKategori.getKategoris(null,null);
        SecondaryDrawerItem itemAbout = new SecondaryDrawerItem().withIdentifier(2).withName("Tentang")
                .withIcon(GoogleMaterial.Icon.gmd_info_outline);
        SectionDrawerItem itemKategori = new SectionDrawerItem().withName("Kategori").withSelectable(false);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(resultHeader)
                .withDisplayBelowStatusBar(false)
                .withOnDrawerItemClickListener(this)
                .addDrawerItems(
                        itemHome,
                        itemKategori
                )
                .build();
        for (Kategori kategori : kategoriList){
            result.addItem(new PrimaryDrawerItem().withName(kategori.getNama()).withIdentifier(kategori.getId_kategori())
                    .withIcon(GoogleMaterial.Icon.gmd_receipt));
        }
        result.addItem(new SectionDrawerItem().withName("Info").withSelectable(false));
        result.addItem(itemAbout);



        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        for (int i = 0 ; i < 3; i++){
            menu.add(R.id.group_nav_drawer_menu_1, Menu.NONE, i, "menu " + i);
        }
        MenuItem itemKategori = menu.getItem(0);
        itemKategori.setChecked(true);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
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
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
/*
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
            Toast.makeText(this, "Bantuan Item Has selected", Toast.LENGTH_SHORT ).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Tentang Item Has selected", Toast.LENGTH_SHORT ).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    */
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

        return super.onOptionsItemSelected(item);
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

    private void menuJurusan(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        JurusanDialogFragment dialogFragment = new JurusanDialogFragment();
        dialogFragment.show(fragmentManager, "jurusanDialog");
        dialogFragment.setCancelable(true);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        long id = drawerItem.getIdentifier();
        if (id == 1){

        } else if (id == 2){

        } else {
            int id_kategori = (int) id;
            new StorageId(id_kategori);
            Intent intent = new Intent(this, AlurListActivity.class);
            startActivity(intent);
        }
        return false;
    }
}

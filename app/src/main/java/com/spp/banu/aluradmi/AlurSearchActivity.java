package com.spp.banu.aluradmi;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spp.banu.aluradmi.model.Alur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 05/02/17.
 */

public class AlurSearchActivity extends AppCompatActivity implements onSearchListSelected {
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        listView = (ListView) findViewById(R.id.listview_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search);
        handleIntent(getIntent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchitem = menu.findItem(R.id.app_bar_search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        return true;
    }

    public void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    private void doSearch(String query){
        ReuniAlur reuniAlur = new ReuniAlur(this);
        List<Alur> alurList = reuniAlur.searchAlur(query);
        SearchAdapter adapter = new SearchAdapter(this, alurList, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onSearchItemSelected(int id_alur) {
        Intent intent = KeteranganPagerActivity.newIntent(this, id_alur);
        startActivity(intent);
    }
}

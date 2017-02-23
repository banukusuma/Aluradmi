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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spp.banu.aluradmi.model.Alur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 05/02/17.
 */

public class AlurSearchActivity extends AppCompatActivity implements onSearchListSelected {
    ListView listView;
    ImageView image_kosong;
    TextView text_tidak_cocok;
    private final static String TAG_LAST_QUERY = "com.spp.banu.aluradmi.last.query";
    private final static String EXTRA_LAST_QUERY = "com.spp.banu.aluradmi.extra.query";
    private String last_query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        image_kosong = (ImageView) findViewById(R.id.imageView_empty_data_search);
        text_tidak_cocok = (TextView) findViewById(R.id.textView_kosong_search);
        image_kosong.setVisibility(View.INVISIBLE);
        text_tidak_cocok.setVisibility(View.INVISIBLE);
        listView = (ListView) findViewById(R.id.listview_search);
        last_query = null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search);
        if (savedInstanceState != null){
            if (savedInstanceState.keySet().contains(TAG_LAST_QUERY)){
                last_query = savedInstanceState.getString(TAG_LAST_QUERY);
            }
        }
        if (last_query != null){
            doSearch(last_query);
        }else {
            handleIntent(getIntent());
        }




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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()){
                    doSearch(newText);
                }
                return true;
            }
        });
        return true;
    }

    public void onNewIntent(Intent intent){
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query;
            if (intent.hasExtra(EXTRA_LAST_QUERY)){
                query = intent.getStringExtra(EXTRA_LAST_QUERY);
                doSearch(query);
            }else {
                query = intent.getStringExtra(SearchManager.QUERY);
                doSearch(query);
            }
        }
    }

    private void doSearch(String query){
        last_query = query;
        Log.e("alursearch", "doSearch: query " +query );
        ReuniAlur reuniAlur = new ReuniAlur(this);
        List<Alur> alurList = reuniAlur.searchAlur(query);
        SearchAdapter adapter = new SearchAdapter(this, alurList, this);
        if (!alurList.isEmpty()){
            image_kosong.setVisibility(View.INVISIBLE);
            text_tidak_cocok.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);
        }else {
            image_kosong.setVisibility(View.VISIBLE);
            text_tidak_cocok.setVisibility(View.VISIBLE);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onSearchItemSelected(int id_alur) {
        if (id_alur != 0 ){
            Intent intent = KeteranganPagerActivity.newIntent(this, id_alur);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TAG_LAST_QUERY, last_query);
        super.onSaveInstanceState(outState);
    }
}

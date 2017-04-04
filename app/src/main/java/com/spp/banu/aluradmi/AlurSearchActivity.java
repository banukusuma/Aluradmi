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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.model.Alur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 05/02/17.
 */

public class AlurSearchActivity extends AppCompatActivity implements onSearchListSelected, TextWatcher {
    private static final String TAG = "AlurSearchActivity";
    ListView listView;
    ImageView image_kosong;
    TextView text_tidak_cocok;
    private final static String TAG_LAST_QUERY = "com.spp.banu.aluradmi.last.query";
    private String last_query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pencarian);
        image_kosong = (ImageView) findViewById(R.id.imageView_empty_data_search);
        text_tidak_cocok = (TextView) findViewById(R.id.textView_kosong_search);
        image_kosong.setVisibility(View.INVISIBLE);
        text_tidak_cocok.setVisibility(View.INVISIBLE);
        EditText search_edit = (EditText) findViewById(R.id.search_edit_text_alur);
        search_edit.addTextChangedListener(this);
        listView = (ListView) findViewById(R.id.listview_search);
        last_query = null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.search);
        /*
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
        */



    }
    /*

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
    */
    private void doSearch(String query){
        last_query = query;
        listView.setVisibility(View.VISIBLE);
        Log.e("alursearch", "doSearch: query " +query + " boolean query " + last_query.isEmpty());
        ReuniAlur reuniAlur = new ReuniAlur(this);
        List<Alur> alurList = reuniAlur.searchAlur(last_query);
        SearchAdapter adapter = new SearchAdapter(this, alurList, this);
        if (!alurList.isEmpty() && !last_query.isEmpty()){
            Log.e(TAG, "doSearch: alurlist tidak kosong" );
            image_kosong.setVisibility(View.INVISIBLE);
            text_tidak_cocok.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);
        }else if (last_query.isEmpty()){
            image_kosong.setVisibility(View.INVISIBLE);
            text_tidak_cocok.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else{
            listView.setVisibility(View.INVISIBLE);
            Log.e(TAG, "doSearch: alurlist kosong" );
            image_kosong.setVisibility(View.VISIBLE);
            text_tidak_cocok.setVisibility(View.VISIBLE);
            //listView.setAdapter(adapter);
        }
    }

    @Override
    public void onSearchItemSelected(int id_alur) {
        if (id_alur != 0 ){
            ReuniAlur reuniAlur = new ReuniAlur(this);
            int id_kategori = reuniAlur.getAlur(AlurDbSchema.AlurTable.Kolom.ID_ALUR + " = ? ",
                    new String[]{Integer.toString(id_alur)}).getId_kategori();
            SharedPreferences preferences = getSharedPreferences(MainActivity.KEY_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(MainActivity.KEY_ID_KATEGORI, id_kategori);
            editor.commit();
            Intent intent = KeteranganPagerActivity.newIntent(this, id_alur);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(TAG_LAST_QUERY, last_query);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            doSearch(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

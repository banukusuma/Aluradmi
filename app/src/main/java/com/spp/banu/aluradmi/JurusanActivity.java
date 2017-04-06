package com.spp.banu.aluradmi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spp.banu.aluradmi.model.Jurusan;

import java.util.List;

/**
 * Created by banu on 02/03/17.
 */

public class JurusanActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{
    private Button btn_oke;
    List<Jurusan> jurusanList;
    Jurusan selected_jurusan;
    ReuniJurusan reuniJurusan;
    boolean isNotSelected_Jurusan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jurusan_activity);
        reuniJurusan = new ReuniJurusan(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_semua);
        isNotSelected_Jurusan = reuniJurusan.isSelectedJurusan();
        if (isNotSelected_Jurusan){
            toolbar.setVisibility(View.GONE);
        }else {
            setSupportActionBar(toolbar);
            setTitle("Ganti Jurusan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ListView listView = (ListView) findViewById(R.id.list_pilih_jurusan);
        btn_oke = (Button) findViewById(R.id.button_oke_pilih_jurusan);
        btn_oke.setOnClickListener(this);
        jurusanList = reuniJurusan.getJurusanList();
        int selectedItem;
        if (reuniJurusan.isSelectedJurusan()){
            selectedItem = 0;
        } else {
            selectedItem = reuniJurusan.getPositionSelectedJurusan(null,null);
        }
        selected_jurusan = new Jurusan();
        ArrayAdapter<Jurusan> adapter = new ArrayAdapter<>(this,
                R.layout.item_jurusan, jurusanList);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        listView.setItemChecked(selectedItem, true);
        selected_jurusan = jurusanList.get(selectedItem);
        //
        //listView.setSelection(selectedItem);

        //

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selected_jurusan = jurusanList.get(i);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this, selected_jurusan.toString() + " telah di pilih", Toast.LENGTH_SHORT).show();
        reuniJurusan.SelectJurusan(selected_jurusan.toString());
        if (isNotSelected_Jurusan){
            startBantuanActivity();
        }else {
            finish();
        }


    }
    public void startBantuanActivity(){
        Intent intent = new Intent(this, BantuanSingkatActivity.class);
        startActivity(intent);
        finish();
    }

}

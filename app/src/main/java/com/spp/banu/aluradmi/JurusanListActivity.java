package com.spp.banu.aluradmi;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spp.banu.aluradmi.fragment.AlurListFragment;
import com.spp.banu.aluradmi.fragment.JurusanListFragment;

import java.util.List;

/**
 * Created by banu on 11/01/17.
 */

public class JurusanListActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jurusan_list);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new JurusanListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_jurusan_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onClick(View view) {

    }
}

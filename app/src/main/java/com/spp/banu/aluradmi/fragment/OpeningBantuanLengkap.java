package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniHelp;
import com.spp.banu.aluradmi.model.Help;

import java.util.List;

/**
 * Created by banu on 02/04/17.
 */

public class OpeningBantuanLengkap extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.opening_bantuan_lengkap, container, false);
        TextView judul = (TextView) view.findViewById(R.id.textView_opening_bantuan_lengkap);
        judul.setText(R.string.opening_bantuan_lengkap);
        ReuniHelp reuniHelp =  ReuniHelp.get(getActivity());
        List<Help> helpList = reuniHelp.getHelpList();
        ListView listView = (ListView) view.findViewById(R.id.listview_bantuan_lengkap);
        ArrayAdapter<Help> helpArrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,helpList);
        listView.setAdapter(helpArrayAdapter);
        return view;
    }
}

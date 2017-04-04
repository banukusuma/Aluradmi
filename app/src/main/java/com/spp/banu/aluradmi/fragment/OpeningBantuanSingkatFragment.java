package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spp.banu.aluradmi.R;

/**
 * Created by banu on 03/04/17.
 */

public class OpeningBantuanSingkatFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.opening_bantuan_singkat, container, false);
        return view;
    }
}

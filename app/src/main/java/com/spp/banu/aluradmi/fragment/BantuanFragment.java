package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.spp.banu.aluradmi.R;

/**
 * Created by banu on 10/01/17.
 */

public class BantuanFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.bantuan);
    }
}

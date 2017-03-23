package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spp.banu.aluradmi.BuildConfig;
import com.spp.banu.aluradmi.R;

/**
 * Created by banu on 20/12/16.
 */

public class AboutFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.about);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_about, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textView4);
        textView.setText("Aluradmi V." + BuildConfig.VERSION_NAME);
        return view;
    }
}

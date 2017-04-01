package com.spp.banu.aluradmi.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spp.banu.aluradmi.R;

/**
 * Created by banu on 02/04/17.
 */

public class BantuanLengkapMaster extends Fragment {
    private String[] isi;
    private final static String STRING_MODE = "mode_bantuan";
    private int mode;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        mode = getArguments().getInt(STRING_MODE);
        switch (mode){
            case 1:
                isi = res.getStringArray(R.array.bantuan_1);
                break;
            case 2:
                isi = res.getStringArray(R.array.bantuan_2);
                break;
            case 3:
                isi = res.getStringArray(R.array.bantuan_3);
                break;
            case 4:
                isi = res.getStringArray(R.array.bantuan_4);
                break;
            case 5:
                isi = res.getStringArray(R.array.bantuan_5);
                break;
            case 6:
                isi = res.getStringArray(R.array.bantuan_6);
                break;
            case 7:
                isi = res.getStringArray(R.array.bantuan_7);
                break;
            case 8:
                isi = res.getStringArray(R.array.bantuan_8);
                break;
            case 9:
                isi = res.getStringArray(R.array.bantuan_9);
                break;
        }
    }

    public static BantuanLengkapMaster newInstance(int mode){
        BantuanLengkapMaster bantuanLengkapMaster = new BantuanLengkapMaster();
        Bundle bundle = new Bundle();
        bundle.putInt(STRING_MODE, mode);
        bantuanLengkapMaster.setArguments(bundle);
        return bantuanLengkapMaster;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (mode){
            case 1:
                View view = inflater.inflate(R.layout.item_bantuan_baru_1, container, false);
                TextView judul = (TextView) view.findViewById(R.id.textView_bantuan_1_judul);
                TextView isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_1_1);
                TextView isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_1_2);
                TextView isi_3 = (TextView) view.findViewById(R.id.textView_bantuan_1_3);
                TextView isi_4 = (TextView) view.findViewById(R.id.textView_bantuan_1_4);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                isi_3.setText(isi[3]);
                isi_4.setText(isi[4]);
                judul.setText(isi[0]);
                return view;
            case 2:
                view = inflater.inflate(R.layout.item_bantuan_baru_2, container, false);
                judul = (TextView) view.findViewById(R.id.textView_bantuan_2_judul);
                isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_2_1);
                isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_2_2);
                isi_3 = (TextView) view.findViewById(R.id.textView_bantuan_2_3);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                isi_3.setText(isi[3]);
                judul.setText(isi[0]);
                return view;
            case 3:
                view = inflater.inflate(R.layout.item_bantuan_baru_3, container, false);
                judul = (TextView) view.findViewById(R.id.textView_bantuan_3_judul);
                isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_3_1);
                isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_3_2);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                judul.setText(isi[0]);
                return view;
            case 4:
                view = inflater.inflate(R.layout.item_bantuan_baru_4, container, false);
                 judul = (TextView) view.findViewById(R.id.textView_bantuan_4_judul);
                 isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_4_1);
                 isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_4_2);
                 isi_3 = (TextView) view.findViewById(R.id.textView_bantuan_4_3);
                 isi_4 = (TextView) view.findViewById(R.id.textView_bantuan_4_4);
                TextView isi_5 = (TextView) view.findViewById(R.id.textView_bantuan_4_5);
                TextView isi_6 = (TextView) view.findViewById(R.id.textView_bantuan_4_6);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                isi_3.setText(isi[3]);
                isi_4.setText(isi[4]);
                isi_5.setText(isi[5]);
                isi_6.setText(isi[6]);
                judul.setText(isi[0]);
                return view;

            case 5:
                view = inflater.inflate(R.layout.item_bantuan_5, container, false);
                judul = (TextView) view.findViewById(R.id.textView_bantuan_5_judul);
                 isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_5_1);
                isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_5_2);
                 isi_3 = (TextView) view.findViewById(R.id.textView_bantuan_5_3);
                 isi_4 = (TextView) view.findViewById(R.id.textView_bantuan_5_4);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                isi_3.setText(isi[3]);
                isi_4.setText(isi[4]);
                judul.setText(isi[0]);
                return view;

            case 6:
                view = inflater.inflate(R.layout.item_bantuan_6, container, false);
                 judul = (TextView) view.findViewById(R.id.textView_bantuan_6_judul);
                 isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_6_1);
                isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_6_2);
                isi_3 = (TextView) view.findViewById(R.id.textView_bantuan_6_3);
                 isi_4 = (TextView) view.findViewById(R.id.textView_bantuan_6_4);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                isi_3.setText(isi[3]);
                isi_4.setText(isi[4]);
                judul.setText(isi[0]);
                return view;

            case 7:
                 view = inflater.inflate(R.layout.item_bantuan_7, container, false);
                 judul = (TextView) view.findViewById(R.id.textView_bantuan_7_judul);
                isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_7_1);
                isi_1.setText(isi[1]);
                judul.setText(isi[0]);
                return view;

            case 8:
                 view = inflater.inflate(R.layout.item_bantuan_8, container, false);
                 judul = (TextView) view.findViewById(R.id.textView_bantuan_8_judul);
                 isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_8_1);
                isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_8_2);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                judul.setText(isi[0]);
                return view;


            case 9:
                 view = inflater.inflate(R.layout.item_bantuan_9, container, false);
                 judul = (TextView) view.findViewById(R.id.textView_bantuan_9_judul);
                isi_1 = (TextView) view.findViewById(R.id.textView_bantuan_9_1);
                 isi_2 = (TextView)view.findViewById(R.id.textView_bantuan_9_2);
                isi_1.setText(isi[1]);
                isi_2.setText(isi[2]);
                judul.setText(isi[0]);
                return view;
            default:
                return null;
        }
    }
}

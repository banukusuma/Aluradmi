package com.spp.banu.aluradmi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spp.banu.aluradmi.DenahActivity;
import com.spp.banu.aluradmi.MapsActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniBerkas;
import com.spp.banu.aluradmi.ReuniKeterangan;

import com.spp.banu.aluradmi.ReuniRuang;
import com.spp.banu.aluradmi.model.Berkas;
import com.spp.banu.aluradmi.model.Keterangan;

import com.spp.banu.aluradmi.model.Ruang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 05/01/17.
 */

public class KeteranganFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private TextView isiKeterangan, isiBerkas, halaman, labelDetail,
    labellokasi, labelberkas, gedungtext, lantaitext, ruangtext;
    private ImageView denah;
    private Button btnrute;
    private CheckBox selesaiCheckBox;
    private static final String EXTRA_ID_GEDUNG = "com.spp.banu.aluradmi.mapsIntent.id.gedung";
    private Keterangan keterangan;
    private ReuniKeterangan reuniKeterangan;
    private static final String ARG_KETERANGAN_ID = "id_keterangan";
    private static final String ARG_CAN_CHECKED = "can_checked_checkbox";
    private static final String ARG_JML_DATA = "jumlah_data_keterangan_list";
    private static final String TAG = "keterangan";
    private int max_data;
    private Ruang ruang;
    private List<Berkas> berkasList;
    private boolean canCheckedSelesai;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reuniKeterangan = new ReuniKeterangan(getActivity());
        ReuniRuang reuniRuang = new ReuniRuang(getActivity());
        int id_keterangan = getArguments().getInt(ARG_KETERANGAN_ID);
        canCheckedSelesai = getArguments().getBoolean(ARG_CAN_CHECKED);
        max_data = getArguments().getInt(ARG_JML_DATA);
        keterangan = reuniKeterangan.getKeterangan(id_keterangan);
        ruang = reuniRuang.getRuang(keterangan.getId_ruang());
        ReuniBerkas reuniBerkas = new ReuniBerkas(getActivity());
        berkasList = reuniBerkas.getBerkasList(keterangan.getId_keterangan());
    }

    public static KeteranganFragment newInstance(int id_keterangan, boolean canCheckedSelesai, int jml_data){
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_KETERANGAN_ID, id_keterangan);
        bundle.putBoolean(ARG_CAN_CHECKED, canCheckedSelesai);
        bundle.putInt(ARG_JML_DATA, jml_data);
        KeteranganFragment fragment = new KeteranganFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_keterangan, container, false);
        isiKeterangan = (TextView)view.findViewById(R.id.isi_keterangan);
        isiKeterangan.setMovementMethod(LinkMovementMethod.getInstance());
        isiBerkas = (TextView) view.findViewById(R.id.isi_berkas);
        halaman = (TextView) view.findViewById(R.id.halaman_keterangan);
        gedungtext = (TextView) view.findViewById(R.id.gedung_text_view);
        lantaitext = (TextView) view.findViewById(R.id.lantai_text_view);
        ruangtext = (TextView) view.findViewById(R.id.ruang_text_view);
        denah = (ImageView) view.findViewById(R.id.thumbnail_denah);
        btnrute = (Button) view.findViewById(R.id.rute_keterangan_btn);
        selesaiCheckBox = (CheckBox) view.findViewById(R.id.checkbox_selesai);
        labelDetail = (TextView)view.findViewById(R.id.label_keterangan);
        labelberkas = (TextView)view.findViewById(R.id.label_berkas_keterangan);
        labellokasi = (TextView) view.findViewById(R.id.label_lokasi_keterangan);
        selesaiCheckBox.setOnCheckedChangeListener(this);
        selesaiCheckBox.setChecked(keterangan.isStatus());
        denah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = DenahActivity.newIntent(getActivity(),ruang.getLink());
                startActivity(intent);
            }
        });
        btnrute.setOnClickListener(this);
        updateUI();
        return view;
    }
    private void updateUI(){
        if (keterangan.getId_keterangan() != 0 ){
            labelDetail.setVisibility(View.VISIBLE);
            isiKeterangan.setVisibility(View.VISIBLE);
            //judulKeterangan.setText(keterangan.getUrut() + ". " + keterangan.getNama());
            isiKeterangan.setText(keterangan.getKeterangan());
            selesaiCheckBox.setVisibility(View.VISIBLE);
            halaman.setVisibility(View.VISIBLE);
            halaman.setText("Halaman " + keterangan.getUrut() + " dari " + max_data);
            bindLokasi();
            bindBerkas();
        } else {
            //judulKeterangan.setText("Data masih kosong");
            labelDetail.setVisibility(View.INVISIBLE);
            isiKeterangan.setVisibility(View.INVISIBLE);
            selesaiCheckBox.setVisibility(View.INVISIBLE);
            halaman.setVisibility(View.INVISIBLE);
            bindLokasi();
            bindBerkas();
        }

        if (canCheckedSelesai){
            selesaiCheckBox.setEnabled(true);
        } else {
            selesaiCheckBox.setVisibility(View.INVISIBLE);
            selesaiCheckBox.setEnabled(false);
        }

    }
    private void bindLokasi(){
        if (ruang.getId_ruang() != 99){
            labellokasi.setVisibility(View.VISIBLE);
            gedungtext.setVisibility(View.VISIBLE);
            gedungtext.setText("Gedung : " + ruang.getNama_gedung());
            lantaitext.setVisibility(View.VISIBLE);
            lantaitext.setText("Lantai : " + ruang.getLantai());
            ruangtext.setVisibility(View.VISIBLE);
            ruangtext.setText("Ruang : " + ruang.getNama());
            btnrute.setVisibility(View.VISIBLE);
            btnrute.setEnabled(true);
            denah.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(ruang.getThumbnail()).resize(100,100).placeholder(R.mipmap.placeholder_image).centerCrop().into(denah);
        }else{
            labellokasi.setVisibility(View.GONE);
            gedungtext.setVisibility(View.GONE);
            lantaitext.setVisibility(View.GONE);
            ruangtext.setVisibility(View.GONE);
            btnrute.setVisibility(View.GONE);
            denah.setVisibility(View.GONE);
            //btnrute.setEnabled(false);
        }
    }
    private void bindBerkas(){
        if (!berkasList.isEmpty()){
            labelberkas.setVisibility(View.VISIBLE);
            isiBerkas.setVisibility(View.VISIBLE);
            List<String> strings = new ArrayList<>();
            for (Berkas berkas: berkasList){
                strings.add(berkas.getNama());
            }
            isiBerkas.setText(TextUtils.join("\n", strings).toString());
        } else {
            labelberkas.setVisibility(View.GONE);
            isiBerkas.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        reuniKeterangan.cekKeterangan(b,keterangan.getId_keterangan());
    }

    @Override
    public void onClick(View view) {
        Intent intent = MapsActivity.newIntent(getActivity(),ruang.getId_gedung());
        startActivity(intent);
    }
}

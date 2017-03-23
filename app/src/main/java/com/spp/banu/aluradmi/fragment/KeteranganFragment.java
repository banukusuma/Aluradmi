package com.spp.banu.aluradmi.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spp.banu.aluradmi.DenahActivity;
import com.spp.banu.aluradmi.KeteranganPagerActivity;
import com.spp.banu.aluradmi.MainActivity;
import com.spp.banu.aluradmi.MapsActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniAlur;
import com.spp.banu.aluradmi.ReuniBerkas;
import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.ReuniKeterangan;

import com.spp.banu.aluradmi.ReuniRuang;
import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;
import com.spp.banu.aluradmi.model.Alur;
import com.spp.banu.aluradmi.model.Berkas;
import com.spp.banu.aluradmi.model.Keterangan;

import com.spp.banu.aluradmi.model.Ruang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.spp.banu.aluradmi.MainActivity.KEY_PREFERENCE;

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
    private static final String ARG_ID_ALUR = "id_alur_pada_keterangan";
    private static final String TAG = "keterangan";
    private int max_data;
    private int id_alur;
    int id_kategori;
    private Ruang ruang;
    ReuniJurusan reuniJurusan;
    private boolean warning_show;
    private Alur alurIni;
    protected static final String CHECK_KEY = "selesai-checked-bisa";
    ReuniAlur reuniAlur;
    private List<Berkas> berkasList;
    private boolean canCheckedSelesai;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reuniKeterangan = new ReuniKeterangan(getActivity());
        ReuniRuang reuniRuang = new ReuniRuang(getActivity());
        int id_keterangan = getArguments().getInt(ARG_KETERANGAN_ID);
        id_alur = getArguments().getInt(ARG_ID_ALUR);
        Log.e(TAG, "onCreate: id_alur " + id_alur );
        max_data = reuniKeterangan.getKeteranganList(
                KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? ", new String[]{Integer.toString(id_alur)}
        ).size();
        keterangan = reuniKeterangan.getKeterangan(id_keterangan);
        Log.e(TAG, "onCreate: keterangan = " + keterangan.getNama() );
        ruang = reuniRuang.getRuang(keterangan.getId_ruang());
        ReuniBerkas reuniBerkas = new ReuniBerkas(getActivity());
        berkasList = reuniBerkas.getBerkasList(keterangan.getId_keterangan());
        reuniAlur = new ReuniAlur(getActivity());
        alurIni = reuniAlur.getAlur(AlurDbSchema.AlurTable.Kolom.ID_ALUR + " = ? ", new String[]{Integer.toString(id_alur)});
        canCheckedSelesai = getArguments().getBoolean(ARG_CAN_CHECKED);
        reuniJurusan = new ReuniJurusan(getActivity());
        SharedPreferences preferences = getActivity().getSharedPreferences(MainActivity.KEY_PREFERENCE, Context.MODE_PRIVATE);
        id_kategori = preferences.getInt(MainActivity.KEY_ID_KATEGORI,0);
       if (savedInstanceState != null){
           if (savedInstanceState.keySet().contains(CHECK_KEY)){
               if (savedInstanceState.getBoolean(CHECK_KEY)){
                   showWarning();
               }
           }
      }
        Log.e(TAG, "onCreate: saat oncreate dijalankan" );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " );
    }

    public static KeteranganFragment newInstance(int id_keterangan, int id_alur, boolean canChecked){
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_KETERANGAN_ID, id_keterangan);
        bundle.putInt(ARG_ID_ALUR, id_alur);
        bundle.putBoolean(ARG_CAN_CHECKED, canChecked);
        KeteranganFragment fragment = new KeteranganFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_keterangan, container, false);
        isiKeterangan = (TextView)view.findViewById(R.id.isi_keterangan);
       // isiKeterangan.setMovementMethod(LinkMovementMethod.getInstance());
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
        //selesaiCheckBox.setOnCheckedChangeListener(this);
        selesaiCheckBox.setOnClickListener(this);
        Log.e(TAG, "onCreateView: saat view dibuat" );
        selesaiCheckBox.setChecked(keterangan.isStatus());
        denah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ruang.getLink().equals("http://aluradmi.pe.hu/denah/no-thumbnail.png")){
                    Intent intent = DenahActivity.newIntent(getActivity(),ruang.getLink());
                    startActivity(intent);
                }

            }
        });
        btnrute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MapsActivity.newIntent(getActivity(),ruang.getId_gedung());
                startActivity(intent);
            }
        });
        updateUI();
        return view;
    }
    private void updateUI(){
        if (keterangan.getId_keterangan() != 0 ){
            labelDetail.setVisibility(View.VISIBLE);
            if (keterangan.getKeterangan().isEmpty()){
                labelDetail.setVisibility(View.GONE);
                isiKeterangan.setVisibility(View.GONE);
            }else {
                labelDetail.setVisibility(View.VISIBLE);
                isiKeterangan.setVisibility(View.VISIBLE);
                isiKeterangan.setText(keterangan.getKeterangan());
            }
            //judulKeterangan.setText(keterangan.getUrut() + ". " + keterangan.getNama());

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
            int i = 1;
            for (Berkas berkas: berkasList){
                strings.add(Integer.toString(i) +". " + berkas.getNama());
                i++;
            }
            isiBerkas.setText(TextUtils.join("\n\n", strings).toString());
        } else {
            labelberkas.setVisibility(View.GONE);
            isiBerkas.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(final CompoundButton compoundButton, boolean b) {

        //reuniKeterangan.cekKeterangan(b,keterangan.getId_keterangan());
    }

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (!checked){
            if (isWarningShow()){
                showWarning();
            }else {
                saveProgress(checked);
            }
        }else{
            saveProgress(checked);
        }
    }

    private void saveProgress(boolean check){
        reuniKeterangan.cekKeterangan(check, keterangan.getId_keterangan());
    }

    private boolean isWarningShow(){
            int jumlah_alur = reuniAlur.getAlurs(AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                            AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                    new String[]{Integer.toString(id_kategori),
                            Integer.toString(reuniJurusan.getSelectJurusan().getId_jurusan())}).size();
            int urutNext = alurIni.getUrut() + 1;
            if (urutNext <= jumlah_alur){
                Alur alurNext = reuniAlur.getAlur(
                        AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                                AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? AND " +
                                AlurDbSchema.AlurTable.Kolom.URUT + " = ? ",
                        new String[]{Integer.toString(id_kategori),
                                Integer.toString(reuniJurusan.getSelectJurusan().getId_jurusan()),
                                Integer.toString(urutNext)}
                );
                int progressNext = Math.round(alurNext.getProgress());
                if (progressNext > 0){
                    return true;
                }
            }
        return false;
    }

    private void showWarning(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Membatalkan status ini akan membuat progress setelah alur " +
        alurIni.getNama() + " kembali ke 0%, Apakah anda yakin ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetAfterThisAlur();
                warning_show = false;
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selesaiCheckBox.setChecked(true);
                warning_show = false;
            }
        });
        builder.setTitle("Peringatan");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.create();
        warning_show = true;
        builder.show();
    }

    private void resetAfterThisAlur(){
        saveProgress(false);
        int urutNext = alurIni.getUrut() + 1;
        ReuniKeterangan reuniKeterangan = new ReuniKeterangan(getActivity());
        List<Alur> alurList_restart_progress = reuniAlur.getAlurs(
                    AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                            AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? AND " +
                            AlurDbSchema.AlurTable.Kolom.URUT + " >= ? ",
                    new String[]{Integer.toString(id_kategori),Integer.toString(reuniJurusan.getSelectJurusan().getId_jurusan()),
                            Integer.toString(urutNext)}
        );
        for (Alur alur : alurList_restart_progress){
                reuniKeterangan.restartProgress(alur.getId_alur());
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(CHECK_KEY,warning_show );
        super.onSaveInstanceState(outState);
    }
    //    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putBoolean(CHECK_KEY, true);
//        super.onSaveInstanceState(outState);
//    }



}

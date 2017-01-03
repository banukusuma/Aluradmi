package com.spp.banu.aluradmi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.spp.banu.aluradmi.AlurListActivity;
import com.spp.banu.aluradmi.KeteranganListActivity;
import com.spp.banu.aluradmi.MainActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniAlur;
import com.spp.banu.aluradmi.ReuniBerkas;
import com.spp.banu.aluradmi.ReuniKeterangan;
import com.spp.banu.aluradmi.ReuniLokasi;
import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;
import com.spp.banu.aluradmi.model.Alur;
import com.spp.banu.aluradmi.model.Berkas;
import com.spp.banu.aluradmi.model.Keterangan;
import com.spp.banu.aluradmi.model.Lokasi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 05/12/16.
 */

public class KeteranganListFragment extends Fragment {
    private RecyclerView keteranganRecyclerView;
    private KeteranganAdapter keteranganAdapter;
    public String KETERANGAN_ARG_ID_ALUR;
    private int id_alur;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_alur = getActivity().getIntent().getIntExtra(KeteranganListActivity.EXTRA_ID_ALUR, 0);
        Log.i("KeteranganListFragment", "id_alur: " + id_alur);
        Log.e("KeteranganListFragment", "jumlah backstake: " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.keterangan_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.show_all_keterangan){
            updateUI();
        } else if (id == R.id.show_cek_keterangan){
            ReuniKeterangan reuniKeterangan = new ReuniKeterangan(getActivity());
            List<Keterangan> keteranganList = reuniKeterangan.getKeteranganList(
                    KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? AND " +
                            KeteranganDbSchema.KeteranganTable.Kolom.STATUS + " = ? ",
                    new String[]{
                            Integer.toString(id_alur), "1"
                    }
            );
            if (keteranganList.isEmpty()){
                Keterangan keterangan = new Keterangan();
                keterangan.setNama("Data Tidak Ada");
                keteranganList.add(keterangan);
            }
            keteranganAdapter.setKeteranganList(keteranganList);
            keteranganAdapter.notifyDataSetChanged();
        }else if (id == R.id.show_uncek_keterangan){
            ReuniKeterangan reuniKeterangan = new ReuniKeterangan(getActivity());
            List<Keterangan> keteranganList = reuniKeterangan.getKeteranganList(
                    KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? AND " +
                            KeteranganDbSchema.KeteranganTable.Kolom.STATUS + " = ? ",
                    new String[]{
                            Integer.toString(id_alur), "0"
                    }
            );
            if (keteranganList.isEmpty()){
                Keterangan keterangan = new Keterangan();
                keterangan.setNama("Data Tidak Ada");
                keteranganList.add(keterangan);
            }
            keteranganAdapter.setKeteranganList(keteranganList);
            keteranganAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        Log.i("" +this, "onResume: this");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keterangan, container, false);
        keteranganRecyclerView = (RecyclerView) view.findViewById(R.id.keterangan_recycler_view);
        keteranganRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }
    public void updateUI(){
        if (id_alur != 0){
            ReuniAlur reuniAlur = new ReuniAlur(getActivity());
            Alur alur = reuniAlur.getAlur(id_alur);
            getActivity().setTitle("Keterangan " + alur.getNama());
        } else {
            getActivity().setTitle("Keterangan");
        }
        ReuniKeterangan reuniKeterangan = new ReuniKeterangan(getActivity());
        List<Keterangan> keteranganList = reuniKeterangan.getKeteranganList(
                KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? ", new String[]{Integer.toString(id_alur)});
        if (keteranganList.isEmpty()){
            Keterangan keterangan = new Keterangan();
            keterangan.setNama("Data Tidak Ada");
            keteranganList.add(keterangan);
        }
            if (keteranganAdapter == null){
                keteranganAdapter = new KeteranganAdapter(keteranganList);
                keteranganRecyclerView.setAdapter(keteranganAdapter);
            } else {
                keteranganAdapter.setKeteranganList(keteranganList);
                keteranganAdapter.notifyDataSetChanged();
            }
    }
    //constuctor must be blank
    public KeteranganListFragment() {
    }

    private class KeteranganHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        private TextView namaKeterangan;
        private TextView isiKeterangan, urut_Keterangan;
        private Button isiLokasi;
        private TextView isiBerkas;
        private CheckBox checkBox;
        private String kumpulanBerkas;
        private Keterangan keterangan;

        public KeteranganHolder(View itemView) {
            super(itemView);
            namaKeterangan = (TextView) itemView.findViewById(R.id.nama_keterangan_text_view);
            isiKeterangan = (TextView) itemView.findViewById(R.id.keterangan_text_view);
            isiLokasi = (Button) itemView.findViewById(R.id.nama_lokasi_btn);
            isiBerkas = (TextView) itemView.findViewById(R.id.nama_berkas_text_view);
            checkBox= (CheckBox) itemView.findViewById(R.id.checkBox_keterangan);
            urut_Keterangan = (TextView) itemView.findViewById(R.id.urut_keterangan_textview);

        }

        public void bindKeterangan(Keterangan keterangan){
            this.keterangan = keterangan;
            namaKeterangan.setText(this.keterangan.getNama());
            isiKeterangan.setText(this.keterangan.getKeterangan());
            isiKeterangan.setMovementMethod(LinkMovementMethod.getInstance());
            ReuniLokasi reuniLokasi = new ReuniLokasi(getActivity());
            ReuniBerkas reuniBerkas = new ReuniBerkas(getActivity());
            Lokasi lokasi = reuniLokasi.getLokasi(this.keterangan.getId_lokasi());
            List<Berkas> berkasList = reuniBerkas.getBerkasList(this.keterangan.getId_keterangan());
            if (berkasList.isEmpty()){
                Berkas berkas = new Berkas();
                berkas.setNama("Tidak Ada");
                berkasList.add(berkas);
            }
            if (this.keterangan.getNama() == "Data Tidak Ada"){
                checkBox.setEnabled(false);
            } else {
                checkBox.setEnabled(true);
            }
            checkBox.setOnCheckedChangeListener(this);
            List<String> strings = new ArrayList<>();
            for (Berkas berkas: berkasList){
                strings.add(berkas.getNama());
            }
            kumpulanBerkas = TextUtils.join(",", strings).toString();
            isiBerkas.setText(kumpulanBerkas);
            isiLokasi.setText(lokasi.getNama());
            checkBox.setChecked(keterangan.isStatus());
            urut_Keterangan.setText(Integer.toString(this.keterangan.getUrut()));
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            ReuniKeterangan reuniKeterangan = new ReuniKeterangan(getActivity());
            reuniKeterangan.cekKeterangan(b,keterangan.getId_keterangan());
        }
    }

    private class KeteranganAdapter extends RecyclerView.Adapter<KeteranganHolder> {
        private List<Keterangan> keteranganList;

        public KeteranganAdapter(List<Keterangan> keteranganList) {
            this.keteranganList = keteranganList;
        }


        @Override
        public KeteranganHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_keterangan, parent, false);
            return new KeteranganHolder(view);
        }

        @Override
        public void onBindViewHolder(KeteranganHolder holder, int position) {
            Keterangan keterangan = keteranganList.get(position);
            holder.bindKeterangan(keterangan);
        }

        @Override
        public int getItemCount() {
            return keteranganList.size();
        }

        public void setKeteranganList(List<Keterangan> keteranganList){
            this.keteranganList = keteranganList;
        }
    }
}

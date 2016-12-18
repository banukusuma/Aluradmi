package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.spp.banu.aluradmi.AlurListActivity;
import com.spp.banu.aluradmi.KeteranganListActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniBerkas;
import com.spp.banu.aluradmi.ReuniKeterangan;
import com.spp.banu.aluradmi.ReuniLokasi;
import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;
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
    private int id_alur;
    private ReuniKeterangan reuniKeterangan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id_alur = (Integer) getActivity().getIntent().getIntExtra(KeteranganListActivity.EXTRA_ID_ALUR,0);
        Log.i("Keterangan List Fragment", "id_alur: " + id_alur);
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
        reuniKeterangan = new ReuniKeterangan(getActivity());
        List<Keterangan> keteranganList = reuniKeterangan.getKeteranganList(
                KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? ", new String[]{Integer.toString(id_alur)});
        if (keteranganList.isEmpty()){
            Keterangan keterangan = new Keterangan();
            keterangan.setNama("Data Tidak Ada");
            keteranganList.add(keterangan);
        }
        if (keteranganAdapter == null) {
            keteranganAdapter = new KeteranganAdapter(keteranganList);
            keteranganRecyclerView.setAdapter(keteranganAdapter);
        }else{
            keteranganAdapter.notifyDataSetChanged();
        }
    }
    //constuctor must be blank
    public KeteranganListFragment() {
    }

    private class KeteranganHolder extends RecyclerView.ViewHolder{
        private TextView namaKeterangan;
        private TextView isiKeterangan;
        private TextView isiLokasi;
        private TextView isiBerkas;
        private Button btnOk;
        private String kumpulanBerkas;

        public KeteranganHolder(View itemView) {
            super(itemView);
            namaKeterangan = (TextView) itemView.findViewById(R.id.nama_keterangan_text_view);
            isiKeterangan = (TextView) itemView.findViewById(R.id.keterangan_text_view);
            isiLokasi = (TextView) itemView.findViewById(R.id.nama_lokasi_text_view);
            isiBerkas = (TextView) itemView.findViewById(R.id.nama_berkas_text_view);
            btnOk = (Button) itemView.findViewById(R.id.btn_check_list_keterangan);
        }

        public void bindKeterangan(final Keterangan keterangan){
            namaKeterangan.setText(keterangan.getNama());
            isiKeterangan.setText(keterangan.getKeterangan());
            ReuniLokasi reuniLokasi = new ReuniLokasi(getActivity());
            ReuniBerkas reuniBerkas = new ReuniBerkas(getActivity());
            Lokasi lokasi = reuniLokasi.getLokasi(keterangan.getId_lokasi());
            List<Berkas> berkasList = reuniBerkas.getBerkasList(keterangan.getId_keterangan());
            if (berkasList.isEmpty()){
                Berkas berkas = new Berkas();
                berkas.setNama("Tidak Ada");
                berkasList.add(berkas);
            }
            List<String> strings = new ArrayList<>();
            for (Berkas berkas: berkasList){
                strings.add(berkas.getNama());
            }
            kumpulanBerkas = TextUtils.join(",", strings).toString();
            isiBerkas.setText(kumpulanBerkas);
            isiLokasi.setText(lokasi.getNama());
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reuniKeterangan.cekketerangan(keterangan.isStatus(),keterangan.getId_keterangan());
                }
            });
        }
    }

    private class KeteranganAdapter extends RecyclerView.Adapter<KeteranganHolder>{
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
    }
}

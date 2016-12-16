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
import android.widget.TextView;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniBerkas;
import com.spp.banu.aluradmi.ReuniKeterangan;
import com.spp.banu.aluradmi.model.Berkas;
import com.spp.banu.aluradmi.model.Keterangan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 05/12/16.
 */

public class KeteranganListFragment extends Fragment {
    private RecyclerView keteranganRecyclerView;
    private KeteranganAdapter keteranganAdapter;

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
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
        ReuniKeterangan reuniKeterangan = ReuniKeterangan.get(getActivity());
        List<Keterangan> keteranganList = reuniKeterangan.getKeteranganList(1);
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
        private Keterangan keterangan;
        private String kumpulanBerkas;
        private String tempBerkas;
        private List<String> groupBerkas;

        public KeteranganHolder(View itemView) {
            super(itemView);
            namaKeterangan = (TextView) itemView.findViewById(R.id.nama_keterangan_text_view);
            isiKeterangan = (TextView) itemView.findViewById(R.id.keterangan_text_view);
            isiLokasi = (TextView) itemView.findViewById(R.id.nama_lokasi_text_view);
            isiBerkas = (TextView) itemView.findViewById(R.id.nama_berkas_text_view);
        }

        public void bindKeterangan(Keterangan keterangan){
            this.keterangan = keterangan;
            namaKeterangan.setText(this.keterangan.getNama());
            isiKeterangan.setText(this.keterangan.getKeterangan());
            isiLokasi.setText(this.keterangan.getLokasi().getNama());

            ReuniBerkas reuniBerkas = new ReuniBerkas(getActivity());
            List<Berkas> berkasList = reuniBerkas.getBerkasList(this.keterangan.getId_keterangan());
            List<String> strings = new ArrayList<>();
            for (Berkas berkas: berkasList){
                strings.add(berkas.getNama());
            }
            kumpulanBerkas = TextUtils.join(",", strings).toString();
            isiBerkas.setText(kumpulanBerkas);
            //nanti menggunakan perulangan di sebelah sini untuk menampilkan list berkas

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

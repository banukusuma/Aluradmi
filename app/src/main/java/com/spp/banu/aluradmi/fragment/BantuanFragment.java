package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.SimpleDividerItemDecoration;
import com.spp.banu.aluradmi.model.Bantuan;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 10/01/17.
 */

public class BantuanFragment extends Fragment {
    private static final String TAG = "bantuanFragment";
    private RecyclerView bantuanRecyclerview;
    private BantuanAdapter bantuanAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.bantuan);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bantuan, container,false);
        bantuanRecyclerview = (RecyclerView) view.findViewById(R.id.bantuan_recyclerview);
        bantuanRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        bantuanRecyclerview.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        return view;
    }

    private void updateUI() {
        List<Bantuan> bantuanList = new ArrayList<>();
        Bantuan item = new Bantuan("Mengakses Menu Home", R.drawable.ic_home_black_24dp);
        bantuanList.add(item);

        item = new Bantuan("Membuka daftar kategori alur yang tersedia", R.drawable.ic_receipt_black_24dp);
        bantuanList.add(item);

        item = new Bantuan("Membuka Peta yang menunjukkan lokasi gedung untuk mengurus berkas", R.drawable.ic_place_black_24dp);
        bantuanList.add(item);

        item = new Bantuan("Mengakses fitur bantuan", R.drawable.ic_help_black_24dp);
        bantuanList.add(item);

        item = new Bantuan("Mengakses info tentang aplikasi ini", R.drawable.ic_info_black_24dp);
        bantuanList.add(item);

        item = new Bantuan("Nomor menunjukkan urutan prosedur pengurusan administrasi ", R.drawable.ic_format_list_numbered_black_24dp );
        bantuanList.add(item);

        item = new Bantuan("Klik checkbox sukses untuk menambahkan progress pada suatu alur. " +
                "Checkbox sukses akan terbuka apabila progress alur sebelumnya sudah mencapai 100% ",
                R.drawable.ic_check_box_black_24dp);
        bantuanList.add(item);

        item = new Bantuan("Memilih jurusan", R.drawable.ic_account_balance_black_24dp);
        bantuanList.add(item);

        item = new Bantuan("Mengakses fitur pencarian alur", R.drawable.ic_search_black_24px);
        bantuanList.add(item);

        item = new Bantuan("Melakukan proses sinkronisasi data dengan server", R.drawable.ic_sync_black_24dp);
        bantuanList.add(item);
        bantuanAdapter = new BantuanAdapter(bantuanList);
        bantuanRecyclerview.setAdapter(bantuanAdapter);
    }

    private class BantuanAdapter extends RecyclerView.Adapter<BantuanHolder>
    {
        private List<Bantuan> bantuanList;

        public BantuanAdapter(List<Bantuan> bantuanList) {
            this.bantuanList = bantuanList;
        }

        @Override
        public BantuanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_bantuan, parent, false);
            return new BantuanHolder(view);
        }

        @Override
        public void onBindViewHolder(BantuanHolder holder, int position) {
            Bantuan bantuan = bantuanList.get(position);
            holder.bindBantuan(bantuan);
        }

        @Override
        public int getItemCount() {
            return bantuanList.size();
        }
    }

    private class BantuanHolder extends RecyclerView.ViewHolder{
        private TextView penjelasan_text;
        private ImageView icon;
        public BantuanHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon_bantuan);
            penjelasan_text = (TextView)itemView.findViewById(R.id.text_penjelasan_bantuan);
        }

        public void bindBantuan(Bantuan bantuan){
            Picasso.with(getActivity()).load(bantuan.getThumbnail()).resize(56,56).into(icon);
            penjelasan_text.setText(bantuan.getPenjelasan());
        }
    }
}

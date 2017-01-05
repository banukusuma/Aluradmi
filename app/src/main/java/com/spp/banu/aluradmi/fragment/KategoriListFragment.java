package com.spp.banu.aluradmi.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.spp.banu.aluradmi.AlurListActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniKategori;
import com.spp.banu.aluradmi.model.Kategori;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KategoriListFragment extends Fragment {
    private RecyclerView kategoriRecyclerView;
    private KategoriAdapter kategoriAdapter;
    private onKategoriListSelectListener listSelectListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_jurusan){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            JurusanDialogFragment dialogFragment = new JurusanDialogFragment();
            dialogFragment.show(fragmentManager, "jurusanDialog");
            dialogFragment.setCancelable(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    public interface onKategoriListSelectListener{
         void onKategoriSelected(int id_kategori);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onKategoriListSelectListener){
            listSelectListener = (onKategoriListSelectListener) context;
        }else {
            throw new RuntimeException(context.toString() +
                "must implement onKategoriListSelectListener"
            );
        }
    }

    public KategoriListFragment() {
        // Required empty public constructor
    }

    public void updateUI(){
        getActivity().setTitle(R.string.kategori);
        ReuniKategori reuniKategori = new ReuniKategori(getActivity());
        List<Kategori> kategoriList = reuniKategori.getKategoris(null,null);
        if (kategoriList.isEmpty()){
            Kategori kategori = new Kategori();
            kategori.setNama("Data Masih Kosong");
            kategoriList.add(kategori);
        }

        if (kategoriAdapter == null){
            kategoriAdapter = new KategoriAdapter(kategoriList);
            kategoriRecyclerView.setAdapter(kategoriAdapter);
        } else{
            kategoriAdapter.setKategoris(kategoriList);
            kategoriAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kategori_list, container, false);
        kategoriRecyclerView = (RecyclerView) view.findViewById(R.id.kategori_recycler_view);
        kategoriRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;

    }

    private class KategoriHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        private TextView namaTextView;
        private Kategori kategori;

        KategoriHolder(View view) {
            super(view);
            namaTextView = (TextView) view.findViewById(R.id.list_item_kategori_nama);
            view.setOnClickListener(this);
        }

         void bindKategori(Kategori kategori) {
            this.kategori = kategori;
            namaTextView.setText(this.kategori.getNama());

        }

        @Override
        public void onClick(View view) {
            listSelectListener.onKategoriSelected(kategori.getId_kategori());
        }
    }
    private class KategoriAdapter extends RecyclerView.Adapter<KategoriHolder> {
        private List<Kategori> kategoris;


        public KategoriAdapter(List<Kategori> kategoriList) {
            kategoris = kategoriList;
        }

        @Override
        public KategoriHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_kategori, parent, false);

            return new KategoriHolder(view);

        }


        @Override
        public void onBindViewHolder(KategoriHolder holder, int position) {
            Kategori kategori = kategoris.get(position);
            holder.bindKategori(kategori);
        }

        @Override
        public int getItemCount() {
            return kategoris.size();
        }

        public void setKategoris(List<Kategori> kategoriList){
            kategoris = kategoriList;
        }

    }
}

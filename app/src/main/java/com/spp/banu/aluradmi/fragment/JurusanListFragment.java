package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.SimpleDividerItemDecoration;
import com.spp.banu.aluradmi.model.Jurusan;

import java.util.List;

/**
 * Created by banu on 03/12/16.
 */

public class JurusanListFragment extends Fragment {
    private RecyclerView jurusanRecyclerView;
    private JurusanAdapter jurusanAdapter;
    private SparseBooleanArray selectedItem;
    public JurusanListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pilih_jurusan, container, false);
        jurusanRecyclerView = (RecyclerView) view.findViewById(R.id.pilih_jurusan_recycler_view);
        jurusanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        jurusanRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        updateUI();
    }

    public void updateUI(){
        ReuniJurusan reuniJurusan = new ReuniJurusan(getActivity());
        List<Jurusan> jurusanList = reuniJurusan.getJurusanList();
        if (jurusanAdapter == null){
            jurusanAdapter = new JurusanAdapter(jurusanList);
            jurusanRecyclerView.setAdapter(jurusanAdapter);
        } else{
            jurusanAdapter.setJurusanList(jurusanList);
            jurusanAdapter.notifyDataSetChanged();
        }

    }
    private class JurusanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textJurusan;
        private Jurusan jurusan;
        public JurusanHolder(View itemView) {
            super(itemView);
            textJurusan = (TextView) itemView.findViewById(R.id.jurusan_text_view);
        }

        public void bindJurusan(Jurusan jurusan){
            this.jurusan = jurusan;
            textJurusan.setText(this.jurusan.getNama());
        }

        @Override
        public void onClick(View view) {
        }
    }

    private class JurusanAdapter extends RecyclerView.Adapter<JurusanHolder>{
        private List<Jurusan> jurusanList;

        public JurusanAdapter(List<Jurusan> jurusanList) {
            this.jurusanList = jurusanList;
        }

        @Override
        public JurusanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_jurusan, parent, false);

            return new JurusanHolder(view);
        }

        @Override
        public void onBindViewHolder(JurusanHolder holder, int position) {
                Jurusan jurusan = jurusanList.get(position);
                holder.bindJurusan(jurusan);
        }

        @Override
        public int getItemCount() {
            return jurusanList.size();
        }

        public void setJurusanList(List<Jurusan> jurusanList) {
            this.jurusanList = jurusanList;
        }
    }


}

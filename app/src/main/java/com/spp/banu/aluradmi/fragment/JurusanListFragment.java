package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.model.Jurusan;

import java.util.List;

/**
 * Created by banu on 03/12/16.
 */

public class JurusanListFragment extends Fragment {
    private RecyclerView jurusanRecyclerView;
    private JurusanAdapter jurusanAdapter;
    public JurusanListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pilih_jurusan, container, false);
        jurusanRecyclerView = (RecyclerView) view.findViewById(R.id.pilih_jurusan_recycler_view);
        jurusanRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    public void updateUI(){
        ReuniJurusan reuniJurusan = ReuniJurusan.get(getActivity());
        List<Jurusan> jurusanList = reuniJurusan.getJurusanList();
        jurusanAdapter = new JurusanAdapter(jurusanList);
        jurusanRecyclerView.setAdapter(jurusanAdapter);
    }
    private class JurusanHolder extends RecyclerView.ViewHolder {
        private RadioGroup groupJurusan;
        private RadioButton radioJurusan;
        private Jurusan jurusan;

        public JurusanHolder(View itemView) {
            super(itemView);
            radioJurusan = (RadioButton) itemView.findViewById(R.id.radioJurusan);
        }

        public void bindJurusan(Jurusan jurusan){
            this.jurusan = jurusan;
            radioJurusan.setText(this.jurusan.getNama());
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
    }


}

package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.model.Jurusan;

/**
 * Created by banu on 09/01/17.
 */

public class HomeFragment extends Fragment {
    private RecyclerView homeRecyclerView;
    private HomeAdapter adapter;
    private ReuniJurusan reuniJurusan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reuniJurusan = new ReuniJurusan(getActivity());
    }

    public void notifyTheAdapater(){
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeRecyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        Jurusan jurusan = reuniJurusan.getSelectJurusan();
        if (adapter == null){
            adapter = new HomeAdapter(jurusan);
            homeRecyclerView.setAdapter(adapter);
        } else {
            adapter.setJurusan(jurusan);
            adapter.notifyDataSetChanged();
        }
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder{
        private Jurusan jurusan;
        private TextView text_jurusan;

        public HomeViewHolder(View itemView) {
            super(itemView);
            text_jurusan = (TextView) itemView.findViewById(R.id.jurusan_home);
        }

        public void bindJurusan(Jurusan jurusan){
            this.jurusan = jurusan;
            text_jurusan.setText(this.jurusan.getNama());
        }

    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
        private Jurusan jurusan;

        public HomeAdapter(Jurusan jurusan) {
            this.jurusan = jurusan;
        }

        @Override
        public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.home_layout, parent, false);
            return new HomeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HomeViewHolder holder, int position) {
            holder.bindJurusan(jurusan);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public void setJurusan(Jurusan jurusan) {
            this.jurusan = jurusan;
        }
    }

}
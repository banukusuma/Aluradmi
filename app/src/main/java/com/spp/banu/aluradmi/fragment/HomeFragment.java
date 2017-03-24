package com.spp.banu.aluradmi.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.SetupActivity;
import com.spp.banu.aluradmi.model.Jurusan;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

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
        getActivity().setTitle(R.string.home);
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

        List<Jurusan> jurusanList = new ArrayList<>();

        //menambah logo
        Jurusan welcome_logo = new Jurusan();
        welcome_logo.setId_jurusan(0);
        jurusanList.add(welcome_logo);

        //menambah keterangan jurusan yang dipilih
        String nama_jurusan_dipilih = reuniJurusan.getSelectJurusan().toString();
        Jurusan jurusan_yang_dipilih = new Jurusan();
        jurusan_yang_dipilih.setId_jurusan(1);
        jurusan_yang_dipilih.setNama(nama_jurusan_dipilih);
        jurusan_yang_dipilih.setTimestamp("Jurusan : ");
        jurusanList.add(jurusan_yang_dipilih);

        SharedPreferences preferences = getActivity().getSharedPreferences(SetupActivity.KEY, Context.MODE_PRIVATE);
        DateTime time = new DateTime(preferences.getLong(SetupActivity.KEY_DATE_SYNC, new DateTime().getMillis()));
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy HH:mm:ss");
        String time_last_sync = formatter.print(time);

        Jurusan last_sync = new Jurusan();
        last_sync.setId_jurusan(2);
        last_sync.setNama(time_last_sync);
        last_sync.setTimestamp("Terakhir Sinkronisasi : ");
        jurusanList.add(last_sync);
        //Jurusan jurusan = reuniJurusan.getSelectJurusan();
        if (adapter == null){
            adapter = new HomeAdapter(jurusanList);
            homeRecyclerView.setAdapter(adapter);
        } else {
            adapter.setJurusanList(jurusanList);
            adapter.notifyDataSetChanged();
        }
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder{
        private Jurusan jurusan;
        private TextView text_jurusan , textjudulHome;

        public HomeViewHolder(View itemView) {
            super(itemView);
            textjudulHome = (TextView) itemView.findViewById(R.id.text_judul_home);
            text_jurusan = (TextView) itemView.findViewById(R.id.isi_home);
        }

        public void bindJurusan(Jurusan jurusan){
            this.jurusan = jurusan;
            textjudulHome.setText(this.jurusan.getTimestamp());
            text_jurusan.setText(this.jurusan.getNama());
        }

    }

    private class WelcomeViewHolder extends RecyclerView.ViewHolder{
        public WelcomeViewHolder(View itemView) {
            super(itemView);
        }
        public void bindJurusan(Jurusan jurusan){
        }
    }

    private class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<Jurusan> jurusanList;
        private final int WELCOME = 0 , INFO = 1;
        public HomeAdapter(List<Jurusan> list) {
            jurusanList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            switch (viewType){
                case WELCOME:
                    View view1 = layoutInflater.inflate(R.layout.home_layout, parent, false);
                    viewHolder = new WelcomeViewHolder(view1);
                    break;
                case INFO:
                    View view2 = layoutInflater.inflate(R.layout.item_home, parent, false);
                    viewHolder = new HomeViewHolder(view2);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()){
                case WELCOME:
                    WelcomeViewHolder holder1 = (WelcomeViewHolder) holder;
                    configureWelcomeViewHolder(holder1, position);
                    break;
                case INFO:
                    HomeViewHolder holder2 = (HomeViewHolder) holder;
                    configureItemHome(holder2, position);
                    break;
            }
        }

        private void configureWelcomeViewHolder(WelcomeViewHolder vh, int position){
            vh.bindJurusan(jurusanList.get(position));
        }
        private void configureItemHome(HomeViewHolder vh, int position){
            vh.bindJurusan(jurusanList.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            if (jurusanList.get(position).getId_jurusan() == 0){
                return WELCOME;
            }else {
                return INFO;
            }
        }

        @Override
        public int getItemCount() {
            return jurusanList.size();
        }

        public void setJurusanList(List<Jurusan> list){
            jurusanList = list;
        }
    }
    /*
    private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
        private Jurusan jurusan;
        private HashMap<String, String> list_item_home;

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
    */

}

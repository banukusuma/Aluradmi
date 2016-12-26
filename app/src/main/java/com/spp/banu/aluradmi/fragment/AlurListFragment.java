package com.spp.banu.aluradmi.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spp.banu.aluradmi.AlurListActivity;
import com.spp.banu.aluradmi.KeteranganListActivity;
import com.spp.banu.aluradmi.MainActivity;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniAlur;
import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.ReuniKategori;
import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.dbSchema.JurusanDbSchema;
import com.spp.banu.aluradmi.model.Alur;
import com.spp.banu.aluradmi.model.Jurusan;

import java.util.List;

/**
 * Created by banu on 01/12/16.
 */

public class AlurListFragment extends Fragment {
    public String ALUR_ARG_ID_KATEGORI;
    private RecyclerView alurRecyclerView;
    private AlurAdapter alurAdapter;
    private onAlurListSelected listSelectListener;
    private int id_kategori;

    public AlurListFragment() {
    }

    public interface onAlurListSelected{
         void onSelectAlur(int id_alur);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onAlurListSelected){
            listSelectListener = (AlurListFragment.onAlurListSelected) context;
        }else {
            throw new RuntimeException(context.toString() +
                    " must implement onAlurListSelected"
            );
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        Log.i("Alur List Fragment", "onResume: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         this.id_kategori = getArguments().getInt(ALUR_ARG_ID_KATEGORI);
        Log.i("alur fragment", "id_kategori : " + Integer.toString(id_kategori));

    }


    public void updateUI(){
        ReuniKategori reuniKategori = new ReuniKategori(getActivity());
        String title = reuniKategori.getKategori(id_kategori).getNama();
        if (title != null){
            getActivity().setTitle("Alur " + title);
        } else {
            getActivity().setTitle("Alur");
        }

        ReuniAlur reuniAlur = new ReuniAlur(getActivity());
        ReuniJurusan reuniJurusan = new ReuniJurusan(getActivity());
        Jurusan jurusan = reuniJurusan.getSelectJurusan();
        List<Alur> alurList = reuniAlur.getAlurs(
                AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                        AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                new String[]{Integer.toString(id_kategori), Integer.toString(jurusan.getId_jurusan())}
        );
        if (alurList.isEmpty()){
            Alur alur = new Alur();
            alur.setNama("Data Tidak Ada");
            alurList.add(alur);
        }
        alurAdapter = new AlurAdapter(alurList);
        alurRecyclerView.setAdapter(alurAdapter);
        /*
        if (alurAdapter == null){
            alurAdapter = new AlurAdapter(alurList);
            alurRecyclerView.setAdapter(alurAdapter);
        } else {
            alurAdapter.setAlurList(alurList);
            alurAdapter.notifyDataSetChanged();
        }
        */
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alur_list, container, false);
        alurRecyclerView = (RecyclerView) view.findViewById(R.id.alur_recycler_view);
        alurRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }


    private class AlurHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView namaAlur, progress_text, urut_Alur;
        private Alur alur;

        public AlurHolder(View itemView) {
            super(itemView);
            namaAlur = (TextView) itemView.findViewById(R.id.nama_alur_text_view);
            progress_text = (TextView) itemView.findViewById(R.id.progress_text_view);
            urut_Alur = (TextView)itemView.findViewById(R.id.urut_alur_text_view);
            itemView.setOnClickListener(this);
            //progressBar = (ProgressBar) itemView.findViewById(R.id.progress_alur_progress_bar);
        }

        public void bindAlur(Alur alur){
            this.alur = alur;
            int urut = this.alur.getUrut();
            urut_Alur.setText(Integer.toString(urut));
            namaAlur.setText(this.alur.getNama());
            progress_text.setText("Progress : " + this.alur.getProgress() + "%");
        }

        @Override
        public void onClick(View view) {
            listSelectListener.onSelectAlur(alur.getId_alur());
        }
    }

    private class AlurAdapter extends RecyclerView.Adapter<AlurHolder>{
        private List<Alur> alurList;

        public AlurAdapter(List<Alur> alurList){
             this.alurList = alurList;
        }
        @Override
        public AlurHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_alur, parent, false);
            return new AlurHolder(view);
        }

        @Override
        public void onBindViewHolder(AlurHolder holder, int position) {
            Alur alur = alurList.get(position);
            holder.bindAlur(alur);
        }

        @Override
        public int getItemCount() {
            return alurList.size();
        }

        public void setAlurList(List<Alur> alurList){
            this.alurList = alurList;
        }
    }


}

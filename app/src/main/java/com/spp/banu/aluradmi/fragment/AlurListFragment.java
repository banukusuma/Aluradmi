package com.spp.banu.aluradmi.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniAlur;
import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.ReuniKategori;
import com.spp.banu.aluradmi.ReuniKeterangan;
import com.spp.banu.aluradmi.SimpleDividerItemDecoration;
import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
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
    private ReuniJurusan reuniJurusan;
    private ReuniAlur reuniAlur;
    private final static String TAG = "alurListFragment";
    private static final String KEY_ID_KATEGORI = "com.spp.banu.aluradmi.key.id.kategori";
    private static final String KEY_PREFERENCE = "com.spp.banu.aluradmi.kategori.pref";
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
        restart_progress_jika_perlu();
        updateUI();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //this.id_kategori = getActivity().getIntent().getIntExtra(AlurListActivity.EXTRA_ID_KATEGORI, 0);
        SharedPreferences preferences = this.getActivity().getSharedPreferences(KEY_PREFERENCE, Context.MODE_PRIVATE);
        id_kategori = preferences.getInt(KEY_ID_KATEGORI,0);
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

        reuniAlur = new ReuniAlur(getActivity());
         reuniJurusan = new ReuniJurusan(getActivity());
        Jurusan jurusan = reuniJurusan.getSelectJurusan();
        List<Alur> alurList = reuniAlur.getAlurs(
                AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                        AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                new String[]{Integer.toString(id_kategori), Integer.toString(jurusan.getId_jurusan())}
        );
        if (alurList.isEmpty()){
            Alur alur = new Alur();
            alur.setId_alur(0);
            alurList.add(alur);
        }

        if (alurAdapter == null){
            alurAdapter = new AlurAdapter(alurList);
            alurRecyclerView.setAdapter(alurAdapter);
        } else {
            alurAdapter.setAlurList(alurList);
            alurAdapter.notifyDataSetChanged();
        }

    }

    private void restart_progress_jika_perlu(){
        Jurusan jurusan = reuniJurusan.getSelectJurusan();
        List<Alur> alurList = reuniAlur.getAlurs(
                AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                        AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                new String[]{Integer.toString(id_kategori), Integer.toString(jurusan.getId_jurusan())}
        );
        for(int i = 0; i < alurList.size(); i++){
            int h = i - 1;
            if (i != 0){
                int progress1 = Math.round(alurList.get(h).getProgress());
                int progress2 = Math.round(alurList.get(i).getProgress());
                Log.e(TAG, "progress 1 :" + progress1 );
                Log.e(TAG, "progress 2 :" + progress2 );
                if ((progress1 != 100 && progress1 <= progress2) || (progress1 != 100 && progress1 >= progress2)){
                    restart_progress(alurList.get(i).getId_kategori(), alurList.get(i).getUrut());
                    break;
                }
            }

        }
    }

    private void restart_progress(int id_Kategori, int urut){
        ReuniKeterangan reuniKeterangan = new ReuniKeterangan(getActivity());
        List<Alur> alurList_restart_progress = reuniAlur.getAlurs(
                AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                        AlurDbSchema.AlurTable.Kolom.URUT + " >= ? ",
                new String[]{Integer.toString(id_Kategori), Integer.toString(urut)}
        );
        for (Alur alur : alurList_restart_progress){
            reuniKeterangan.restartProgress(alur.getId_alur());
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alur_list, container, false);
        alurRecyclerView = (RecyclerView) view.findViewById(R.id.alur_recycler_view);
        alurRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        alurRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        updateUI();
        return view;
    }


    private class AlurHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView namaAlur, progress_text, urut_Alur;
        private Alur alur;
        private RelativeLayout layout_urut;

        public AlurHolder(View itemView) {
            super(itemView);
            namaAlur = (TextView) itemView.findViewById(R.id.nama_alur_text_view);
            progress_text = (TextView) itemView.findViewById(R.id.progress_text_view);
            urut_Alur = (TextView)itemView.findViewById(R.id.urut_alur_text_view);
            layout_urut = (RelativeLayout) itemView.findViewById(R.id.relative_layout_urut_Alur);
            itemView.setOnClickListener(this);
            //progressBar = (ProgressBar) itemView.findViewById(R.id.progress_alur_progress_bar);
        }

        public void bindAlur(Alur alur){
            this.alur = alur;
            if (this.alur.getId_alur() != 0 ){
                int urut = this.alur.getUrut();
                namaAlur.setVisibility(View.VISIBLE);
                progress_text.setVisibility(View.VISIBLE);
                layout_urut.setVisibility(View.VISIBLE);
                urut_Alur.setText(Integer.toString(urut));
                namaAlur.setText(this.alur.getNama());
                progress_text.setText("Progress : " + this.alur.getProgress() + "%");
            } else {
                namaAlur.setText("Data Masih Kosong");
                layout_urut.setVisibility(View.GONE);
                progress_text.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View view) {listSelectListener.onSelectAlur(alur.getId_alur());
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

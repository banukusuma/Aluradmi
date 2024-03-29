package com.spp.banu.aluradmi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private List<Alur> alurList;
    private final static String TAG = "alurListFragment";
    public static final String KEY_ID_KATEGORI = "com.spp.banu.aluradmi.key.id.kategori";
    public static final String KEY_PREFERENCE = "com.spp.banu.aluradmi.kategori.pref";
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


        reuniAlur = new ReuniAlur(getActivity());
         reuniJurusan = new ReuniJurusan(getActivity());
        Jurusan jurusan = reuniJurusan.getSelectJurusan();
        //List<Alur>
        alurList = reuniAlur.getAlurs(
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
        List<Alur> alurList = reuniAlur.getAlurs(
                AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + " = ? AND " +
                        AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                new String[]{Integer.toString(id_kategori), Integer.toString(reuniJurusan.getSelectJurusan().getId_jurusan())}
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
                        AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? AND " +
                        AlurDbSchema.AlurTable.Kolom.URUT + " >= ? ",
                new String[]{Integer.toString(id_Kategori),Integer.toString(reuniJurusan.getSelectJurusan().getId_jurusan()), Integer.toString(urut)}
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
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        AlurHolder(View itemView) {
            super(itemView);
            namaAlur = (TextView) itemView.findViewById(R.id.nama_alur_text_view);
            progress_text = (TextView) itemView.findViewById(R.id.progress_text_view);
            urut_Alur = (TextView)itemView.findViewById(R.id.urut_alur_text_view);
            itemView.setOnClickListener(this);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_alur_ganti_warna);
            //image_urut = (ImageView) itemView.findViewById(R.id.image_urut);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_urut_Alur);
            //progressBar = (ProgressBar) itemView.findViewById(R.id.progress_alur_progress_bar);
        }

        void bindAlur(Alur alur){
                this.alur = alur;
                int progress = Math.round(this.alur.getProgress());
                if (progress == 100){
                    //linearLayout.setBackgroundColor(getResources().getColor(R.color.md_green_400));
                    linearLayout.setBackgroundResource(R.drawable.done_state);
                }else if (progress > 0 && progress < 100){
                    linearLayout.setBackgroundResource(R.drawable.ongoing_state);
                }else {
                    //linearLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    TypedValue outValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                    linearLayout.setBackgroundResource(outValue.resourceId);
                    //linearLayout.setBackground(android.R.attr.selectableItemBackground);
                }
                int urut = this.alur.getUrut();
                urut_Alur.setText(Integer.toString(urut));
                namaAlur.setText(this.alur.getNama());
                progress_text.setText("Progress : " + this.alur.getProgress() + "%");
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

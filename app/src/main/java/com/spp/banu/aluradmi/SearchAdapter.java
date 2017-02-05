package com.spp.banu.aluradmi;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spp.banu.aluradmi.model.Alur;

import java.util.List;

/**
 * Created by banu on 06/02/17.
 */

public class SearchAdapter extends BaseAdapter {
    private onSearchListSelected listSelected;
    List<Alur> alurList;
    private Context context;
    public SearchAdapter(Context context, List<Alur> alurs, onSearchListSelected selected) {
        alurList = alurs;
        this.context = context;
        listSelected = selected;
    }

    @Override
    public int getCount() {
        return alurList.size();
    }

    @Override
    public Object getItem(int position) {
        return alurList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.list_item_alur, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Alur alur = (Alur) getItem(position);

        /*
        viewHolder.nama_alur.setText(alur.getNama());
        viewHolder.kategori.setText("Kategori : " + nama_kategori);
        viewHolder. urut_alur.setText(Integer.toString(alur.getUrut()));
        */
        viewHolder.bindAlur(alur);
        return view;
    }

    private class ViewHolder implements View.OnClickListener {
        private TextView nama_alur , kategori, urut_alur;
        private Alur alur;
        public ViewHolder(View view) {
            nama_alur = (TextView) view.findViewById(R.id.nama_alur_text_view);
            kategori = (TextView) view.findViewById(R.id.progress_text_view);
            urut_alur = (TextView) view.findViewById(R.id.urut_alur_text_view);
            view.setOnClickListener(this);
        }

        public void bindAlur(Alur alur2){
            alur = alur2;
            ReuniKategori reuniKategori = new ReuniKategori(context);
            String nama_kategori = reuniKategori.getKategori(alur.getId_kategori()).getNama();
            nama_alur.setText(alur.getNama());
            kategori.setText("Kategori : " + nama_kategori);
            urut_alur.setText(Integer.toString(alur.getUrut()));
        }

        @Override
        public void onClick(View view) {
            listSelected.onSearchItemSelected(alur.getId_alur());
        }
    }

}

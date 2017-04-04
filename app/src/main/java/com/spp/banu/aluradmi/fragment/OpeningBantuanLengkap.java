package com.spp.banu.aluradmi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniHelp;
import com.spp.banu.aluradmi.model.Help;

import java.util.List;

/**
 * Created by banu on 02/04/17.
 */

public class OpeningBantuanLengkap extends Fragment {
    private onBantuanLengkapSelected listener;
    public interface onBantuanLengkapSelected{
        void onSelectItemBantuan(int id_bantuan);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onBantuanLengkapSelected){
            listener = (OpeningBantuanLengkap.onBantuanLengkapSelected) context;
        }else {
            throw new RuntimeException(context.toString() +
                    " must implement onBantuanLengkapSelected"
            );
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.opening_bantuan_lengkap, container, false);
        TextView judul = (TextView) view.findViewById(R.id.textView_opening_bantuan_lengkap);
        judul.setText(R.string.opening_bantuan_lengkap);
        ReuniHelp reuniHelp =  ReuniHelp.get(getActivity());
        ListView listView = (ListView) view.findViewById(R.id.listview_bantuan_lengkap);
        BantuanMenuAdapter adapter = new BantuanMenuAdapter(reuniHelp.getHelpList(), getActivity());

        listView.setAdapter(adapter);
        return view;
    }

    private class BantuanMenuAdapter extends BaseAdapter{
        private Context context;
        private List<Help> helpList;
        public BantuanMenuAdapter(List<Help> helps, Context context) {
            helpList = helps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return helpList.size();
        }

        @Override
        public Object getItem(int i) {
            return helpList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null){
                view = LayoutInflater.from(context).inflate(R.layout.item_list_bantuan,viewGroup,false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Help help = (Help) getItem(i);
            viewHolder.bindHelp(help);

            return view;
        }

        private class ViewHolder implements View.OnClickListener{
            private TextView textView;
            private Help help;
            public ViewHolder(View view) {
                textView = (TextView) view.findViewById(R.id.textView_list_bantuan);
                view.setOnClickListener(this);
            }

            public void bindHelp(Help help){
                this.help = help;
                textView.setText(help.toString());
            }

            @Override
            public void onClick(View view) {
                listener.onSelectItemBantuan(help.getId());
            }
        }
    }
}

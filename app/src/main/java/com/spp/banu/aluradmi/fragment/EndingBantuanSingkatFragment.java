package com.spp.banu.aluradmi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.spp.banu.aluradmi.R;

/**
 * Created by banu on 03/04/17.
 */

public class EndingBantuanSingkatFragment extends Fragment {
    private onEndingBantuanChoice listener;
    public interface onEndingBantuanChoice{
        void onTombolKlik(boolean hasil);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onEndingBantuanChoice){
            listener = (EndingBantuanSingkatFragment.onEndingBantuanChoice) context;
        }else {
            throw new RuntimeException(context.toString() +
                    " must implement onEndingBantuanChoice"
            );
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ending_bantuan_singkat, container, false);
        Button tidak = (Button) view.findViewById(R.id.button_tidak_paham);
        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTombolKlik(false);
            }
        });
        Button paham = (Button) view.findViewById(R.id.button_paham);
        paham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTombolKlik(true);
            }
        });
        return view;
    }
}

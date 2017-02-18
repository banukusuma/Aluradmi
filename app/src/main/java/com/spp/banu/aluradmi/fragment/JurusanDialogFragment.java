package com.spp.banu.aluradmi.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.spp.banu.aluradmi.ReuniJurusan;
import com.spp.banu.aluradmi.model.Jurusan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 17/12/16.
 */

public class JurusanDialogFragment extends DialogFragment {
    private String namaJurusan;
    private ReuniJurusan reuniJurusan;
    private JurusanDialogInterfaceListener listener;
    private final static String TAG_home_fragment = "home_fragment";
    private static final String ALUR_FRAGMENT_TAG = "alur_list_fragment";
    public interface JurusanDialogInterfaceListener{
        void onSelectedJurusan(String nama_jurusan);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        reuniJurusan = new ReuniJurusan(getActivity());
        List<Jurusan> jurusanList = reuniJurusan.getJurusanList();
        int selectedItem;
        if (reuniJurusan.isSelectedJurusan()){
            selectedItem = 0;
        } else {
            selectedItem = reuniJurusan.getPositionSelectedJurusan(null,null);
        }
        boolean isFirstRun = reuniJurusan.isSelectedJurusan();
        List<String> strings = new ArrayList<>();
        for (Jurusan jurusan : jurusanList){
            strings.add(jurusan.getNama());
        }
        final CharSequence[] listItem = strings.toArray(new CharSequence[strings.size()]);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih Jurusan");

        namaJurusan = listItem[selectedItem].toString();
        builder.setSingleChoiceItems(listItem, selectedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                namaJurusan = listItem[i].toString();
                //listener.onSelectedJurusan(namaJurusan);
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reuniJurusan.SelectJurusan(namaJurusan);
                Toast.makeText(getActivity(),namaJurusan + " telah di pilih", Toast.LENGTH_SHORT).show();
                Log.i("Dialog Jurusan", "onClick: " + namaJurusan);
                HomeFragment homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_home_fragment);
                if (homeFragment != null && homeFragment.isVisible()){
                    homeFragment.notifyTheAdapater();
                    Log.e("DialogJurusan", "homefragment " );
                }

                AlurListFragment alurListFragment = (AlurListFragment) getActivity().getSupportFragmentManager()
                        .findFragmentByTag(ALUR_FRAGMENT_TAG);
                if (alurListFragment != null && alurListFragment.isVisible()){
                    alurListFragment.updateUI();
                    Log.e("DialogJurusan", "alurfragment " );
                }

            }
        });
        if (!isFirstRun){
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        return  builder.create();
    }

}

package com.spp.banu.aluradmi;

import android.content.Context;

import com.spp.banu.aluradmi.model.Help;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 01/04/17.
 */

public class ReuniHelp {
    private static ReuniHelp reuniHelp;
    private List<Help> helpList;

    public static ReuniHelp get(Context context){
        if (reuniHelp == null){
            reuniHelp = new ReuniHelp(context);
        }
        return reuniHelp;
    }

    public List<Help> getHelpList(){
        return helpList;
    }
    private ReuniHelp(Context context){
        helpList = new ArrayList<>();
        Help item = new Help(1, "Melihat Alur Administrasi");
        helpList.add(item);

        item = new Help(2, "Melihat Detail Alur Administrasi");
        helpList.add(item);

        item = new Help(3, "Melihat Denah Lokasi");
        helpList.add(item);

        item = new Help(4, "Melihat Rute Menuju Lokasi");
        helpList.add(item);

        item = new Help(5, "Menambah Progress");
        helpList.add(item);

        item = new Help(6, "Mengganti Jurusan");
        helpList.add(item);

        item = new Help(7, "Melakukan Sinkronisasi Data");
        helpList.add(item);

        item = new Help(8, "Mencari Alur Administrasi");
        helpList.add(item);

        item = new Help(9, "Memberikan Review dan Rating");
        helpList.add(item);
    }

    public Help getHelp(int id){
        for (Help help : helpList){
            if (help.getId() == id){
                return help;
            }
        }
        return null;
    }
}

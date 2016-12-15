package com.spp.banu.aluradmi;

import android.content.Context;

import com.spp.banu.aluradmi.model.Jurusan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 03/12/16.
 */

public class ReuniJurusan {
    private static ReuniJurusan reuniJurusan;
    private List<Jurusan> jurusanList;
    private ReuniJurusan(Context context){
        jurusanList = new ArrayList<>();
        for (int i = 0; i < 4 ; i++){
            Jurusan jurusan = new Jurusan();
            jurusan.setId_jurusan(i+ 1);
            int no = i+1;
            jurusan.setNama(Integer.toString(no));
            jurusanList.add(jurusan);
        }
    }

    public static ReuniJurusan get(Context context){
        if (reuniJurusan == null){
            reuniJurusan = new ReuniJurusan(context);
        }
        return reuniJurusan;
    }

    public List<Jurusan> getJurusanList(){
        return jurusanList;
    }

    public Jurusan getJurusan(Integer id_jurusan){
        for (Jurusan jurusan : jurusanList){
            if (jurusan.getId_jurusan() == id_jurusan){
                return jurusan;
            }
        }
        return null;
    }
}

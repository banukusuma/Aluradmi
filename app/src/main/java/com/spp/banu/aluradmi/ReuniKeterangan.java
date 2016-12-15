package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spp.banu.aluradmi.cursorwrapper.KeteranganCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;
import com.spp.banu.aluradmi.model.Keterangan;
import com.spp.banu.aluradmi.model.Lokasi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 05/12/16.
 */

public class ReuniKeterangan {
    private static ReuniKeterangan reuniKeterangan;
    private Context context;
    private SQLiteDatabase database;


    public static ReuniKeterangan get(Context context){
        if (reuniKeterangan == null){
            reuniKeterangan = new ReuniKeterangan(context);
        }
        return reuniKeterangan;
    }

    private ReuniKeterangan(Context context){
       this.context = context;
        this.database = new DatabaseHelper(this.context, true)
        .getReadableDatabase();
    }

    public List<Keterangan> getKeteranganList(int id_alur){
        List<Keterangan> keteranganList = new ArrayList<>();
        KeteranganCursorWrapper cursorWrapper = queryKeterangan("id_alur = ? ", new String[]{Integer.toString(id_alur)});
        try {
            if (cursorWrapper.getCount() > 0){
                cursorWrapper.moveToFirst();
                while (!cursorWrapper.isAfterLast()){
                    keteranganList.add(cursorWrapper.getKeterangan());
                    cursorWrapper.moveToNext();
                }
            }else{
                Keterangan keterangan = new Keterangan();
                keterangan.setNama("Data Tidak ada");
                keterangan.setKeterangan(" ");
                Lokasi lokasi = new Lokasi();
                lokasi.setNama("tidak ada");
                keterangan.setLokasi(lokasi);
                keteranganList.add(keterangan);
            }
        }finally {
            cursorWrapper.close();

        }
        return keteranganList;
    }


    public KeteranganCursorWrapper queryKeterangan(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                KeteranganDbSchema.KeteranganTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                KeteranganDbSchema.KeteranganTable.Kolom.URUT + " ASC"
        );
        return new KeteranganCursorWrapper(cursor, context);
    }
}

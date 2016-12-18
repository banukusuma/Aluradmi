package com.spp.banu.aluradmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
    private Context context;
    private SQLiteDatabase database;

    public ReuniKeterangan(Context context){
       this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this.context,true);
        this.database = databaseHelper
        .getWritableDatabase();
    }

    public List<Keterangan> getKeteranganList(String whereClause, String[] whereArgs){
        List<Keterangan> keteranganList = new ArrayList<>();
        KeteranganCursorWrapper cursorWrapper = queryKeterangan(whereClause,whereArgs);
        try {
                cursorWrapper.moveToFirst();
                while (!cursorWrapper.isAfterLast()){
                    keteranganList.add(cursorWrapper.getKeterangan());
                    cursorWrapper.moveToNext();
                }
        }finally {
            cursorWrapper.close();
        }
        return keteranganList;
    }


    private KeteranganCursorWrapper queryKeterangan(String whereClause, String[] whereArgs){
        Cursor cursor = cursorKeterangan(whereClause, whereArgs);
        return new KeteranganCursorWrapper(cursor, context);
    }

    private Cursor cursorKeterangan(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                KeteranganDbSchema.KeteranganTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                KeteranganDbSchema.KeteranganTable.Kolom.URUT + " ASC "
        );
        return cursor;
    }

    public void cekketerangan(boolean isChecked, int id_keterangan){
        ContentValues values = new ContentValues();
        if (isChecked){
            values.put(KeteranganDbSchema.KeteranganTable.Kolom.STATUS, 1);
        }else {
            values.put(KeteranganDbSchema.KeteranganTable.Kolom.STATUS, 0);
        }

        int cek = database.update(
                KeteranganDbSchema.KeteranganTable.TABLE_NAME,
                values,
                KeteranganDbSchema.KeteranganTable.Kolom.ID_KETERANGAN + " = ? ",
                new String[]{Integer.toString(id_keterangan)}
        );
        Log.i("Reuni Keterangan", "cekketerangan: " + cek);
    }
}

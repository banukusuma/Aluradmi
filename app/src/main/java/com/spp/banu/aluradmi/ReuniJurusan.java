package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spp.banu.aluradmi.cursorwrapper.JurusanCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.JurusanDbSchema;
import com.spp.banu.aluradmi.model.Jurusan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 03/12/16.
 */

public class ReuniJurusan {
    private static ReuniJurusan reuniJurusan;
    private Context context;
    private SQLiteDatabase database;
    private ReuniJurusan(Context context){
            this.context = context;
        database = new DatabaseHelper(this.context, true)
                .getWritableDatabase();
    }

    public static ReuniJurusan get(Context context){
        if (reuniJurusan == null){
            reuniJurusan = new ReuniJurusan(context);
        }
        return reuniJurusan;
    }

    public List<Jurusan> getJurusanList(){
        List<Jurusan> jurusanList = new ArrayList<>();
        JurusanCursorWrapper cursorWrapper = queryJurusan(null,null);
        try {
            if (cursorWrapper.getCount() > 0){
                cursorWrapper.moveToFirst();
                while (!cursorWrapper.isAfterLast()){
                    jurusanList.add(cursorWrapper.getJurusan());
                    cursorWrapper.moveToNext();
                }
            }else{
                Jurusan jurusan = new Jurusan();
                jurusan.setNama("data tidak ada");
                jurusanList.add(jurusan);
            }
        } finally {
            cursorWrapper.close();
        }
        return jurusanList;
    }


    public JurusanCursorWrapper queryJurusan(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                JurusanDbSchema.JurusanTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new JurusanCursorWrapper(cursor);
    }
}

package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spp.banu.aluradmi.cursorwrapper.RuangCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.RuangDbSchema;
import com.spp.banu.aluradmi.model.Ruang;

/**
 * Created by banu on 31/01/17.
 */

public class ReuniRuang {
    private SQLiteDatabase database;

    public ReuniRuang(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context, true);
        database = databaseHelper.getReadableDatabase();
    }

    private Cursor cursorRuang(String whereClause, String[] whereArgs){
        //where clause harus ditambahkan WHERE di awal
        Cursor cursor = database.rawQuery("SELECT r.id_ruang, g.id_gedung, r.nama, r.timestamp, g.nama as nama_gedung, l.nama as lantai," +
                " l.link, l.thumbnail FROM ruang r JOIN gedung g ON g.id_gedung = l.id_gedung " +
                "JOIN lantai l ON l.id_lantai = r.id_lantai " + whereClause , whereArgs);
        return cursor;
    }

    private RuangCursorWrapper queryRuang(String whereClause, String[] whereArgs){
        Cursor cursor = cursorRuang(whereClause,whereArgs);
        return new RuangCursorWrapper(cursor);
    }

    public Ruang getRuang(int id_ruang){
        Ruang ruang = new Ruang();
        RuangCursorWrapper cursorWrapper = queryRuang("WHERE id_ruang = ? " , new String[]{Integer.toString(id_ruang)});
        try {
            if (cursorWrapper.getCount() == 0){
                ruang.setId_ruang(99);
            }else {
                cursorWrapper.moveToFirst();
                ruang = cursorWrapper.getRuang();
            }
        }finally {
            cursorWrapper.close();
        }
        return ruang;
    }
}

package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spp.banu.aluradmi.cursorwrapper.LokasiCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.LokasiDbSchema;
import com.spp.banu.aluradmi.model.Lokasi;

/**
 * Created by banu on 13/12/16.
 */

public class ReuniLokasi {
    private SQLiteDatabase database;

    public ReuniLokasi(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context.getApplicationContext(),true);
        database = databaseHelper
        .getReadableDatabase();
    }

    public Lokasi getLokasi(int id_lokasi){
        Lokasi lokasi = new Lokasi();
        LokasiCursorWrapper cursorWrapper = queryLokasi("id_lokasi = ? ", new String[] {Integer.toString(id_lokasi)});
        try {
                cursorWrapper.moveToFirst();
                lokasi = cursorWrapper.getLokasi();
        }finally {
            cursorWrapper.close();
        }
        return lokasi;
    }

    private LokasiCursorWrapper queryLokasi(String whereClause, String[] whereArgs){
        Cursor cursor = cursorLokasi(whereClause,whereArgs);
        return new LokasiCursorWrapper(cursor);
    }

    private Cursor cursorLokasi(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                LokasiDbSchema.LokasiTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return cursor;
    }
}

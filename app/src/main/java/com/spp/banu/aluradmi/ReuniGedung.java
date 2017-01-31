package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spp.banu.aluradmi.cursorwrapper.GedungCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.GedungDbSchema;
import com.spp.banu.aluradmi.model.Gedung;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 31/01/17.
 */

public class ReuniGedung {
    private SQLiteDatabase database;

    public ReuniGedung(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context, true);
        database = databaseHelper.getReadableDatabase();
    }
    private Cursor cursorGedung(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                GedungDbSchema.GedungTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    private GedungCursorWrapper queryGedung(String whereClause, String[] whereArgs){
        Cursor cursor = cursorGedung(whereClause, whereArgs);
        return new GedungCursorWrapper(cursor);
    }

    public List<Gedung> getGedungList(String whereClause, String[] whereArgs){
        List<Gedung> gedungList = new ArrayList<>();
        GedungCursorWrapper cursorWrapper = queryGedung(whereClause, whereArgs);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                gedungList.add(cursorWrapper.getGedung());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return gedungList;
    }
}

package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spp.banu.aluradmi.cursorwrapper.AlurCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.dbSchema.JurusanDbSchema;
import com.spp.banu.aluradmi.model.Alur;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by banu on 01/12/16.
 */

public class ReuniAlur {
    private Context context;
    private SQLiteDatabase db;

    public ReuniAlur(Context context){
        this.context = context.getApplicationContext();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this.context,true);
        db = databaseHelper
        .getReadableDatabase();

    }

    public List<Alur> getAlurs(String whereClause, String[] whereArgs){
        List<Alur> alurList = new ArrayList<>();
        AlurCursorWrapper cursorWrapper = queryAlur(whereClause, whereArgs);
            try {
                    cursorWrapper.moveToFirst();
                    while (!cursorWrapper.isAfterLast()) {
                        alurList.add(cursorWrapper.getAlur());
                        cursorWrapper.moveToNext();
                    }
            } finally {
                cursorWrapper.close();
            }
        return alurList;

    }
    private AlurCursorWrapper queryAlur(String whereClause, String[] whereArgs){
        Cursor cursor = db.query(
                AlurDbSchema.AlurTable.TABLE_NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                AlurDbSchema.AlurTable.Kolom.URUT + " ASC" // orderBy
        );
        return new AlurCursorWrapper(cursor, this.context);
    }
    private AlurCursorWrapper querySearchAlur(String whereClause, String[] whereArgs){
        Cursor cursor = db.query(
                AlurDbSchema.AlurTable.TABLE_NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                AlurDbSchema.AlurTable.Kolom.NAMA + " ASC" // orderBy
        );
        return new AlurCursorWrapper(cursor, this.context);
    }

    public Alur getAlur(String whereClause, String[] whereArgs){
        Alur alur = new Alur();
        AlurCursorWrapper cursorWrapper = queryAlur(whereClause, whereArgs
        );
        try {
            if (cursorWrapper.getCount() == 0){
                return null;
            } else {
                cursorWrapper.moveToFirst();
                alur = cursorWrapper.getAlur();
            }

        }finally {
            cursorWrapper.close();
        }
        return alur;
    }

    public List<Alur> searchAlur(String name){
        ReuniJurusan reuniJurusan = new ReuniJurusan(context);
        List<Alur> alurList = new ArrayList<>();
        AlurCursorWrapper cursorWrapper = querySearchAlur(AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? AND " +
                AlurDbSchema.AlurTable.Kolom.NAMA + " LIKE ?", new String[]{Integer.toString(
                reuniJurusan.getSelectJurusan().getId_jurusan())
                , "%"+name+"%"});
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                alurList.add(cursorWrapper.getAlur());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return alurList;
    }
}

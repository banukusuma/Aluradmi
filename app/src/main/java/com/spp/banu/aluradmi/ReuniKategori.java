package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.spp.banu.aluradmi.cursorwrapper.KategoriCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.KategoriDbSchema;
import com.spp.banu.aluradmi.model.Kategori;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by banu on 30/11/16.
 */

public class ReuniKategori {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public ReuniKategori(Context context){
        this.context = context.getApplicationContext();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this.context,true);
        sqLiteDatabase = databaseHelper
        .getReadableDatabase();

    }

    public List<Kategori> getKategoris(String whereClause, String[] whereArgs) {
        List<Kategori> kategoriList = new ArrayList<>();
        KategoriCursorWrapper cursorWrapper = queryKategori(whereClause, whereArgs);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                kategoriList.add(cursorWrapper.getKategori());
                cursorWrapper.moveToNext();
            }
        }finally{
                cursorWrapper.close();
            }
            return kategoriList;
        }


    private KategoriCursorWrapper queryKategori(String whereClause, String[] whereArgs){
        return new KategoriCursorWrapper(cursorKategori(whereClause,
                whereArgs
                ));
    }

    public Kategori getKategori(int id_kategori){
        Kategori kategori = new Kategori();
        KategoriCursorWrapper cursorWrapper = queryKategori(
                KategoriDbSchema.KategoriTable.Kolom.ID_KATEGORI + " = ? ",
                new String[]{Integer.toString(id_kategori)}
        );
        try {
            if (cursorWrapper.getCount() == 0){
                kategori.setId_kategori(0);
            } else {
                cursorWrapper.moveToFirst();
                kategori = cursorWrapper.getKategori();
            }
        }finally {
            cursorWrapper.close();
        }
        return kategori;
    }
    private Cursor cursorKategori(String whereClause, String[] whereArgs){
        Cursor cursor = sqLiteDatabase.query(
                KategoriDbSchema.KategoriTable.TABLE_NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return cursor;
    }

}

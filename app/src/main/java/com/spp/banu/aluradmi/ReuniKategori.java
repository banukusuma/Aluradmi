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
    private static ReuniKategori reuniKategori;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;


    public static ReuniKategori get(Context context){
        if (reuniKategori == null){
            reuniKategori = new ReuniKategori(context);
        }
        return reuniKategori;
    }

    private ReuniKategori(Context context){
        this.context = context.getApplicationContext();

        sqLiteDatabase = new DatabaseHelper(this.context, true)
        .getReadableDatabase();

    }

    public List<Kategori> getKategoris(){
        List<Kategori> kategoriList = new ArrayList<>();
        KategoriCursorWrapper cursorWrapper = queryKategori(null,null);

        try {
            if (cursorWrapper.getCount() >0 ){
                cursorWrapper.moveToFirst();
                while (!cursorWrapper.isAfterLast()){
                    kategoriList.add(cursorWrapper.getKategori());
                    cursorWrapper.moveToNext();
                }
            }else{
                Kategori kategori = new Kategori();
                kategori.setNama("Data Tidak Ada");
                kategoriList.add(kategori);
            }
        }finally {
            cursorWrapper.close();
        }
        return kategoriList;
    }


    private KategoriCursorWrapper queryKategori(String whereClause, String[] whereArgs){
        Cursor cursor = sqLiteDatabase.query(
                KategoriDbSchema.KategoriTable.TABLE_NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new KategoriCursorWrapper(cursor);
    }
}

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
    private static ReuniAlur reuniAlur;
    private Context context;
    private SQLiteDatabase db;

    public static ReuniAlur get(Context context){
        if (reuniAlur == null){
            reuniAlur = new ReuniAlur(context);
        }
        return reuniAlur;
    }
    private ReuniAlur(Context context){
        this.context = context.getApplicationContext();
        db = new DatabaseHelper(this.context, true)
        .getReadableDatabase();

    }

    public List<Alur> getAlurs(Integer id_kategori){
        List<Alur> alurList = new ArrayList<>();
        AlurCursorWrapper cursorWrapper = queryAlur(AlurDbSchema.AlurTable.Kolom.ID_KATEGORI + "= ? " +
                "AND " + AlurDbSchema.AlurTable.Kolom.ID_JURUSAN + " = ? ",
                new String[]{id_kategori.toString(), isSelectedJurusanQuery().toString()}
        );

            try {
                if (cursorWrapper.getCount() > 0) {
                    cursorWrapper.moveToFirst();
                    while (!cursorWrapper.isAfterLast()) {
                        alurList.add(cursorWrapper.getAlur());
                        cursorWrapper.moveToNext();
                    }
                }else {
                    Alur alur = new Alur();
                    alur.setNama("Data Tidak Ada");
                    alurList.add(alur);
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

        return new AlurCursorWrapper(cursor);
    }

    private Integer isSelectedJurusanQuery(){
        Cursor cursor = db.query(
                JurusanDbSchema.JurusanTable.TABLE_NAME,
                new String[]{"id_jurusan"}, // Columns - null selects all columns
                "status = ?",
                new String []{"1"},
                null, // groupBy
                null, // having
                null // orderBy
        );
        try {
            cursor.moveToFirst();
            Integer id_jurusan = cursor.getColumnIndex(JurusanDbSchema.JurusanTable.Kolom.ID_JURUSAN);
            return id_jurusan;
        } finally {
            cursor.close();
        }

    }

}

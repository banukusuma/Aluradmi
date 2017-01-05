package com.spp.banu.aluradmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.spp.banu.aluradmi.cursorwrapper.JurusanCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.JurusanDbSchema;
import com.spp.banu.aluradmi.model.Jurusan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 03/12/16.
 */

public class ReuniJurusan {
    private Context context;
    private SQLiteDatabase database;
    public ReuniJurusan(Context context){
            this.context = context.getApplicationContext();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this.context, true);
        database = databaseHelper
                .getWritableDatabase();
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


    private JurusanCursorWrapper queryJurusan(String whereClause, String[] whereArgs){
        Cursor cursor = cursorJurusan(whereClause, whereArgs);
        return new JurusanCursorWrapper(cursor);
    }

    public int getPositionSelectedJurusan(String whereClause, String[] whereArgs){
        int angka = 0;
        Cursor cursor = cursorJurusan(whereClause, whereArgs);
        cursor.moveToFirst();
        while (cursor.getInt(cursor.getColumnIndex(JurusanDbSchema.JurusanTable.Kolom.STATUS)) != 1){
            cursor.moveToNext();
        }
        angka = cursor.getPosition();
        cursor.close();
        return angka;
    }

    public void SelectJurusan(String nama){
        unSelectAllJurusan();
        ContentValues values = new ContentValues();
        values.put(JurusanDbSchema.JurusanTable.Kolom.STATUS, "1");
        int cek = database.update(JurusanDbSchema.JurusanTable.TABLE_NAME, values, JurusanDbSchema.JurusanTable.Kolom.NAMA + " = ? ", new String[]{nama});
        Log.i("Reuni Jurusan", "SelectJurusan: " + cek);
    }

    public void unSelectAllJurusan(){
        ContentValues values = new ContentValues();
        values.put(JurusanDbSchema.JurusanTable.Kolom.STATUS,"0");
        int cek = database.update(JurusanDbSchema.JurusanTable.TABLE_NAME, values, null, null);
        Log.i("Reuni Jurusan", "unSelectAllJurusan: " +cek);
    }

    public boolean isSelectedJurusan(){
        Cursor cursor = cursorJurusan(JurusanDbSchema.JurusanTable.Kolom.STATUS + " = ? ", new String[]{"1"});
        boolean isSelect = cursor.getCount() != 1;
        return isSelect;
    }

    private Cursor cursorJurusan(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                JurusanDbSchema.JurusanTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return cursor;
    }
    public Jurusan getSelectJurusan(){
        Cursor cursor = cursorJurusan(JurusanDbSchema.JurusanTable.Kolom.STATUS + " = ? ", new String[]{"1"});
        JurusanCursorWrapper cursorWrapper = new JurusanCursorWrapper(cursor);
        if (cursor.getCount() == 0){
            Jurusan jurusan = new Jurusan();
            jurusan.setId_jurusan(0);
            return jurusan;
        }
        cursorWrapper.moveToFirst();
        return cursorWrapper.getJurusan();
    }


}

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
        Cursor cursor = cursorAlur(whereClause,whereArgs);
        return new AlurCursorWrapper(cursor, this.context);
    }

    private Cursor cursorAlur(String whereClause, String[] whereArgs){
        Cursor cursor = db.query(
                AlurDbSchema.AlurTable.TABLE_NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                AlurDbSchema.AlurTable.Kolom.URUT + " ASC" // orderBy
        );
        return cursor;
    }
}

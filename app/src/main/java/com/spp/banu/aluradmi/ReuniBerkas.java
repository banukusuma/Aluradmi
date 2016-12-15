package com.spp.banu.aluradmi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.spp.banu.aluradmi.DatabaseHelper;
import com.spp.banu.aluradmi.cursorwrapper.BerkasCursorWrapper;
import com.spp.banu.aluradmi.dbSchema.BerkasDbSchema;
import com.spp.banu.aluradmi.model.Berkas;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 13/12/16.
 */

public class ReuniBerkas {
    private Context context;
    private SQLiteDatabase database;

    public ReuniBerkas(Context context) {
        this.context = context;
        this.database = new DatabaseHelper(this.context, true)
        .getReadableDatabase();
    }

    public BerkasCursorWrapper queryBerkas(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(
                BerkasDbSchema.BerkasTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new BerkasCursorWrapper(cursor);
    }
    public List<Berkas> getBerkasList(int id_keterangan){
        List<Berkas> berkasList = new ArrayList<>();
        BerkasCursorWrapper cursorWrapper = queryBerkas("id_keterangan = ? ", new String[]{Integer.toString(id_keterangan)});
        try {
            if (cursorWrapper.getCount() > 0){
                cursorWrapper.moveToFirst();
                while (!cursorWrapper.isAfterLast()){
                    berkasList.add(cursorWrapper.getBerkas());
                    cursorWrapper.moveToNext();
                }
            }else{
                Berkas berkas = new Berkas();
                berkas.setNama("tidak ada");
                berkasList.add(berkas);
            }
        }finally {
            cursorWrapper.close();
        }
        return berkasList;
    }
}

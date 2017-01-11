package com.spp.banu.aluradmi.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.JurusanDbSchema;
import com.spp.banu.aluradmi.model.Jurusan;

/**
 * Created by banu on 16/12/16.
 */

public class JurusanCursorWrapper extends CursorWrapper {
    public JurusanCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Jurusan getJurusan(){
        int id_jurusan = getInt(getColumnIndex(JurusanDbSchema.JurusanTable.Kolom.ID_JURUSAN));
        String nama = getString(getColumnIndex(JurusanDbSchema.JurusanTable.Kolom.NAMA));
        int status = getInt(getColumnIndex(JurusanDbSchema.JurusanTable.Kolom.STATUS));
        String timestamp = getString(getColumnIndex(JurusanDbSchema.JurusanTable.Kolom.TIMESTAMP));

        Jurusan jurusan = new Jurusan();
        jurusan.setId_jurusan(id_jurusan);
        jurusan.setNama(nama);
        jurusan.setChoose(status != 0);
        jurusan.setTimestamp(timestamp);
        return jurusan;
    }

    public String getNamaJurusan(){
        String nama = getString(getColumnIndex(JurusanDbSchema.JurusanTable.Kolom.NAMA));
        return nama ;
    }
}

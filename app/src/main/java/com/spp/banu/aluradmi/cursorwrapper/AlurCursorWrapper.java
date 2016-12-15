package com.spp.banu.aluradmi.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.model.Alur;

/**
 * Created by banu on 12/12/16.
 */

public class AlurCursorWrapper extends CursorWrapper {
    public AlurCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Alur getAlur(){
        Integer id_alur = getInt(getColumnIndex(AlurDbSchema.AlurTable.Kolom.ID_ALUR));
        String nama = getString(getColumnIndex(AlurDbSchema.AlurTable.Kolom.NAMA));
        Integer id_kategori = getInt(getColumnIndex(AlurDbSchema.AlurTable.Kolom.ID_KATEGORI));
        Integer id_jurusan = getInt(getColumnIndex(AlurDbSchema.AlurTable.Kolom.ID_JURUSAN));
        String timestamp = getString(getColumnIndex(AlurDbSchema.AlurTable.Kolom.TIMESTAMP));
        Integer urut = getInt(getColumnIndex(AlurDbSchema.AlurTable.Kolom.URUT));

        Alur alur = new Alur();
        alur.setId_alur(id_alur);
        alur.setNama(nama);
        alur.setId_jurusan(id_jurusan);
        alur.setId_kategori(id_kategori);
        alur.setUrut(urut);
        alur.setTimestamp(timestamp);
        //ini nanti ditambahkan untuk progress dari berapa jumlah keterangan yang sudah diselesaikan

        return alur;
    }
}


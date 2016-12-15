package com.spp.banu.aluradmi.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.KategoriDbSchema;
import com.spp.banu.aluradmi.model.Kategori;

/**
 * Created by banu on 12/12/16.
 */

public class KategoriCursorWrapper extends CursorWrapper {

    public KategoriCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Kategori getKategori(){
        Integer id_kategori = getInt(getColumnIndex(KategoriDbSchema.KategoriTable.Kolom.ID_KATEGORI));
        String nama = getString(getColumnIndex(KategoriDbSchema.KategoriTable.Kolom.NAMA));
        String timestamp = getString(getColumnIndex(KategoriDbSchema.KategoriTable.Kolom.TIMESTAMP));

        Kategori kategori = new Kategori();
        kategori.setId_kategori(id_kategori);
        kategori.setNama(nama);
        kategori.setTimestamp(timestamp);
        return kategori;
    }
}

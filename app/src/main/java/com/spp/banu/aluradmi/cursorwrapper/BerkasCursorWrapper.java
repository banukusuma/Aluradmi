package com.spp.banu.aluradmi.cursorwrapper;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.BerkasDbSchema;
import com.spp.banu.aluradmi.model.Berkas;

/**
 * Created by banu on 13/12/16.
 */

public class BerkasCursorWrapper extends CursorWrapper {

    public BerkasCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Berkas getBerkas(){
        int id_berkas = getInt(getColumnIndex(BerkasDbSchema.BerkasTable.Kolom.ID_BERKAS));
        int id_keterangan = getInt(getColumnIndex(BerkasDbSchema.BerkasTable.Kolom.ID_KETERANGAN));
        String nama = getString(getColumnIndex(BerkasDbSchema.BerkasTable.Kolom.NAMA));
        String timestamp = getString(getColumnIndex(BerkasDbSchema.BerkasTable.Kolom.TIMESTAMP));

        Berkas berkas = new Berkas();
        berkas.setId_berkas(id_berkas);
        berkas.setNama(nama);
        berkas.setId_keterangan(id_keterangan);
        berkas.setTimestamp(timestamp);
        return berkas;
    }
}

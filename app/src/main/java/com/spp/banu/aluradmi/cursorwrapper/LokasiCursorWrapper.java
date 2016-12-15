package com.spp.banu.aluradmi.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.LokasiDbSchema;
import com.spp.banu.aluradmi.model.Lokasi;

/**
 * Created by banu on 13/12/16.
 */

public class LokasiCursorWrapper extends CursorWrapper {
    public LokasiCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Lokasi getLokasi(){
        int id_lokasi = getInt(getColumnIndex(LokasiDbSchema.LokasiTable.Kolom.ID_LOKASI));
        String nama = getString(getColumnIndex(LokasiDbSchema.LokasiTable.Kolom.NAMA));
        double lattitude = getDouble(getColumnIndex(LokasiDbSchema.LokasiTable.Kolom.LATTITUDE));
        double longitude = getDouble(getColumnIndex(LokasiDbSchema.LokasiTable.Kolom.LONGITUDE));
        String timestamp = getString(getColumnIndex(LokasiDbSchema.LokasiTable.Kolom.TIMESTAMP));

        Lokasi lokasi = new Lokasi();
        lokasi.setId_lokasi(id_lokasi);
        lokasi.setNama(nama);
        lokasi.setLattitude(lattitude);
        lokasi.setLongitude(longitude);
        lokasi.setTimestamp(timestamp);

        return lokasi;

    }
}

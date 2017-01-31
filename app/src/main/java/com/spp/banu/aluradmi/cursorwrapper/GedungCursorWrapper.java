package com.spp.banu.aluradmi.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.GedungDbSchema;
import com.spp.banu.aluradmi.model.Gedung;

/**
 * Created by banu on 31/01/17.
 */

public class GedungCursorWrapper extends CursorWrapper {
    public GedungCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Gedung getGedung(){
        int id_gedung = getInt(getColumnIndex(GedungDbSchema.GedungTable.Kolom.ID_GEDUNG));
        String nama = getString(getColumnIndex(GedungDbSchema.GedungTable.Kolom.NAMA));
        double lat = getDouble(getColumnIndex(GedungDbSchema.GedungTable.Kolom.LATITUDE));
        double lng = getDouble(getColumnIndex(GedungDbSchema.GedungTable.Kolom.LONGITUDE));
        String timestamp = getString(getColumnIndex(GedungDbSchema.GedungTable.Kolom.TIMESTAMP));

        Gedung gedung = new Gedung();
        gedung.setId_gedung(id_gedung);
        gedung.setNama(nama);
        gedung.setLatitude(lat);
        gedung.setLongitude(lng);
        gedung.setTimestamp(timestamp);

        return gedung;
    }
}

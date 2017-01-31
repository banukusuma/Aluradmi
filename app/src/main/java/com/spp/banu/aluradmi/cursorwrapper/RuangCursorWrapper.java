package com.spp.banu.aluradmi.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.GedungDbSchema;
import com.spp.banu.aluradmi.dbSchema.RuangDbSchema;
import com.spp.banu.aluradmi.model.Ruang;

/**
 * Created by banu on 31/01/17.
 */

public class RuangCursorWrapper extends CursorWrapper {

    public RuangCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Ruang getRuang(){
        int id_ruang = getInt(getColumnIndex(RuangDbSchema.RuangTable.Kolom.ID_RUANG));
        String nama = getString(getColumnIndex(RuangDbSchema.RuangTable.Kolom.NAMA));
        String timestamp = getString(getColumnIndex(RuangDbSchema.RuangTable.Kolom.TIMESTAMP));
        String nama_gedung = getString(getColumnIndex("nama_gedung"));
        String lantai = getString(getColumnIndex("lantai"));
        String thumbnail = getString(getColumnIndex("thumbnail"));
        String link = getString(getColumnIndex("link"));
        int id_Gedung = getInt(getColumnIndex("id_gedung"));
        Ruang ruang = new Ruang();
        ruang.setNama(nama);
        ruang.setTimestamp(timestamp);
        ruang.setId_ruang(id_ruang);
        ruang.setNama_gedung(nama_gedung);
        ruang.setLantai(lantai);
        ruang.setThumbnail(thumbnail);
        ruang.setLink(link);
        ruang.setId_gedung(id_Gedung);
        return ruang;
    }
}

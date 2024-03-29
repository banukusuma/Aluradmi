package com.spp.banu.aluradmi.cursorwrapper;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;

import com.spp.banu.aluradmi.model.Keterangan;




/**
 * Created by banu on 12/12/16.
 */

public class KeteranganCursorWrapper extends CursorWrapper {
    public KeteranganCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Keterangan getKeterangan(){
        int id_keterangan = getInt(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.ID_KETERANGAN));
        String nama = getString(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.NAMA));
        String isi_keterangan = getString(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.KETERANGAN));
        int id_alur = getInt(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR));
        int id_ruang = getInt(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.ID_RUANG));
        String timestamp = getString(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.TIMESTAMP));
        int urut = getInt(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.URUT));
        int status = getInt(getColumnIndex(KeteranganDbSchema.KeteranganTable.Kolom.STATUS));

        Keterangan keterangan = new Keterangan();
        keterangan.setId_keterangan(id_keterangan);
        keterangan.setNama(nama);
        keterangan.setId_alur(id_alur);
        keterangan.setTimestamp(timestamp);
        keterangan.setUrut(urut);
        keterangan.setStatus(status != 0);
        keterangan.setKeterangan(isi_keterangan);
        keterangan.setId_ruang(id_ruang);
        return keterangan;
    }
}

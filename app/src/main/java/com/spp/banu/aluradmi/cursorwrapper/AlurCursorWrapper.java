package com.spp.banu.aluradmi.cursorwrapper;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.spp.banu.aluradmi.ReuniKeterangan;
import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;
import com.spp.banu.aluradmi.model.Alur;
import com.spp.banu.aluradmi.model.Keterangan;

import java.util.List;

/**
 * Created by banu on 12/12/16.
 */

public class AlurCursorWrapper extends CursorWrapper {
    private Context context;
    public AlurCursorWrapper(Cursor cursor, Context context) {
        super(cursor);
        this.context = context;

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
        ReuniKeterangan reuniKeterangan = new ReuniKeterangan(context);
        int jumlahSelesai = reuniKeterangan.getKeteranganList(
                KeteranganDbSchema.KeteranganTable.Kolom.STATUS + " = ? AND " +
                        KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? ", new String[]{"1", Integer.toString(id_alur)}
        ).size();
        int jumlahSemua = reuniKeterangan.getKeteranganList(
                KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR + " = ? ", new String[]{Integer.toString(id_alur)}
        ).size();
        if (jumlahSemua == 0 ){
            jumlahSemua = 1;
        }

        float progress = (float) (jumlahSelesai * 100) / jumlahSemua;
        //ini nanti ditambahkan untuk progress dari berapa jumlah keterangan yang sudah diselesaikan
        alur.setProgress(progress);
        return alur;
    }
}


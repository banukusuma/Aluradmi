package com.spp.banu.aluradmi.model;

import java.util.Date;

/**
 * Created by banu on 05/12/16.
 */

public class Berkas {
    private int id_berkas;
    private String nama;
    private int id_keterangan;
    private String timestamp;

    public int getId_berkas() {
        return id_berkas;
    }

    public void setId_berkas(int id_berkas) {
        this.id_berkas = id_berkas;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getId_keterangan() {
        return id_keterangan;
    }

    public void setId_keterangan(int id_keterangan) {
        this.id_keterangan = id_keterangan;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

package com.spp.banu.aluradmi.model;

import java.util.List;

/**
 * Created by banu on 05/12/16.
 */

public class Keterangan {
    private int id_keterangan;
    private String nama;
    private int id_lokasi;
    private int id_alur;
    private String keterangan;
    private String timestamp;
    private int urut;
    private boolean status;




    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getUrut() {
        return urut;
    }

    public void setUrut(int urut) {
        this.urut = urut;
    }


    public int getId_lokasi() {
        return id_lokasi;
    }

    public void setId_lokasi(int id_lokasi) {
        this.id_lokasi = id_lokasi;
    }

    public int getId_keterangan() {
        return id_keterangan;
    }

    public void setId_keterangan(int id_keterangan) {
        this.id_keterangan = id_keterangan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String  getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId_alur() {
        return id_alur;
    }

    public void setId_alur(int id_alur) {
        this.id_alur = id_alur;
    }
}

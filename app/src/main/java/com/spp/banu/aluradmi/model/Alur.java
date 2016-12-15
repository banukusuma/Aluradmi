package com.spp.banu.aluradmi.model;

import java.util.Date;

/**
 * Created by banu on 01/12/16.
 */

public class Alur {
    private int id_alur;
    private String nama;
    private int urut;
    private String timestamp;
    private int id_kategori;
    private int id_jurusan;
    private int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getId_alur() {
        return id_alur;
    }

    public void setId_alur(int id_alur) {
        this.id_alur = id_alur;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getUrut() {
        return urut;
    }

    public void setUrut(int urut) {
        this.urut = urut;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(int id_kategori) {
        this.id_kategori = id_kategori;
    }

    public int getId_jurusan() {
        return id_jurusan;
    }

    public void setId_jurusan(int id_jurusan) {
        this.id_jurusan = id_jurusan;
    }
}

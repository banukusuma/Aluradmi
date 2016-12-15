package com.spp.banu.aluradmi.model;

/**
 * Created by banu on 03/12/16.
 */

public class Jurusan {
    private int id_jurusan;
    private String nama;
    private boolean isChoose;

    public int getId_jurusan() {
        return id_jurusan;
    }

    public void setId_jurusan(int id_jurusan) {
        this.id_jurusan = id_jurusan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}

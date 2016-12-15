package com.spp.banu.aluradmi.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by banu on 30/11/16.
 */

public class Kategori {
    private int id_kategori;
    private String nama;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Kategori(){
    }

    public int getId_kategori() {
        return id_kategori;
    }
    public void setId_kategori(int id_kategori){
        this.id_kategori = id_kategori;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}

package com.spp.banu.aluradmi.model;

/**
 * Created by banu on 31/01/17.
 */

public class Ruang {
    private int id_ruang;
    private String nama_gedung;
    private String lantai;
    private String nama;
    private String thumbnail;
    private String link;
    private String timestamp;
    private int id_gedung;

    public int getId_gedung() {
        return id_gedung;
    }

    public void setId_gedung(int id_gedung) {
        this.id_gedung = id_gedung;
    }

    public int getId_ruang() {
        return id_ruang;
    }

    public void setId_ruang(int id_ruang) {
        this.id_ruang = id_ruang;
    }

    public String getNama_gedung() {
        return nama_gedung;
    }

    public void setNama_gedung(String nama_gedung) {
        this.nama_gedung = nama_gedung;
    }

    public String getLantai() {
        return lantai;
    }

    public void setLantai(String lantai) {
        this.lantai = lantai;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

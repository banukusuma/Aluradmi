package com.spp.banu.aluradmi.model;

/**
 * Created by banu on 31/01/17.
 */

public class Gedung {
    private int id_gedung;
    private String nama;
    private double latitude;
    private double longitude;
    private String timestamp;

    public int getId_gedung() {
        return id_gedung;
    }

    public void setId_gedung(int id_gedung) {
        this.id_gedung = id_gedung;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

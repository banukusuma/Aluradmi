package com.spp.banu.aluradmi.model;

/**
 * Created by banu on 28/02/17.
 */

public class Bantuan {
    private int thumbnail;
    private String penjelasan;

    public Bantuan(String penjelasan, int thumbnail) {
        this.penjelasan = penjelasan;
        this.thumbnail = thumbnail;
    }

    public String getPenjelasan() {
        return penjelasan;
    }

    public int getThumbnail() {
        return thumbnail;
    }
}

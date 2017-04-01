package com.spp.banu.aluradmi.model;

/**
 * Created by banu on 01/04/17.
 */

public class Help {
    private int id;
    private String name;

    public Help(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ". " + name ;}
}

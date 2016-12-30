package com.spp.banu.aluradmi.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by banu on 30/12/16.
 */

public class Rute {
    private LatLng startLocation;
    private LatLng endLocation;
    private List<LatLng> point;
    private String startAddress;
    private String endAddress;

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public List<LatLng> getPoint() {
        return point;
    }

    public void setPoint(List<LatLng> point) {
        this.point = point;
    }
}

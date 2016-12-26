package com.spp.banu.aluradmi.fragment;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by banu on 26/12/16.
 */

public class LokasiFragment extends SupportMapFragment implements OnMapReadyCallback{
    private static final String TAG = "LokasiFragment";
    private GoogleMap map;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        gotoLocationZoom(-7.7686093925, 110.38846627937, 19);
    }

    private void gotoLocation(double lat, double lng){
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
        map.moveCamera(update);
    }
    private void gotoLocationZoom(double lat, double lng, float zoom){
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.moveCamera(update);
    }
}

package com.spp.banu.aluradmi.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.spp.banu.aluradmi.DirectionFinderListener;
import com.spp.banu.aluradmi.R;
import com.spp.banu.aluradmi.ReuniLokasi;
import com.spp.banu.aluradmi.httpcall.DirectionFinder;
import com.spp.banu.aluradmi.model.Lokasi;
import com.spp.banu.aluradmi.model.Rute;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 26/12/16.
 */

public class LokasiFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionFinderListener {
    private static final String TAG = "LokasiFragment";
    GoogleMap map;
    Marker currentLocationMarker;
    GoogleApiClient apiClient;
    Location currentLocation;
    Double currentLat;
    Double currentLng;
    LocationRequest locationRequest;
    ProgressDialog progressDialog;
    private List<Polyline> polylineList = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        apiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();
        ReuniLokasi reuniLokasi = new ReuniLokasi(getActivity());
        Lokasi lokasi = reuniLokasi.getLokasi(9);
        currentLat = currentLocation.getLatitude();
        currentLng = currentLocation.getLongitude();
        String saatini = currentLat + "," + currentLng;
        try {
            new DirectionFinder(this, saatini, lokasi).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void gotoLocation(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
        map.moveCamera(update);
    }

    private void gotoLocationZoom(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.animateCamera(update);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Log.e(TAG, "onConnected: lattitude longitude" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() );
        MarkerOptions options = new MarkerOptions()
                .title("Current Location")
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        if (currentLocation == null){
            Toast.makeText(getActivity(), "Can't get current location", Toast.LENGTH_LONG);
        } else {
            if (currentLocationMarker != null){
                currentLocationMarker.remove();
            }
            currentLocationMarker = map.addMarker(options);
            gotoLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 12);
        }
    }

    public void placeMarker(String title, double lat, double lng){
        MarkerOptions options = new MarkerOptions()
                .title(title)
                .position(new LatLng(lat, lng));
        map.addMarker(options);
    }

    @Override
    public void DirectionFinderStart() {
        progressDialog = ProgressDialog.show(getActivity(), "Please wait.",
                "Finding direction..!", true);
        if (polylineList != null) {
            for (Polyline polyline:polylineList ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void DirectionFinderSuccess(List<Rute> rutes) {
        progressDialog.dismiss();
        polylineList = new ArrayList<>();

        for (Rute rute : rutes){
            gotoLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 17);
            placeMarker(rute.getEndAddress(), rute.getEndLocation().latitude,rute.getEndLocation().longitude);

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < rute.getPoint().size(); i++)
                polylineOptions.add(rute.getPoint().get(i));

            polylineList.add(map.addPolyline(polylineOptions));
        }
    }
}

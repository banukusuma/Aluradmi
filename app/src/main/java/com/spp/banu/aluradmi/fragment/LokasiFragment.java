package com.spp.banu.aluradmi.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
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

import com.spp.banu.aluradmi.httpcall.CheckNetwork;
import com.spp.banu.aluradmi.httpcall.DirectionFinder;

import com.spp.banu.aluradmi.model.Gedung;
import com.spp.banu.aluradmi.model.Rute;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 26/12/16.
 */

public class LokasiFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionFinderListener, DialogInterface.OnClickListener


{
    private static final String TAG = "LokasiFragment";
    GoogleMap map;
    Marker currentLocationMarker;
    GoogleApiClient apiClient;
    LocationRequest locationRequest;
    Location currentLocation;
    android.support.v7.widget.SearchView searchView;
    ProgressDialog progressDialog;
    private List<Polyline> polylineList = new ArrayList<>();
    Intent dialogSettingintent;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivity().setTitle(R.string.lokasi);
        setHasOptionsMenu(true);
        getMapAsync(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lokasi_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lokasi_saya){
            gotoLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 15);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        CheckNetwork network = new CheckNetwork(getActivity());
        if (!network.isNetworkAvailable()) {
            showDialog(getActivity(), "internet");
        }
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
        //diganti gedung

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
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getActivity(), "Can't get current location", Toast.LENGTH_LONG);
        } else {
            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            currentLocation = location;
            Log.e(TAG, "onConnected: lattitude longitude" + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            MarkerOptions options = new MarkerOptions()
                    .title("Current Location")
                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            currentLocationMarker = map.addMarker(options);
            gotoLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 12);
        }
    }

    public void placeMarker(String title, double lat, double lng) {
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
            for (Polyline polyline : polylineList) {
                polyline.remove();
            }
        }
    }

    @Override
    public void DirectionFinderFailed() {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), "Gagal Mengambil Data", Toast.LENGTH_LONG);
    }

    @Override
    public void DirectionFinderSuccess(List<Rute> rutes, Gedung destination_gedung) {

    }


    public void showDialog(final Context ctx, String Mode) {
        final Context context = ctx;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (Mode == "internet") {
            builder.setCancelable(true);
            dialogSettingintent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            builder.setMessage("Untuk Menggunakan Fitur Peta Membutuhkan Koneksi internet");
            builder.setTitle("Tidak Ada Koneksi Internet");
            builder.setPositiveButton("Buka Setting", this);
        } else {
            builder.setCancelable(true);
            dialogSettingintent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            builder.setMessage("Untuk Menjalankan Rute Peta Memerlukan GPS dalam keadaan menyala");
            builder.setTitle("GPS tidak aktif");
            builder.setPositiveButton("Buka Setting", this);
        }

        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        startActivity(dialogSettingintent);
    }


}
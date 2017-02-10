package com.spp.banu.aluradmi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.spp.banu.aluradmi.dbSchema.GedungDbSchema;
import com.spp.banu.aluradmi.httpcall.DirectionFinder;
import com.spp.banu.aluradmi.model.Edge;
import com.spp.banu.aluradmi.model.Gedung;
import com.spp.banu.aluradmi.model.Rute;
import com.spp.banu.aluradmi.model.Vertex;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionFinderListener, DialogInterface.OnClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener{

    private static final String TAG = "LokasiFragment";
    GoogleMap map;
    Marker currentLocationMarker;
    private ReuniGedung reuniGedung;
    GoogleApiClient apiClient;
    LocationRequest locationRequest;
    Location currentLocation;
    Marker getCurrentClickMarker;
    private List<Vertex> nodes;
    private List<Edge> edges;
    ProgressDialog progressDialog;
    private LinkedList<Polyline> routeList = new LinkedList<>();
    private List<Polyline> polylineList = new ArrayList<>();
    Intent dialogSettingintent;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        reuniGedung = new ReuniGedung(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lokasi_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lokasi_saya){
            gotoLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 12);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (map == null){
            map = googleMap;
        }
        if (map != null){
            map.setInfoWindowAdapter(this);
            map.setOnInfoWindowClickListener(this);
        }
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        apiClient.connect();

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(this, "Can't get current location", Toast.LENGTH_LONG).show();
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
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Sedang mencari rute..", true);
        if (polylineList != null) {
            for (Polyline polyline : polylineList) {
                polyline.remove();
            }
        }
    }

    @Override
    public void DirectionFinderFailed() {
        progressDialog.dismiss();
        Toast.makeText(this, "gagal mengambil data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void DirectionFinderSuccess(List<Rute> rutes) {
        progressDialog.dismiss();
        polylineList = new ArrayList<>();

        for (Rute rute : rutes) {
            gotoLocationZoom(currentLocationMarker.getPosition().latitude, currentLocationMarker.getPosition().longitude, 12);
            placeMarker(rute.getEndAddress(), rute.getEndLocation().latitude, rute.getEndLocation().longitude);

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < rute.getPoint().size(); i++) {
                polylineOptions.add(rute.getPoint().get(i));
            }


            polylineList.add(map.addPolyline(polylineOptions));
        }
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

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.info_window, null);
        TextView textView = (TextView) view.findViewById(R.id.nama_lokasi_text_view);
        Button button = (Button) view.findViewById(R.id.show_rute_btn);
        getCurrentClickMarker = marker;
        textView.setText(marker.getTitle());
        button.setText("Tunjukkan Rute");
        return view;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String destination = marker.getPosition().latitude + "," + marker.getPosition().longitude;
        String origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo) {
        Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo) );
        edges.add(lane);
    }
}

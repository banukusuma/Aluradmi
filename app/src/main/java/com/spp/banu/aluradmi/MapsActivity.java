package com.spp.banu.aluradmi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.spp.banu.aluradmi.dbSchema.GedungDbSchema;
import com.spp.banu.aluradmi.httpcall.DirectionFinder;
import com.spp.banu.aluradmi.model.Edge;
import com.spp.banu.aluradmi.model.Gedung;
import com.spp.banu.aluradmi.model.Graph;
import com.spp.banu.aluradmi.model.Rute;
import com.spp.banu.aluradmi.model.Vertex;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DirectionFinderListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapsActivity";
    GoogleMap map;
    public static final double R_bumi = 6372.8; // In kilometers
    private Circle northCircle, southCircle,  midleCircle;

    Marker currentLocationMarker;
    GoogleApiClient apiClient;
    LocationRequest locationRequest;
    Location currentLocation;
    private List<Vertex> nodes;
    private List<Edge> edges;
    ProgressDialog progressDialog;
    private static int STATUS_POSISI;
    private static boolean MODE_DI_DALAM_FT;
    private List<Polyline> polylineList = new ArrayList<>();
    private final static int REQUEST_LOCATION = 202;
    Intent dialogSettingintent;
    private ArrayList<LatLng> pointToUseInRoute;
    //titik pembatas di area ft
    private final static LatLng southpoint = new LatLng(-7.77186626415878, 110.38690410554409);
    private final static LatLng northpoint = new LatLng(-7.768903792602624, 110.3882696852088);
    private final static LatLng midlepoint = new LatLng(-7.769945766027917, 110.3877255320549);
    List<Gedung> gedungList;

    private boolean isUsingRoute;
    //extra id gedung intent
    private static final String EXTRA_ID_GEDUNG = "com.spp.banu.aluradmi.mapsIntent.id.gedung";

    //key untuk disimpan di bundle
    protected final static String ROUTE_KEY = "route-gedung-aluradmi-ft";
    protected final static String ID_ROUTE_LIST_KEY = "route-list-gedung-aluradmi-ft";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isUsingRoute = false;
        MODE_DI_DALAM_FT = true;
        nodes = new ArrayList<>();
        ReuniGedung reuniGedung = new ReuniGedung(this);
        gedungList = reuniGedung.getGedungList(GedungDbSchema.GedungTable.Kolom.ID_GEDUNG + " != ? ",
                new String[]{"99"});
        pointToUseInRoute = new ArrayList<>();
        //menggambar titik persimpangan node
        nodes.add(new Vertex("1", "node 1", new LatLng(-7.769449329031687, 110.38801185786724)));
        nodes.add(new Vertex("2", "node 2", new LatLng(-7.7695613213732395, 110.3878864645958)));
        nodes.add(new Vertex("3", "node 3", new LatLng(-7.769425784475547, 110.38769602775574)));
        nodes.add(new Vertex("4", "node 4", new LatLng(-7.769096243600631, 110.38781940937042)));
        nodes.add(new Vertex("5", "node 5", new LatLng(-7.769223807840961, 110.38768798112869)));
        nodes.add(new Vertex("6", "node 6", new LatLng(-7.76904574941147, 110.38764238357544)));
        nodes.add(new Vertex("7", "node 7", new LatLng(-7.769322138583081, 110.387382209301)));
        nodes.add(new Vertex("8", "node 8", new LatLng(-7.768671029186398, 110.38774967193604)));
        nodes.add(new Vertex("9", "node 9", new LatLng(-7.769641048939684, 110.38733124732971)));
        nodes.add(new Vertex("10", "node 10", new LatLng(-7.769901492217901, 110.38780063390732)));
        nodes.add(new Vertex("11", "node 11", new LatLng(-7.7702602658570425, 110.38768798112869)));
        nodes.add(new Vertex("12", "node 12", new LatLng(-7.770297471994675, 110.3877604007721)));
        nodes.add(new Vertex("13", "node 13", new LatLng(-7.770980469792488, 110.38755655288696)));
        nodes.add(new Vertex("14", "node 14", new LatLng(-7.77137910612597, 110.38744390010834)));
        nodes.add(new Vertex("15", "node 15", new LatLng(-7.7714535181996265, 110.38761287927628)));
        nodes.add(new Vertex("16", "node 16", new LatLng(-7.771724590642157, 110.3872936964035)));
        nodes.add(new Vertex("17", "node 17", new LatLng(-7.77154919084639, 110.38722395896912)));
        nodes.add(new Vertex("18", "node 18", new LatLng(-7.771302036464343, 110.38710862398148)));
        nodes.add(new Vertex("19", "node 19", new LatLng(-7.771674096769067, 110.38699865341187)));
        nodes.add(new Vertex("20", "node 20", new LatLng(-7.771429600034542, 110.38692891597748)));
        nodes.add(new Vertex("21", "node 21", new LatLng(-7.771655493761666, 110.38685113191605)));
        nodes.add(new Vertex("22", "node 22", new LatLng(-7.77185746922651, 110.38686722517014)));
        nodes.add(new Vertex("23", "node 23", new LatLng(-7.771472121215996, 110.38705229759216)));
        nodes.add(new Vertex("24", "node 24", new LatLng(-7.771631575608071, 110.38757801055908)));
        nodes.add(new Vertex("25", "node 25", new LatLng(-7.771820263227128, 110.38767457008362)));
        nodes.add(new Vertex("26", "node 26", new LatLng(-7.769385920673774, 110.38762360811234)));
        nodes.add(new Vertex("27", "node 27", new LatLng(-7.769731406830022, 110.38785696029663)));
        nodes.add(new Vertex("28", "node 28", new LatLng(-7.769789873689877, 110.3874546289444)));
        nodes.add(new Vertex("29", "node 29", new LatLng(-7.770063604789108, 110.38774967193604)));
        nodes.add(new Vertex("30", "node 30", new LatLng(-7.770632327084954, 110.38766384124756)));
        nodes.add(new Vertex("31", "node 31", new LatLng(-7.769086435469498, 110.38814328610897)));
        nodes.add(new Vertex("32", "node 32", new LatLng(-7.769300803147504, 110.38807002827525)));

        //menghubungkan node tersebut dikurangi 1
        edges = new ArrayList<>();
        addLane("1", 0, 1);
        addLane("2", 1, 0);
        addLane("3", 1, 2);
        addLane("4", 1, 26);
        addLane("5", 2, 3);
        addLane("6", 2, 1);
        addLane("7", 2, 4);
        addLane("8", 2, 25);
        addLane("9", 3, 2);
        addLane("10", 3, 4);
        addLane("11", 3, 5);
        addLane("12", 4, 2);
        addLane("13", 4, 3);
        addLane("14", 4, 5);
        addLane("15", 4, 25);
        addLane("16", 4, 6);
        addLane("17", 5, 4);
        addLane("18", 5, 3);
        addLane("19", 5, 7);
        addLane("20", 6, 25);
        addLane("21", 6, 4);
        addLane("22", 6, 8);
        addLane("23", 7, 5);
        addLane("24", 8, 6);
        addLane("25", 8, 27);
        addLane("26", 9, 26);
        addLane("27", 9, 28);
        addLane("28", 10, 28);
        addLane("29", 10, 11);
        addLane("30", 11, 10);
        addLane("31", 11, 29);
        addLane("32", 12, 29);
        addLane("33", 12, 13);
        addLane("34", 13, 12);
        addLane("35", 13, 14);
        addLane("36", 13, 15);
        addLane("37", 13, 17);
        addLane("38", 13, 16);
        addLane("39", 14, 13);
        addLane("40", 14, 23);
        addLane("41", 15, 13);
        addLane("42", 15, 16);
        addLane("43", 15, 18);
        addLane("44", 16, 15);
        addLane("45", 16, 13);
        addLane("46", 16, 18);
        addLane("47", 16, 22);
        addLane("48", 16, 17);
        addLane("49", 17, 22);
        addLane("50", 17, 13);
        addLane("51", 17, 22);
        addLane("52", 17, 16);
        addLane("53", 18, 22);
        addLane("54", 18, 15);
        addLane("55", 18, 16);
        addLane("56", 18, 20);
        addLane("57", 18, 21);
        addLane("58", 19, 22);
        addLane("59", 19, 20);
        addLane("60", 20, 19);
        addLane("61", 20, 18);
        addLane("62", 20, 21);
        addLane("63", 21, 20);
        addLane("64", 21, 18);
        addLane("65", 22, 17);
        addLane("66", 22, 16);
        addLane("67", 22, 19);
        addLane("68", 22, 18);
        addLane("69", 23, 14);
        addLane("70", 23, 24);
        addLane("71", 24, 23);
        addLane("72", 25, 2);
        addLane("73", 25, 4);
        addLane("74", 25, 6);
        addLane("75", 26, 1);
        addLane("76", 26, 9);
        addLane("77", 27, 8);
        addLane("78", 28, 9);
        addLane("79", 28, 10);
        addLane("80", 29, 11);
        addLane("81", 29, 12);
        //tambahan
        addLane("82", 30, 31);
        addLane("83", 31, 0);
        addLane("84", 0, 31);
        addLane("85", 31, 30);

        if (savedInstanceState != null){
            if (savedInstanceState.keySet().contains(ROUTE_KEY)){
                isUsingRoute = savedInstanceState.getBoolean(ROUTE_KEY);
            }
            if (savedInstanceState.keySet().contains(ID_ROUTE_LIST_KEY)){
               pointToUseInRoute = savedInstanceState.getParcelableArrayList(ID_ROUTE_LIST_KEY);
            }
        }
        buildApiClient();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    protected void buildApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createLocationRequest();
    }


    public static Intent newIntent(Context packagecontext, int id_gedung) {
        Intent intent = new Intent(packagecontext, MapsActivity.class);
        intent.putExtra(EXTRA_ID_GEDUNG, id_gedung);
        return intent;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.lokasi_saya:
                if (currentLocation != null){
                    gotoLocationZoom(currentLocation.getLatitude(), currentLocation.getLongitude(), 14);
                }else {
                    Toast.makeText(this, "Lokasi anda tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return(super.onOptionsItemSelected(item));

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
        if (map == null) {
            map = googleMap;
        }
        if (map != null) {
            map.setInfoWindowAdapter(this);
        }


        for (Gedung gedung : gedungList) {
            placeMarker(gedung.getNama(), gedung.getLatitude(), gedung.getLongitude());
        }
        northCircle = map.addCircle(new CircleOptions().center(new LatLng(-7.769193170842021, 110.38809886202216))
                .radius(98).visible(false)
        );
        midleCircle = map.addCircle(new CircleOptions().center(new LatLng(-7.769959319637975, 110.38760885596275))
                .radius(76).visible(false)
        );
        southCircle = map.addCircle(new CircleOptions().center(new LatLng(-7.7712030666905685, 110.38731381297112))
                .radius(76).visible(false)
        );
    }


    private void gotoLocationZoom(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.animateCamera(update);
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume: " );
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Disconnecting the client invalidates it.
        if (apiClient != null && apiClient.isConnected()){
            Log.e(TAG, "onPause: disconnet location update" );
            LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
        }
        super.onPause();

    }

    @Override
    protected void onStop() {
        // only stop if it's connected, otherwise we crash
        if (apiClient != null && apiClient.isConnected()) {
            apiClient.disconnect();
        }
        super.onStop();
    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "onConnected: di connect" );
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
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (currentLocation != null){
            //map.setOnInfoWindowClickListener(this);
            Log.e(TAG, "onConnected: lokasi di ulang lalala" );
            MarkerOptions options = new MarkerOptions()
                    .title("Lokasi Anda")
                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .visible(true);
            if (currentLocationMarker != null){
                currentLocationMarker.remove();
            }
            currentLocationMarker = map.addMarker(options);
            currentLocationMarker.showInfoWindow();
            boolean isInFT = checkPosition(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));

            if (isInFT) {
                STATUS_POSISI = 1;
                Log.e(TAG, "onConnected: status = " + STATUS_POSISI );
                gotoLocationZoom(currentLocation.getLatitude(),currentLocation.getLongitude(), 18);
                Toast.makeText(this, "Di dalam FT", Toast.LENGTH_SHORT).show();
            }
            else {
                STATUS_POSISI = 2;
                Log.e(TAG, "onConnected: status = " + STATUS_POSISI );
                gotoLocationZoom(currentLocation.getLatitude(),currentLocation.getLongitude(), 13);
                Toast.makeText(this, "Di luar FT", Toast.LENGTH_SHORT).show();
            }
        }else {
            changeSetting();
        }

        Intent intent = getIntent();
        PolylineOptions polylineOptions = new PolylineOptions().
                geodesic(true).
                color(Color.BLUE).
                width(10);
        if (intent.hasExtra(EXTRA_ID_GEDUNG)) {
            int id_gedung = intent.getIntExtra(EXTRA_ID_GEDUNG, 99);
            if (!isUsingRoute){
                doRoute(id_gedung);
            }else {
                for (LatLng latLng : pointToUseInRoute){
                    polylineOptions.add(latLng);
                }
                polylineList.add(map.addPolyline(polylineOptions));
            }

            Log.e(TAG, "id_gedung intent: " + intent.getIntExtra(EXTRA_ID_GEDUNG, 99));
        }else {
            if (isUsingRoute){
                for (LatLng latLng : pointToUseInRoute){
                    polylineOptions.add(latLng);
                }
                polylineList.add(map.addPolyline(polylineOptions));
            }
        }

    }
    private void createLocationRequest(){
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(300000);
        locationRequest.setFastestInterval(60000);
    }

    private void changeSetting(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.
                checkLocationSettings(apiClient,builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MapsActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        if (apiClient != null && apiClient.isConnected()) {
                            apiClient.disconnect();
                            apiClient.connect();
                        }
                        gotoLocationZoom(-7.771472121215996, 110.38705229759216, 14);

                        //Toast.makeText(MapsActivity.this, "Layanan Lokasi di aktifkan", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        gotoLocationZoom(-7.771472121215996, 110.38705229759216, 16);
                        //Toast.makeText(MapsActivity.this, "Layanan Lokasi tidak diperbolehkan oleh pengguna.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    private boolean checkPosition(LatLng location){
        double distance1 = haversine(location.latitude, location.longitude,
                northCircle.getCenter().latitude, northCircle.getCenter().longitude);
        double distance2 = haversine(location.latitude, location.longitude,
                midleCircle.getCenter().latitude, midleCircle.getCenter().longitude);
        double distance3 = haversine(location.latitude, location.longitude,
                southCircle.getCenter().latitude, southCircle.getCenter().longitude);
        Log.e(TAG, "checkPosition: distance 1 " +distance1 );
        Log.e(TAG, "checkPosition: distance 2 " +distance2 );
        Log.e(TAG, "checkPosition: distance 3 " +distance3 );
        Log.e(TAG, "checkPosition: north circle radius " + northCircle.getRadius() );
        Log.e(TAG, "checkPosition: midle circle radius " + midleCircle.getRadius() );
        Log.e(TAG, "checkPosition: south circle radius " + southCircle.getRadius() );
        Log.e(TAG, "checkPosition: north in ft " + (distance1 < northCircle.getRadius()));
        Log.e(TAG, "checkPosition: midle in ft " + (distance2 < midleCircle.getRadius()) );
        Log.e(TAG, "checkPosition: south south in ft " + (distance3 < southCircle.getRadius()));
        if( (distance1 < northCircle.getRadius()) || (distance2 < midleCircle.getRadius()) || (distance3 < southCircle.getRadius())){
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, "Can't get current location", Toast.LENGTH_LONG).show();
        } else {
            map.setOnInfoWindowClickListener(this);
            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            currentLocation = location;
            Log.e(TAG, "onLocationChanged: lattitude longitude" + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            MarkerOptions options = new MarkerOptions()
                    .title("Lokasi Anda")
                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            currentLocationMarker = map.addMarker(options);
            boolean isInFT = checkPosition(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
            if (isInFT) {
                    STATUS_POSISI = 1;
                    Log.e(TAG, "onLocationChange: status = " + STATUS_POSISI );
            }
            else {
                STATUS_POSISI = 2;
                    Log.e(TAG, "onLocationChange: status = " + STATUS_POSISI );
            }
        }

    }

    public void placeMarker(String title, double lat, double lng) {
        MarkerOptions options = new MarkerOptions()
                .title(title)
                .position(new LatLng(lat, lng))
                ;
        map.addMarker(options);
    }

    @Override
    public void DirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Harap Tunggu.",
                "Sedang mencari rute..", true);
        if (polylineList != null) {
            for (Polyline polyline : polylineList) {
                polyline.remove();
            }
        }
        pointToUseInRoute.clear();
    }

    @Override
    public void DirectionFinderFailed() {
        progressDialog.dismiss();
        Toast.makeText(this, "gagal mengambil data", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void DirectionFinderSuccess(List<Rute> rutes , Gedung destination_gedung) {
        polylineList = new ArrayList<>();
        for (Rute rute : rutes) {
            Log.e(TAG, "DirectionFinderSuccess: rute " + rute.getStartLocation());
            Log.e(TAG, "DirectionFinderSuccess: rute " + rute.getEndLocation());
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < rute.getPoint().size(); i++) {
                polylineOptions.add(rute.getPoint().get(i));
                pointToUseInRoute.add(rute.getPoint().get(i));
            }
            boolean isGedunginFT = checkPosition(new LatLng(destination_gedung.getLatitude(), destination_gedung.getLongitude()));
            if (isGedunginFT){
                LatLng current_latlng = null;
                //tes di ganti posisi gedung di sebelah mana
                if (MODE_DI_DALAM_FT){
                    if (destination_gedung.getLatitude() >= midlepoint.latitude) {
                        current_latlng = northpoint;
                    } else if (destination_gedung.getLatitude() < midlepoint.latitude) {
                        current_latlng = southpoint;
                    }
                }else {
                    current_latlng = rute.getEndLocation();
                }


                LatLng destination_latlng = new LatLng(destination_gedung.getLatitude(), destination_gedung.getLongitude());
                LinkedList<Vertex> path = getPathInFT(current_latlng, destination_latlng);
                if (path != null && path.size() != 0) {
                    for (Vertex vertex : path) {
                        polylineOptions.add(vertex.getLocation()).color(Color.BLUE).
                                width(10);
                        pointToUseInRoute.add(vertex.getLocation());
                        Log.e("mapsActivity", "Vertex: " + vertex.getId());
                    }
                }
            }
            polylineList.add(map.addPolyline(polylineOptions));
        }

        progressDialog.dismiss();
    }

    private LinkedList<Vertex> getPathInFT(LatLng current, LatLng destination){
        int jml_nodes = nodes.size();
        int id_current_location_node = jml_nodes + 1;
        int id_tujuan_location_node = jml_nodes + 2;
        int no_current_dalam_edges = id_current_location_node  - 1;
        int no_tujuan_dalam_edges = id_tujuan_location_node - 1;
        int jml_edges = edges.size();
        int id_current_edge = jml_edges + 1;
        int id_tujuan_edge = jml_edges + 2;
        Log.e(TAG, "jml_nodes " + jml_nodes );
        Log.e(TAG, "current source " +no_current_dalam_edges );
        Log.e(TAG, "tujuan source "  + no_tujuan_dalam_edges);

        Vertex node_current = new Vertex(Integer.toString(id_current_location_node ), "node " + id_current_location_node ,
                new LatLng(current.latitude, current.longitude));
        Vertex node_tujuan = new Vertex(Integer.toString(id_tujuan_location_node), "node " + id_tujuan_location_node,
                new LatLng(destination.latitude, destination.longitude));
        Vertex terdekat_dari_current = getNearestVertex(node_current);
        Vertex terdekat_dari_tujuan = getNearestVertex(node_tujuan);
        nodes.add(node_current);
        nodes.add(node_tujuan);

        if ((terdekat_dari_current != null) && (terdekat_dari_tujuan != null)){
            int current_Destination_lane = Integer.parseInt(terdekat_dari_current.getId()) - 1 ;
            int tujuan_destination_lane = Integer.parseInt(terdekat_dari_tujuan.getId()) - 1;
            addLane(Integer.toString(id_current_edge),no_current_dalam_edges,current_Destination_lane);
            addLane(Integer.toString(id_tujuan_edge), no_tujuan_dalam_edges, tujuan_destination_lane);
            addLane(Integer.toString((id_current_edge + 1)), current_Destination_lane, no_current_dalam_edges);
            addLane(Integer.toString((id_tujuan_edge + 1)), tujuan_destination_lane, no_tujuan_dalam_edges);


            Graph graph = new Graph(nodes, edges);
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
            dijkstra.execute(nodes.get(no_current_dalam_edges));
            LinkedList<Vertex> path = dijkstra.getPath(nodes.get(no_tujuan_dalam_edges));
            edges.remove((id_tujuan_edge ));
            edges.remove((id_current_edge));
            edges.remove((id_tujuan_edge - 1));
            edges.remove((id_current_edge - 1));
            nodes.remove(no_tujuan_dalam_edges);
            nodes.remove(no_current_dalam_edges);
            Log.e(TAG, "jml_nodes setelah diremove " + jml_nodes);
            Log.e(TAG, "jml_edge setelah diremove " + jml_edges);
            return path;
        }
        nodes.remove(no_tujuan_dalam_edges);
        nodes.remove(no_current_dalam_edges);
        Log.e(TAG, "jika disekitar null jml_nodes setelah diremove " + jml_nodes);
        Log.e(TAG, "jika disekitar null jml_edge setelah diremove " + jml_edges);
        return null;
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
        textView.setText(marker.getTitle());
        button.setText(R.string.btn_show_route);
        return view;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        for (Gedung gedung : gedungList){
            if ((gedung.getLatitude() == marker.getPosition().latitude) && (gedung.getLongitude() == marker.getPosition().longitude)){
                doRoute(gedung.getId_gedung());
                break;
            }
        }
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo) {
        Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo) );
        edges.add(lane);
    }

    private void doRoute(int id_gedung){
        Gedung destinationGedung = new Gedung();
        for (Gedung gedung : gedungList){
            if (gedung.getId_gedung() == id_gedung){
                destinationGedung = gedung;
                Log.e(TAG, "doRoute: destination gedung " + destinationGedung.getNama() );
                break;
            }
        }

        if (STATUS_POSISI == 2){
            String origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
            String destination = null;
            boolean isGedungInFT = checkPosition(new LatLng(destinationGedung.getLatitude(), destinationGedung.getLongitude()));
            Log.e(TAG, "destination in ft : " + isGedungInFT );
            if (isGedungInFT){
                if (destinationGedung.getLatitude() >= midlepoint.latitude){
                    destination = northpoint.latitude + "," + northpoint.longitude;
                }else if (destinationGedung.getLatitude() < midlepoint.latitude){
                    destination = southpoint.latitude + "," + southpoint.longitude;
                }
            }else {
                destination = destinationGedung.getLatitude() + "," +destinationGedung.getLongitude();
            }

            try {
                new DirectionFinder(this, origin, destination, destinationGedung).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if (STATUS_POSISI == 1){
            if (polylineList != null) {
                for (Polyline polyline : polylineList) {
                    polyline.remove();
                }
            }
            boolean isGedungInFT = checkPosition(new LatLng(destinationGedung.getLatitude(), destinationGedung.getLongitude()));
            if (isGedungInFT){

                progressDialog = ProgressDialog.show(this, "Harap Tunggu.",
                        "Sedang mencari rute..", true);
                LatLng current_latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                LatLng destination_latlng = new LatLng(destinationGedung.getLatitude(),destinationGedung.getLongitude());
                LinkedList<Vertex> path = getPathInFT(current_latlng, destination_latlng);
                PolylineOptions polylineOptions = new PolylineOptions().
                        geodesic(true).
                        color(Color.BLUE).
                        width(10);
                if (path != null && path.size() != 0) {
                    for (Vertex vertex : path) {
                        polylineOptions.add(vertex.getLocation()).color(Color.BLUE).
                                width(10);
                        Log.e("mapsActivity", "Vertex: " + vertex.getId());
                    }
                }
                polylineList.add(map.addPolyline(polylineOptions));

                progressDialog.dismiss();
            }else {
                MODE_DI_DALAM_FT = false;
                Gedung posisi_berdiri = new Gedung();
                posisi_berdiri.setLatitude(currentLocation.getLatitude());
                posisi_berdiri.setLongitude(currentLocation.getLongitude());
                String destination = null;
                    if (destinationGedung.getLatitude() >= midlepoint.latitude){
                        destination = northpoint.latitude + "," + northpoint.longitude;
                    }else if (destinationGedung.getLatitude() < midlepoint.latitude){
                        destination = southpoint.latitude + "," + southpoint.longitude;
                    }

                String  origin = destinationGedung.getLatitude() + "," +destinationGedung.getLongitude();

                try {
                    new DirectionFinder(this, origin, destination, posisi_berdiri).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        isUsingRoute = true;
    }
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (R_bumi * c) * 1000;
    }

    private Vertex getNearestVertex(Vertex location){
        if (location != null){
            List<Vertex> inLocationRadiusVertex = new ArrayList<>();
            Circle radiusLocation = map.addCircle(new CircleOptions().center(location.getLocation()).radius(50)
                    .strokeWidth(5)
                    .strokeColor(Color.GRAY));
            if (nodes.size() != 0){
                for (Vertex vertex : nodes){
                    double jarak_node_ke_lokasi = haversine(vertex.getLocation().latitude, vertex.getLocation().longitude,
                            location.getLocation().latitude, location.getLocation().longitude);
                    if (jarak_node_ke_lokasi < radiusLocation.getRadius()){
                        inLocationRadiusVertex.add(vertex);
                    }
                }
                radiusLocation.remove();

                if (inLocationRadiusVertex.size() != 0 ){
                    Vertex terdekat = null;
                    double jarak_terdekat = Double.MAX_VALUE;
                    for (Vertex vertex : inLocationRadiusVertex){
                        double jarak_node_ke_lokasi = haversine(vertex.getLocation().latitude, vertex.getLocation().longitude,
                                location.getLocation().latitude,location.getLocation().longitude);
                        if (jarak_node_ke_lokasi < jarak_terdekat){
                            terdekat = vertex;
                            jarak_terdekat = jarak_node_ke_lokasi;
                        }
                    }
                    return terdekat;
                }
            }
        }
        return null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ROUTE_KEY, isUsingRoute);
        outState.putParcelableArrayList(ID_ROUTE_LIST_KEY, pointToUseInRoute);
        super.onSaveInstanceState(outState);
    }
}

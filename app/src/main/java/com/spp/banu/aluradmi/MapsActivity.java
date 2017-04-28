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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private final static LatLng southpoint = new LatLng(-7.77186506821928,110.38687460124493);
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

        nodes.add(new Vertex("1", new LatLng(-7.76864445327121,110.38788914680481)));
        nodes.add(new Vertex("2", new LatLng(-7.768702920282386,110.38772016763687)));
        nodes.add(new Vertex("3", new LatLng(-7.768827827051751,110.38803398609161)));
        nodes.add(new Vertex("4", new LatLng(-7.768923500296765,110.38780868053436)));
        nodes.add(new Vertex("5", new LatLng(-7.768979309679639,110.38797497749329)));
        nodes.add(new Vertex("6", new LatLng(-7.769029803876789,110.38819491863251)));
        nodes.add(new Vertex("7", new LatLng(-7.769016515930754,110.38755655288696)));
        nodes.add(new Vertex("8",  new LatLng(-7.769133449841402,110.387744307518)));
        nodes.add(new Vertex("9", new LatLng(-7.769223807840961,110.3881761431694)));
        nodes.add(new Vertex("10",  new LatLng(-7.769348714455395,110.38810640573502)));
        nodes.add(new Vertex("11",  new LatLng(-7.769399208608119,110.38764506578445)));
        nodes.add(new Vertex("12", new LatLng(-7.769497539309186,110.38795620203018)));
        nodes.add(new Vertex("13", new LatLng(-7.769566636544804,110.38787841796875)));
        nodes.add(new Vertex("14", new LatLng(-7.769641048939684,110.38749754428864)));
        nodes.add(new Vertex("15",  new LatLng(-7.769654336865949,110.38732320070267)));
        nodes.add(new Vertex("16",  new LatLng(-7.769805819195691,110.3874546289444)));
        nodes.add(new Vertex("17",  new LatLng(-7.769808476779934,110.38769602775574)));
        nodes.add(new Vertex("18",  new LatLng(-7.769803161611436,110.38782745599747 )));
        nodes.add(new Vertex("19",  new LatLng(-7.769896177050572,110.38778990507126)));
        nodes.add(new Vertex("20",  new LatLng(-7.769811134364164,110.38801789283752)));
        nodes.add(new Vertex("21",  new LatLng(-7.7699519863041635,110.38796693086624)));
        nodes.add(new Vertex("22",  new LatLng(-7.770254950694269,110.38768261671066)));
        nodes.add(new Vertex("23", new LatLng(-7.77028949925117,110.38777649402618)));
        nodes.add(new Vertex("24",  new LatLng(-7.770324047805247,110.38795351982117)));
        nodes.add(new Vertex("25", new LatLng(-7.770172565662445,110.38716495037079)));
        nodes.add(new Vertex("26", new LatLng(-7.77025229311285,110.38756728172302)));
        nodes.add(new Vertex("27", new LatLng(-7.770337335709897,110.38728833198547)));
        nodes.add(new Vertex("28", new LatLng(-7.7704914753731655,110.38772016763687)));
        nodes.add(new Vertex("29", new LatLng(-7.770523366330926,110.38789719343185)));
        nodes.add(new Vertex("30", new LatLng(-7.770456926832867,110.38705229759216)));
        nodes.add(new Vertex("31",  new LatLng(-7.770523366330926,110.38738757371902)));
        nodes.add(new Vertex("32",  new LatLng(-7.770648272558783,110.38765847682953)));
        nodes.add(new Vertex("33",  new LatLng(-7.770704081712417,110.3878515958786)));
        nodes.add(new Vertex("34",  new LatLng(-7.770807727263761,110.38761287927628)));
        nodes.add(new Vertex("35", new LatLng(-7.770860878818622,110.38780331611633)));
        nodes.add(new Vertex("36",  new LatLng(-7.77086619397375,110.38719177246094)));
        nodes.add(new Vertex("37", new LatLng(-7.770969839485071,110.38756459951401)));
        nodes.add(new Vertex("38",  new LatLng(-7.771020333442865,110.3877604007721)));
        nodes.add(new Vertex("39",  new LatLng(-7.770911372789522,110.3869879245758)));
        nodes.add(new Vertex("40", new LatLng(-7.77094060613832,110.38744658231735)));
        nodes.add(new Vertex("41",  new LatLng(-7.7710548819367915,110.38713276386261)));
        nodes.add(new Vertex("42",  new LatLng(-7.771139924371299,110.3875270485878)));
        nodes.add(new Vertex("43",  new LatLng(-7.771246227390198,110.3874734044075)));
        nodes.add(new Vertex("44", new LatLng(-7.771310009188624,110.38769870996475)));
        nodes.add(new Vertex("45", new LatLng(-7.77129672131475,110.38709789514542)));
        nodes.add(new Vertex("46", new LatLng(-7.771333927360528,110.38725882768631)));
        nodes.add(new Vertex("47",  new LatLng(-7.77138973642302,110.38744121789932)));
        nodes.add(new Vertex("48", new LatLng(-7.7714535181996265,110.38765043020248)));
        nodes.add(new Vertex("49",  new LatLng(-7.771426942460554,110.38690745830536)));
        nodes.add(new Vertex("50",  new LatLng(-7.771477436363376,110.38704693317413)));
        nodes.add(new Vertex("51",  new LatLng(-7.771519957539982,110.38723737001419)));
        nodes.add(new Vertex("52",  new LatLng(-7.771546533273165,110.38688331842422)));
        nodes.add(new Vertex("53",  new LatLng(-7.771650178616539,110.38757264614105)));
        nodes.add(new Vertex("54", new LatLng(-7.771652836189102,110.38680016994476)));
        nodes.add(new Vertex("55",  new LatLng(-7.771660808906718,110.38700670003891)));
        nodes.add(new Vertex("56", new LatLng(-7.771724590642157,110.38728564977646)));
        nodes.add(new Vertex("57", new LatLng(-7.771809632940995,110.38764238357544)));
        nodes.add(new Vertex("58", new LatLng(-7.771777742080942,110.38692355155945)));
        nodes.add(new Vertex("59", new LatLng(-7.77186506821928,110.38687460124493)));


        //menghubungkan node tersebut dikurangi 1
        edges = new ArrayList<>();

        addLane("1-2", 0, 1);
        addLane("1-3",0,2);

        addLane("2-1",1,0);
        addLane("2-4",1,3);
        addLane("2-7",1,6);

        addLane("3-1",2,0);
        addLane("3-5",2,4);

        addLane("4-5",3,4);
        addLane("4-2",3,1);
        addLane("4-7",3,6);
        addLane("4-8",3,7);

        addLane("5-3",4,2);
        addLane("5-4",4,3);
        addLane("5-6",4,5);

        addLane("6-5",5,4);
        addLane("6-9",5,8);

        addLane("7-2",6,1);
        addLane("7-4",6,3);
        addLane("7-8",6,7);

        addLane("8-4",7,3);
        addLane("8-7",7,6);
        addLane("8-11",7,10);

        addLane("9-6",8,5);
        addLane("9-10",8,9);

        addLane("10-9",9,8);
        addLane("10-12",9,11);

        addLane("11-8",10,7);
        addLane("11-13", 10,12);

        addLane("12-10",11,9);
        addLane("12-13",11,12);

        addLane("13-12",12,11);
        addLane("13-11",12,10);
        addLane("13-18",12,17);

        addLane("14-15",13,14);
        addLane("14-16",13,15);
        addLane("14-17",13,16);

        addLane("15-14",14,13);
        addLane("15-16",14,15);

        addLane("16-14",15,13);
        addLane("16-15",15,14);
        addLane("16-17",15,16);

        addLane("17-14",16,13);
        addLane("17-16",16,15);
        addLane("17-19", 16,18);

        addLane("18-13",17,12);
        addLane("18-19",17,18);
        addLane("18-20",17,19);

        addLane("19-17",18,16);
        addLane("19-18",18,17);
        addLane("19-21",18,20);
        addLane("19-22",18,21);

        addLane("20-18",19,17);

        addLane("21-19",20,18);

        addLane("22-19",21,18);
        addLane("22-23",21,22);
        addLane("22-26",21,25);

        addLane("23-22",22,21);
        addLane("23-24",22,23);
        addLane("23-28",22,27);

        addLane("24-23",23,22);

        addLane("25-26",24,25);
        addLane("25-27",24,26);
        addLane("25-30",24,29);

        addLane("26-22",25,21);
        addLane("26-25",25,24);
        addLane("26-27",25,26);
        addLane("26-31",26,30);

        addLane("27-25",26,24);
        addLane("27-26",26,25);
        addLane("27-30",26,29);
        addLane("27-31",26,30);

        addLane("28-23",27,22);
        addLane("28-29",27,28);
        addLane("28-32",27,31);

        addLane("29-28",28,27);

        addLane("30-25",29,24);
        addLane("30-27",29,26);
        addLane("30-31",29,30);

        addLane("31-26",30,25);
        addLane("31-27",30,26);
        addLane("31-30",30,29);

        addLane("32-28",31,27);
        addLane("32-33",31,32);
        addLane("32-34",31,33);

        addLane("33-32",32,31);

        addLane("34-32",33,31);
        addLane("34-35",33,34);
        addLane("34-37",33,36);

        addLane("35-34",34,33);

        addLane("36-39",35,38);
        addLane("36-40",35,39);
        addLane("36-41",35,40);

        addLane("37-34",36,33);
        addLane("37-38",36,37);
        addLane("37-40",36,39);
        addLane("37-42",36,41);

        addLane("38-37",37,36);

        addLane("39-36",38,35);
        addLane("39-41",38,40);

        addLane("40-36",39,35);
        addLane("40-37",39,36);
        addLane("40-41",39,40);

        addLane("41-36",40,35);
        addLane("41-39",40,38);
        addLane("41-40",40,39);

        addLane("42-37",41,36);
        addLane("42-43",41,42);

        addLane("43-42",42,41);
        addLane("43-47",42,46);
        addLane("43-44",42,43);

        addLane("44-43",43,42);
        addLane("44-48",43,47);

        addLane("45-46",44,45);
        addLane("45-50",44,49);
        addLane("45-51",44,50);

        addLane("46-45",45,44);
        addLane("46-47",45,46);
        addLane("46-51",45,50);

        addLane("47-46",46,45);
        addLane("47-51",46,50);
        addLane("47-43",46,42);
        addLane("47-48",46,47);

        addLane("48-44",47,43);
        addLane("48-47",47,46);
        addLane("48-53",47,52);

        addLane("49-50",48,49);
        addLane("49-52",48,51);

        addLane("50-49",49,48);
        addLane("50-51",49,50);
        addLane("50-45",49,44);
        addLane("50-55",49,54);

        addLane("51-45",50,44);
        addLane("51-46",50,45);
        addLane("51-47",50,46);
        addLane("51-50",50,49);
        addLane("51-55",50,54);
        addLane("51-56",50,55);

        addLane("52-49",51,48);
        addLane("52-54",51,53);

        addLane("53-48",52,47);
        addLane("53-57",52,56);

        addLane("54-52",53,51);
        addLane("54-58",52,57);
        addLane("54-55",52,54);

        addLane("55-50",54,49);
        addLane("55-51",54,50);
        addLane("55-54",54,53);
        addLane("55-56",54,55);
        addLane("55-58",54,57);

        addLane("56-51",55,50);
        addLane("56-55",55,54);

        addLane("57-",56,52);

        addLane("58-54",57,53);
        addLane("58-55",57,54);
        addLane("58-59",57,58);

        addLane("59-58",58,57);
        //setelah ini ditambah 1 lagi karena ada A dan B di 41


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
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    map.animateCamera(update);
                    currentLocationMarker.showInfoWindow();
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
            map.setOnInfoWindowClickListener(this);
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
        LatLng latLng = new LatLng(-7.771472121215996, 110.38705229759216);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        map.moveCamera(update);
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
            MarkerOptions options = new MarkerOptions()
                    .title("Lokasi Anda")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_biru_kecil))
                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                    .visible(true);
            if (currentLocationMarker == null){
                currentLocationMarker = map.addMarker(options);
            }
            boolean isInFT = checkPosition(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()));
            if (isInFT) {
                STATUS_POSISI = 1;
                Log.e(TAG, "onConnected: status = " + STATUS_POSISI );
            }
            else {
                STATUS_POSISI = 2;
                Log.e(TAG, "onConnected: status = " + STATUS_POSISI );
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
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(120000);
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

                        //Toast.makeText(MapsActivity.this, "Layanan Lokasi di aktifkan", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
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

            if (currentLocationMarker != null) {
                currentLocationMarker.remove();
            }
            currentLocation = location;
            Log.e(TAG, "onLocationChanged: lattitude longitude" + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
            MarkerOptions options = new MarkerOptions()
                    .title("Lokasi Anda")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_biru_kecil))
                    .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            currentLocationMarker = map.addMarker(options);
            currentLocationMarker.showInfoWindow();
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

        Vertex node_current = new Vertex(Integer.toString(id_current_location_node ),
                new LatLng(current.latitude, current.longitude));
        Vertex node_tujuan = new Vertex(Integer.toString(id_tujuan_location_node),
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
        if (marker.equals(currentLocationMarker)){
            button.setVisibility(View.GONE);
        }else {
            button.setVisibility(View.VISIBLE);
            button.setText(R.string.btn_show_route);
        }

        return view;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        if (currentLocation != null){
            for (Gedung gedung : gedungList){
                if ((gedung.getLatitude() == marker.getPosition().latitude) && (gedung.getLongitude() == marker.getPosition().longitude)){
                    doRoute(gedung.getId_gedung());
                    break;
                }
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

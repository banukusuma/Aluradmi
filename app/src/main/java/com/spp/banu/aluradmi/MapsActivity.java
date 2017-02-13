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
import com.spp.banu.aluradmi.model.Graph;
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
        nodes = new ArrayList<>();

        //menggambar titik persimpangan node
        nodes.add(new Vertex("1", "node 1", new LatLng(-7.769449329031687,110.38801185786724 )));
        nodes.add(new Vertex("2", "node 2", new LatLng(-7.7695613213732395,110.3878864645958 )));
        nodes.add(new Vertex("3", "node 3", new LatLng(-7.769425784475547,110.38769602775574 )));
        nodes.add(new Vertex("4", "node 4", new LatLng(-7.769096243600631,110.38781940937042 )));
        nodes.add(new Vertex("5", "node 5", new LatLng(-7.769223807840961,110.38768798112869 )));
        nodes.add(new Vertex("6", "node 6", new LatLng(-7.76904574941147,110.38764238357544 )));
        nodes.add(new Vertex("7", "node 7", new LatLng(-7.769322138583081,110.387382209301 )));
        nodes.add(new Vertex("8", "node 8", new LatLng(-7.768671029186398,110.38774967193604 )));
        nodes.add(new Vertex("9", "node 9", new LatLng(-7.769641048939684,110.38733124732971 )));
        nodes.add(new Vertex("10", "node 10", new LatLng(-7.769901492217901,110.38780063390732 )));
        nodes.add(new Vertex("11", "node 11", new LatLng(-7.7702602658570425,110.38768798112869 )));
        nodes.add(new Vertex("12", "node 12", new LatLng(-7.770297471994675,110.3877604007721 )));
        nodes.add(new Vertex("13", "node 13", new LatLng(-7.770980469792488,110.38755655288696 )));
        nodes.add(new Vertex("14", "node 14", new LatLng(-7.77137910612597,110.38744390010834 )));
        nodes.add(new Vertex("15", "node 15", new LatLng(-7.7714535181996265,110.38761287927628 )));
        nodes.add(new Vertex("16", "node 16", new LatLng(-7.771724590642157,110.3872936964035 )));
        nodes.add(new Vertex("17", "node 17", new LatLng(-7.77154919084639,110.38722395896912 )));
        nodes.add(new Vertex("18", "node 18", new LatLng(-7.771302036464343,110.38710862398148 )));
        nodes.add(new Vertex("19", "node 19", new LatLng(-7.771674096769067,110.38699865341187 )));
        nodes.add(new Vertex("20", "node 20", new LatLng(-7.771429600034542,110.38692891597748 )));
        nodes.add(new Vertex("21", "node 21", new LatLng(-7.771655493761666,110.38685113191605 )));
        nodes.add(new Vertex("22", "node 22", new LatLng(-7.77185746922651,110.38686722517014 )));
        nodes.add(new Vertex("23", "node 23", new LatLng(-7.771472121215996,110.38705229759216 )));
        nodes.add(new Vertex("24", "node 24", new LatLng(-7.771631575608071,110.38757801055908 )));
        nodes.add(new Vertex("25", "node 25", new LatLng(-7.771820263227128,110.38767457008362 )));
        nodes.add(new Vertex("26", "node 26", new LatLng(-7.769385920673774,110.38762360811234 )));
        nodes.add(new Vertex("27", "node 27", new LatLng(-7.769731406830022,110.38785696029663 )));
        nodes.add(new Vertex("28", "node 28", new LatLng(-7.769789873689877,110.3874546289444 )));
        nodes.add(new Vertex("29", "node 29", new LatLng(-7.770063604789108,110.38774967193604 )));
        nodes.add(new Vertex("30", "node 30", new LatLng(-7.770632327084954,110.38766384124756 )));

        //menghubungkan node tersebut dikurangi 1
        edges = new ArrayList<>();
        addLane("1",0,1 );
        addLane("2",1, 0);
        addLane("3", 1, 2);
        addLane("4", 1, 26);
        addLane("5", 2, 3);
        addLane("6", 2, 1);
        addLane("7", 2, 4);
        addLane("8", 2, 25);
        addLane("9", 3, 2);
        addLane("10",3 ,4 );
        addLane("11",3 ,5 );
        addLane("12",4 ,2 );
        addLane("13",4 ,3 );
        addLane("14",4, 5);
        addLane("15",4 , 25);
        addLane("16", 4, 6);
        addLane("17",5 ,4 );
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
        addLane("44", 16, 15 );
        addLane("45", 16, 13 );
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
        addLane("60", 20, 19 );
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
        gotoLocation(-7.769901492217901,110.38780063390732 );
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(5));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(18));
        PolylineOptions polylineOptions = new PolylineOptions();
        for (Vertex vertex : path){
            polylineOptions.add(vertex.getLocation()).color(Color.BLUE).
                    width(5);
            Log.e("mapsActivity", "Vertex: " + vertex.getId());
        }
        map.addPolyline(polylineOptions);
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

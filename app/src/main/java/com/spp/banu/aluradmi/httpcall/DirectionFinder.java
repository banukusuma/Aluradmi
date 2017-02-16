package com.spp.banu.aluradmi.httpcall;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.spp.banu.aluradmi.DirectionFinderListener;

import com.spp.banu.aluradmi.model.Gedung;
import com.spp.banu.aluradmi.model.Rute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by banu on 30/12/16.
 */

public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String API_KEY = "AIzaSyDWem_5YPZUTxDjgxlGXg7tLDrZG6Xfu7o";
    private final static String TAG = "DirectionFinder";
    private DirectionFinderListener listener;
    private Gedung gedungTujuan;
    private String origin;
    private String destination;

    public DirectionFinder(DirectionFinderListener listener, String origin,String destination, Gedung lokasi_tujuan) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
        gedungTujuan = lokasi_tujuan;
    }
    public void execute()  throws UnsupportedEncodingException{
        listener.DirectionFinderStart();
        new ParseRute().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException{
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");

        return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + API_KEY;
    }

    private class ParseRute extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String link = strings[0];
            HttpHandler httpHandler = new HttpHandler();
            String jsonString = httpHandler.makeServiceCall(link);
            Log.e(TAG, "doInBackground: " + jsonString );
            if (jsonString != null){
                return jsonString;
            }
            Log.i(TAG, "tidak dapat mengambil data rute");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s != null){
                    parseJson(s);
                } else {
                    listener.DirectionFinderFailed();
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void parseJson(String s) throws JSONException{
        if (s == null){
            return;
        }
        List<Rute> ruteList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(s);
        JSONArray jsonRutes = jsonObject.getJSONArray("routes");
        for (int i = 0; i < jsonRutes.length();i++){
            JSONObject jsonRute = jsonRutes.getJSONObject(i);
            Rute rute = new Rute();

            JSONObject overview_polylineJson = jsonRute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
            rute.setStartAddress(jsonLeg.getString("start_address"));
            rute.setEndAddress(jsonLeg.getString("end_address"));
            rute.setStartLocation(new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng")));
            rute.setEndLocation(new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng")));
            rute.setPoint(decodePolyLine(overview_polylineJson.getString("points")));
            ruteList.add(rute);
        }
        listener.DirectionFinderSuccess(ruteList, gedungTujuan);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}

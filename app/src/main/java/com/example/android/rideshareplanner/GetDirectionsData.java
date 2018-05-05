package com.example.android.rideshareplanner;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.esri.core.geometry.Point;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @auth Priyanka
 */

public class GetDirectionsData extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    static String duration, distance;
    LatLng latLng;
    DatabaseReference databaseReference;
    static String polyline;
    Context context;
    static ArrayList<String> allcities;


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        context = (Context) objects[2];
        //getPolyline();



        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {

        String[] directionsList;
        DataParser parser = new DataParser(context);

        HashMap<String,String> distanceAndDuration = parser.getDuration(getJsonArray(s));
        distance = distanceAndDuration.get("distance");
        duration = distanceAndDuration.get("duration");

        directionsList = parser.parseDirections(s);
        allcities = parser.getAllCities(s);

//        for (int i=0;i<directionsList.length;i++){
//            Log.e("GetDirections", "directionsList:- "+ directionsList[i]);
//        }




        polyline = parser.getPolyline(s);



//        displayDirection(directionsList);


    }

    public JSONArray getJsonArray(String s){
        JSONArray jsonArray = null;
        JSONArray jsonArray1 = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(s);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    public void displayDirection(String[] directionsList)
    {

        int count = directionsList.length;
        for(int i = 0;i<count;i++)
        {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(15);
            options.addAll(PolyUtil.decode(directionsList[i]));
            Log.e("GetDirections", "displayDirection: options "+ PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }

//        PolylineOptions options2 = new PolylineOptions();
//        options2.addAll(PolyUtil.decode(polyline2));
//        options2.color(Color.BLUE);
//        options2.width(8);
//        mMap.addPolyline(options2);

        MarkerOptions startmarker = new MarkerOptions();
        startmarker.title("Starting Postion");
        LatLng star_latLng = new LatLng(ShareFragment.start_latitude,ShareFragment.start_longitude);
        startmarker.position(star_latLng);
        mMap.addMarker(startmarker);
        MarkerOptions endmarker = new MarkerOptions();
        endmarker.title("Ending Postion");
        LatLng end_latLng = new LatLng(ShareFragment.dest_latitude,ShareFragment.dest_longitude);
        endmarker.position(end_latLng);
        mMap.addMarker(endmarker);
    }






}
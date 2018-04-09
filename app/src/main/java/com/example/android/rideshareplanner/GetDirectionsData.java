package com.example.android.rideshareplanner;

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
    String duration, distance;
    LatLng latLng;
    DatabaseReference databaseReference;
    static String polyline2;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        getPolyline();



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
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);
        parser.getPolylineArray(s);

        for (int i=0;i<directionsList.length;i++){
            Log.e("GetDirections", "directionsList:- "+ directionsList[i]);
        }



        displayDirection(directionsList);


    }

    public void getPolyline(){
        databaseReference = FirebaseDatabase.getInstance().getReference("My Rides").child("Path");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    String string = new String();

                    string = i.getValue(String.class);
                    polyline2 = string.toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
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

        PolylineOptions options2 = new PolylineOptions();
        options2.addAll(PolyUtil.decode(polyline2));
        options2.color(Color.BLUE);
        options2.width(8);
        mMap.addPolyline(options2);

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
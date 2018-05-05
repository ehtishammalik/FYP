package com.example.android.rideshareplanner;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.OperatorContains;
import com.esri.core.geometry.OperatorOverlaps;
import com.esri.core.geometry.OperatorWithin;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.google.maps.android.PolyUtil.isLocationOnPath;

/**
 * Created by hamza on 3/18/18.
 */

public class DataParser {
    DatabaseReference databaseReference;
    MyRidesModel myRidesModel;
    String polyline2 = null;
    static String polyline = null;
    ArrayList<Point> points2;
    MultiPath multiPath1;
    MultiPath multiPath;
    SpatialReference sr = SpatialReference.create(4326);
    Context context;

    public DataParser(Context context){
        this.context = context;
    }


    public String[] parseDirections(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONArray jsonArray1 = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPaths(jsonArray);
    }
    public String[] getWayPoints(JSONArray path){
        String waypoint = "";
        int count = path.length();
        String[] waypoints = new String[count];
        try {
            waypoint = path.getJSONObject(0).getString("place_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i=0;i<count;i++){
            try {
                waypoints[i]= path.getJSONObject(i).getString("place_id");
                Log.e("DataParser", "getWayPoints: place_id: "+waypoints[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return waypoints;
    }

    public String[] getPaths(JSONArray googleStepsJson )
    {
        int count = googleStepsJson.length();
        Log.e("Dataparser", "getPaths: count " + count);
        String[] polylines = new String[count];

        for(int i = 0;i<count;i++)
        {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
                Log.e("DataParser", "getPaths: polylines" + polylines[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }

    public String getPath(JSONObject googlePathJson)
    {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }


    public HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
    {
        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration = "";
        String distance ="";


        try {

            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration" , duration);
            googleDirectionsMap.put("distance", distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return googleDirectionsMap;
    }

    public ArrayList<String> getAllCities(String jsonData){
        JSONObject jsonObject;


        try {
            jsonObject = new JSONObject(jsonData);
            polyline = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> allcities = new ArrayList<String>();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        LinkedHashSet<String> cities=new LinkedHashSet<String>();
                    List<LatLng> poly = new ArrayList<>();
                    poly.addAll(PolyUtil.decode(polyline));
                    for (int x=0; x<poly.size();x++){
                        LatLng latLng = poly.get(x);
                        try {
                            List<Address>addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                            if (geocoder.isPresent()) {
                                if (addresses.size()>0) {
                                    Address returnAddress = addresses.get(0);
                                    String localityString = returnAddress.getLocality();
                                    if (localityString != null){
                                        cities.add(localityString);
                                    }

                                }
                            } else {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
        allcities.addAll(cities);

        return allcities;


    }


    public String getPolyline(String jsonData){

        JSONArray jsonArray = null;
        JSONArray jsonArray1 = null;
        JSONObject jsonObject;


        try {
            jsonObject = new JSONObject(jsonData);
            polyline = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }









    static boolean geometryContains(Geometry geometryA, Geometry geometryB, SpatialReference sr)
    {
        boolean contains = OperatorContains.local().execute(geometryA, geometryB, sr, null);
        return contains;
    }
    static boolean geometryOverlaps(Geometry geometryA, Geometry geometryB, SpatialReference sr)
    {
        boolean contains = OperatorOverlaps.local().execute(geometryA, geometryB, sr, null);
        return contains;
    }
    static boolean geometryWithin(Geometry geometryA, Geometry geometryB, SpatialReference sr)
    {
        boolean contains = OperatorWithin.local().execute(geometryA, geometryB, sr, null);
        return contains;
    }

}

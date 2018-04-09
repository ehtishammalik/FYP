package com.example.android.rideshareplanner;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
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

    public MultiPath getPolylineArray(String jsonData){

        JSONArray jsonArray = null;
        JSONArray jsonArray1 = null;
        JSONObject jsonObject;


        try {
            jsonObject = new JSONObject(jsonData);
            polyline = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        myRidesModel = new MyRidesModel();

         points2 = new ArrayList<Point>();

        polyline2 = GetDirectionsData.polyline2;
        Log.e("DataParser", "getPolylineArray: polyline1" + polyline2);

        PolylineOptions options2 = new PolylineOptions();
        options2.addAll(PolyUtil.decode(polyline2));

        for (int x=0; x<options2.getPoints().size();x++){
            LatLng latLng = options2.getPoints().get(x);
            Log.e("DataParser", "getPolylineArray: database LatLan" + latLng);
            Point point = new Point(latLng.latitude,latLng.longitude);
            points2.add(point);

        }


        multiPath1 = new Polyline();
        multiPath1.startPath(points2.get(0));
//        Log.e("DataParser", "getPolylineArray: startPoint" + points.get(0));
        for (int y =1;y<points2.size();y++){
            multiPath1.lineTo(points2.get(y));
//            Log.e("DataParser", "getPolylineArray: Point: " +y +"  = " + points.get(y));
        }



        Log.e("DataParser", "getPolylineArray: polyline" + polyline);
        PolylineOptions options = new PolylineOptions();
                    options.addAll(PolyUtil.decode(polyline));
                    ArrayList<Point> points = new ArrayList<Point>();
                    for (int x=0; x<options.getPoints().size();x++){
                        LatLng latLng = options.getPoints().get(x);
                        Log.e("DataParser", "getPolylineArray: current LatLan" + latLng);
                        Point point = new Point(latLng.latitude,latLng.longitude);
                        points.add(point);

                    }




                    multiPath = new Polyline();
                    multiPath.startPath(points.get(0));
//        Log.e("DataParser", "getPolylineArray: startPoint" + points.get(0));
                    for (int y =1;y<points.size();y++){
                        multiPath.lineTo(points.get(y));
//            Log.e("DataParser", "getPolylineArray: Point: " +y +"  = " + points.get(y));
                    }


                    boolean oneContainTwo = geometryContains(multiPath,multiPath1,sr);
                    boolean twoContainOne = geometryContains(multiPath1,multiPath,sr);
                    boolean oneWithinTwo = geometryWithin(multiPath,multiPath1,sr);
                    boolean twoWithinOne = geometryWithin(multiPath1,multiPath,sr);
                    boolean oneOverLapsTwo = geometryOverlaps(multiPath,multiPath1,sr);
                    boolean twoOverlapsOne = geometryOverlaps(multiPath1,multiPath,sr);


                    Log.e("DataParser", "getPolylineArray: 1contain2 " +oneContainTwo);
                    Log.e("DataParser", "getPolylineArray: 2contain1 " +twoContainOne);
                    Log.e("DataParser", "getPolylineArray: 1within2 " +oneWithinTwo);
                    Log.e("DataParser", "getPolylineArray: 2within1 " +twoWithinOne);
                    Log.e("DataParser", "getPolylineArray: 1overlaps2 " +oneOverLapsTwo);
                    Log.e("DataParser", "getPolylineArray: 1overlaps2 " +twoOverlapsOne);






        return multiPath;


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

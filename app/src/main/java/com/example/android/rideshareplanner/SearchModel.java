package com.example.android.rideshareplanner;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamza on 4/20/18.
 */

public class SearchModel extends AsyncTask<Void,String,List<MyRidesDataModel>> {
    DatabaseReference databaseReference;
    String polyline2;
    List<String> StringPath = new ArrayList<>();
    List<MyRidesDataModel> myRides;
    static List<MyRidesDataModel> matchedRides;


    @Override
    protected void onPostExecute(List<MyRidesDataModel> s) {
//        search(s);
        matchedRides = s;
    }

    @Override
    protected List<MyRidesDataModel> doInBackground(Void... params) {
        String polyline = getPolyline();
        getMyRides();

        Log.e("SearchModel", "doInBackground: " +polyline);
        return getMyRides();
    }

    public List<MyRidesDataModel> getMyRides(){
        databaseReference = FirebaseDatabase.getInstance().getReference("My Rides");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    MyRidesDataModel myRidesDataModel = new MyRidesDataModel();
                    myRidesDataModel = i.getValue(MyRidesDataModel.class);

                    List<LatLng> poly = new ArrayList<>();
                    poly.addAll(PolyUtil.decode(myRidesDataModel.getPolyline()));
                    LatLng startPoint = new LatLng(Double.parseDouble(SearchFragment.start_latitude),Double.parseDouble(SearchFragment.start_longitude));
                    LatLng destPoint = new LatLng(Double.parseDouble(SearchFragment.dest_latitude),Double.parseDouble(SearchFragment.dest_longitude));

                    if (searchPoint(startPoint,poly) && searchPoint(destPoint, poly)){
                        myRides.add(myRidesDataModel);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        Log.e("SearchModel", "getStringPath: " +polyline2 );
        return myRides;
    }


    public boolean search(String s){

        boolean containStart = false;
        boolean containDest = false;

        LatLng startPoint = new LatLng(Double.parseDouble(SearchFragment.start_latitude),Double.parseDouble(SearchFragment.start_longitude));
        LatLng destPoint = new LatLng(Double.parseDouble(SearchFragment.dest_latitude),Double.parseDouble(SearchFragment.dest_longitude));

//            if (searchPoint(startPoint)){
//                containStart = true;
//            }
//
//            if (searchPoint(destPoint)){
//                containDest = true;
//            }

        if (containDest == true && containStart == true){
            return true;
        }





        return false;
    }


    public String getPolyline(){
        String poly = null;
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

        return poly;
    }


    public boolean searchPoint(LatLng pointx, List<LatLng> polyline){

        List<LatLng> poly = new ArrayList<>();


        PolylineOptions options2 = new PolylineOptions();

        options2.addAll(PolyUtil.decode(polyline2));

        for (int x=0; x<options2.getPoints().size();x++){
            LatLng latLng = options2.getPoints().get(x);
            poly.add(latLng);
        }
        poly = polyline;


        Log.i("SearchModel", "searchPoint: " + PolyUtil.isLocationOnPath(pointx,poly,true,5000));

        return PolyUtil.isLocationOnPath(pointx,poly,true,5000);
    }
}

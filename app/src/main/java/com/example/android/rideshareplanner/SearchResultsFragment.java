package com.example.android.rideshareplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SearchResultsFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    RecyclerView mRecyclerView;
    TextView nodata;
    DatabaseReference databaseReference;
    String polyline2;
    List<MyRidesDataModel> myRides;
    List<String> cities;
    GoogleMap mMap;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results_fragment, container, false);

        nodata = (TextView) view.findViewById(R.id.sr_noMatches_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.sr_rview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.i("TIME", new Date() + "");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        getDirections();






//
//        List<MyRidesDataModel> matchedRides = SearchModel.matchedRides;
//        RecyclerView.Adapter mAdapter = new IProvidedAdapter(getContext(),matchedRides);
//        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    public boolean searchPoint(LatLng pointx, List<LatLng> polyline) {

        List<LatLng> poly = polyline;


        Log.i("SearchModel", "searchPoint: " + PolyUtil.isLocationOnPath(pointx, poly, true, 5000));

        return PolyUtil.isLocationOnPath(pointx, poly, true, 5000);
    }

    public boolean checkDirection(List<String> obtainedcities){
        int firstIndex = 0 , secondIndex = 0, thirdIndex = 0;
        Log.e("SearchResultFragment", "checkDirection: cities:   " + cities);

        for (int i=0;i<obtainedcities.size();i++){
            if (cities.get(0) != null && cities.get(0).equals(obtainedcities.get(i))){
                firstIndex = i;
            }
            if (cities.get(1) != null && cities.get(1).equals(obtainedcities.get(i))){
                secondIndex = i;
            }
            if (cities.get(2) != null && cities.get(2).equals(obtainedcities.get(i))){
                thirdIndex = i;
                break;
            }





//            for (int j=0;j<cities.size();j++){
//                if (cities.get(j).equals(obtainedcities.get(i))){
//                    if (!cities.get(j+1).equals(null) || !obtainedcities.get(i+1).equals(null)){
//
//                    }
//                    if (!cities.get(j+1).equals(null) && !obtainedcities.get(i+1).equals(null) && cities.get(j+1).equals(obtainedcities.get(i+1))){
//                        if (!cities.get(j+2).equals(null) && !obtainedcities.get(i+2).equals(null) &&cities.get(j+2).equals(obtainedcities.get(i+3))){
//                            return true;
//                        }
//                    }
//                    else if (!cities.get(j+2).equals(null) && !obtainedcities.get(i+2).equals(null) &&cities.get(j+2).equals(obtainedcities.get(i+2))){
//                        if (!cities.get(j+3).equals(null) && !obtainedcities.get(i+3).equals(null) &&cities.get(j+3).equals(obtainedcities.get(i+3))){
//                            return true;
//                        }
//                    }
//                }
//            }
        }
        if ((firstIndex < secondIndex) || (firstIndex < thirdIndex)){
            return true;
        }

        return false;
    }

    public void getDirections() {
        Object dataTransfer[] = new Object[3];
        String url = getDirectionsUrl();
        GetDirections getDirections = new GetDirections();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = getContext();
        getDirections.execute(dataTransfer);

    }


    public class GetDirections extends AsyncTask<Object, String, String> {

        GoogleMap mMap;
        String url;
        String googleDirectionsData;
        Context context;


        @Override
        protected String doInBackground(Object... objects) {
            mMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            context = (Context) objects[2];
            //getPolyline();


            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googleDirectionsData = downloadUrl.readUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("TIME IN DOINBACK", new Date() + "");

            return googleDirectionsData;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("TIME IN PostEXECUTE", new Date() + "");

            DataParser parser = new DataParser(context);
            cities = parser.getAllCities(s);
            getSetAndCheckData();

        }
    }

    private String getDirectionsUrl() {
        Log.e("getDirectionsUrl", "latitude: " + SearchFragment.start_longitude);
        Log.e("getDirectionsUrl", "latitude: " + SearchFragment.start_latitude);
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + SearchFragment.start_latitude + "," + SearchFragment.start_longitude);
        googleDirectionsUrl.append("&destination=" + SearchFragment.dest_latitude + "," + SearchFragment.dest_longitude);
        googleDirectionsUrl.append("&key=" + "AIzaSyD5dedqklHHrJvgU9-qEYQmq_YjKHtziqY");

        return googleDirectionsUrl.toString();
    }

    public void getSetAndCheckData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("My Rides");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRides = new ArrayList<>();

                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    i.toString();
                    Log.i("TIME", "value of I;    " + i.toString());



                    MyRidesDataModel myRidesDataModel  = i.getValue(MyRidesDataModel.class);

                    List<LatLng> poly = new ArrayList<>();
                    poly.addAll(PolyUtil.decode(myRidesDataModel.getPolyline()));
                    LatLng startPoint = new LatLng(Double.parseDouble(SearchFragment.start_latitude), Double.parseDouble(SearchFragment.start_longitude));
                    LatLng destPoint = new LatLng(Double.parseDouble(SearchFragment.dest_latitude), Double.parseDouble(SearchFragment.dest_longitude));

                    Log.i("TIME", new Date() + "");
                    if (searchPoint(startPoint, poly) && searchPoint(destPoint, poly)) {
                        Log.i("TIME", new Date() + "");
                        if (checkDirection(myRidesDataModel.getCities())){
                            Log.i("TIME", new Date() + "");
                            myRides.add(myRidesDataModel);
                        }
                    }


                }
                progressDialog.dismiss();
                if (myRides.size()!=0){
                    Log.e("SearchResultFragment", "onDataChange: " + "Rides Found");
                    RecyclerView.Adapter mAdapter = new IProvidedAdapter(getContext(), myRides);
                    mRecyclerView.setAdapter(mAdapter);
                }
                else {
                    Log.e("SearchResultFragment", "onDataChange: " + "Rides Not Found");
                    nodata.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
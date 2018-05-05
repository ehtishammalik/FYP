package com.example.android.rideshareplanner;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        Button show = (Button) findViewById(R.id.show_directions);
//
//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Object dataTransfer[] = new Object[3];
//                dataTransfer = new Object[3];
//                String url = getDirectionsUrl();
//                Log.e("getDirectionsUrl", "url: " +url);
//                GetDirectionsData getDirectionsData = new GetDirectionsData();
//                dataTransfer[0] = mMap;
//                dataTransfer[1] = url;
//                getDirectionsData.execute(dataTransfer);
//            }
//        });






        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


//        Double start_Latitude = Double.parseDouble(SearchFragment.start_latitude);
//        Double start_Longitude = Double.parseDouble(SearchFragment.start_longitude);
//        Double end_Longitude = Double.parseDouble(SearchFragment.dest_longitude);
//        Double end_Latitude = Double.parseDouble(SearchFragment.dest_latitude);
//        LatLng startLocation = new LatLng(start_Latitude,start_Longitude);

        Object dataTransfer[] = new Object[3];
        dataTransfer = new Object[3];
        String url = getDirectionsUrl();
        Log.e("getDirectionsUrl", "url: " +url);
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = getApplicationContext();
        getDirectionsData.execute(dataTransfer);



        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng startLocation = new LatLng(ShareFragment.start_latitude,ShareFragment.start_longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 10));
    }



    private String getDirectionsUrl(){
        Log.e("getDirectionsUrl", "latitude: " +ShareFragment.start_longitude);
        Log.e("getDirectionsUrl", "latitude: " +ShareFragment.start_latitude);
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+ShareFragment.start_latitude+","+ShareFragment.start_longitude);
        googleDirectionsUrl.append("&destination="+ShareFragment.dest_latitude+","+ShareFragment.dest_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyD5dedqklHHrJvgU9-qEYQmq_YjKHtziqY");

        return googleDirectionsUrl.toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void showDirect(){
        Double start_Latitude = Double.parseDouble(SearchFragment.start_latitude);
        Double start_Longitude = Double.parseDouble(SearchFragment.start_longitude);
        Double end_Longitude = Double.parseDouble(SearchFragment.dest_longitude);
        Double end_Latitude = Double.parseDouble(SearchFragment.dest_latitude);
        LatLng startLocation = new LatLng(start_Latitude,start_Longitude);
        mMap.addMarker(new MarkerOptions().position(startLocation).title("Source"));

        LatLng endLocation = new LatLng(end_Latitude,end_Longitude);
        mMap.addMarker(new MarkerOptions().position(endLocation).title("Destination"));


        LatLng zaragoza = new LatLng(41.648823,-0.889085);

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("YOUR_API_KEY")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, SearchFragment.start_latitude+","+SearchFragment.start_longitude, SearchFragment.dest_latitude+","+SearchFragment.dest_longitude);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("", ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 26));
    }
}


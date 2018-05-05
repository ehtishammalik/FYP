package com.example.android.rideshareplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.andanhm.quantitypicker.QuantityPicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreateRideDetails extends AppCompatActivity implements OnMapReadyCallback {
    public android.support.v7.widget.Toolbar toolbar;
    private String fair,seats,baggageType;
    boolean instantBookingAllowed = true, allowSmoking = true, allowAnimals = true;
    String distance, duration, polyline;
    GoogleMap mMap;
    ArrayList<String> cities;
    static MyRidesDataModel myRidesDataModel;
    DatabaseReference databaseReference;
    CheckBox animalsAllowed, smokingAllowed;
    RadioGroup radioBookingGroup;
    Spinner spinner;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride_details);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.cr_toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        radioBookingGroup = (RadioGroup) findViewById(R.id.cr_booking_radio_btn_group);



        CardView mapCarView = (CardView) findViewById(R.id.cr_map_card_view);
        mapCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


        spinner = (Spinner) findViewById(R.id.cr_drop_down_spinner);





        QuantityPicker fairPicker = (QuantityPicker) findViewById(R.id.cr_fair_picker);

        if (fairPicker.getQuantity()!=0){
            fair = Integer.toString(fairPicker.getQuantity());
        }

        fairPicker.setMinQuantity(2);

        fairPicker.setMaxQuantity(10);

        fairPicker.setQuantityPicker(true);

        fairPicker.setTextStyle(QuantityPicker.BOLD);

        fairPicker.setQuantityTextColor(R.color.colorPrimaryDark);

        fairPicker.setQuantityButtonColor(R.color.colorAccent);

        fairPicker.setOnQuantityChangeListener(new QuantityPicker.OnQuantityChangeListener() {
            @Override
            public void onValueChanged(int quantity) {
                fair = Integer.toString(quantity);
            }
        });


        QuantityPicker seatsPicker = (QuantityPicker) findViewById(R.id.cr_seats_picker);

        if (seatsPicker.getQuantity()!=0){
            seats = Integer.toString(seatsPicker.getQuantity());
        }

        seatsPicker.setMinQuantity(1);

        seatsPicker.setMaxQuantity(10);

        seatsPicker.setQuantityPicker(true);

        seatsPicker.setTextStyle(QuantityPicker.BOLD);

        seatsPicker.setQuantityTextColor(R.color.colorPrimaryDark);

        seatsPicker.setQuantityButtonColor(R.color.colorAccent);

        seatsPicker.setOnQuantityChangeListener(new QuantityPicker.OnQuantityChangeListener() {
            @Override
            public void onValueChanged(int quantity) {
                seats = Integer.toString(quantity);
            }
        });


        RadioButton bookingAllowed = (RadioButton) findViewById(R.id.cr_booking_allowed_radio_btn);
        bookingAllowed.setChecked(true);
        RadioButton bookingNotAllowed = (RadioButton) findViewById(R.id.cr_booking_not_allowed_radio_btn);

        smokingAllowed = (CheckBox) findViewById(R.id.cr_smoking_checkbox);



        animalsAllowed = (CheckBox) findViewById(R.id.cr_animals_checkbox);




        Button createRide = (Button) findViewById(R.id.cr_create_ride);
        createRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CarpoolFragment fragment = new CarpoolFragment();
//                getSupportActionBar().hide();
//                loadFragment(fragment);

                progressDialog = new ProgressDialog(CreateRideDetails.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("Creating Ride");
                progressDialog.show();
                getDirections();

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }


    private void saveData(){
        LatLng startingPoint = new LatLng(ShareFragment.start_latitude,ShareFragment.start_longitude);
        LatLng destinationPoint = new LatLng(ShareFragment.dest_latitude,ShareFragment.dest_longitude);

        Date sumDate = toMins(duration);
        Log.e("CreateRideDetails", "saveData: Date as Date: " + sumDate );
        long arrivalTime = sumDate.getTime();
        Long.toString(arrivalTime);


        if (smokingAllowed.isChecked()){
            allowSmoking = true;
        }else{
            allowSmoking = false;
        }

        if (animalsAllowed.isChecked()){
            allowAnimals = true;
        }else{
            allowAnimals = false;
        }

        int selectedRadioBtn = radioBookingGroup.getCheckedRadioButtonId();
        if (selectedRadioBtn == R.id.cr_booking_allowed_radio_btn){
            instantBookingAllowed = true;
        }else{
            instantBookingAllowed = false;
        }

        baggageType = spinner.getSelectedItem().toString();

        myRidesDataModel = new MyRidesDataModel();
        myRidesDataModel.setAllowAnimals(allowAnimals);
        myRidesDataModel.setAllowSmoking(allowSmoking);
        myRidesDataModel.setDistance(distance);
        myRidesDataModel.setDuration(duration);
        myRidesDataModel.setSeatsAvailable(seats);
        myRidesDataModel.setFairPerKm(fair);
        myRidesDataModel.setStartingDate(ShareFragment.selectedDate);
        myRidesDataModel.setStartingTime(ShareFragment.selectedTime);
        myRidesDataModel.setBaggageType(baggageType);
        myRidesDataModel.setInstantBookingAllowed(instantBookingAllowed);
//        myRidesDataModel.setSource(startingPoint);
//        myRidesDataModel.setDestination(destinationPoint);
        myRidesDataModel.setCities(cities);
        myRidesDataModel.setPolyline(polyline);
        myRidesDataModel.setArrivalTime(Long.toString(arrivalTime));
        myRidesDataModel.setStartingAddress(ShareFragment.startingAddress);
        myRidesDataModel.setDestinationAddress(ShareFragment.destinationAddress);



        databaseReference = FirebaseDatabase.getInstance().getReference().child("My Rides").child(LoginActivity.userId);
        databaseReference.setValue(myRidesDataModel);
    }

    private void getDirections(){
        Object dataTransfer[] = new Object[3];
        String url = getDirectionsUrl();
        GetDirections getDirections = new GetDirections();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = getApplicationContext();
        getDirections.execute(dataTransfer);

    }


    public class GetDirections extends AsyncTask<Object,String,String> {

        GoogleMap mMap;
        String url;
        String googleDirectionsData;
        Context context;


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
            cities = parser.getAllCities(s);
            polyline = parser.getPolyline(s);
            saveData();
            progressDialog.dismiss();
            finish();

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






    }


    private static Date toMins(String s) {
        String[] hourMin = s.split(" ");
        int hour;
        int mins;

        if (hourMin.length>2){
            hour = Integer.parseInt(hourMin[0]);
            mins = Integer.parseInt(hourMin[2]);
        }
        else {
            hour = Integer.parseInt(hourMin[0]);
            mins = Integer.parseInt(hourMin[1]);
        }

        Date arrivaldate = new Date();
        arrivaldate.setHours(hour);
        arrivaldate.setMinutes(mins);

        Date startingDate = new Date();
        startingDate.setHours(ShareFragment.hour);
        startingDate.setMinutes(ShareFragment.min);

        long sum = startingDate.getTime() + arrivaldate.getTime();

        Date sumDate = new Date(sum);


        return sumDate;
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

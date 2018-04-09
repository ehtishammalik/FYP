package com.example.android.rideshareplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

/**
 * Created by Ehtisham on 12/11/17.
 */
public class ShareFragment extends android.support.v4.app.Fragment {
    private TextView from, to;
    public static TextView when, Return;
    private Button create,seats;
    static String selectedTime,selectedDate;
    public String Seats = "";

    static double start_longitude,start_latitude;
    static double dest_longitude,dest_latitude;
    int PLACE_PICKER_REQUEST_FROM = 1;
    int PLACE_PICKER_REQUEST_TO = 2;
    public Place place;

    DatabaseReference databaseReference;

    //Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);


        from = (TextView) view.findViewById(R.id.create_from);
        to = (TextView) view.findViewById(R.id.create_to);

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent,PLACE_PICKER_REQUEST_TO);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent,PLACE_PICKER_REQUEST_FROM);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        when = (TextView) view.findViewById(R.id.create_when);
        when.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });
        Return = (TextView) view.findViewById(R.id.create_return_time);

        seats = (Button) view.findViewById(R.id.btn_Seats);
        seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialoge();
            }
        });

        create = (Button) view.findViewById(R.id.btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_PICKER_REQUEST_FROM){
            if (resultCode == getActivity().RESULT_OK){
                place = PlacePicker.getPlace(data,getContext());
                String address = place.getAddress().toString();
                LatLng location = place.getLatLng();

                start_longitude = location.longitude;

                Log.e("ShareFragment", "onActivityResult: startLatitude" + start_latitude);
                Log.e("ShareFragment", "onActivityResult: startLongitude" + start_longitude);

                start_latitude = location.latitude;

                from.setTextSize(10);

                from.setText(address);

                databaseReference = FirebaseDatabase.getInstance().getReference("My Rides");
                databaseReference.child("Source").child("Latitude").setValue(start_latitude);
                databaseReference.child("Source").child("Longitude").setValue(start_longitude);



            }
        }
        else {
            if (resultCode == getActivity().RESULT_OK){
                place = PlacePicker.getPlace(data,getContext());
                String address = place.getAddress().toString();
                LatLng location = place.getLatLng();

                dest_longitude = location.longitude;

                dest_latitude = location.latitude;

                to.setTextSize(10);

                to.setText(address);
                databaseReference = FirebaseDatabase.getInstance().getReference("My Rides");
                databaseReference.child("Destination").child("Latitude").setValue(dest_latitude);
                databaseReference.child("Destination").child("Longitude").setValue(dest_longitude);

            }
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void transactionsView() {

    }
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (Integer.toString(minute).length()<2){
                selectedTime = Integer.toString(hourOfDay) + ": 0" + Integer.toString(minute);
            }else{
                selectedTime = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
            }
            if (!selectedTime.toString().isEmpty()){
                when.setText(selectedTime+", "+selectedDate);
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            selectedDate = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
//            if (!selectedDate.equals(null)){
//                date.setText(selectedDate);
//            }
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
        }
    }

    public void showDialoge(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Select Number Of Seats");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("1");
        arrayAdapter.add("2");
        arrayAdapter.add("3");
        arrayAdapter.add("4");
        arrayAdapter.add("5");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Seats = arrayAdapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                builderInner.setMessage(Seats);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    public static void findPath(){

        Object dataTransfer[] = new Object[3];
        String url = getDirectionsUrl();
        Log.e("getDirectionsUrl", "url: " + url);
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        GoogleMap mMap = null;
        dataTransfer[0] = mMap ;
        dataTransfer[1] = url;
        getDirectionsData.execute(dataTransfer);
    }

        private static String getDirectionsUrl(){
            Log.e("getDirectionsUrl", "latitude: " +ShareFragment.start_longitude);
            Log.e("getDirectionsUrl", "latitude: " +ShareFragment.start_latitude);
            StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
            googleDirectionsUrl.append("origin="+ShareFragment.start_latitude+","+ShareFragment.start_longitude);
            googleDirectionsUrl.append("&destination="+ShareFragment.dest_latitude+","+ShareFragment.dest_longitude);
            googleDirectionsUrl.append("&key="+"AIzaSyD5dedqklHHrJvgU9-qEYQmq_YjKHtziqY");

            return googleDirectionsUrl.toString();
        }


}

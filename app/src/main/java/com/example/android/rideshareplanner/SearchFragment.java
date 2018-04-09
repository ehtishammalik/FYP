package com.example.android.rideshareplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

/**
 * Created by Ehtisham on 12/11/17.
 */
public class SearchFragment extends android.support.v4.app.Fragment {
    private TextView from, to;
    public static TextView date,time;
    private Button search;
    static String selectedTime,selectedDate;
    static String start_longitude,start_latitude;
    static String dest_longitude,dest_latitude;
    int PLACE_PICKER_REQUEST_FROM = 1;
    int PLACE_PICKER_REQUEST_TO = 2;
    private Place place;

    //Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        from = (TextView) view.findViewById(R.id.search_from);
        to = (TextView) view.findViewById(R.id.search_to);

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

        date = (TextView) view.findViewById(R.id.search_when);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        time = (TextView) view.findViewById(R.id.search_return_time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getFragmentManager(), "timePicker");
            }
        });


        search = (Button) view.findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),MapsActivity.class);
//                startActivity(intent);
                Fragment fragment = new SearchResultsFragment();
                loadFragment(fragment);
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
                String  place_id = place.getId();
                Log.e("SearchFragment", "onActivityResult: place_id" +place_id);

                start_longitude = Double.toString(location.longitude);

                start_latitude = Double.toString(location.latitude);

                from.setTextSize(10);

                from.setText(address);

            }
        }
        else {
            if (resultCode == getActivity().RESULT_OK){
                place = PlacePicker.getPlace(data,getContext());
                String address = place.getAddress().toString();
                LatLng location = place.getLatLng();

                dest_longitude = Double.toString(location.longitude);

                dest_latitude = Double.toString(location.latitude);

                to.setTextSize(10);

                to.setText(address);

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
                time.setText(selectedTime);
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
            if (!selectedDate.equals(null)){
                date.setText(selectedDate);
            }
        }
    }


    private void loadFragment(Fragment fragment) {
        // load fragment

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

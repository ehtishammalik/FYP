package com.example.android.rideshareplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RideDetails extends AppCompatActivity {
    TextView dateAndTime, seatsAvailable;
    TextView startingTime, destinationTime;
    TextView startingLocation, destinationLocation;
    TextView luggageDetails, smokingAllowed, animalsAllowed;
    MyRidesDataModel ride;
    ImageView directionImage;
    ImageButton minusButton, plusButton;
    TextView seatsToReserve;
    Button reserveSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        ride = IProvidedAdapter.selectedItem;

        directionImage = (ImageView) findViewById(R.id.rd_direction_img);

        dateAndTime = (TextView) findViewById(R.id.rd_date_time);
        dateAndTime.setText("Date: " + ride.getStartingDate() + "Time: " + ride.getStartingTime());

        seatsAvailable = (TextView) findViewById(R.id.rd_seats_available);
        int availableSeats;
        if (ride.getSeatsBooked() != null && !ride.getSeatsAvailable().equals(null)){
            availableSeats = Integer.parseInt(ride.getSeatsAvailable()) - Integer.parseInt(ride.getSeatsBooked());
        }
        else{
            availableSeats = Integer.parseInt(ride.getSeatsAvailable());
        }
        seatsAvailable.setText("Available Seats: " + (Integer.toString(availableSeats)));

        startingTime = (TextView) findViewById(R.id.rd_time_from);
        startingTime.setText(ride.getStartingTime());

        destinationTime = (TextView) findViewById(R.id.rd_time_to);
        destinationTime.setText(ride.getDuration());

        startingLocation = (TextView) findViewById(R.id.rd_from_text);
        startingLocation.setText(ride.getStartingAddress());

        destinationLocation = (TextView) findViewById(R.id.rd_to_text);
        destinationLocation.setText(ride.getDestinationAddress());

        luggageDetails = (TextView) findViewById(R.id.rd_luggage_details);
        luggageDetails.setText(ride.getBaggageType());

        smokingAllowed = (TextView) findViewById(R.id.rd_smoking_text);
        if (ride.isAllowSmoking()){
            smokingAllowed.setText("Smoking Allowed");
        }
        else {
            smokingAllowed.setText("Smoking NOT Allowed");
        }

        animalsAllowed = (TextView) findViewById(R.id.rd_animal_text);
        if (ride.isAllowAnimals()){
            smokingAllowed.setText("Animals Allowed");
        }
        else {
            smokingAllowed.setText("Animals NOT Allowed");
        }

        seatsToReserve = (TextView) findViewById(R.id.rd_seats_toReserve);

        minusButton = (ImageButton) findViewById(R.id.rd_btn_minus);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seats = Integer.parseInt(seatsToReserve.getText().toString());
                seats--;
                seatsToReserve.setText(Integer.toString(seats));
            }
        });
        plusButton = (ImageButton) findViewById(R.id.rd_btn_add);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seats = Integer.parseInt(seatsToReserve.getText().toString());
                seats++;
                seatsToReserve.setText(Integer.toString(seats));
            }
        });

        reserveSeats = (Button) findViewById(R.id.rd_btn_reserveSeats);
        reserveSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}

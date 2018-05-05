package com.example.android.rideshareplanner;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hamza on 4/21/18.
 */

public class MyRidesDataModel {
    private LatLng source;
    private LatLng destination;
    private ArrayList<String> cities;
    private String startingTime;
    private String arrivalTime;
    private String fairPerKm;
    private String seatsAvailable;
    private String startingDate;
    private String polyline;
    private String baggageType;
    private boolean allowSmoking;
    private boolean allowAnimals;
    private boolean instantBookingAllowed;
    private String distance;
    private String duration;
    private ArrayList<Integer> passengers;
    private String startingAddress;
    private String destinationAddress;
    private String seatsBooked;

    public MyRidesDataModel(){
        source = null;
        destination = null;
        cities = new ArrayList<>();
        startingTime = null;
        arrivalTime = null;
        fairPerKm = null;
        seatsAvailable = null;
        seatsBooked = null;
        startingDate = null;
        polyline = null;
        baggageType = null;
        allowSmoking = true;
        allowAnimals = true;
        distance = null;
        duration = null;
        instantBookingAllowed = true;
        passengers = new ArrayList<>();
    }

    public boolean isInstantBookingAllowed() {
        return instantBookingAllowed;
    }

    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public void setInstantBookingAllowed(boolean instantBookingAllowed) {
        this.instantBookingAllowed = instantBookingAllowed;

    }

    public void setSeatsBooked(String seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public void setSource(LatLng source) {

        this.source = source;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setFairPerKm(String fairPerKm) {
        this.fairPerKm = fairPerKm;
    }

    public void setSeatsAvailable(String seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public void setBaggageType(String baggageType) {
        this.baggageType = baggageType;
    }

    public void setAllowSmoking(boolean allowSmoking) {
        this.allowSmoking = allowSmoking;
    }

    public void setAllowAnimals(boolean allowAnimals) {
        this.allowAnimals = allowAnimals;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setPassengers(ArrayList<Integer> passengers) {
        this.passengers = passengers;
    }



    public LatLng getSource() {
        return source;
    }

    public LatLng getDestination() {
        return destination;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getFairPerKm() {
        return fairPerKm;
    }

    public String getSeatsBooked() {
        return seatsBooked;
    }

    public String getSeatsAvailable() {
        return seatsAvailable;
    }

    public String getStartingAddress() {
        return startingAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getStartingDate() {
        return startingDate;

    }

    public String getPolyline() {
        return polyline;
    }

    public String getBaggageType() {
        return baggageType;
    }

    public boolean isAllowSmoking() {
        return allowSmoking;
    }

    public boolean isAllowAnimals() {
        return allowAnimals;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public ArrayList<Integer> getPassengers() {
        return passengers;
    }
}

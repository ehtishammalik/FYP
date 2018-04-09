package com.example.android.rideshareplanner;

/**
 * Created by hamza on 3/11/18.
 */

public class carpoolItems {
    String dateAndDay;
    String departureTime;
    String estArrivalTime;
    int totalSeats;
    int availableSeats;
    int bookedSeats;
    String Source;
    String Destination;

    public carpoolItems(){
        this.dateAndDay = null;
        this.departureTime = null;
        this.estArrivalTime = null;
        this.totalSeats = 0;
        this.availableSeats =0;
        this.bookedSeats =0;
        this.Source = null;
        this.Destination = null;
    }

    public String getDateAndDay() {
        return dateAndDay;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getEstArrivalTime() {
        return estArrivalTime;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setDateAndDay(String dateAndDay) {
        this.dateAndDay = dateAndDay;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setEstArrivalTime(String estArrivalTime) {
        this.estArrivalTime = estArrivalTime;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setSource(String source) {
        Source = source;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public int getAvailableSeats() {

        return availableSeats;
    }

    public String getSource() {
        return Source;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public String getDestination() {
        return Destination;
    }
}

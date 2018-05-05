package com.example.android.rideshareplanner;

import java.net.URL;

/**
 * Created by hamza on 5/4/18.
 */

public class VehicleDetails {
    String vehicleModel, vehicleColor, vehicleRegNo;
    String carImageUrl;

    public VehicleDetails(String vehicleModel, String vehicleColor, String vehicleRegNo, String carImageUrl) {
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
        this.vehicleRegNo = vehicleRegNo;
        this.carImageUrl = carImageUrl;
    }

    public VehicleDetails() {
        this.vehicleModel = null;
        this.vehicleColor = null;
        this.vehicleRegNo = null;
        this.carImageUrl = null;
    }
}

package com.example.android.rideshareplanner;

import java.net.URL;

/**
 * Created by hamza on 2/19/18.
 */

public class Users {
    private String name,email,driverLicenceNo, driverRegNo,phoneNo;
    private String imageUrl;
    private VehicleDetails vehicleDetails;

    public Users (){
        name = null;
        email = null;
        driverLicenceNo = null;
        driverRegNo = null;
        phoneNo = null;
        imageUrl = null;
        vehicleDetails = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDriverLicenceNo(String driverLicenceNo) {
        this.driverLicenceNo = driverLicenceNo;
    }

    public void setDriverRegNo(String driverRegNo) {
        this.driverRegNo = driverRegNo;
    }

    public String getName() {

        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVehicleDetails(VehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public VehicleDetails getVehicleDetails() {
        return vehicleDetails;
    }

    public String getEmail() {
        return email;
    }

    public String getDriverLicenceNo() {
        return driverLicenceNo;
    }

    public String getDriverRegNo() {
        return driverRegNo;
    }
}

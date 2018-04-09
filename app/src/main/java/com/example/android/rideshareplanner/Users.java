package com.example.android.rideshareplanner;

/**
 * Created by hamza on 2/19/18.
 */

public class Users {
    private String name,email,licenceNo,regNo;

    public Users (){
        name = null;
        email = null;
        licenceNo = null;
        regNo = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public String getRegNo() {
        return regNo;
    }
}

package com.example.android.rideshareplanner;

/**
 * Created by hamza on 3/29/18.
 */

public class MyRidesModel {
    String path;

    public void setPath(String path) {
        this.path = path;
    }

    public MyRidesModel(){
        this.path = null;

    }

    public String getPath() {
        return path;
    }
}

package com.example.android.rideshareplanner;

/**
 * Created by hamza on 3/7/18.
 */

public class ProfileListviewItems {
    String EventName;
    String Value;
    String IsVerified;
    int imageId;
    public ProfileListviewItems(){
        this.EventName = null;
        this.IsVerified = null;
        this.Value = null;
        this.imageId = 0;

    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getEventName() {
        return EventName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public void setValue(String value) {
        Value = value;
    }

    public void setIsVerified(String isVerified) {
        IsVerified = isVerified;
    }

    public String getValue() {
        return Value;
    }

    public String getIsVerified() {
        return IsVerified;
    }
}

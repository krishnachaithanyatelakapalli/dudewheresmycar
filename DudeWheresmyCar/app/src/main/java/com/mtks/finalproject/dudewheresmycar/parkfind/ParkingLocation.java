package com.mtks.finalproject.dudewheresmycar.parkfind;

/**
 * Created by Krishna on 5/17/2017.
 */

public class ParkingLocation {
    private String LocName;
    private Boolean isEmpty;
    private double latitude;
    private double longitude;

    public ParkingLocation(String LocName, Boolean isEmpty, double latitude, double longitude) {
        this.LocName = LocName;
        this.isEmpty = isEmpty;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocName() {
        return LocName;
    }

    public Boolean getIsEmpty() {
        return isEmpty;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

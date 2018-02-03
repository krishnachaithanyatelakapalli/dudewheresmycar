package com.mtks.finalproject.dudewheresmycar.parkfind;

/**
 * Created by Krishna on 5/17/2017.
 */

public class severParams {
    private String method;
    private String nameTag;
    private String status;
    private double latitude;
    private double longitude;
    private String url;
    private String action;

    severParams(String method, String nameTag, String status, double latitude, double longitude, String url, String action) {
        this.method = method;
        this.nameTag = nameTag;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public String getNameTag() {
        return nameTag;
    }
    public String getStatus() {
        return status;
    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getUrl() {
        return url;
    }

    public String getAction() {
        return action;
    }
}

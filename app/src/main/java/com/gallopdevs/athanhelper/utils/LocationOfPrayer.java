package com.gallopdevs.athanhelper.utils;

public class LocationOfPrayer {

    private double latitude;
    private double longitude;
    private static final LocationOfPrayer instance = new LocationOfPrayer();

    private LocationOfPrayer() {

    }

    public static LocationOfPrayer getInstance() {
        return instance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

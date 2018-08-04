package com.gallopdevs.athanhelper.utils;

public class LocationOfPrayer {

    private double latitude;
    private double longitude;
    private static LocationOfPrayer instance = null;

    private LocationOfPrayer() {

    }

    public static LocationOfPrayer getInstance() {
        if (instance == null) {
            instance = new LocationOfPrayer();
        }
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

package com.gallopdevs.athanhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gallopdevs.athanhelper.model.PrayTime;
import com.google.android.gms.common.util.CrashUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarPrayerTimes {

    private static final String TAG = "CalendarPrayerTimes";

    private static PrayTime prayerTime = PrayTime.getInstance();

    private static ArrayList<String> newTimes = new ArrayList<>();
    private static ArrayList<String> nextDayTimes = new ArrayList<>();

    private static Calendar calendar = Calendar.getInstance();
    private static int month = calendar.get(Calendar.MONTH);
    private static int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private static int year = calendar.get(Calendar.YEAR);
    private static int dstOffset = calendar.get(Calendar.DST_OFFSET) / 3600000;
    private static int timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset;

//    private SharedPreferences sharedPrefLat;
//    private SharedPreferences sharePrefLong;
    private static double latitude;
    private static double longitude;

//    private static LocationOfPrayer locationOfPrayer = LocationOfPrayer.getInstance();
//    private static double latitude = locationOfPrayer.getLatitude();
//    private static double longitude = locationOfPrayer.getLongitude();

    public static ArrayList<String> getNewTimes(Context context) {
//        SharedPreferences sharedPrefLat = context.getSharedPreferences("latitude", Context.MODE_PRIVATE);
//        SharedPreferences sharedPrefLong = context.getSharedPreferences("longitude", Context.MODE_PRIVATE);
//        double latitude = Double.parseDouble(sharedPrefLat.getString("latitude", "0.0"));
//        double longitude = Double.parseDouble(sharedPrefLong.getString("longitude", "0.0"));
        Log.d(TAG, "latitude: " + String.valueOf(latitude) + " longitude: " + String.valueOf(longitude));
        newTimes = prayerTime.getPrayerTimes(calendar, latitude, longitude, timeZoneOffset);
        return newTimes;
    }

    public static ArrayList<String> getNextDayTimes(Context context, int i) {
//        SharedPreferences sharedPrefLat = context.getSharedPreferences("latitude", Context.MODE_PRIVATE);
//        SharedPreferences sharedPrefLong = context.getSharedPreferences("longitude", Context.MODE_PRIVATE);
//        double latitude = Double.parseDouble(sharedPrefLat.getString("latitude", "0.0"));
//        double longitude = Double.parseDouble(sharedPrefLong.getString("longitude", "0.0"));
        Log.d(TAG, "latitude: " + String.valueOf(latitude) + " longitude: " + String.valueOf(longitude));
        calendar.set(year, month, dayOfMonth + i);
        nextDayTimes = prayerTime.getPrayerTimes(calendar, latitude, longitude, timeZoneOffset);
        return nextDayTimes;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLatitude(double latitude) {
        Log.d(TAG, "setLatitude: setting latitude: " + String.valueOf(latitude));
        CalendarPrayerTimes.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        Log.d(TAG, "setLongitude: setting longitude: " + String.valueOf(longitude));
        CalendarPrayerTimes.longitude = longitude;
    }
}

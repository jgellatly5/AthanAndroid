package com.gallopdevs.athanhelper.utils;

import com.gallopdevs.athanhelper.model.PrayTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarPrayerTimes {

    private static final String TAG = "CalendarPrayerTimes";

    private static final int DEFAULT_CALC_METHOD = 2;
    private static final int DEFAULT_JURISTIC_METHOD = 0;
    private static final int DEFAULT_HIGH_LATITUDES = 0;
    private static final int DEFAULT_TIME_FORMAT = 0;

    private static PrayTime prayerTime = PrayTime.getInstance();

    public static long getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String currentTime = simpleDateFormat.format(Calendar.getInstance().getTime());
        long currentTimeMilliSeconds = 0;
        try {
            currentTimeMilliSeconds = simpleDateFormat.parse(currentTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentTimeMilliSeconds;
    }

    public static void configureSettings() {
        prayerTime.setCalcMethod(DEFAULT_CALC_METHOD);
        prayerTime.setAsrJuristic(DEFAULT_JURISTIC_METHOD);
        prayerTime.setAdjustHighLats(DEFAULT_HIGH_LATITUDES);
        prayerTime.setTimeFormat(DEFAULT_TIME_FORMAT);
    }

    public static void updateCalcMethod(int value) {
        prayerTime.setCalcMethod(value);
    }

    public static void updateAsrJuristic(int value) {
        prayerTime.setAsrJuristic(value);
    }

    public static void updateHighLats(int value) {
        prayerTime.setAdjustHighLats(value);
    }

    public static void updateTimeFormat() {
        prayerTime.setTimeFormat(DEFAULT_TIME_FORMAT);
    }

    private static Calendar calendar = Calendar.getInstance();
    private static int month = calendar.get(Calendar.MONTH);
    private static int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private static int year = calendar.get(Calendar.YEAR);
    private static int dstOffset = calendar.get(Calendar.DST_OFFSET) / 3600000;
    private static int timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset;

    private static double latitude;
    private static double longitude;

    public static ArrayList<String> getNewTimes() {
        return prayerTime.getPrayerTimes(calendar, latitude, longitude, timeZoneOffset);
    }

    public static ArrayList<String> getNextDayTimes(int i) {
        calendar.set(year, month, dayOfMonth + i);
        return prayerTime.getPrayerTimes(calendar, latitude, longitude, timeZoneOffset);
    }

    public static void setLatitude(double latitude) {
        CalendarPrayerTimes.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        CalendarPrayerTimes.longitude = longitude;
    }
}

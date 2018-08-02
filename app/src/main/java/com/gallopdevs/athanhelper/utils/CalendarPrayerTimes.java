package com.gallopdevs.athanhelper.utils;

import com.gallopdevs.athanhelper.model.PrayTime;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarPrayerTimes {

    private static PrayTime prayerTime = PrayTime.getInstance();

    private static ArrayList<String> newTimes = new ArrayList<>();
    private static ArrayList<String> nextDayTimes = new ArrayList<>();

    private static Calendar calendar = Calendar.getInstance();
    private static int month = calendar.get(Calendar.MONTH);
    private static int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private static int year = calendar.get(Calendar.YEAR);
    private static int dstOffset = calendar.get(Calendar.DST_OFFSET) / 3600000;
    private static int timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset;

    private static LocationOfPrayer locationOfPrayer = LocationOfPrayer.getInstance();
    private static double latitude = locationOfPrayer.getLatitude();
    private static double longitude = locationOfPrayer.getLongitude();

    public static ArrayList<String> getNewTimes() {
        newTimes = prayerTime.getPrayerTimes(calendar, latitude, longitude, timeZoneOffset);
        return newTimes;
    }

    public static ArrayList<String> getNextDayTimes(int i) {
        calendar.set(year, month, dayOfMonth + i);
        nextDayTimes = prayerTime.getPrayerTimes(calendar, latitude, longitude, timeZoneOffset);
        return nextDayTimes;
    }
}

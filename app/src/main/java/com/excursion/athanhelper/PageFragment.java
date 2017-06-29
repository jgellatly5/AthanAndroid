package com.excursion.athanhelper;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    TextView dateTextView;
    TextView dawnTimeTextView;
    TextView middayTimeTextView;
    TextView afternoonTimeTextView;
    TextView sunsetTimeTextView;
    TextView nightTimeTextView;
    PrayTime prayerTime = new PrayTime();

    final int DEFAULT_CALC_METHOD = 2;
    final int DEFAULT_JURISTIC_METHOD = 0;
    final int DEFAULT_HIGH_LATITUDES = 0;
    final int DEFAULT_TIME_FORMAT = 1;

    final String KEY_PREF_CALC_METHOD = "calculation_method";
    final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";
    final String KEY_PREF_TIME_FORMATS = "time_formats";

    int calcMethod = 0;
    int juristicMethod = 0;
    int highLatitudes = 0;
    int timeFormat = 1;

    double latitude;
    double longitude;

    Calendar nextDay = Calendar.getInstance();
    int dstOffset = nextDay.get(Calendar.DST_OFFSET)/3600000;
    int timeZoneOffset = nextDay.get(Calendar.ZONE_OFFSET)/3600000 + dstOffset;
    ArrayList<String> nextDayTimes = new ArrayList<>();

    public PageFragment() {
        // Required empty public constructor
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case KEY_PREF_CALC_METHOD:
                    String calcMethodString = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
                    calcMethod = Integer.parseInt(calcMethodString);
                    prayerTime.setCalcMethod(calcMethod);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, timeZoneOffset);
                    break;
                case KEY_PREF_JURISTIC_METHOD:
                    String juristicMethodString = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
                    juristicMethod = Integer.parseInt(juristicMethodString);
                    prayerTime.setAsrJuristic(juristicMethod);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, timeZoneOffset);
                    break;
                case KEY_PREF_HIGH_LATITUDES:
                    String highLatitudesString = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
                    highLatitudes = Integer.parseInt(highLatitudesString);
                    prayerTime.setAdjustHighLats(highLatitudes);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, timeZoneOffset);
                    break;
                case KEY_PREF_TIME_FORMATS:
                    String timeFormatsString = sharedPreferences.getString(KEY_PREF_TIME_FORMATS, "");
                    timeFormat = Integer.parseInt(timeFormatsString);
                    prayerTime.setTimeFormat(timeFormat);
                    nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, timeZoneOffset);
                    break;
            }
            updateView();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        dateTextView = (TextView) view.findViewById(R.id.dayTextView);

        String locationProvider = LocationManager.NETWORK_PROVIDER;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        latitude = lastKnownLocation.getLatitude();
        longitude = lastKnownLocation.getLongitude();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        prayerTime.setAsrJuristic(DEFAULT_JURISTIC_METHOD);
        prayerTime.setCalcMethod(DEFAULT_CALC_METHOD);
        prayerTime.setAdjustHighLats(DEFAULT_HIGH_LATITUDES);
        prayerTime.setTimeFormat(DEFAULT_TIME_FORMAT);

        dawnTimeTextView = (TextView) view.findViewById(R.id.dawnTimeTextView);
        middayTimeTextView = (TextView) view.findViewById(R.id.middayTimeTextView);
        afternoonTimeTextView = (TextView) view.findViewById(R.id.afternoonTimeTextView);
        sunsetTimeTextView = (TextView) view.findViewById(R.id.sunsetTimeTextView);
        nightTimeTextView = (TextView) view.findViewById(R.id.nightTimeTextView);

        Bundle bundle = getArguments();
        formatDate(bundle);
        formatPrayers(bundle);
        return view;
    }

    private void formatPrayers(Bundle bundle) {
        int count = bundle.getInt("count");
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);

        nextDay.set(year, month, dayOfMonth + count);
        nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, timeZoneOffset);

        updateView();
    }

    private void updateView() {
        String newDawmTime = nextDayTimes.get(1).replaceFirst("^0+(?!$)", "");
        String newMiddayTime = nextDayTimes.get(2).replaceFirst("^0+(?!$)", "");
        String newAfternoonTime = nextDayTimes.get(3).replaceFirst("^0+(?!$)", "");
        String newSunsetTime = nextDayTimes.get(5).replaceFirst("^0+(?!$)", "");
        String newNightTime = nextDayTimes.get(6).replaceFirst("^0+(?!$)", "");
        if (timeFormat == 0) {
            dawnTimeTextView.setText(nextDayTimes.get(1));
            middayTimeTextView.setText(nextDayTimes.get(2));
            afternoonTimeTextView.setText(nextDayTimes.get(3));
            sunsetTimeTextView.setText(nextDayTimes.get(5));
            nightTimeTextView.setText(nextDayTimes.get(6));
        } else {
            dawnTimeTextView.setText(newDawmTime);
            middayTimeTextView.setText(newMiddayTime);
            afternoonTimeTextView.setText(newAfternoonTime);
            sunsetTimeTextView.setText(newSunsetTime);
            nightTimeTextView.setText(newNightTime);
        }
    }

    private void formatDate(Bundle bundle) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int weekDay = bundle.getInt("day");
        int count = bundle.getInt("count");
        nextDay.set(year, month, dayOfMonth + count);
        int numberDay = nextDay.get(Calendar.DAY_OF_MONTH);
        int monthDay = nextDay.get(Calendar.MONTH) + 1;

        String numberString = String.valueOf(numberDay);

        String monthString = "";
        if (monthDay < 10) {
            monthString = "0" + String.valueOf(monthDay);
        } else {
            monthString = String.valueOf(monthDay);
        }

        if (weekDay >= 8) {
            weekDay = weekDay - 7;
        }
        String weekDayString = "";
        switch (weekDay) {
            case 1: weekDayString = "Sunday";
                    break;
            case 2: weekDayString = "Monday";
                    break;
            case 3: weekDayString = "Tuesday";
                    break;
            case 4: weekDayString = "Wednesday";
                    break;
            case 5: weekDayString = "Thursday";
                    break;
            case 6: weekDayString = "Friday";
                    break;
            case 7: weekDayString = "Saturday";
                    break;
            default: weekDayString = "This is not a day";
                break;
        }
        if (numberDay < 10) {
            dateTextView.setText(weekDayString + " " + monthString + "/0" + numberString);
        } else {
            dateTextView.setText(weekDayString + " " + monthString + "/" + numberString);
        }
    }
}

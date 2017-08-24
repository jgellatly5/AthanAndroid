package com.gallopdevs.athanhelper;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
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
    int dstOffset = nextDay.get(Calendar.DST_OFFSET) / 3600000;
    int timeZoneOffset = nextDay.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset;
    ArrayList<String> nextDayTimes = new ArrayList<>();

    String locationProvider;
    LocationManager locationManager;

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

        if (hasPermissions()) {
            getLocation();
        } else {
            requestPerms();
        }

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

    private void requestPerms() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    private void getLocation() {
        locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPerms();
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        } else {
            locationProvider = LocationManager.GPS_PROVIDER;
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        }
    }

    private boolean hasPermissions() {
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        for (String perms : permissions) {
            res = getActivity().checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                for (int res : grantResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
        if (allowed) {
            getLocation();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(getActivity(), "Location permissions denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        String newDawmTime = nextDayTimes.get(0).replaceFirst("^0+(?!$)", "");
        String newMiddayTime = nextDayTimes.get(2).replaceFirst("^0+(?!$)", "");
        String newAfternoonTime = nextDayTimes.get(3).replaceFirst("^0+(?!$)", "");
        String newSunsetTime = nextDayTimes.get(5).replaceFirst("^0+(?!$)", "");
        String newNightTime = nextDayTimes.get(6).replaceFirst("^0+(?!$)", "");
        if (timeFormat == 0) {
            dawnTimeTextView.setText(nextDayTimes.get(0));
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

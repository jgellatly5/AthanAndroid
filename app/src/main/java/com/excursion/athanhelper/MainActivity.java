package com.excursion.athanhelper;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    ViewPager viewPager;
    TextView prayerTimer;
    CountDownTimer timer;

    String dawnTime = "";
    String middayTime = "";
    String afternoonTime = "";
    String sunsetTime = "";
    String nightTime = "";
    String nextDawnTime = "";

    int currentTimeIndex = 0;

    ArrayList<String> newTimes = new ArrayList<>();
    ArrayList<String> nextDayTimes = new ArrayList<>();

    long difference1 = 0;
    long difference2 = 0;
    long difference3 = 0;
    long difference4 = 0;
    long difference5 = 0;
    long difference6 = 0;
    long[] differences = {difference1, difference2, difference3, difference4, difference5, difference6};

    double latitude;
    double longitude;

    int calcMethod = 0;
    int juristicMethod = 0;
    int highLatitudes = 0;
    int timeFormat = 0;

    SimpleDateFormat offset;
    PrayTime prayerTime;

    final String KEY_PREF_CALC_METHOD = "calculation_method";
    final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";

    final int DEFAULT_CALC_METHOD = 2;
    final int DEFAULT_JURISTIC_METHOD = 0;
    final int DEFAULT_HIGH_LATITUDES = 0;
    final int DEFAULT_TIME_FORMAT = 1;

    Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH);
    int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    int year = c.get(Calendar.YEAR);
    int dstOffset = c.get(Calendar.DST_OFFSET)/3600000;
    int timeZoneOffset = c.get(Calendar.ZONE_OFFSET)/3600000 + dstOffset;

    String locationProvider;
    LocationManager locationManager;

    Calendar nextDay = Calendar.getInstance();

    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case KEY_PREF_CALC_METHOD:
                    String calcMethodString = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
                    calcMethod = Integer.parseInt(calcMethodString);
                    prayerTime.setCalcMethod(calcMethod);
                    searchPrayerTimes();
                    break;
                case KEY_PREF_JURISTIC_METHOD:
                    String juristicMethodString = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
                    juristicMethod = Integer.parseInt(juristicMethodString);
                    prayerTime.setAsrJuristic(juristicMethod);
                    searchPrayerTimes();
                    break;
                case KEY_PREF_HIGH_LATITUDES:
                    String highLatitudesString = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
                    highLatitudes = Integer.parseInt(highLatitudesString);
                    prayerTime.setAdjustHighLats(highLatitudes);
                    searchPrayerTimes();
                    break;
            }
            timer.cancel();
            startNewTimer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //Request location updates:
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();
            }
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        // set default settings
        calcMethod = DEFAULT_CALC_METHOD;
        juristicMethod = DEFAULT_JURISTIC_METHOD;
        highLatitudes = DEFAULT_HIGH_LATITUDES;
        timeFormat = DEFAULT_TIME_FORMAT;

        prayerTime = new PrayTime();
        prayerTime.setCalcMethod(calcMethod);
        prayerTime.setAsrJuristic(juristicMethod);
        prayerTime.setAdjustHighLats(highLatitudes);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionGranted = PackageManager.PERMISSION_GRANTED;
        locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        latitude = lastKnownLocation.getLatitude();
        longitude = lastKnownLocation.getLongitude();
        Log.i("latitude", String.valueOf(latitude));
        Log.i("longitude", String.valueOf(longitude));

        searchPrayerTimes();
        getActionBar();
        setupSwipe();
        startNewTimer();
    }

    public boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Request")
                        .setMessage("Give location access to app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                        latitude = lastKnownLocation.getLatitude();
                        longitude = lastKnownLocation.getLongitude();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    private void searchPrayerTimes() {
        newTimes = prayerTime.getPrayerTimes(c, latitude, longitude, timeZoneOffset);
        nextDay.set(year, month, dayOfMonth + 1);
        nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, timeZoneOffset);
    }

    public long[] getTimerDifference() {
        // get currentTime and set format
        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        final String currentTime = simpleDateFormat.format(cal.getTime());

        // instantiate dates
        Date dawnDate = null;
        Date middayDate = null;
        Date afternoonDate = null;
        Date sunsetDate = null;
        Date nightDate = null;
        Date nextDawnDate = null;
        Date currentTimeDate = null;

        // format times received from PrayTime model
        dawnTime = newTimes.get(1)+ ":00";
        middayTime = newTimes.get(2) + ":00";
        afternoonTime = newTimes.get(3) + ":00";
        sunsetTime = newTimes.get(4) + ":00";
        nightTime = newTimes.get(6) + ":00";
        nextDawnTime = nextDayTimes.get(1) + ":00";
        try {
            // get milliseconds from parsing dates
            dawnDate = simpleDateFormat.parse(dawnTime);
            long dawnMillis = dawnDate.getTime();

            middayDate = simpleDateFormat.parse(middayTime);
            long middayMillis = middayDate.getTime();

            afternoonDate = simpleDateFormat.parse(afternoonTime);
            long afMillis = afternoonDate.getTime();

            sunsetDate = simpleDateFormat.parse(sunsetTime);
            long sunsetMillis = sunsetDate.getTime();

            nightDate = simpleDateFormat.parse(nightTime);
            long nightMillis = nightDate.getTime();

            nextDawnDate = simpleDateFormat.parse(nextDawnTime);
            long nextDawnMillis = nextDawnDate.getTime();

            //get milliseconds from parsing currentTime
            currentTimeDate = simpleDateFormat.parse(currentTime);
            long currentTimeMilliSeconds = currentTimeDate.getTime();

            //get intervals between times
            difference1 = dawnMillis - currentTimeMilliSeconds;
            difference2 = middayMillis - currentTimeMilliSeconds;
            difference3 = afMillis - currentTimeMilliSeconds;
            difference4 = sunsetMillis - currentTimeMilliSeconds;
            difference5 = nightMillis - currentTimeMilliSeconds;
            difference6 = nextDawnMillis - currentTimeMilliSeconds + 86400000;

            // format for prayerTimer
            offset = new SimpleDateFormat("HH:mm:ss", Locale.US);
            offset.setTimeZone(TimeZone.getTimeZone("GMT"));

            differences[0] = difference1;
            differences[1] = difference2;
            differences[2] = difference3;
            differences[3] = difference4;
            differences[4] = difference5;
            differences[5] = difference6;
            return differences;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        differences[0] = difference1;
        differences[1] = difference2;
        differences[2] = difference3;
        differences[3] = difference4;
        differences[4] = difference5;
        differences[5] = difference6;
        return differences;
    }

    private void startNewTimer() {
        timer = new CountDownTimer(getTimerDifference()[getNextTime()], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Date newDateTimer = new Date();
                newDateTimer.setTime(millisUntilFinished);
                prayerTimer = (TextView) findViewById(R.id.prayerTimer);
                prayerTimer.setText(offset.format(newDateTimer) + "s");
            }

            @Override
            public void onFinish() {
                prayerTimer.setText("00:00:00s");
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.crescent)
                                .setContentTitle("Start Prayer: " + prayerTime.getTimeNames().get(currentTimeIndex + 1))
                                .setContentText("Beginning timer until next prayer.");
                int mNotificationId = 001;
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                startNewTimer();
            }
        }.start();
    }

    private int getNextTime() {
        for(int i = 0; i < differences.length; i++) {
            if(differences[i] < 0) {
                currentTimeIndex = i + 1;
                if (currentTimeIndex > 5) {
                    currentTimeIndex = 0;
                }
            }
        }
        return currentTimeIndex;
    }

    private void setupSwipe() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.gallopdevs.athanhelper.views;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.model.PrayTime;
import com.gallopdevs.athanhelper.settings.SettingsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "TimerActivity";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int DEFAULT_CALC_METHOD = 2;
    private static final int DEFAULT_JURISTIC_METHOD = 0;
    private static final int DEFAULT_HIGH_LATITUDES = 0;

    private static final String KEY_PREF_CALC_METHOD = "calculation_method";
    private static final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    private static final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";

    private CountDownTimer timer;

    private int currentTimeIndex = 0;

    private ArrayList<String> newTimes = new ArrayList<>();
    private ArrayList<String> nextDayTimes = new ArrayList<>();

    private long difference1 = 0;
    private long difference2 = 0;
    private long difference3 = 0;
    private long difference4 = 0;
    private long difference5 = 0;
    private long difference6 = 0;
    private long[] differences = {difference1, difference2, difference3, difference4, difference5, difference6};

    private SimpleDateFormat offset;
    private PrayTime prayerTime;

    private Calendar nextDay = Calendar.getInstance();

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.prayerTimer)
    TextView prayerTimer;
    @BindView(R.id.bottom_nav)
    BottomNavigationViewEx bottomNav;

    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);
        init();

        // ask for permissions
        if (hasPermissions()) {
            getLocation();
        } else {
            requestPerms();
        }

        searchPrayerTimes();
        startNewTimer();
    }

    private void init() {
        // bottom nav init
        bottomNav.enableAnimation(false);
        bottomNav.enableShiftingMode(false);
        bottomNav.enableItemShiftingMode(false);

        // location listener
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // settings listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        // set default settings
        prayerTime = new PrayTime();
        prayerTime.setCalcMethod(DEFAULT_CALC_METHOD);
        prayerTime.setAsrJuristic(DEFAULT_JURISTIC_METHOD);
        prayerTime.setAdjustHighLats(DEFAULT_HIGH_LATITUDES);

        // init swipeAdapter
        DayViewAdapter dayViewAdapter = new DayViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(dayViewAdapter);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPerms();
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        });
    }

    private void requestPerms() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    private boolean hasPermissions() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        for (String perms : permissions) {
            int res = checkCallingOrSelfPermission(perms);
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
            timer.cancel();
            getLocation();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void searchPrayerTimes() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        int dstOffset = c.get(Calendar.DST_OFFSET) / 3600000;
        int timeZoneOffset = c.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset;
        newTimes = prayerTime.getPrayerTimes(c, latitude, longitude, timeZoneOffset);
        nextDay.set(year, month, dayOfMonth + 1);
        nextDayTimes = prayerTime.getPrayerTimes(nextDay, latitude, longitude, timeZoneOffset);
    }

    public long[] getTimerDifference() {
        // get currentTime and set format
        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        final String currentTime = simpleDateFormat.format(cal.getTime());

        // format times received from PrayTime model
        String dawnTime = newTimes.get(0) + ":00";
        String middayTime = newTimes.get(2) + ":00";
        String afternoonTime = newTimes.get(3) + ":00";
        String sunsetTime = newTimes.get(4) + ":00";
        String nightTime = newTimes.get(6) + ":00";
        String nextDawnTime = nextDayTimes.get(0) + ":00";
        try {
            // get milliseconds from parsing dates
            Date dawnDate = simpleDateFormat.parse(dawnTime);
            long dawnMillis = dawnDate.getTime();

            Date middayDate = simpleDateFormat.parse(middayTime);
            long middayMillis = middayDate.getTime();

            Date afternoonDate = simpleDateFormat.parse(afternoonTime);
            long afMillis = afternoonDate.getTime();

            Date sunsetDate = simpleDateFormat.parse(sunsetTime);
            long sunsetMillis = sunsetDate.getTime();

            Date nightDate = simpleDateFormat.parse(nightTime);
            long nightMillis = nightDate.getTime();

            Date nextDawnDate = simpleDateFormat.parse(nextDawnTime);
            long nextDawnMillis = nextDawnDate.getTime();

            //get milliseconds from parsing currentTime
            Date currentTimeDate = simpleDateFormat.parse(currentTime);
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
        for (int i = 0; i < differences.length; i++) {
            if (differences[i] < 0) {
                currentTimeIndex = i + 1;
                if (currentTimeIndex > 5) {
                    currentTimeIndex = 0;
                }
            }
        }
        return currentTimeIndex;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case KEY_PREF_CALC_METHOD:
                String calcMethod = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
                prayerTime.setCalcMethod(Integer.parseInt(calcMethod));
                break;
            case KEY_PREF_JURISTIC_METHOD:
                String juristicMethod = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
                prayerTime.setAsrJuristic(Integer.parseInt(juristicMethod));
                break;
            case KEY_PREF_HIGH_LATITUDES:
                String highLatitudes = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
                prayerTime.setAdjustHighLats(Integer.parseInt(highLatitudes));
                break;
        }
        searchPrayerTimes();
        timer.cancel();
        startNewTimer();
    }
}

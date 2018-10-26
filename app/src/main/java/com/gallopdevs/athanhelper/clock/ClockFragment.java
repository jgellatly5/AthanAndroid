package com.gallopdevs.athanhelper.clock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.utils.CalendarPrayerTimes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class ClockFragment extends Fragment {

    private static final String TAG = "ClockFragment";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private static final int NEXT_DAY_TIMES = 1;

    private CountDownTimer timer;

    private static int currentTimeIndex = 0;

    private static long difference1 = 0;
    private static long difference2 = 0;
    private static long difference3 = 0;
    private static long difference4 = 0;
    private static long difference5 = 0;
    private static long difference6 = 0;
    private static long[] differences = {difference1, difference2, difference3, difference4, difference5, difference6};

    @BindView(R.id.view_pager_fragment)
    ViewPager viewPager;
    @BindView(R.id.prayer_timer_text)
    TextView prayerTimer;
    @BindView(R.id.next_prayer_text)
    TextView nextPrayer;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.moon_icon)
    ImageView moonIcon;
    @BindView(R.id.tab_dots)
    TabLayout tabDots;

    Unbinder unbinder;

    private Boolean progressDisplayStatus = true;

    private FusedLocationProviderClient mFusedLocationClient;
    private double latitude;
    private double longitude;

    private DayViewAdapter dayViewAdapter;

    @SuppressLint("ValidFragment")
    public ClockFragment(DayViewAdapter dayViewAdapter) {
        this.dayViewAdapter = dayViewAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        getLocation();

        return view;
    }

    private void init() {
        // hide elements
        displayElements(progressDisplayStatus);

        // location listener
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // set default settings
        CalendarPrayerTimes.configureSettings();
    }

    private void displayElements(Boolean isProgressDisplayed) {
        if (isProgressDisplayed) {
            moonIcon.setVisibility(ImageView.INVISIBLE);
            prayerTimer.setVisibility(TextView.INVISIBLE);
            nextPrayer.setVisibility(TextView.INVISIBLE);
        } else {
            moonIcon.setVisibility(ImageView.VISIBLE);
            prayerTimer.setVisibility(TextView.VISIBLE);
            nextPrayer.setVisibility(TextView.VISIBLE);
        }
    }

    private void initSwipeAdapter() {
        viewPager.setAdapter(dayViewAdapter);
        tabDots.setupWithViewPager(viewPager, true);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (hasPermissions()) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                CalendarPrayerTimes.setLatitude(latitude);
                                CalendarPrayerTimes.setLongitude(longitude);

                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                progressDisplayStatus = false;
                                displayElements(progressDisplayStatus);

                                startNewTimer();
                                initSwipeAdapter();
                            } else {
                                Toast.makeText(getActivity(), "We cannot find your location. Please enable in settings.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "We cannot find your location. Please enable in settings.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onFailure: " + e.getMessage());
                        }
                    });
        } else {
            requestPerms();
        }
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
            int res = getActivity().checkCallingOrSelfPermission(perms);
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
                    Toast.makeText(getActivity(), "LocationOfPrayer permissions denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private long[] getTimerDifference() {

        ArrayList<String> newTimes = CalendarPrayerTimes.getNewTimes();
        ArrayList<String> nextDayTimes = CalendarPrayerTimes.getNextDayTimes(NEXT_DAY_TIMES);

        // get currentTime and set format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        final String currentTime = simpleDateFormat.format(Calendar.getInstance().getTime());

        // format times received from PrayTime model
        String dawnTime = newTimes.get(0) + ":00";
        String middayTime = newTimes.get(2) + ":00";
        String afternoonTime = newTimes.get(3) + ":00";
        String sunsetTime = newTimes.get(4) + ":00";
        String nightTime = newTimes.get(6) + ":00";
        String nextDawnTime = nextDayTimes.get(0) + ":00";
        try {
            // get milliseconds from parsing dates
            long dawnMillis = simpleDateFormat.parse(dawnTime).getTime();
            long middayMillis = simpleDateFormat.parse(middayTime).getTime();
            long afMillis = simpleDateFormat.parse(afternoonTime).getTime();
            long sunsetMillis = simpleDateFormat.parse(sunsetTime).getTime();
            long nightMillis = simpleDateFormat.parse(nightTime).getTime();
            long nextDawnMillis = simpleDateFormat.parse(nextDawnTime).getTime();

            //get milliseconds from parsing currentTime
            long currentTimeMilliSeconds = simpleDateFormat.parse(currentTime).getTime();

            //get intervals between times
            difference1 = dawnMillis - currentTimeMilliSeconds;
            difference2 = middayMillis - currentTimeMilliSeconds;
            difference3 = afMillis - currentTimeMilliSeconds;
            difference4 = sunsetMillis - currentTimeMilliSeconds;
            difference5 = nightMillis - currentTimeMilliSeconds;
            difference6 = nextDawnMillis - currentTimeMilliSeconds + 86400000;

            // set index of each element in differences array
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
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(getTimerDifference()[getNextTime()], 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // format for prayerTimer
                SimpleDateFormat offset = new SimpleDateFormat("HH:mm:ss", Locale.US);
                offset.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date newDateTimer = new Date();
                newDateTimer.setTime(millisUntilFinished);
                prayerTimer.setText(offset.format(newDateTimer) + "s");
            }

            @Override
            public void onFinish() {
                prayerTimer.setText("00:00:00s");

                // TODO send notification

                startNewTimer();
            }
        }.start();
    }

    public static int getNextTime() {
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

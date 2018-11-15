package com.gallopdevs.athanhelper.clock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
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
import com.gallopdevs.athanhelper.model.PrayTime;
import com.gallopdevs.athanhelper.utils.CalendarPrayerTimes;
import com.gallopdevs.athanhelper.utils.SwiperActivity;
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
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class ClockFragment extends Fragment {

    private static final String TAG = "ClockFragment";

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private static final int NEXT_DAY_TIMES = 1;
    private static final String CHANNEL_ID = "Notification";

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

//    @BindView(R.id.prayer_timer_text)
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
    private SimpleDateFormat simpleDateFormat;

    @SuppressLint("ValidFragment")
    public ClockFragment(DayViewAdapter dayViewAdapter) {
        this.dayViewAdapter = dayViewAdapter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        unbinder = ButterKnife.bind(this, view);

        prayerTimer = view.findViewById(R.id.prayer_timer_text);

        init();

        getLocation();

        Log.w(TAG, "onCreateView: creatingView");

        return view;
    }

    private void init() {
        // hide elements
        displayElements(progressDisplayStatus);

        // location listener
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // set default settings for PrayTime instance
        CalendarPrayerTimes.configureSettings();

        // set date format
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
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

                                long currentTimeMilliSeconds = CalendarPrayerTimes.getCurrentTime();
                                startNewTimer(getTimerDifference(currentTimeMilliSeconds)[getNextTime()]);
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

    public long[] getTimerDifference(long currentTime) {
        CalendarPrayerTimes.updateTimeFormat();
        ArrayList<String> newTimes = CalendarPrayerTimes.getNewTimes();
        ArrayList<String> nextDayTimes = CalendarPrayerTimes.getNextDayTimes(NEXT_DAY_TIMES);

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

            //get intervals between times
            difference1 = dawnMillis - currentTime;
            difference2 = middayMillis - currentTime;
            difference3 = afMillis - currentTime;
            difference4 = sunsetMillis - currentTime;
            difference5 = nightMillis - currentTime;
            difference6 = nextDawnMillis - currentTime + 86400000;

            // set index of each element in differences array
            differences[0] = difference1;
            differences[1] = difference2;
            differences[2] = difference3;
            differences[3] = difference4;
            differences[4] = difference5;
            differences[5] = difference6;
            return differences;
        } catch (ParseException e) {
            Log.e(TAG, "getTimerDifference: cannot parse the dates: " + e.getMessage());
        }
        differences[0] = difference1;
        differences[1] = difference2;
        differences[2] = difference3;
        differences[3] = difference4;
        differences[4] = difference5;
        differences[5] = difference6;
        return differences;
    }

    public void startNewTimer(final long countDownTime) {
        Log.d(TAG, "in startNewTimer");
        if (timer != null) {
            timer.cancel();
            Log.e(TAG, "startNewTimer: canceled timer");
        }
        Log.e(TAG, "startNewTimer: countDownTime: " + countDownTime);
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat offset = new SimpleDateFormat("HH:mm:ss", Locale.US);
                offset.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date newDateTimer = new Date();
                newDateTimer.setTime(millisUntilFinished);
                prayerTimer.setText(offset.format(newDateTimer) + "s");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFinish() {
                prayerTimer.setText("00:00:00s");

//                getActivity().getSupportFragmentManager().beginTransaction().remove(ClockFragment.this).commit();

                SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
                boolean enableNotifications = sharedPref.getBoolean("enableNotifications", false);
                if (enableNotifications) createNotification();
                long currentTimeMilliSeconds = CalendarPrayerTimes.getCurrentTime();
                long[] getTimeDifference = getTimerDifference(currentTimeMilliSeconds);
                long newCountDownTime = getTimeDifference[getNextTime()];
                startNewTimer(newCountDownTime);
            }
        }.start();
    }

    private void createNotification() {
        Intent intent = new Intent(getActivity(), SwiperActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.drawable.moon)
                .setContentTitle("Athan")
                .setContentText("Next prayer time.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
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
        Log.w(TAG, "onDestroyView: destroying view");
    }
}

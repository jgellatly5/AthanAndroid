package com.excursion.athanhelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TextView prayerTimer;
    String myTime = "";
    String dawnTime = "";
    String middayTime = "";
    String afternoonTime = "";
    String sunsetTime = "";
    String nightTime = "";
    int currentTimeIndex = 0;
    ArrayList<String> newTimes = new ArrayList<>();
    long difference1 = 0;
    long difference2 = 0;
    long difference3 = 0;
    long difference4 = 0;
    long difference5 = 0;
    long[] differences = {difference1, difference2, difference3, difference4, difference5};
    long difference = 0;

    SimpleDateFormat offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrayTime prayerTime = new PrayTime();
        ArrayList<String> names = new ArrayList<>();
        names = prayerTime.getTimeNames();
        Log.i("prayer names", String.valueOf(names));

        Calendar c = Calendar.getInstance();
        newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, -7);
        Log.i("prayer times", String.valueOf(newTimes));

        customizeActionBar();
        setupSwipe();
        //getTimerDifference();
        startNewTimer();
    }

    public long[] getTimerDifference() {
        // get currentTime and set format
        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        final String currentTime = simpleDateFormat.format(cal.getTime());

        // instantiate dates
        Date targetDate = null;
        Date dawnDate = null;
        Date middayDate = null;
        Date afternoonDate = null;
        Date sunsetDate = null;
        Date nightDate = null;
        Date currentTimeDate = null;

        // format times received from PrayTime model
        dawnTime = newTimes.get(1)+ ":00";
        middayTime = newTimes.get(2) + ":00";
        afternoonTime = newTimes.get(3) + ":00";
        sunsetTime = newTimes.get(4) + ":00";
        nightTime = newTimes.get(5) + ":00";
        myTime = getNextTime();
        try {
            //get milliseconds from targetTime
            targetDate = simpleDateFormat.parse(myTime);
            long milliSeconds = targetDate.getTime();
            String formattedTime = simpleDateFormat.format(targetDate);
            Log.i("newTime", formattedTime);
            Log.i("newTimeMilliSeconds", String.valueOf(milliSeconds));

            dawnDate = simpleDateFormat.parse(dawnTime);
            long dawnMillis = dawnDate.getTime();
            String formatDawn = simpleDateFormat.format(dawnDate);
            Log.i("dawnTime", formatDawn);
            Log.i("dawnTimeMilliSeconds", String.valueOf(dawnMillis));

            middayDate = simpleDateFormat.parse(middayTime);
            long middayMillis = middayDate.getTime();
            String formatMidday = simpleDateFormat.format(dawnDate);
            Log.i("middayTime", formatMidday);
            Log.i("middayTimeMilliSeconds", String.valueOf(middayMillis));

            afternoonDate = simpleDateFormat.parse(afternoonTime);
            long afMillis = afternoonDate.getTime();
            String formatAf = simpleDateFormat.format(afternoonDate);
            Log.i("afTime", formatAf);
            Log.i("afTimeMilliSeconds", String.valueOf(afMillis));

            sunsetDate = simpleDateFormat.parse(sunsetTime);
            long sunsetMillis = sunsetDate.getTime();
            String formatSunset = simpleDateFormat.format(sunsetDate);
            Log.i("sunsetTime", formatSunset);
            Log.i("sunsetTimeMilliSeconds", String.valueOf(sunsetMillis));

            nightDate = simpleDateFormat.parse(nightTime);
            long nightMillis = nightDate.getTime();
            String formatNight = simpleDateFormat.format(nightDate);
            Log.i("nightTime", formatNight);
            Log.i("nightTimeMilliSeconds", String.valueOf(nightMillis));

            //get milliseconds from currentTime
            currentTimeDate = simpleDateFormat.parse(currentTime);
            long currentTimeMilliSeconds = currentTimeDate.getTime();
            String formattedCurrent = simpleDateFormat.format(currentTimeDate);
            Log.i("currentTime", formattedCurrent);
            Log.i("cuurentTimeInMillis", String.valueOf(currentTimeMilliSeconds));

            //get milliseconds from difference
            difference = milliSeconds - currentTimeMilliSeconds;
            if (difference < 0) {
                difference = Math.abs(difference);
            }
            Log.i("differenceTimeInMillis", String.valueOf(difference));

            difference1 = dawnMillis - currentTimeMilliSeconds;
            Log.i("difference1TimeInMillis", String.valueOf(difference1));

            difference2 = middayMillis - currentTimeMilliSeconds;
            Log.i("difference2TimeInMillis", String.valueOf(difference2));

            difference3 = afMillis - currentTimeMilliSeconds;
            Log.i("difference3TimeInMillis", String.valueOf(difference3));

            difference4 = sunsetMillis - currentTimeMilliSeconds;
            Log.i("difference4TimeInMillis", String.valueOf(difference4));

            difference5 = nightMillis - currentTimeMilliSeconds;
            Log.i("difference5TimeInMillis", String.valueOf(difference5));

            // format for prayerTimer
            offset = new SimpleDateFormat("HH:mm:ss", Locale.US);
            offset.setTimeZone(TimeZone.getTimeZone("GMT"));

//            return difference;
            differences[0] = difference1;
            differences[1] = difference2;
            differences[2] = difference3;
            differences[3] = difference4;
            differences[4] = difference5;
            getNextTime();
            return differences;
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        return difference;
        getNextTime();
        differences[0] = difference1;
        differences[1] = difference2;
        differences[2] = difference3;
        differences[3] = difference4;
        differences[4] = difference5;
        return differences;
    }

    private void startNewTimer() {
        CountDownTimer timer = new CountDownTimer(getTimerDifference()[2], 1000) {
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
                Toast.makeText(MainActivity.this, "New prayer starting", Toast.LENGTH_SHORT).show();
                myTime = getNextTime();
                Log.i("myTime", myTime);
                startNewTimer();
            }
        }.start();
    }

    private String getNextTime() {
        Log.i("difference", String.valueOf(difference));
        Log.i("difference1", String.valueOf(differences[0]));
        Log.i("difference2", String.valueOf(differences[1]));
        Log.i("difference3", String.valueOf(differences[2]));

        for(int i = 1; i < differences.length; i++) {
            if(differences[i] < 0) {
                currentTimeIndex = i + 1;
                Log.i("currentTimeIndex", String.valueOf(currentTimeIndex));
            }
        }
        if (currentTimeIndex >= newTimes.size()) {
            currentTimeIndex = 0;
        }
        Log.i("currentTimeIndex", String.valueOf(currentTimeIndex));
        myTime = newTimes.get(currentTimeIndex) + ":00";
        return  myTime;
    }

    private void setupSwipe() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);
    }

    private void customizeActionBar() {
        //Customize the ActionBar
        final ActionBar abar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("Athan");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

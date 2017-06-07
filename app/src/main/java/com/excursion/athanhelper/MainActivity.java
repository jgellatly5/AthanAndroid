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
    int currentTimeIndex = -1;
    ArrayList<String> times = new ArrayList<>();
    long difference = 0;
    SimpleDateFormat offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        times.add("21:13:00");
        times.add("05:45:00");
        times.add("12:20:00");
        times.add("15:37:00");
        times.add("18:07:00");
        times.add("19:07:00");

        customizeActionBar();
        setupSwipe();
        //start new timer
        getTimerDifference();
        startNewTimer();
        //getNextTime();
    }

    public long getTimerDifference() {
        // get currentTime and set format
        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        final String currentTime = simpleDateFormat.format(cal.getTime());

        // instantiate dates
        Date targetDate = null;
        Date currentTimeDate = null;
        Date timerValue = new Date();

        //myTime = getNextTime();
        myTime = "21:17:00";
        //long difference = 0;
        try {
            //get milliseconds from targetTime
            targetDate = simpleDateFormat.parse(myTime);
            long milliSeconds = targetDate.getTime();
            String formattedTime = simpleDateFormat.format(targetDate);
            Log.i("newTime", formattedTime);
            Log.i("newTimeMilliSeconds", String.valueOf(milliSeconds));

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

            //set Timer to show difference in timer
            Calendar diff = Calendar.getInstance();
            diff.setTimeInMillis(difference);
            String diffTime = simpleDateFormat.format(diff.getTime());
            Log.i("diffTime", String.valueOf(diff));

            // check to see if legit
            timerValue.setTime(difference);
            offset = new SimpleDateFormat("HH:mm:ss", Locale.US);
            offset.setTimeZone(TimeZone.getTimeZone("GMT"));
            String timerValueString = offset.format(timerValue);
            Log.i("timerValue", timerValueString);

//            new CountDownTimer(difference, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    Date newDateTimer = new Date();
//                    newDateTimer.setTime(millisUntilFinished);
//                    prayerTimer = (TextView) findViewById(R.id.prayerTimer);
//                    prayerTimer.setText(offset.format(newDateTimer) + "s");
//                }
//
//                @Override
//                public void onFinish() {
//                    prayerTimer.setText("00:00:00s");
//                    Toast.makeText(MainActivity.this, "New Prayer starting", Toast.LENGTH_SHORT).show();
//                    myTime = getNextTime();
//                }
//            }.start();
            return difference;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return difference;
    }

    private void startNewTimer() {
        CountDownTimer timer = new CountDownTimer(difference, 1000) {
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
        if (difference > 0) {
            currentTimeIndex++;
        }
        if (currentTimeIndex >= times.size()) {
            currentTimeIndex = 0;
        }
        Log.i("current item", times.get(currentTimeIndex));
        return times.get(currentTimeIndex);
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

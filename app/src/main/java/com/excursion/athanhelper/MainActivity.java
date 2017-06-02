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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TextView prayerTimer;
    Date targetDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customizeActionBar();

        setupSwipe();

        prayerTimer = (TextView) findViewById(R.id.prayerTimer);

        Calendar cal = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        final String currentTime = simpleDateFormat.format(cal.getTime());

        targetDate = new Date();
        String myTime = "02:20:54";
        try {
            //get milliseconds from targetTime
            targetDate = simpleDateFormat.parse(myTime);
            long milliSeconds = targetDate.getTime();
            String formattedTime = simpleDateFormat.format(targetDate);
            Log.i("newTime", formattedTime);

            //get milliseconds from currentTime
            Date currentTimeDate = simpleDateFormat.parse(currentTime);
            long currentTimeMilliSeconds = currentTimeDate.getTime();

            //get milliseconds from difference
            long difference = milliSeconds - currentTimeMilliSeconds;
            Log.i("differeneInMillis", String.valueOf(difference));

            //set Timer to show difference in timer
            Date timerValue = new Date();
            timerValue.setTime(difference);
            String timerValueString = simpleDateFormat.format(timerValue);
            Log.i("TimerValue", timerValueString);

            new CountDownTimer(difference, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Date newDateTimer = new Date();
                    newDateTimer.setTime(millisUntilFinished);
                    prayerTimer.setText(simpleDateFormat.format(newDateTimer) + "s");
                }

                @Override
                public void onFinish() {
                    prayerTimer.setText("00:00:00s");
                }
            }.start();

        } catch (ParseException e) {
            e.printStackTrace();
        }
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

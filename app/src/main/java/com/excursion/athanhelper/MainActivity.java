package com.excursion.athanhelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends FragmentActivity {
    ViewPager viewPager;

    TextView dawnTimeTextView;
    TextView middayTimeTextView;
    TextView afternoonTimeTextView;
    TextView sunsetTimeTextView;
    TextView nightTimeTextView;
    TextView prayerTimer;
    SimpleDateFormat sdf;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);


        dawnTimeTextView = (TextView) findViewById(R.id.dawnTimeTextView);
        middayTimeTextView = (TextView) findViewById(R.id.middayTimeTextView);
        afternoonTimeTextView = (TextView) findViewById(R.id.afternoonTimeTextView);
        sunsetTimeTextView = (TextView) findViewById(R.id.sunsetTimeTextView);
        nightTimeTextView = (TextView) findViewById(R.id.nightTimeTextView);
        prayerTimer = (TextView) findViewById(R.id.prayerTimer);
        sdf = new SimpleDateFormat("hh:mm:ss");
        date = new Date();

        new CountDownTimer(50000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                date.setTime(millisUntilFinished);
                //String timerValue = sdf.format(String.valueOf(millisUntilFinished / 1000) + "s");
                //prayerTimer.setText(timerValue);
                prayerTimer.setText(sdf.format(date) + "s");
            }

            @Override
            public void onFinish() {
                prayerTimer.setText("00:00:00s");
            }
        }.start();
    }
}

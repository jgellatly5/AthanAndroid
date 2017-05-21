package com.excursion.athanhelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView dawnTimeTextView;
    TextView middayTimeTextView;
    TextView afternoonTimeTextView;
    TextView sunsetTimeTextView;
    TextView nightTimeTextView;
    TextView prayerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dawnTimeTextView = (TextView) findViewById(R.id.dawnTimeTextView);
        middayTimeTextView = (TextView) findViewById(R.id.middayTimeTextView);
        afternoonTimeTextView = (TextView) findViewById(R.id.afternoonTimeTextView);
        sunsetTimeTextView = (TextView) findViewById(R.id.sunsetTimeTextView);
        nightTimeTextView = (TextView) findViewById(R.id.nightTimeTextView);
        prayerTimer = (TextView) findViewById(R.id.prayerTimer);

        new CountDownTimer(50000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                prayerTimer.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                prayerTimer.setText("00:00:00s");
            }
        }.start();
    }
}

package com.excursion.athanhelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
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

//        String title = "Athan";
//        SpannableString s = new SpannableString(title);
//        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        getSupportActionBar().setTitle(s);

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

        final Calendar c = Calendar.getInstance();

        new CountDownTimer(50000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                date.setTime(millisUntilFinished);
                //String timerValue = sdf.format(String.valueOf(millisUntilFinished / 1000) + "s");
                //prayerTimer.setText(timerValue);
                prayerTimer.setText(sdf.format(date) + "s");
                int seconds = c.get(Calendar.SECOND);
                //Log.i("give seconds", String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                prayerTimer.setText("00:00:00s");
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

package com.gallopdevs.athanhelper.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.clock.ClockFragment;
import com.gallopdevs.athanhelper.clock.DayViewAdapter;
import com.gallopdevs.athanhelper.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwiperActivity extends AppCompatActivity {

    private static final String TAG = "SwiperActivity";
    private static final String CHANNEL_ID = "Notification";

    @BindView(R.id.view_pager_activity)
    ViewPager viewPager;
    @BindView(R.id.tab_layout_activity)
    TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);
        ButterKnife.bind(this);

        final DayViewAdapter dayViewAdapter = new DayViewAdapter(getSupportFragmentManager());
        final SettingsPagerAdapter settingsPagerAdapter = new SettingsPagerAdapter(getSupportFragmentManager());

        final ClockFragment clockFragment = new ClockFragment(dayViewAdapter);

        settingsPagerAdapter.addFrag(clockFragment);
        settingsPagerAdapter.addFrag(new SettingsFragment());
        viewPager.setAdapter(settingsPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setIcon(R.drawable.clock_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.settings_icon);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    long currentTimeMilliSeconds = CalendarPrayerTimes.getCurrentTime();
                    long[] getTimeDifference = clockFragment.getTimerDifference(currentTimeMilliSeconds);
                    long countDownTime = getTimeDifference[ClockFragment.getNextTime()];
                    dayViewAdapter.notifyDataSetChanged();
                    clockFragment.startNewTimer(countDownTime);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

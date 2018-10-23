package com.gallopdevs.athanhelper.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.clock.ClockFragment;
import com.gallopdevs.athanhelper.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwiperActivity extends AppCompatActivity {

    private static final String TAG = "SwiperActivity";

    @BindView(R.id.view_pager_activity)
    ViewPager viewPager;
    @BindView(R.id.tab_layout_activity)
    TabLayout tabLayout;

    private static final String KEY_PREF_CALC_METHOD = "calculation_method";
    private static final String KEY_PREF_JURISTIC_METHOD = "juristic_method";
    private static final String KEY_PREF_HIGH_LATITUDES = "high_latitudes";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            switch (key) {
                case KEY_PREF_CALC_METHOD:
                    Log.d(TAG, "onSharedPreferenceChanged: changing calc method");
                    String calcMethod = sharedPreferences.getString(KEY_PREF_CALC_METHOD, "");
                    CalendarPrayerTimes.updateCalcMethod(Integer.parseInt(calcMethod));
                    break;
                case KEY_PREF_JURISTIC_METHOD:
                    Log.d(TAG, "onSharedPreferenceChanged: changing juristic method");
                    String juristicMethod = sharedPreferences.getString(KEY_PREF_JURISTIC_METHOD, "");
                    CalendarPrayerTimes.updateAsrJuristic(Integer.parseInt(juristicMethod));
                    break;
                case KEY_PREF_HIGH_LATITUDES:
                    Log.d(TAG, "onSharedPreferenceChanged: changing high lats");
                    String highLatitudes = sharedPreferences.getString(KEY_PREF_HIGH_LATITUDES, "");
                    CalendarPrayerTimes.updateHighLats(Integer.parseInt(highLatitudes));
                    break;
            }
            Toast.makeText(SwiperActivity.this, "Shared Pref changing", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);
        ButterKnife.bind(this);

        SettingsPagerAdapter adapter = new SettingsPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ClockFragment());
        adapter.addFrag(new SettingsFragment());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setIcon(R.drawable.clock_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.settings_icon);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}

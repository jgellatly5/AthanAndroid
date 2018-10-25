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
import com.gallopdevs.athanhelper.settings.CustomELVAdapter;
import com.gallopdevs.athanhelper.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwiperActivity extends AppCompatActivity {

    private static final String TAG = "SwiperActivity";

    @BindView(R.id.view_pager_activity)
    ViewPager viewPager;
    @BindView(R.id.tab_layout_activity)
    TabLayout tabLayout;

    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);
        ButterKnife.bind(this);

        settingsFragment = new SettingsFragment();
        final SettingsPagerAdapter settingsPagerAdapter = new SettingsPagerAdapter(getSupportFragmentManager());
        settingsPagerAdapter.addFrag(new ClockFragment());
        settingsPagerAdapter.addFrag(settingsFragment);
        viewPager.setAdapter(settingsPagerAdapter);

        settingsFragment.setSettingsChangedListener(new SettingsFragment.SettingsListener() {
            @Override
            public void onSettingsChanged() {
                Toast.makeText(SwiperActivity.this, "Adding new fragment", Toast.LENGTH_SHORT).show();
//                settingsPagerAdapter.addFrag(new ClockFragment());
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setIcon(R.drawable.clock_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.settings_icon);
    }
}

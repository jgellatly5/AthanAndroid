package com.gallopdevs.athanhelper.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gallopdevs.athanhelper.R;
import com.gallopdevs.athanhelper.clock.ClockFragment;
import com.gallopdevs.athanhelper.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwiperActivity extends AppCompatActivity {

    @BindView(R.id.view_pager_activity)
    ViewPager viewPager;
    @BindView(R.id.tab_layout_activity)
    TabLayout tabLayout;

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
    }
}

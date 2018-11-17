package com.gallopdevs.athanhelper.clock;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;

/**
 * Created by jgell on 5/23/2017.
 */

public class DayViewAdapter extends FragmentPagerAdapter {

    private static final String TAG = "DayViewAdapter";

    private static final int NUM_ITEMS = 7;

    public DayViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DayViewFragment();
        Bundle bundle = new Bundle();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        int indicator = ClockFragment.getNextTime();
        bundle.putInt("day", day + i);
        bundle.putInt("count", i);
        bundle.putInt("indicator", indicator);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}

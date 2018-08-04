package com.gallopdevs.athanhelper.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

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
        bundle.putInt("day", day + i);
        bundle.putInt("count", i);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}

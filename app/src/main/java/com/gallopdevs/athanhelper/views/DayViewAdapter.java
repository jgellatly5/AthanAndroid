package com.gallopdevs.athanhelper.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;

/**
 * Created by jgell on 5/23/2017.
 */

public class DayViewAdapter extends FragmentPagerAdapter {

    public DayViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DayViewFragment();
        Bundle bundle = new Bundle();

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        bundle.putInt("day", day + i);
        bundle.putInt("count", i);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }
}

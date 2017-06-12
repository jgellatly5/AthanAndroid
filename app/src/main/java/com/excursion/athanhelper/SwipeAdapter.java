package com.excursion.athanhelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jgell on 5/23/2017.
 */

public class SwipeAdapter extends FragmentPagerAdapter {
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PageFragment();
        Bundle bundle = new Bundle();

        // TODO fix this code
        PrayTime prayerTime = new PrayTime();

        ArrayList<Calendar> daysOfTheWeek = new ArrayList<>();
        for (i = 0; i < getCount(); i++) {
            daysOfTheWeek.add(Calendar.getInstance());
        }

        Calendar c = Calendar.getInstance();
        int month = c.MONTH;
        int dayOfMonth = c.DAY_OF_MONTH;
        int year = c.YEAR;
        for (Calendar day : daysOfTheWeek) {
            day.set(year, month, dayOfMonth + i);
            ArrayList<String> nextDayTimes = new ArrayList<>();
            nextDayTimes =  prayerTime.getPrayerTimes(day, 32.8, -117.2, -7);
            Log.i("nextDayTimes", String.valueOf(nextDayTimes));
            bundle.putStringArrayList("prayerTimes", nextDayTimes);
            i++;
        }

        ArrayList<String> newTimes = new ArrayList<>();
        newTimes = prayerTime.getPrayerTimes(c, 32.8, -117.2, -7);

        // new code ends here

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        bundle.putInt("day", day + i);
        bundle.putInt("count", i + 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }
}

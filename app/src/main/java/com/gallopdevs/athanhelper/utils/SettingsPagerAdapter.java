package com.gallopdevs.athanhelper.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

    private static final String TAG = "SettingsPagerAdapter";

    private final List<Fragment> fragmentList = new ArrayList<>();

    public SettingsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFrag(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: " + String.valueOf(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

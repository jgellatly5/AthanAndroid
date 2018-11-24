package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter

import java.util.Calendar

/**
 * Created by jgell on 5/23/2017.
 */

class DayViewAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val TAG = "DayViewAdapter"
    private val NUM_ITEMS = 7

    override fun getItem(i: Int): Fragment {
        val fragment = DayViewFragment()
        val bundle = Bundle()
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_WEEK)
        val indicator = ClockFragment.nextTime
        bundle.putInt("day", day + i)
        bundle.putInt("count", i)
        bundle.putInt("indicator", indicator)
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return NUM_ITEMS
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}

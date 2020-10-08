package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

import java.util.Calendar

/**
 * Created by jgell on 5/23/2017.
 */

class DayViewAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private val TAG = "DayViewAdapter"
    private val NUM_ITEMS = 7

    override fun getItem(i: Int): androidx.fragment.app.Fragment {
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
        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
    }
}

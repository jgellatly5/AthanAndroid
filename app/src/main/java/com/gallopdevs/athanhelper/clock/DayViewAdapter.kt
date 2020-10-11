package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.*

/**
 * Created by jgell on 5/23/2017.
 */

class DayViewAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val NUM_ITEMS = 7

    override fun createFragment(i: Int): Fragment {
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

    override fun getItemCount(): Int {
        return NUM_ITEMS
    }
}

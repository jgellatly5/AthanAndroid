package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gallopdevs.athanhelper.model.PrayTime
import java.util.*

/**
 * Created by jgell on 5/23/2017.
 */

class DayViewAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val NUM_ITEMS = 7

    override fun createFragment(i: Int): Fragment {
        val fragment = DayViewFragment()

        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_WEEK)
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1
        val indicator = PrayTime.nextTime

        val bundle = Bundle().apply {
            putInt("count", i)
            putInt("day", day + i)
            putInt("dayOfMonth", dayOfMonth + i)
            putInt("month", month)
            putInt("indicator", indicator)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int = NUM_ITEMS
}

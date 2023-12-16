package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.Calendar

class DayViewAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(i: Int): Fragment {
        val fragment = DayViewFragment()

        val c = Calendar.getInstance()
        val weekDay = c.get(Calendar.DAY_OF_WEEK)
        val month = c.get(Calendar.MONTH) + 1
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)

        val bundle = Bundle().apply {
            putInt(WEEK_DAY, weekDay + i)
            putInt(MONTH, month)
            putInt(DAY_OF_MONTH, dayOfMonth + i)
            putInt(PAGE_INDEX, i)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int = NUM_ITEMS

    companion object {
        private const val NUM_ITEMS = 7
        const val PAGE_INDEX = "PAGE_INDEX"
        const val WEEK_DAY = "WEEK_DAY"
        const val DAY_OF_MONTH = "DAY_OF_MONTH"
        const val MONTH = "MONTH"
    }
}

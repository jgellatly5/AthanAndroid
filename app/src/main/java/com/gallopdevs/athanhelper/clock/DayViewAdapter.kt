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
        val day = c.get(Calendar.DAY_OF_WEEK)
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1

        val bundle = Bundle().apply {
            putInt(PAGE_INDEX, i)
            putInt(DAY, day + i)
            putInt(DAY_OF_MONTH, dayOfMonth + i)
            putInt(MONTH, month)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int = NUM_ITEMS

    companion object {
        private const val NUM_ITEMS = 7
        const val PAGE_INDEX = "PAGE_INDEX"
        const val DAY = "DAY"
        const val DAY_OF_MONTH = "DAY_OF_MONTH"
        const val MONTH = "MONTH"
    }
}

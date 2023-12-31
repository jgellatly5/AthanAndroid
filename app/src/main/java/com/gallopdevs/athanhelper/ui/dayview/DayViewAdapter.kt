package com.gallopdevs.athanhelper.ui.dayview

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DayViewAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(i: Int): Fragment {
        val fragment = DayViewFragment()

        val bundle = Bundle().apply {
            putInt(PAGE_INDEX, i)
        }

        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int = NUM_ITEMS

    companion object {
        private const val NUM_ITEMS = 7
        const val PAGE_INDEX = "PAGE_INDEX"
    }
}

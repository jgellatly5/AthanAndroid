package com.gallopdevs.athanhelper.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class SettingsPagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private val TAG = "SettingsPagerAdapter"
    private val fragmentList = ArrayList<androidx.fragment.app.Fragment>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFrag(fragment: androidx.fragment.app.Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemPosition(`object`: Any): Int {
        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
    }
}

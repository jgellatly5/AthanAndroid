package com.gallopdevs.athanhelper.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*

class SettingsPagerAdapter(fm: FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private val TAG = "SettingsPagerAdapter"

    private val fragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFrag(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}

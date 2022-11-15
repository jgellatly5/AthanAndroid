package com.gallopdevs.athanhelper.model

import android.system.StructUtsname
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*

class PrayTimeTest {

    @Before
    fun setup() {
        val SUT = PrayTime.instance
    }

    @Test
    fun pray_time() {
//        val latitude = -37.823689
//        val longitude = 145.121597
//        val timezone = 10.0
//        // Test Prayer times here
//        val prayers = PrayTime.instance
//        prayers?.timeFormat = prayers!!.time12
//        prayers?.calcMethod = prayers.jafari
//        prayers?.asrJuristic = prayers.shafii
//        prayers?.adjustHighLats = prayers.angleBased
//        val offsets = intArrayOf(0, 0, 0, 0, 0, 0, 0) // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
//        prayers?.tune(offsets)
//        val now = Date()
//        val cal = Calendar.getInstance()
//        cal.time = now
//        val prayerTimes = prayers?.getPrayerTimes(cal,
//                latitude, longitude, timezone)
//        val prayerNames = prayers?.timeNames
//        for (i in prayerTimes?.indices!!) {
//            println(prayerNames!![i] + " - " + prayerTimes[i])
//        }
    }
}
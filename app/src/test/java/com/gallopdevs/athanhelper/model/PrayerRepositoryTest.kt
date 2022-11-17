package com.gallopdevs.athanhelper.model

import org.junit.Test

import org.junit.Assert.*
import org.mockito.kotlin.mock
import java.util.*

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val mockPrayerTime: PrayerTime = mock()

    @Test
    fun get_date_prayer_times_successful() {
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
        val pageIndex = 0
        testObject.getDatePrayerTimes(pageIndex)
    }
}
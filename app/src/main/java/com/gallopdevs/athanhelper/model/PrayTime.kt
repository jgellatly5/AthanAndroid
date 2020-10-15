package com.gallopdevs.athanhelper.model

import com.gallopdevs.athanhelper.model.utils.computeDayTimes
import com.gallopdevs.athanhelper.model.utils.julianDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by jgell on 6/8/2017.
 */
//--------------------- Copyright Block ----------------------
/*

PrayTime.java: Prayer Times Calculator (ver 1.0)
Copyright (C) 2007-2010 PrayTimes.org

Java Code By: Hussain Ali Khan
Original JS Code By: Hamid Zarrabi-Zadeh

License: GNU LGPL v3.0

TERMS OF USE:
	Permission is granted to use this code, with or
	without modification, in any website or application
	provided that credit is given to the original work
	with a link back to PrayTimes.org.

This program is distributed in the hope that it will
be useful, but WITHOUT ANY WARRANTY.

PLEASE DO NOT REMOVE THIS COPYRIGHT BLOCK.

*/
object PrayTime {

    val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val year = calendar.get(Calendar.YEAR)
    val dstOffset = calendar.get(Calendar.DST_OFFSET) / 3600000
    val timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset

    var lat = 0.0
    var lng = 0.0
    var timeZone = 0.0
    var jDate = 0.0

    // Initialize vars
    var calcMethod = 0
    var asrJuristic = 0
    var dhuhrMinutes = 0
    var adjustHighLats = 1
    var timeFormat = 1

    // Juristic Methods
    val shafii = 0 // Shafii (standard)
    val hanafi = 1 // Hanafi

    // Calculation Methods
    const val jafari = 0 // Ithna Ashari
    const val karachi = 1 // University of Islamic Sciences, Karachi
    const val iSNA = 2 // Islamic Society of North America (ISNA)
    const val mWL = 3 // Muslim World League (MWL)
    const val makkah = 4 // Umm al-Qura, Makkah
    const val egypt = 5 // Egyptian General Authority of Survey
    const val tehran = 6 // Institute of Geophysics, University of Tehran
    var custom = 7 // Custom Setting

    // Adjusting Methods for Higher Latitudes
    const val none = 0
    const val midNight = 1 // middle of night
    const val oneSeventh = 2 // 1/7th of night
    const val angleBased = 3 // angle/60th of night

    // Time Formats
    const val time24 = 0 // 24-hour format
    const val time12 = 1 // 12-hour format
    const val time12NS = 2 // 12-hour format with no suffix
    const val floating = 3 // floating point number

    // Tuning offsets {fajr, sunrise, dhuhr, asr, sunset, maghrib, isha}
    val offsets = IntArray(7) { 0 }

    /*
     *
     * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
     * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
     * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter
     * value (in angle or minutes)
     */
    val methodParams: HashMap<Int, DoubleArray> = hashMapOf(
            jafari to doubleArrayOf(16.0, 0.0, 4.0, 0.0, 14.0),
            karachi to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0),
            iSNA to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0),
            mWL to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0),
            makkah to doubleArrayOf(18.5, 1.0, 0.0, 1.0, 90.0),
            egypt to doubleArrayOf(19.5, 1.0, 0.0, 0.0, 17.5),
            tehran to doubleArrayOf(17.7, 0.0, 4.5, 0.0, 14.0),
            custom to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0)
    )

    val currentTime: Long
        get() {
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            val currentTime = simpleDateFormat.format(Calendar.getInstance().time)
            var currentTimeMilliSeconds: Long = 0
            try {
                currentTimeMilliSeconds = simpleDateFormat.parse(currentTime).time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return currentTimeMilliSeconds
        }

    // -------------------- Interface Functions --------------------
    // return prayer times for a given date
    fun getPrayerTimes(
            date: Calendar,
            latitude: Double,
            longitude: Double,
            tZone: Double
    ): ArrayList<String> {
        val year = date[Calendar.YEAR]
        val month = date[Calendar.MONTH]
        val day = date[Calendar.DATE]
        return getDatePrayerTimes(year, month + 1, day, latitude, longitude, tZone)
    }

    // return prayer times for a given date
    fun getDatePrayerTimes(
            year: Int,
            month: Int,
            day: Int,
            latitude: Double,
            longitude: Double,
            tZone: Double
    ): ArrayList<String> {
        lat = latitude
        lng = longitude
        timeZone = tZone
        jDate = julianDate(year, month, day)
        val lonDiff = longitude / (15.0 * 24.0)
        jDate -= lonDiff
        return computeDayTimes()
    }

    private var difference1: Long = 0
    private var difference2: Long = 0
    private var difference3: Long = 0
    private var difference4: Long = 0
    private var difference5: Long = 0
    private var difference6: Long = 0
    private val differences = LongArray(6) { 0 }

    private var currentTimeIndex = 0
    val nextTime: Int
        get() {
            for (i in differences.indices) {
                if (differences[i] < 0) {
                    currentTimeIndex = i + 1
                    if (currentTimeIndex > 5) {
                        currentTimeIndex = 0
                    }
                }
            }
            return currentTimeIndex
        }

    fun getTimerDifference(currentTime: Long): LongArray {
        val newTimes = getDatePrayerTimes(
                PrayTime.year,
                PrayTime.month + 1,
                PrayTime.dayOfMonth,
                PrayTime.lat,
                PrayTime.lng,
                PrayTime.timeZoneOffset.toDouble()
        )
        val nextDayTimes = getDatePrayerTimes(
                PrayTime.year,
                PrayTime.month + 1,
                PrayTime.dayOfMonth + 1,
                PrayTime.lat,
                PrayTime.lng,
                PrayTime.timeZoneOffset.toDouble()
        )

        // format times received from PrayTime model
        val dawnTime = newTimes[0] + ":00"
        val middayTime = newTimes[2] + ":00"
        val afternoonTime = newTimes[3] + ":00"
        val sunsetTime = newTimes[5] + ":00"
        val nightTime = newTimes[6] + ":00"
        val nextDawnTime = nextDayTimes[0] + ":00"
        try {
            // get milliseconds from parsing dates
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            val dawnMillis = simpleDateFormat.parse(dawnTime).time
            val middayMillis = simpleDateFormat.parse(middayTime).time
            val afMillis = simpleDateFormat.parse(afternoonTime).time
            val sunsetMillis = simpleDateFormat.parse(sunsetTime).time
            val nightMillis = simpleDateFormat.parse(nightTime).time
            val nextDawnMillis = simpleDateFormat.parse(nextDawnTime).time

            //get intervals between times
            difference1 = dawnMillis - currentTime
            difference2 = middayMillis - currentTime
            difference3 = afMillis - currentTime
            difference4 = sunsetMillis - currentTime
            difference5 = nightMillis - currentTime
            difference6 = nextDawnMillis - currentTime + 86400000

            // set index of each element in differences array
            differences[0] = difference1
            differences[1] = difference2
            differences[2] = difference3
            differences[3] = difference4
            differences[4] = difference5
            differences[5] = difference6
            return differences
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        differences[0] = difference1
        differences[1] = difference2
        differences[2] = difference3
        differences[3] = difference4
        differences[4] = difference5
        differences[5] = difference6
        return differences
    }
}
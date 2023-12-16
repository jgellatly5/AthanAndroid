package com.gallopdevs.athanhelper.model

import com.gallopdevs.athanhelper.model.utils.computeDayTimes
import com.gallopdevs.athanhelper.model.utils.julianDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

object PrayerCalculatorIpml : PrayerCalculator {

    private val calendar = Calendar.getInstance()
    private val dstOffset = calendar.get(Calendar.DST_OFFSET) / 3600000
    private val timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset

    var lat = 0.0
    var lng = 0.0
    var timeZone = timeZoneOffset
    var jDate = 0.0

    // Initialize vars
    var calcMethod = 0
    var asrJuristic = 0
    var dhuhrMinutes = 0
    var adjustHighLats = 1
    var timeFormat = 0

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

    // return prayer times for a given date
    override fun getPrayerTimesForDate(offset: Int): ArrayList<String> {
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        jDate = julianDate(year, month, dayOfMonth + offset)
        val lonDiff = lng / (15.0 * 24.0)
        jDate -= lonDiff
        return computeDayTimes()
    }

    override fun getNextPrayerName(): String {
        val prayerNames = arrayOf(
            "Dawn",
            "Mid-day",
            "Afternoon",
            "Sunset",
            "Night"
        )
        return prayerNames[getNextTimeIndex()]
    }

    override fun setLocation(latitude: Double, longitude: Double) {
        lat = latitude
        lng = longitude
    }

    override fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) {
        this.calcMethod = calcMethod
        this.asrJuristic = asrJuristic
        this.adjustHighLats = adjustHighLats
    }

    private val differences = LongArray(6) { 0 }
    private var nextTimeIndex = 0

    override fun getNextTimeIndex(): Int {
        for (i in differences.indices) {
            if (differences[i] < 0) {
                nextTimeIndex = i + 1
                if (nextTimeIndex >= 5) {
                    nextTimeIndex = 0
                }
            }
        }
        return nextTimeIndex
    }

    override fun setTimeFormat() {
        timeFormat = time12
    }

    override fun getNextTimeMillis(): Long {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val currentTime = simpleDateFormat.format(Calendar.getInstance().time)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val newTimes = getPrayerTimesForDate(dayOfMonth)
        val nextDayTimes = getPrayerTimesForDate(dayOfMonth + 1)

        try {
            // get milliseconds from parsing dates
            val currentTimeMillis = simpleDateFormat.parse(currentTime)?.time ?: 0
            val dawnMillis = simpleDateFormat.parse("${newTimes[0]}:00")?.time ?: 0
            val middayMillis = simpleDateFormat.parse("${newTimes[2]}:00")?.time ?: 0
            val afMillis = simpleDateFormat.parse("${newTimes[3]}:00")?.time ?: 0
            val sunsetMillis = simpleDateFormat.parse("${newTimes[5]}:00")?.time ?: 0
            val nightMillis = simpleDateFormat.parse("${newTimes[6]}:00")?.time ?: 0
            val nextDawnMillis = simpleDateFormat.parse("${nextDayTimes[0]}:00")?.time ?: 0
            val MILLIS_IN_DAY = 86400000

            // set index of each element in differences array
            differences[0] = dawnMillis - currentTimeMillis
            differences[1] = middayMillis - currentTimeMillis
            differences[2] = afMillis - currentTimeMillis
            differences[3] = sunsetMillis - currentTimeMillis
            differences[4] = nightMillis - currentTimeMillis
            differences[5] = nextDawnMillis - currentTimeMillis + MILLIS_IN_DAY
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var nextTimeIndex = 0
        for (i in differences.indices) {
            if (differences[i] < 0) {
                nextTimeIndex = i + 1
                if (nextTimeIndex > 5) {
                    nextTimeIndex = 0
                }
            }
        }
        return differences[nextTimeIndex]
    }
}

interface PrayerCalculator {
    fun getPrayerTimesForDate(offset: Int): ArrayList<String>
    fun getNextTimeMillis(): Long
    fun getNextPrayerName(): String
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int)
    fun getNextTimeIndex(): Int
    fun setTimeFormat()
}

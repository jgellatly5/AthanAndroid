package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.data.utils.computeDayTimes
import com.gallopdevs.athanhelper.data.utils.julianDate
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.DAYS_IN_WEEK
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

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

class PrayerCalculator @Inject constructor() : PrayerCalc {

    private val calendar = Calendar.getInstance()
    private val dstOffset = calendar.get(Calendar.DST_OFFSET) / 3600000
    private val timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset
    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    var lat = 0.0
    var lng = 0.0
    var timeZone = timeZoneOffset
    var jDate = 0.0

    // Initialize vars
    var calcMethod = JAFARI
    var asrJuristic = SHAFII
    var dhuhrMinutes = 0
    var adjustHighLats = MIDNIGHT
    var timeFormat = TIME_12

    // Tuning offsets {fajr, sunrise, dhuhr, asr, sunset, maghrib, isha}
    val offsets = IntArray(7)
    private val differences = LongArray(6)

    override fun getPrayerInfo(): PrayerInfo {
        return PrayerInfo(
            dates = getDatesForWeek(),
            prayerTimesForDate = getPrayerTimesForWeek(),
            nextTimeMillis = getNextTimeMillis(),
            nextTimeIndex = getNextTimeIndex()
        )
    }

    private fun getDatesForWeek(): List<String> {
        val dates = mutableListOf<String>()
        for (i in 0 until DAYS_IN_WEEK) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, i)
            val sdf = SimpleDateFormat("EEEE, MM/dd", Locale.getDefault())
            dates.add(sdf.format(calendar.time))
        }
        return dates
    }

    // return prayer times for a given date
    private fun getPrayerTimesForWeek(): List<List<Array<String>>> {
        val prayerTimesForWeek = mutableListOf<List<Array<String>>>()
        for (i in 0 until DAYS_IN_WEEK) {
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            jDate = julianDate(year, month, dayOfMonth + i)
            val lonDiff = lng / (15.0 * 24.0)
            jDate -= lonDiff
            prayerTimesForWeek.add(computeDayTimes())
        }
        return prayerTimesForWeek
    }

    private fun getNextTimeMillis(): Long {
        val currentTimeMillis = getCurrentTimeMillis()
        val newTimes = getPrayerTimesForWeek().first()
        val nextMorningTime = getPrayerTimesForWeek()[1]

        try {
            val times = arrayOf(
                newTimes[0],
                newTimes[1],
                newTimes[2],
                newTimes[3],
                newTimes[4],
                nextMorningTime[0]
            )
            val millisArray = times.map { parseTimeToMillis(it[0]) }

            // Calculate differences
            for (i in differences.indices) {
                differences[i] = millisArray[i] - currentTimeMillis
                if (i == differences.lastIndex) {
                    differences[i] = differences[i] + 86400000
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return differences[getNextTimeIndex()]
    }

    private fun getCurrentTimeMillis(): Long {
        val currentTime = simpleDateFormat.format(Calendar.getInstance().time)
        return parseTimeToMillis(currentTime)
    }

    private fun parseTimeToMillis(time: String): Long {
        return try {
            simpleDateFormat.parse("$time:00")?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    private fun getNextTimeIndex(): Int {
        var nextTimeIndex = 0
        for (i in differences.indices) {
            if (differences[i] < 0) {
                nextTimeIndex = (i + 1) % 5
            }
        }
        return nextTimeIndex
    }

    override fun setLocation(latitude: Double, longitude: Double) {
        lat = latitude
        lng = longitude
    }

    override fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    ) {
        this.calcMethod = calcMethod
        this.asrJuristic = asrJuristic
        this.adjustHighLats = adjustHighLats
        this.timeFormat = timeFormat
    }

    companion object {
        // Calculation Methods
        const val JAFARI = 0 // Ithna Ashari
        const val KARACHI = 1 // University of Islamic Sciences, Karachi
        const val ISNA = 2 // Islamic Society of North America (ISNA)
        const val MWL = 3 // Muslim World League (MWL)
        const val MAKKAH = 4 // Umm al-Qura, Makkah
        const val EGYPT = 5 // Egyptian General Authority of Survey
        const val TEHRAN = 6 // Institute of Geophysics, University of Tehran
        const val CUSTOM = 7 // Custom Setting

        // Asr Juristic Methods
        const val SHAFII = 0 // Shafii (standard)
        const val HANAFI = 1 // Hanafi

        // Adjusting Methods for Higher Latitudes
        const val NONE = 0
        const val MIDNIGHT = 1 // middle of night
        const val ONE_SEVENTH = 2 // 1/7th of night
        const val ANGLE_BASED = 3 // angle/60th of night

        // Time Formats
        const val TIME_24 = 0 // 24-hour format
        const val TIME_12 = 1 // 12-hour format
        const val TIME_12_NS = 2 // 12-hour format with no suffix
        const val FLOATING = 3 // floating point number

        /*
         * fa : fajr angle
         * ms : maghrib selector (0 = angle; 1 = minutes after sunset)
         * mv : maghrib parameter value (in angle or minutes)
         * is : isha selector (0 = angle; 1 = minutes after maghrib)
         * iv : isha parameter value (in angle or minutes)
         */
        val methodParams: HashMap<Int, DoubleArray> = hashMapOf(
            JAFARI to doubleArrayOf(16.0, 0.0, 4.0, 0.0, 14.0),
            KARACHI to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0),
            ISNA to doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0),
            MWL to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0),
            MAKKAH to doubleArrayOf(18.5, 1.0, 0.0, 1.0, 90.0),
            EGYPT to doubleArrayOf(19.5, 1.0, 0.0, 0.0, 17.5),
            TEHRAN to doubleArrayOf(17.7, 0.0, 4.5, 0.0, 14.0),
            CUSTOM to doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0)
        )
    }
}

interface PrayerCalc {
    fun getPrayerInfo(): PrayerInfo
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    )
}

data class PrayerInfo(
    val dates: List<String>,
    val prayerTimesForDate: List<List<Array<String>>>,
    val nextTimeMillis: Long,
    val nextTimeIndex: Int
) {
    companion object
}

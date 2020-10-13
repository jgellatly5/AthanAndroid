package com.gallopdevs.athanhelper.model

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

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
class PrayTime private constructor() {
    // Global Variables
    // calculation method
    var calcMethod = 0

    // Juristic method for Asr
    var asrJuristic = 0

    // minutes after mid-day for Dhuhr
    var dhuhrMinutes = 0

    // adjusting method for higher latitudes
    var adjustHighLats = 0

    // time format
    var timeFormat = 0

    // latitude
    var lat = 0.0

    // longitude
    var lng = 0.0

    // time-zone
    var timeZone = 0.0

    // Julian date
    var jDate = 0.0

    // Calculation Methods
    // Ithna Ashari
    private var jafari = 0

    // University of Islamic Sciences, Karachi
    private var karachi = 0

    // Islamic Society of North America (ISNA)
    private var iSNA = 0

    // Muslim World League (MWL)
    private var mWL = 0

    // Umm al-Qura, Makkah
    private var makkah = 0

    // Egyptian General Authority of Survey
    private var egypt = 0

    // Custom Setting
    private var custom = 0

    // Institute of Geophysics, University of Tehran
    private var tehran = 0

    // Juristic Methods
    // Shafii (standard)
    private var shafii = 0

    // Hanafi
    private var hanafi = 0

    // Adjusting Methods for Higher Latitudes
    // No adjustment
    private var none = 0

    // middle of night
    private var midNight = 0

    // 1/7th of night
    private var oneSeventh = 0

    // angle/60th of night
    private var angleBased = 0

    // Time Formats
    // 24-hour format
    private var time24 = 0

    // 12-hour format
    private var time12 = 0

    // 12-hour format with no suffix
    private var time12NS = 0

    // floating point number
    private var floating = 0

    // Time Names
    val timeNames: ArrayList<String>

    // Technical settings_icon
    // number of iterations needed to compute times
    private var numIterations = 0

    // Calc Method Parameters
    private val methodParams: HashMap<Int, DoubleArray>

    /*
     * this.methodParams[methodNum] = new Array(fa, ms, mv, is, iv);
     *
     * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
     * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
     * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter value
     * (in angle or minutes)
     */

    private val offsets: IntArray

    // ---------------------- Calculation Functions -----------------------
    // References:
    // http://www.ummah.net/astronomy/saltime
    // http://aa.usno.navy.mil/faq/docs/SunApprox.html
    // compute declination angle of sun_icon and equation of time
    private fun sunPosition(jd: Double): DoubleArray {
        val D = jd - 2451545
        val g = fixAngle(357.529 + 0.98560028 * D)
        val q = fixAngle(280.459 + 0.98564736 * D)
        val L = fixAngle(q + 1.915 * degreeSin(g) + 0.020 * degreeSin(2 * g))

        // double R = 1.00014 - 0.01671 * [self dcos:g] - 0.00014 * [self dcos:
        // (2*g)];
        val e = 23.439 - 0.00000036 * D
        val d = degreeArcsin(degreeSin(e) * degreeSin(L))
        var RA = degreeArctan2(degreeCos(e) * degreeSin(L), degreeCos(L)) / 15.0
        RA = fixHour(RA)
        val EqT = q / 15.0 - RA
        val sPosition = DoubleArray(2)
        sPosition[0] = d
        sPosition[1] = EqT
        return sPosition
    }

    // compute equation of time
    private fun equationOfTime(jd: Double): Double = sunPosition(jd)[1]

    // compute declination angle of sun_icon
    private fun sunDeclination(jd: Double): Double = sunPosition(jd)[0]

    // compute mid-day (Dhuhr, Zawal) time
    private fun computeMidDay(t: Double): Double {
        val time = equationOfTime(jDate + t)
        return fixHour(12 - time)
    }

    // compute time for a given angle G
    private fun computeTime(G: Double, t: Double): Double {
        val D = sunDeclination(jDate + t)
        val Z = computeMidDay(t)
        val Beg = -degreeSin(G) - degreeSin(D) * degreeSin(lat)
        val Mid = degreeCos(D) * degreeCos(lat)
        val V = degreeArccos(Beg / Mid) / 15.0
        return Z + if (G > 90) -V else V
    }

    // compute the time of Asr
    // Shafii: step=1, Hanafi: step=2
    private fun computeAsr(step: Double, t: Double): Double {
        val D = sunDeclination(jDate + t)
        val G = -degreeArccot(step + degreeTan(abs(lat - D)))
        return computeTime(G, t)
    }

    // ---------------------- Misc Functions -----------------------
    // compute the difference between two times
    private fun timeDiff(time1: Double, time2: Double): Double = fixHour(time2 - time1)

    // -------------------- Interface Functions --------------------
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

    // set custom values for calculation parameters
    private fun setCustomParams(params: DoubleArray) {
        for (i in 0..4) {
            if (params[i].equals(-1.0)) {
                params[i] = methodParams[calcMethod]!![i]
                methodParams[custom] = params
            } else {
                methodParams[custom]!![i] = params[i]
            }
        }
        calcMethod = custom
    }

    // set the angle for calculating Fajr
    fun setFajrAngle(angle: Double) {
        val params = doubleArrayOf(angle, -1.0, -1.0, -1.0, -1.0)
        setCustomParams(params)
    }

    // set the angle for calculating Maghrib
    fun setMaghribAngle(angle: Double) {
        val params = doubleArrayOf(-1.0, 0.0, angle, -1.0, -1.0)
        setCustomParams(params)
    }

    // set the angle for calculating Isha
    fun setIshaAngle(angle: Double) {
        val params = doubleArrayOf(-1.0, -1.0, -1.0, 0.0, angle)
        setCustomParams(params)
    }

    // set the minutes after Sunset for calculating Maghrib
    fun setMaghribMinutes(minutes: Double) {
        val params = doubleArrayOf(-1.0, 1.0, minutes, -1.0, -1.0)
        setCustomParams(params)
    }

    // set the minutes after Maghrib for calculating Isha
    fun setIshaMinutes(minutes: Double) {
        val params = doubleArrayOf(-1.0, -1.0, -1.0, 1.0, minutes)
        setCustomParams(params)
    }

    // ---------------------- Compute Prayer Times -----------------------
    // compute prayer times at given julian date
    private fun computeTimes(times: DoubleArray): DoubleArray {
        val t = dayPortion(times)
        val fajr = computeTime(180 - methodParams[calcMethod]!![0], t[0])
        val sunrise = computeTime(180 - 0.833, t[1])
        val dhuhr = computeMidDay(t[2])
        val asr = computeAsr(1 + asrJuristic.toDouble(), t[3])
        val sunset = computeTime(0.833, t[4])
        val maghrib = computeTime(methodParams[calcMethod]!![2], t[5])
        val isha = computeTime(methodParams[calcMethod]!![4], t[6])
        return doubleArrayOf(fajr, sunrise, dhuhr, asr, sunset, maghrib, isha)
    }

    // compute prayer times at given julian date
    private fun computeDayTimes(): ArrayList<String> {
        // default times
        var times = doubleArrayOf(5.0, 6.0, 12.0, 13.0, 18.0, 18.0, 18.0)
        for (i in 1..numIterations) {
            times = computeTimes(times)
        }
        times = adjustTimes(times)
        times = tuneTimes(times)
        return adjustTimesFormat(times)
    }

    // adjust times in a prayer time array
    private fun adjustTimes(t: DoubleArray): DoubleArray {
        var times = t
        for (i in times.indices) {
            times[i] += timeZone - lng / 15
        }
        // Dhuhr
        times[2] = times[2] + dhuhrMinutes / 60
        // Maghrib
        if (methodParams[calcMethod]!![1].equals(1.0)) {
            times[5] = times[4] + methodParams[calcMethod]!![2] / 60
        }
        // Isha
        if (methodParams[calcMethod]!![3].equals(1.0)) {
            times[6] = times[5] + methodParams[calcMethod]!![4] / 60
        }
        if (adjustHighLats != none) {
            times = adjustHighLatTimes(times)
        }
        return times
    }

    // convert times array to given time format
    private fun adjustTimesFormat(times: DoubleArray): ArrayList<String> {
        val result = ArrayList<String>()
        if (timeFormat == floating) {
            for (time in times) {
                result.add(time.toString())
            }
            return result
        }
        for (i in 0..6) {
            when (timeFormat) {
                time12 -> {
                    result.add(floatToTime12(times[i], false))
                }
                time12NS -> {
                    result.add(floatToTime12(times[i], true))
                }
                else -> {
                    result.add(floatToTime24(times[i]))
                }
            }
        }
        return result
    }

    // adjust Fajr, Isha and Maghrib for locations in higher latitudes
    private fun adjustHighLatTimes(times: DoubleArray): DoubleArray {
        val nightTime = timeDiff(times[4], times[1]) // sunset to sunrise

        // Adjust Fajr
        val fajrDiff = nightPortion(methodParams[calcMethod]!![0]) * nightTime
        if (java.lang.Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > fajrDiff) {
            times[0] = times[1] - fajrDiff
        }

        // Adjust Isha
        val ishaAngle: Double = if (methodParams[calcMethod]!![3].equals(0.0)) methodParams[calcMethod]!![4] else 18.0
        val ishaDiff = nightPortion(ishaAngle) * nightTime
        if (java.lang.Double.isNaN(times[6]) || timeDiff(times[4], times[6]) > ishaDiff) {
            times[6] = times[4] + ishaDiff
        }

        // Adjust Maghrib
        val maghribAngle: Double = if (methodParams[calcMethod]!![1].equals(0.0)) methodParams[calcMethod]!![2] else 4.0
        val maghribDiff = nightPortion(maghribAngle) * nightTime
        if (java.lang.Double.isNaN(times[5]) || timeDiff(times[4], times[5]) > maghribDiff) {
            times[5] = times[4] + maghribDiff
        }
        return times
    }

    // the night portion used for adjusting times in higher latitudes
    private fun nightPortion(angle: Double): Double {
        var calc = 0.0
        if (adjustHighLats == angleBased) calc = angle / 60.0
        else if (adjustHighLats == midNight) calc = 0.5
        else if (adjustHighLats == oneSeventh) calc = 0.14286
        return calc
    }

    // convert hours to day portions
    private fun dayPortion(times: DoubleArray): DoubleArray {
        for (i in 0..6) {
            times[i] = times[i] / 24
        }
        return times
    }

    // Tune timings for adjustments
    // Set time offsets
    fun tune(offsetTimes: IntArray) {
        for (i in offsetTimes.indices) { // offsetTimes length
            // should be 7 in order
            // of Fajr, Sunrise,
            // Dhuhr, Asr, Sunset,
            // Maghrib, Isha
            offsets[i] = offsetTimes[i]
        }
    }

    private fun tuneTimes(times: DoubleArray): DoubleArray {
        for (i in times.indices) {
            times[i] = times[i] + offsets[i] / 60.0
        }
        return times
    }

    companion object {
        private var prayerTime: PrayTime? = null
        val instance: PrayTime?
            get() {
                if (prayerTime == null) {
                    prayerTime = PrayTime()
                }
                return prayerTime
            }
    }

    init {
        // Initialize vars
        calcMethod = 0
        asrJuristic = 0
        dhuhrMinutes = 0
        adjustHighLats = 1
        timeFormat = 0

        // Calculation Methods
        jafari = 0 // Ithna Ashari
        karachi = 1 // University of Islamic Sciences, Karachi
        iSNA = 2 // Islamic Society of North America (ISNA)
        mWL = 3 // Muslim World League (MWL)
        makkah = 4 // Umm al-Qura, Makkah
        egypt = 5 // Egyptian General Authority of Survey
        tehran = 6 // Institute of Geophysics, University of Tehran
        custom = 7 // Custom Setting

        // Juristic Methods
        shafii = 0 // Shafii (standard)
        hanafi = 1 // Hanafi

        // Adjusting Methods for Higher Latitudes
        none = 0 // No adjustment
        midNight = 1 // middle of night
        oneSeventh = 2 // 1/7th of night
        angleBased = 3 // angle/60th of night

        // Time Formats
        time24 = 0 // 24-hour format
        time12 = 1 // 12-hour format
        time12NS = 2 // 12-hour format with no suffix
        floating = 3 // floating point number

        // Time Names
        timeNames = ArrayList()
        timeNames.add("Fajr")
        timeNames.add("Sunrise")
        timeNames.add("Dhuhr")
        timeNames.add("Asr")
        timeNames.add("Sunset")
        timeNames.add("Maghrib")
        timeNames.add("Isha")

        // --------------------- Technical settings_icon --------------------
        numIterations = 1 // number of iterations needed to compute times

        // ------------------- Calc Method Parameters --------------------

        // Tuning offsets {fajr, sunrise, dhuhr, asr, sunset, maghrib, isha}
        offsets = IntArray(7)
        offsets[0] = 0
        offsets[1] = 0
        offsets[2] = 0
        offsets[3] = 0
        offsets[4] = 0
        offsets[5] = 0
        offsets[6] = 0

        /*
         *
         * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
         * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
         * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter
         * value (in angle or minutes)
         */
        methodParams = HashMap()

        // Jafari
        methodParams[Integer.valueOf(jafari)] = doubleArrayOf(16.0, 0.0, 4.0, 0.0, 14.0)

        // Karachi
        methodParams[Integer.valueOf(karachi)] = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0)

        // ISNA
        methodParams[Integer.valueOf(iSNA)] = doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0)

        // MWL
        methodParams[Integer.valueOf(mWL)] = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0)

        // Makkah
        methodParams[Integer.valueOf(makkah)] = doubleArrayOf(18.5, 1.0, 0.0, 1.0, 90.0)

        // Egypt
        methodParams[Integer.valueOf(egypt)] = doubleArrayOf(19.5, 1.0, 0.0, 0.0, 17.5)

        // Tehran
        methodParams[Integer.valueOf(tehran)] = doubleArrayOf(17.7, 0.0, 4.5, 0.0, 14.0)

        // Custom
        methodParams[Integer.valueOf(custom)] = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0)
    }
}
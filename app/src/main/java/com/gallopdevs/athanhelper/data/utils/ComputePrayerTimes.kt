package com.gallopdevs.athanhelper.data.utils

import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.ANGLE_BASED
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.FLOATING
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.NONE
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.ONE_SEVENTH
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.TIME_12
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.TIME_12_NS
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.methodParams

fun ArrayList<String>.getPrayerTimesForDate(): List<Array<String>> {
    fun formatTime(time: String): Array<String> =
        time.replaceFirst("^0+(?!$)".toRegex(), "").split(" ".toRegex()).toTypedArray()

    return listOf(
        formatTime(this[0]),
        formatTime(this[2]),
        formatTime(this[3]),
        formatTime(this[5]),
        formatTime(this[6])
    )
}

// compute prayer times at given julian date
fun PrayerCalculatorIpml.computeDayTimes(): List<Array<String>> {
    // default times
    var times = doubleArrayOf(5.0, 6.0, 12.0, 13.0, 18.0, 18.0, 18.0)
    times = computeTimes(times)
    times = adjustTimes(times)
    times = tuneTimes(times)
    return adjustTimesFormat(times).getPrayerTimesForDate()
}

// compute prayer times at given julian date
fun PrayerCalculatorIpml.computeTimes(times: DoubleArray): DoubleArray {
    val t = dayPortion(times)
    val fajr = computeTime(
        180 - methodParams[calcMethod]!![0],
        t[0]
    )
    val sunrise = computeTime(180 - 0.833, t[1])
    val dhuhr = computeMidDay(t[2])
    val asr = computeAsr(1 + asrJuristic.toDouble(), t[3])
    val sunset = computeTime(0.833, t[4])
    val maghrib =
        computeTime(methodParams[calcMethod]!![2], t[5])
    val isha =
        computeTime(methodParams[calcMethod]!![4], t[6])
    return doubleArrayOf(fajr, sunrise, dhuhr, asr, sunset, maghrib, isha)
}

// convert hours to day portions
fun dayPortion(times: DoubleArray): DoubleArray {
    for (i in 0..6) {
        times[i] = times[i] / 24
    }
    return times
}

// adjust times in a prayer time array
fun PrayerCalculatorIpml.adjustTimes(t: DoubleArray): DoubleArray {
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
    if (adjustHighLats != NONE) {
        times = adjustHighLatTimes(times)
    }
    return times
}


// adjust Fajr, Isha and Maghrib for locations in higher latitudes
fun PrayerCalculatorIpml.adjustHighLatTimes(times: DoubleArray): DoubleArray {
    val nightTime = timeDiff(times[4], times[1]) // sunset to sunrise

    // Adjust Fajr
    val fajrDiff =
        nightPortion(methodParams[calcMethod]!![0]) * nightTime
    if (java.lang.Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > fajrDiff) {
        times[0] = times[1] - fajrDiff
    }

    // Adjust Isha
    val ishaAngle: Double =
        if (methodParams[calcMethod]!![3].equals(0.0)) methodParams[calcMethod]!![4] else 18.0
    val ishaDiff = nightPortion(ishaAngle) * nightTime
    if (java.lang.Double.isNaN(times[6]) || timeDiff(times[4], times[6]) > ishaDiff) {
        times[6] = times[4] + ishaDiff
    }

    // Adjust Maghrib
    val maghribAngle: Double =
        if (methodParams[calcMethod]!![1].equals(0.0)) methodParams[calcMethod]!![2] else 4.0
    val maghribDiff = nightPortion(maghribAngle) * nightTime
    if (java.lang.Double.isNaN(times[5]) || timeDiff(times[4], times[5]) > maghribDiff) {
        times[5] = times[4] + maghribDiff
    }
    return times
}

// the night portion used for adjusting times in higher latitudes
fun PrayerCalculatorIpml.nightPortion(angle: Double): Double {
    var calc = 0.0
    when (adjustHighLats) {
        ANGLE_BASED -> calc = angle / 60.0
        MIDNIGHT -> calc = 0.5
        ONE_SEVENTH -> calc = 0.14286
    }
    return calc
}

// convert times array to given time format
fun PrayerCalculatorIpml.adjustTimesFormat(times: DoubleArray): ArrayList<String> {
    val result = ArrayList<String>()
    if (timeFormat == FLOATING) {
        for (time in times) {
            result.add(time.toString())
        }
        return result
    }
    for (i in 0..6) {
        when (timeFormat) {
            TIME_12 -> {
                result.add(floatToTime12(times[i], false))
            }

            TIME_12_NS -> {
                result.add(floatToTime12(times[i], true))
            }

            else -> {
                result.add(floatToTime24(times[i]))
            }
        }
    }
    return result
}

// Tune timings for adjustments
// Set time offsets
fun PrayerCalculatorIpml.tune(offsetTimes: IntArray) {
    for (i in offsetTimes.indices) { // offsetTimes length
        // should be 7 in order
        // of Fajr, Sunrise,
        // Dhuhr, Asr, Sunset,
        // Maghrib, Isha
        offsets[i] = offsetTimes[i]
    }
}

fun PrayerCalculatorIpml.tuneTimes(times: DoubleArray): DoubleArray {
    for (i in times.indices) {
        times[i] = times[i] + offsets[i] / 60.0
    }
    return times
}

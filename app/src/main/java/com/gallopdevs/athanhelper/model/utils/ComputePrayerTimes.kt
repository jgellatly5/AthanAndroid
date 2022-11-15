package com.gallopdevs.athanhelper.model.utils

import com.gallopdevs.athanhelper.model.*

// compute prayer times at given julian date
fun computeDayTimes(): ArrayList<String> {
    // default times
    var times = doubleArrayOf(5.0, 6.0, 12.0, 13.0, 18.0, 18.0, 18.0)
    times = computeTimes(times)
    times = adjustTimes(times)
    times = tuneTimes(times)
    return adjustTimesFormat(times)
}

// compute prayer times at given julian date
fun computeTimes(times: DoubleArray): DoubleArray {
    val t = dayPortion(times)
    val fajr = computeTime(180 - PrayTime.methodParams[PrayTime.calcMethod]!![0], t[0])
    val sunrise = computeTime(180 - 0.833, t[1])
    val dhuhr = computeMidDay(t[2])
    val asr = computeAsr(1 + PrayTime.asrJuristic.toDouble(), t[3])
    val sunset = computeTime(0.833, t[4])
    val maghrib = computeTime(PrayTime.methodParams[PrayTime.calcMethod]!![2], t[5])
    val isha = computeTime(PrayTime.methodParams[PrayTime.calcMethod]!![4], t[6])
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
fun adjustTimes(t: DoubleArray): DoubleArray {
    var times = t
    for (i in times.indices) {
        times[i] += PrayTime.timeZone - PrayTime.lng / 15
    }
    // Dhuhr
    times[2] = times[2] + PrayTime.dhuhrMinutes / 60
    // Maghrib
    if (PrayTime.methodParams[PrayTime.calcMethod]!![1].equals(1.0)) {
        times[5] = times[4] + PrayTime.methodParams[PrayTime.calcMethod]!![2] / 60
    }
    // Isha
    if (PrayTime.methodParams[PrayTime.calcMethod]!![3].equals(1.0)) {
        times[6] = times[5] + PrayTime.methodParams[PrayTime.calcMethod]!![4] / 60
    }
    if (PrayTime.adjustHighLats != PrayTime.none) {
        times = adjustHighLatTimes(times)
    }
    return times
}


// adjust Fajr, Isha and Maghrib for locations in higher latitudes
fun adjustHighLatTimes(times: DoubleArray): DoubleArray {
    val nightTime = timeDiff(times[4], times[1]) // sunset to sunrise

    // Adjust Fajr
    val fajrDiff = nightPortion(PrayTime.methodParams[PrayTime.calcMethod]!![0]) * nightTime
    if (java.lang.Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > fajrDiff) {
        times[0] = times[1] - fajrDiff
    }

    // Adjust Isha
    val ishaAngle: Double = if (PrayTime.methodParams[PrayTime.calcMethod]!![3].equals(0.0)) PrayTime.methodParams[PrayTime.calcMethod]!![4] else 18.0
    val ishaDiff = nightPortion(ishaAngle) * nightTime
    if (java.lang.Double.isNaN(times[6]) || timeDiff(times[4], times[6]) > ishaDiff) {
        times[6] = times[4] + ishaDiff
    }

    // Adjust Maghrib
    val maghribAngle: Double = if (PrayTime.methodParams[PrayTime.calcMethod]!![1].equals(0.0)) PrayTime.methodParams[PrayTime.calcMethod]!![2] else 4.0
    val maghribDiff = nightPortion(maghribAngle) * nightTime
    if (java.lang.Double.isNaN(times[5]) || timeDiff(times[4], times[5]) > maghribDiff) {
        times[5] = times[4] + maghribDiff
    }
    return times
}

// the night portion used for adjusting times in higher latitudes
fun nightPortion(angle: Double): Double {
    var calc = 0.0
    when (PrayTime.adjustHighLats) {
        PrayTime.angleBased -> calc = angle / 60.0
        PrayTime.midNight -> calc = 0.5
        PrayTime.oneSeventh -> calc = 0.14286
    }
    return calc
}

// convert times array to given time format
fun adjustTimesFormat(times: DoubleArray): ArrayList<String> {
    val result = ArrayList<String>()
    if (PrayTime.timeFormat == PrayTime.floating) {
        for (time in times) {
            result.add(time.toString())
        }
        return result
    }
    for (i in 0..6) {
        when (PrayTime.timeFormat) {
            PrayTime.time12 -> {
                result.add(floatToTime12(times[i], false))
            }
            PrayTime.time12NS -> {
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
fun tune(offsetTimes: IntArray) {
    for (i in offsetTimes.indices) { // offsetTimes length
        // should be 7 in order
        // of Fajr, Sunrise,
        // Dhuhr, Asr, Sunset,
        // Maghrib, Isha
        PrayTime.offsets[i] = offsetTimes[i]
    }
}

fun tuneTimes(times: DoubleArray): DoubleArray {
    for (i in times.indices) {
        times[i] = times[i] + PrayTime.offsets[i] / 60.0
    }
    return times
}
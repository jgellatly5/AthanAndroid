package com.gallopdevs.athanhelper.model



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
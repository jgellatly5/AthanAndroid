package com.gallopdevs.athanhelper.model

import kotlin.math.floor
import kotlin.math.roundToInt

// compute the difference between two times
fun timeDiff(time1: Double, time2: Double): Double = fixHour(time2 - time1)

// convert double hours to 24h format
fun floatToTime24(t: Double): String {
    var time = t
    val result: String
    if (java.lang.Double.isNaN(time)) {
        return "-----"
    }
    time = fixHour(time + 0.5 / 60.0) // add 0.5 minutes to round
    val hours = floor(time).toInt()
    val minutes = floor((time - hours) * 60.0)
    result = if (hours in 0..9 && minutes >= 0 && minutes <= 9) {
        "0" + hours + ":0" + minutes.roundToInt()
    } else if (hours in 0..9) {
        "0" + hours + ":" + minutes.roundToInt()
    } else if (minutes in 0.0..9.0) {
        hours.toString() + ":0" + minutes.roundToInt()
    } else {
        hours.toString() + ":" + minutes.roundToInt()
    }
    return result
}

// convert double hours to 12h format
fun floatToTime12(t: Double, noSuffix: Boolean): String {
    var time = t
    if (java.lang.Double.isNaN(time)) {
        return "-----"
    }
    time = fixHour(time + 0.5 / 60) // add 0.5 minutes to round
    var hours = floor(time).toInt()
    val minutes = floor((time - hours) * 60)
    val suffix = if (hours >= 12) {
        "pm"
    } else {
        "am"
    }
    hours = (hours + 12 - 1) % 12 + 1
    /*hours = (hours + 12) - 1;
    int hrs = (int) hours % 12;
    hrs += 1;*/
    return if (!noSuffix) {
        if (hours in 0..9 && minutes >= 0 && minutes <= 9) {
            ("0" + hours + ":0" + minutes.roundToInt() + " " + suffix)
        } else if (hours in 0..9) {
            "0" + hours + ":" + minutes.roundToInt() + " " + suffix
        } else if (minutes in 0.0..9.0) {
            hours.toString() + ":0" + minutes.roundToInt() + " " + suffix
        } else {
            hours.toString() + ":" + minutes.roundToInt() + " " + suffix
        }
    } else {
        if (hours in 0..9 && minutes >= 0 && minutes <= 9) {
            "0" + hours + ":0" + minutes.roundToInt()
        } else if (hours in 0..9) {
            "0" + hours + ":" + minutes.roundToInt()
        } else if (minutes in 0.0..9.0) {
            hours.toString() + ":0" + minutes.roundToInt()
        } else {
            hours.toString() + ":" + minutes.roundToInt()
        }
    }
}

// convert double hours to 12h format with no suffix
fun floatToTime12NS(time: Double): String = floatToTime12(time, true)
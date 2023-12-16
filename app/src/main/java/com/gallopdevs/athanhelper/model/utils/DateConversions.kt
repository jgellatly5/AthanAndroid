package com.gallopdevs.athanhelper.model.utils

import java.util.Date
import kotlin.math.floor

// calculate julian date from a calendar date
fun julianDate(y: Int, m: Int, d: Int): Double {
    var year = y
    var month = m
    if (month <= 2) {
        year -= 1
        month += 12
    }
    val a = floor(year / 100.0)
    val b = 2 - a + floor(a / 4.0)
    return (floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + d + b) - 1524.5
}

// convert a calendar date to julian date (second method)
fun calcJD(year: Int, month: Int, day: Int): Double {
    val j1970 = 2440588.0
    val date = Date(year, month - 1, day)
    val ms = date.time.toDouble() // # of milliseconds since midnight Jan 1, 1970
    val days = floor(ms / (1000.0 * 60.0 * 60.0 * 24.0))
    return j1970 + days - 0.5
}

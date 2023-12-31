package com.gallopdevs.athanhelper.data.utils

import java.util.TimeZone

// compute local time-zone for a specific date
val timeZone1: Double = TimeZone.getDefault().rawOffset / 1000.0 / 3600

// compute base time-zone of the system
private val baseTimeZone: Double = TimeZone.getDefault().rawOffset / 1000.0 / 3600

// detect daylight saving in a given date
private fun detectDaylightSaving(): Double = TimeZone.getDefault().dstSavings.toDouble()

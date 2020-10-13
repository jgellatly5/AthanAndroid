package com.gallopdevs.athanhelper.model

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CalendarPrayerTimes {

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
}

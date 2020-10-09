package com.gallopdevs.athanhelper.utils

import com.gallopdevs.athanhelper.model.PrayTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CalendarPrayerTimes {
    private val TAG = "CalendarPrayerTimes"
    private val DEFAULT_TIME_FORMAT = 0
    private val prayerTime = PrayTime.instance

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

    private val calendar = Calendar.getInstance()
    private val month = calendar.get(Calendar.MONTH)
    private val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    private val year = calendar.get(Calendar.YEAR)
    private val dstOffset = calendar.get(Calendar.DST_OFFSET) / 3600000
    private val timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET) / 3600000 + dstOffset

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    fun updateCalcMethod(value: Int) {
        prayerTime?.calcMethod = value
    }

    fun updateAsrJuristic(value: Int) {
        prayerTime?.asrJuristic = value
    }

    fun updateHighLats(value: Int) {
        prayerTime?.adjustHighLats = value
    }

    fun updateTimeFormat() {
        prayerTime?.timeFormat = DEFAULT_TIME_FORMAT
    }

    fun getNewTimes(): ArrayList<String> {
        return prayerTime!!.getDatePrayerTimes(year, month + 1, dayOfMonth, latitude, longitude, timeZoneOffset.toDouble())
    }

    fun getNextDayTimes(i: Int): ArrayList<String> {
        return prayerTime!!.getDatePrayerTimes(year, month + 1, dayOfMonth + i, latitude, longitude, timeZoneOffset.toDouble())
    }

    fun setLatitude(latitude: Double) {
        CalendarPrayerTimes.latitude = latitude
    }

    fun setLongitude(longitude: Double) {
        CalendarPrayerTimes.longitude = longitude
    }
}

package com.gallopdevs.athanhelper.clock

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import com.gallopdevs.athanhelper.model.PrayerRepository

class ClockViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PrayerRepository = PrayerRepository()

    private fun getDatePrayerTimes(count: Int) = repository.getDatePrayerTimes(count)

    fun getNextTimeMillis() = repository.getNextTimeMillis()

    fun getNextPrayerName() = repository.getNextPrayerName()

    fun setLocation(latitude: Double, longitude: Double) = repository.setLocation(latitude, longitude)

    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) = repository.setCalculations(calcMethod, asrJuristic, adjustHighLats)

    fun setTimeFormat() = repository.setTimeFormat()

    fun formatDate(bundle: Bundle): String {
        val dayOfMonth = bundle.getInt("dayOfMonth")
        val dayOfMonthString = dayOfMonth.toString()

        val month = bundle.getInt("month")
        val monthString = if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }

        var weekDay = bundle.getInt("day")
        if (weekDay >= 8) {
            weekDay -= 7
        }
        val weekDayString = when (weekDay) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "This is not a day"
        }

        return if (dayOfMonth < 10) {
            "$weekDayString, $monthString/0$dayOfMonthString"
        } else {
            "$weekDayString, $monthString/$dayOfMonthString"
        }
    }

    fun formatTimes(bundle: Bundle): List<Array<String>> {
        val count = bundle.getInt("count")
        val nextDayTimes = getDatePrayerTimes(count)

        val newDawnTime = nextDayTimes[0].replaceFirst("^0+(?!$)".toRegex(), "")
        val newMiddayTime = nextDayTimes[2].replaceFirst("^0+(?!$)".toRegex(), "")
        val newAfternoonTime = nextDayTimes[3].replaceFirst("^0+(?!$)".toRegex(), "")
        val newSunsetTime = nextDayTimes[5].replaceFirst("^0+(?!$)".toRegex(), "")
        val newNightTime = nextDayTimes[6].replaceFirst("^0+(?!$)".toRegex(), "")

        val splitDawnTime = newDawnTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitMiddayTime = newMiddayTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitAfternoonTime = newAfternoonTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitSunsetTime = newSunsetTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitNightTime = newNightTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return listOf(
            splitDawnTime,
            splitMiddayTime,
            splitAfternoonTime,
            splitSunsetTime,
            splitNightTime
        )
    }

    fun getNextTimeIndex() = repository.getNextTimeIndex()
}
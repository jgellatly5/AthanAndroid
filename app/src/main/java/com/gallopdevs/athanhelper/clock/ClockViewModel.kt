package com.gallopdevs.athanhelper.clock

import android.app.Application
import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gallopdevs.athanhelper.model.PrayerRepository

class ClockViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PrayerRepository = PrayerRepository()
    private var timer: CountDownTimer? = null
    private var isFinished: Boolean = true

    val timerCountDown = MutableLiveData<Long>()

    fun startNewTimer() {
        if (isFinished) {
            timer?.cancel()
            timer = object : CountDownTimer(getNextTimeMillis(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    isFinished = false
                    timerCountDown.value = millisUntilFinished
                }

                override fun onFinish() {
                    isFinished = true
                    startNewTimer()
                }
            }.start()
        }
    }

    private fun getDatePrayerTimes(count: Int) = repository.getDatePrayerTimes(count)

    private fun getNextTimeMillis() = repository.getNextTimeMillis()

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
        val pageIndex = bundle.getInt("pageIndex")
        val nextDayTimes = getDatePrayerTimes(pageIndex)

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
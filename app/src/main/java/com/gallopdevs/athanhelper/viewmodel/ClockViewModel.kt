package com.gallopdevs.athanhelper.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.repository.PrayerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val prayerRepo: PrayerRepo
) : ViewModel() {

    private var timer: CountDownTimer? = null
    private var isFinished: Boolean = true

    private val _timerCountDown = MutableLiveData<Long>()
    val timerCountDown: LiveData<Long>
        get() = _timerCountDown

    fun startNewTimer() {
        if (isFinished) {
            timer?.cancel()
            timer = object : CountDownTimer(getNextTimeMillis(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    isFinished = false
                    _timerCountDown.value = millisUntilFinished
                }

                override fun onFinish() {
                    isFinished = true
                    startNewTimer()
                }
            }.start()
        }
    }

    private fun getNextTimeMillis() = prayerRepo.getNextTimeMillis()

    fun getNextPrayerName() = prayerRepo.getNextPrayerName()

    fun getNextTimeIndex() = prayerRepo.getNextTimeIndex()

    fun setLocation(latitude: Double, longitude: Double) =
        prayerRepo.setLocation(latitude, longitude)

    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) =
        prayerRepo.setCalculations(calcMethod, asrJuristic, adjustHighLats)

    fun setTimeFormat() = prayerRepo.setTimeFormat()

    fun formatDate(
        weekDayArg: Int,
        monthArg: Int,
        dayOfMonthArg: Int,
    ): String {
        val dayOfMonthString = dayOfMonthArg.toString()

        val monthString = if (monthArg < 10) {
            "0$monthArg"
        } else {
            monthArg.toString()
        }

        var weekDay = weekDayArg
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

        return if (dayOfMonthArg < 10) {
            "$weekDayString, $monthString/0$dayOfMonthString"
        } else {
            "$weekDayString, $monthString/$dayOfMonthString"
        }
    }

    fun formatTimes(pageIndex: Int): List<Array<String>> {
        val nextDayTimes = getPrayerTimesForDate(pageIndex)

        val newDawnTime = nextDayTimes[0].replaceFirst("^0+(?!$)".toRegex(), "")
        val newMiddayTime = nextDayTimes[2].replaceFirst("^0+(?!$)".toRegex(), "")
        val newAfternoonTime = nextDayTimes[3].replaceFirst("^0+(?!$)".toRegex(), "")
        val newSunsetTime = nextDayTimes[5].replaceFirst("^0+(?!$)".toRegex(), "")
        val newNightTime = nextDayTimes[6].replaceFirst("^0+(?!$)".toRegex(), "")

        val splitDawnTime =
            newDawnTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitMiddayTime =
            newMiddayTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitAfternoonTime =
            newAfternoonTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitSunsetTime =
            newSunsetTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitNightTime =
            newNightTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        return listOf(
            splitDawnTime,
            splitMiddayTime,
            splitAfternoonTime,
            splitSunsetTime,
            splitNightTime
        )
    }

    private fun getPrayerTimesForDate(pageIndex: Int) = prayerRepo.getPrayerTimesForDate(pageIndex)
}

package com.gallopdevs.athanhelper.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.repository.PrayerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
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

    fun saveBoolean(key: String, value: Boolean) = prayerRepo.saveBoolean(key, value)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean = prayerRepo.getBoolean(key, defaultValue)

    fun saveInt(key: String, value: Int) = prayerRepo.saveInt(key, value)

    fun getInt(key: String, defaultValue: Int = 0): Int = prayerRepo.getInt(key, defaultValue)

    fun formatDate(pageIndex: Int): String {
        val c = Calendar.getInstance()
        var weekDay = c.get(Calendar.DAY_OF_WEEK) + pageIndex
        val month = c.get(Calendar.MONTH) + 1
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH) + pageIndex
        val dayOfMonthString = dayOfMonth.toString()

        val monthString = if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }

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

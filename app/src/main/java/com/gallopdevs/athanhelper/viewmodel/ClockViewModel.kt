package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.domain.GetNextTimeMillisUseCase
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    getNextTimeMillisUseCase: GetNextTimeMillisUseCase,
    private val prayerRepo: PrayerRepo,
    private val settingsRepo: SettingsRepo
) : ViewModel() {

//    val nextTimeMillisUiState: StateFlow<NextTimeMillisUiState> = getNextTimeMillisUseCase()

    fun getNextTimeMillis() = prayerRepo.getNextTimeMillis()

    fun getNextTimeIndex() = prayerRepo.getNextTimeIndex()

    fun setLocation(latitude: Double, longitude: Double) =
        prayerRepo.setLocation(latitude, longitude)

    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) =
        prayerRepo.setCalculations(calcMethod, asrJuristic, adjustHighLats)

    fun setTimeFormat() = prayerRepo.setTimeFormat()

    fun saveBoolean(key: String, value: Boolean) = settingsRepo.saveBoolean(key, value)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        settingsRepo.getBoolean(key, defaultValue)

    fun saveInt(key: String, value: Int) = settingsRepo.saveInt(key, value)

    fun getInt(key: String, defaultValue: Int = 0): Int = settingsRepo.getInt(key, defaultValue)

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

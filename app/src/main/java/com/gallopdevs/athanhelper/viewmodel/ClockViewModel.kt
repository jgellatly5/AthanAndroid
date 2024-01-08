package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
//    getNextTimeMillisUseCase: GetNextTimeMillisUseCase,
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

    fun getDate(pageIndex: Int): String {
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, pageIndex)

        val sdf = SimpleDateFormat("EEEE, MM/dd", Locale.getDefault())
        return sdf.format(c.time)
    }

    fun getPrayerTimesForDate(pageIndex: Int): List<Array<String>> {
        return prayerRepo.getPrayerTimesForDate(pageIndex)
    }
}

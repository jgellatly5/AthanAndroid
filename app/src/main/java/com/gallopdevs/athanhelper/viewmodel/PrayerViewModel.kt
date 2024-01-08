package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.repository.PrayerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
//    getNextTimeMillisUseCase: GetNextTimeMillisUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

//    val nextTimeMillisUiState: StateFlow<NextTimeMillisUiState> = getNextTimeMillisUseCase()

    fun getPrayerTimesForDate(pageIndex: Int): List<Array<String>> {
        return prayerRepo.getPrayerTimesForDate(pageIndex)
    }

    fun getNextTimeMillis() = prayerRepo.getNextTimeMillis()

    fun getNextTimeIndex() = prayerRepo.getNextTimeIndex()

    fun setLocation(latitude: Double, longitude: Double) =
        prayerRepo.setLocation(latitude, longitude)

    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) =
        prayerRepo.setCalculations(calcMethod, asrJuristic, adjustHighLats)

    fun setTimeFormat() = prayerRepo.setTimeFormat()

    fun getDate(pageIndex: Int): String {
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, pageIndex)

        val sdf = SimpleDateFormat("EEEE, MM/dd", Locale.getDefault())
        return sdf.format(c.time)
    }
}

package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.repository.PrayerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
//    getNextTimeMillisUseCase: GetNextTimeMillisUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

//    val nextTimeMillisUiState: StateFlow<NextTimeMillisUiState> = getNextTimeMillisUseCase()

    fun getPrayerTimesForDate(pageIndex: Int): List<Array<String>> =
        prayerRepo.getPrayerTimesForDate(pageIndex)

    fun getNextTimeMillis() = prayerRepo.getNextTimeMillis()

    fun getNextTimeIndex() = prayerRepo.getNextTimeIndex()

    fun getDate(pageIndex: Int): String = prayerRepo.getDate(pageIndex)

    fun setLocation(latitude: Double, longitude: Double) =
        prayerRepo.setLocation(latitude, longitude)

    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    ) = prayerRepo.setCalculations(calcMethod, asrJuristic, adjustHighLats, timeFormat)
}

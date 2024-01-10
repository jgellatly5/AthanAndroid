package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import com.gallopdevs.athanhelper.data.NextTimeInfo
import com.gallopdevs.athanhelper.data.PrayerTimesInfo
import com.gallopdevs.athanhelper.repository.PrayerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
//    getNextTimeMillisUseCase: GetNextTimeMillisUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

//    val nextTimeMillisUiState: StateFlow<NextTimeMillisUiState> = getNextTimeMillisUseCase()

    fun getPrayerTimesInfo(pageIndex: Int): PrayerTimesInfo = prayerRepo.getPrayerTimesInfo(pageIndex)

    fun getNextTimeInfo(): NextTimeInfo = prayerRepo.getNextTimeInfo()

    fun setLocation(latitude: Double, longitude: Double) =
        prayerRepo.setLocation(latitude, longitude)

    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    ) = prayerRepo.setCalculations(calcMethod, asrJuristic, adjustHighLats, timeFormat)
}

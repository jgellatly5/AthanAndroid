package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallopdevs.athanhelper.data.PrayerInfo
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.domain.GetDatesForWeekUseCase
import com.gallopdevs.athanhelper.domain.GetPrayerTimesForWeekUseCase
import com.gallopdevs.athanhelper.repository.PrayerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase,
    private val getDatesForWeekUseCase: GetDatesForWeekUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow<DayViewScreenUiState>(DayViewScreenUiState.Loading)
    val uiState: StateFlow<DayViewScreenUiState> = _uiState.asStateFlow()

    /*init {
        getPrayerTimesForDate(
            date = "11-01-2024",
            latitude = 33.860889,
            longitude = -118.392632,
            method = 2
        )
    }*/

    fun getPrayerTimesForDate(pageIndex: Int) {
        _uiState.value = DayViewScreenUiState.Loading
        viewModelScope.launch {
            try {
                val timings = getPrayerTimesForWeekUseCase()
                val dates = getDatesForWeekUseCase()
                _uiState.value = DayViewScreenUiState.Success(timings[pageIndex], dates)
            } catch (e: Exception) {
                _uiState.value = DayViewScreenUiState.Error("An error has occurred.")
            }
        }
    }

    fun getPrayerInfo(): PrayerInfo = prayerRepo.getPrayerInfo()

    fun setLocation(latitude: Double, longitude: Double) =
        prayerRepo.setLocation(latitude, longitude)

    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    ) = prayerRepo.setCalculations(calcMethod, asrJuristic, adjustHighLats, timeFormat)
}

sealed class DayViewScreenUiState {
    data class Success(val timings: Timings, val dates: List<String>) : DayViewScreenUiState()
    data class Error(val message: String) : DayViewScreenUiState()
    data object Loading : DayViewScreenUiState()
}

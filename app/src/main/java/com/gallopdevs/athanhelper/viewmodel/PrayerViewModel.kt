package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallopdevs.athanhelper.data.PrayerInfo
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.domain.GetPrayerTimesForWeekUseCase
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.repository.PrayerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

    private val _prayerTimesUiState =
        MutableStateFlow<PrayerTimesUiState>(PrayerTimesUiState.Loading)
    val prayerTimesUiState: StateFlow<PrayerTimesUiState> = _prayerTimesUiState.asStateFlow()

    init {
        getPrayerTimesForWeek()
    }

    fun getPrayerTimesForWeek() {
        viewModelScope.launch {
            try {
                getPrayerTimesForWeekUseCase().collect { result ->
                    when (result) {
                        Result.Loading -> _prayerTimesUiState.update { PrayerTimesUiState.Loading }

                        is Result.Success -> _prayerTimesUiState.update {
                            PrayerTimesUiState.Success(
                                result.data
                            )
                        }

                        is Result.Error -> _prayerTimesUiState.update { PrayerTimesUiState.Error("Result Error") }
                    }
                }
            } catch (e: Exception) {
                _prayerTimesUiState.update { PrayerTimesUiState.Error("Coroutine Error") }
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

sealed class PrayerTimesUiState {
    data class Success(val prayerTimesList: List<PrayerTimes>) : PrayerTimesUiState()
    data class Error(val message: String) : PrayerTimesUiState()
    data object Loading : PrayerTimesUiState()
}

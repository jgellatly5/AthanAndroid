package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallopdevs.athanhelper.data.PrayerInfo
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.domain.GetNextPrayerTimeUseCase
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
    private val getNextPrayerTimeUseCase: GetNextPrayerTimeUseCase,
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

    private val _nextPrayerTimeUiState =
        MutableStateFlow<NextPrayerTimeUiState>(NextPrayerTimeUiState.Loading)
    val nextPrayerTimeUiState: StateFlow<NextPrayerTimeUiState> = _nextPrayerTimeUiState.asStateFlow()

    private val _prayerTimesUiState =
        MutableStateFlow<PrayerTimesUiState>(PrayerTimesUiState.Loading)
    val prayerTimesUiState: StateFlow<PrayerTimesUiState> = _prayerTimesUiState.asStateFlow()

    init {
        getNextPrayerTime()
        getPrayerTimesForWeek()
    }

    fun getNextPrayerTime() {
        viewModelScope.launch {
            try {
                getNextPrayerTimeUseCase().collect { result ->
                    when (result) {
                        Result.Loading -> _nextPrayerTimeUiState.update { NextPrayerTimeUiState.Loading }

                        is Result.Success -> _nextPrayerTimeUiState.update { NextPrayerTimeUiState.Success(result.data) }

                        is Result.Error -> _nextPrayerTimeUiState.update { NextPrayerTimeUiState.Error("Result Error") }
                    }
                }
            } catch (e: Exception) {
                _nextPrayerTimeUiState.update { NextPrayerTimeUiState.Error("Coroutine Error") }
            }
        }
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

sealed class NextPrayerTimeUiState {
    data class Success(val nextPrayerTime: Long) : NextPrayerTimeUiState()
    data class Error(val message: String) : NextPrayerTimeUiState()
    data object Loading : NextPrayerTimeUiState()
}

sealed class PrayerTimesUiState {
    data class Success(val prayerTimesList: List<PrayerTimes>) : PrayerTimesUiState()
    data class Error(val message: String) : PrayerTimesUiState()
    data object Loading : PrayerTimesUiState()
}

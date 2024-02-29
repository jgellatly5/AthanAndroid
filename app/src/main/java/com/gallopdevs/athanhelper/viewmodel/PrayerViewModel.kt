package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallopdevs.athanhelper.data.PrayerInfo
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.GetDatesUseCase
import com.gallopdevs.athanhelper.domain.GetPrayerTimesForWeekUseCase
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
    private val getDatesUseCase: GetDatesUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

    private lateinit var timingsResponses: List<TimingsResponse?>

    private val _datesUiState = MutableStateFlow<DatesUiState>(DatesUiState.Loading)
    val datesUiState: StateFlow<DatesUiState> = _datesUiState.asStateFlow()

    private val _timingsResponseUiState =
        MutableStateFlow<TimingsResponseUiState>(TimingsResponseUiState.Loading)
    val timingsResponseUiState: StateFlow<TimingsResponseUiState> = _timingsResponseUiState.asStateFlow()

    init {
        getPrayerTimesForWeek()
    }

    fun fetchTimingsResponseForIndex(pageIndex: Int) {
        _timingsResponseUiState.update { TimingsResponseUiState.Loading }
        if (pageIndex < timingsResponses.size) {
            val timingsResponse = timingsResponses[pageIndex]
            if (timingsResponse != null) {
                _timingsResponseUiState.update { TimingsResponseUiState.Success(timingsResponse) }
            } else {
                _timingsResponseUiState.update { TimingsResponseUiState.Error("Null Response") }
            }
        } else {
            _timingsResponseUiState.update { TimingsResponseUiState.Error("Index Out Of Bounds") }
        }
    }

    fun getPrayerTimesForWeek() {
        viewModelScope.launch {
            try {
                val dates = getDatesUseCase(pattern = "EEEE, MM/dd")
                val timingsResponsesFlow = getPrayerTimesForWeekUseCase()
                timingsResponsesFlow.collect { result ->
                    when (result) {
                        Result.Loading -> _datesUiState.update { DatesUiState.Loading }

                        is Result.Success -> {
                            timingsResponses = result.data
                            _datesUiState.update { DatesUiState.Success(dates) }
                        }

                        is Result.Error -> _datesUiState.update { DatesUiState.Error("Result Error") }
                    }
                }
            } catch (e: Exception) {
                _datesUiState.update { DatesUiState.Error("Coroutine Error") }
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

sealed class DatesUiState {
    data class Success(val dates: List<String>) : DatesUiState()
    data class Error(val message: String) : DatesUiState()
    data object Loading : DatesUiState()
}

sealed class TimingsResponseUiState {
    data class Success(val timingsResponse: TimingsResponse) : TimingsResponseUiState()
    data class Error(val message: String) : TimingsResponseUiState()
    data object Loading : TimingsResponseUiState()
}

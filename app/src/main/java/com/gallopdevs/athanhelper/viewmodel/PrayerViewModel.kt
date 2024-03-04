package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.domain.GetPrayerInfoUseCase
import com.gallopdevs.athanhelper.domain.PrayerInfo
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
    private val getPrayerInfoUseCase: GetPrayerInfoUseCase,
    private val prayerRepo: PrayerRepo
) : ViewModel() {

    private val _prayerInfoUiState =
        MutableStateFlow<PrayerInfoUiState>(PrayerInfoUiState.Loading)
    val prayerInfoUiState: StateFlow<PrayerInfoUiState> = _prayerInfoUiState.asStateFlow()

    init {
        getPrayerInfo()
    }

    fun getPrayerInfo() {
        viewModelScope.launch {
            try {
                getPrayerInfoUseCase().collect { result ->
                    when (result) {
                        Result.Loading -> _prayerInfoUiState.update { PrayerInfoUiState.Loading }

                        is Result.Success -> _prayerInfoUiState.update {
                            PrayerInfoUiState.Success(
                                result.data
                            )
                        }

                        is Result.Error -> _prayerInfoUiState.update { PrayerInfoUiState.Error("Result Error") }
                    }
                }
            } catch (e: Exception) {
                _prayerInfoUiState.update { PrayerInfoUiState.Error("Coroutine Error") }
            }
        }
    }

    fun setLocation(latitude: Double, longitude: Double) =
        prayerRepo.setLocation(latitude, longitude)

    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    ) = prayerRepo.setCalculations(calcMethod, asrJuristic, adjustHighLats, timeFormat)
}

sealed class PrayerInfoUiState {
    data class Success(val prayerInfo: PrayerInfo) : PrayerInfoUiState()
    data class Error(val message: String) : PrayerInfoUiState()
    data object Loading : PrayerInfoUiState()
}

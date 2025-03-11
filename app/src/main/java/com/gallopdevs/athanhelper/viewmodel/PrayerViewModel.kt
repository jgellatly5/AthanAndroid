package com.gallopdevs.athanhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.domain.GetLocationUpdatesUseCase
import com.gallopdevs.athanhelper.domain.GetPrayerInfoUseCase
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerViewModel @Inject constructor(
    private val getLocationUpdatesUseCase: GetLocationUpdatesUseCase,
    private val getPrayerInfoUseCase: GetPrayerInfoUseCase,
) : ViewModel() {

    private val _locationState = MutableStateFlow<LocationResult?>(null)
    val locationState: StateFlow<LocationResult?> = _locationState.asStateFlow()

    private val _prayerInfoUiState =
        MutableStateFlow<PrayerInfoUiState>(PrayerInfoUiState.Loading)
    val prayerInfoUiState: StateFlow<PrayerInfoUiState> = _prayerInfoUiState.asStateFlow()

    init {
        getLocationUpdates()
        getPrayerInfo()
    }

    fun getLocationUpdates() {
        viewModelScope.launch {
            getLocationUpdatesUseCase.invoke().collect { locationResult ->
                _locationState.update { locationResult }
            }
        }
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
}

sealed class PrayerInfoUiState {
    data class Success(val prayerInfo: PrayerInfo) : PrayerInfoUiState()
    data class Error(val message: String) : PrayerInfoUiState()
    data object Loading : PrayerInfoUiState()
}

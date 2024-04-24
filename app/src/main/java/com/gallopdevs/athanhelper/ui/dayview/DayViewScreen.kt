package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_VIEW_SCREEN
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.LOADING_STATE
import com.gallopdevs.athanhelper.viewmodel.PrayerInfoUiState
import com.gallopdevs.athanhelper.viewmodel.PrayerViewModel

@Composable
fun DayViewScreen(
    pageIndex: Int?,
    prayerViewModel: PrayerViewModel = hiltViewModel()
) {
    pageIndex?.let {
        val prayerInfoUiState by prayerViewModel.prayerInfoUiState.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(DAY_VIEW_SCREEN),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (prayerInfoUiState) {
                is PrayerInfoUiState.Loading -> {
                    LoadingIndicator(testTag = LOADING_STATE)
                }

                is PrayerInfoUiState.Success -> {
                    val prayerInfo = (prayerInfoUiState as PrayerInfoUiState.Success).prayerInfo
                    val nextPrayer = prayerInfo.nextPrayerTime.nextPrayer
                    val prayerTimesList = prayerInfo.prayerTimesList
                    DayOfWeekPlusDateHeader(dayOfWeekPlusDate = prayerTimesList[it].date)
                    prayerTimesList[it].timingsResponse.timings?.let { timings ->
                        for ((name, time) in timings) {
                            time?.let {
                                PrayerRow(
                                    prayerTitle = name,
                                    prayerTime = time,
                                    showHighlighted = nextPrayer.name == name
                                )
                            }
                        }
                    }
                }

                is PrayerInfoUiState.Error -> {
                    ErrorMessage(message = (prayerInfoUiState as PrayerInfoUiState.Error).message)
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator(testTag: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Center)
                .testTag(testTag)
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .width(300.dp)
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}

object DayViewScreenConstants {
    const val DAY_VIEW_SCREEN = "DAY_VIEW_SCREEN"
    const val NEXT_PRAYER = "NEXT_PRAYER"
    const val LOADING_STATE = "LOADING_STATE"
}

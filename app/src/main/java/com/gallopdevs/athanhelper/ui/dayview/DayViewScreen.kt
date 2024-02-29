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
import androidx.compose.runtime.LaunchedEffect
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
import com.gallopdevs.athanhelper.viewmodel.DatesUiState
import com.gallopdevs.athanhelper.viewmodel.PrayerViewModel
import com.gallopdevs.athanhelper.viewmodel.TimingsResponseUiState

@Composable
fun DayViewScreen(
    pageIndex: Int?,
    prayerViewModel: PrayerViewModel = hiltViewModel()
) {
    pageIndex?.let {
        val datesUiState by prayerViewModel.datesUiState.collectAsState()
        val timingsResponseUiState by prayerViewModel.timingsResponseUiState.collectAsState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(DAY_VIEW_SCREEN),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (datesUiState) {
                is DatesUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Center))
                    }
                }

                is DatesUiState.Success -> {
                    val dates = (datesUiState as DatesUiState.Success).dates
                    DayOfWeekPlusDateHeader(dayOfWeekPlusDate = dates[it])
                    LaunchedEffect(pageIndex) {
                        prayerViewModel.fetchTimingsResponseForIndex(pageIndex)
                    }
                }

                is DatesUiState.Error -> {
                    ErrorMessage(message = (datesUiState as DatesUiState.Error).message)
                }
            }
            when (timingsResponseUiState) {
                TimingsResponseUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Center))
                    }
                }

                is TimingsResponseUiState.Success -> {
                    val timingsResponse =
                        (timingsResponseUiState as TimingsResponseUiState.Success).timingsResponse
                    timingsResponse.timings?.let { timings ->
                        for ((name, time) in timings) {
                            time?.let {
                                PrayerRow(
                                    prayerTitle = name,
                                    prayerTime = time,
                                    showHighlighted = false
                                )
                            }
                        }
                    }
                }

                is TimingsResponseUiState.Error -> {
                    ErrorMessage(message = (timingsResponseUiState as TimingsResponseUiState.Error).message)
                }
            }
        }
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
}

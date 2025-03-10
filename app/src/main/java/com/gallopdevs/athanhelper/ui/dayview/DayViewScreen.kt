package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_OF_WEEK_PLUS_DATE_HEADER
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_VIEW_SCREEN
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.LOADING_STATE
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.PRAYER_ROW
import com.gallopdevs.athanhelper.ui.shared.ErrorMessage
import com.gallopdevs.athanhelper.ui.shared.LoadingIndicator
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.PrayerInfoUiState

@Composable
fun DayViewScreen(
    pageIndex: Int?,
    prayerInfoUiState: PrayerInfoUiState
) {
    pageIndex?.let {
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
                    val prayerInfo = prayerInfoUiState.prayerInfo
                    val nextPrayer = prayerInfo.nextPrayerTime.nextPrayer
                    val prayerTimesList = prayerInfo.prayerTimesList
                    DayOfWeekPlusDateHeader(
                        dayOfWeekPlusDate = prayerTimesList[it].date,
                        testTag = DAY_OF_WEEK_PLUS_DATE_HEADER
                    )
                    prayerTimesList[it].timingsResponse.timings?.let { timings ->
                        for ((name, time) in timings) {
                            time?.let {
                                PrayerRow(
                                    prayerTitle = name,
                                    prayerTime = time,
                                    showHighlighted = nextPrayer.name == name,
                                    testTag = PRAYER_ROW
                                )
                            }
                        }
                    }
                }

                is PrayerInfoUiState.Error -> {
                    ErrorMessage(message = prayerInfoUiState.message)
                }
            }
        }
    }
}


object DayViewScreenConstants {
    const val DAY_VIEW_SCREEN = "DAY_VIEW_SCREEN"
    const val NEXT_PRAYER = "NEXT_PRAYER"
    const val LOADING_STATE = "LOADING_STATE"
    const val DAY_OF_WEEK_PLUS_DATE_HEADER = "DAY_OF_WEEK_PLUS_DATE_HEADER"
    const val PRAYER_ROW = "PRAYER_ROW"
}

@Preview(showBackground = true)
@Composable
private fun DayViewScreenPreview() {
    AthanHelperTheme {
        val prayerInfoUiState = PrayerInfoUiState.Success(
            prayerInfo = PrayerInfo.test(
                nextPrayerTime = NextPrayerTime.test(
                    nextPrayerTimeMillis = 10000,
                    nextPrayer = NextPrayer.test(
                        name = "Fajr",
                        index = 0
                    )
                ),
                prayerTimesList = listOf(
                    PrayerTimes.test(
                        date = "24 Apr 2024",
                        timingsResponse = TimingsResponse.test()
                    )
                )
            )
        )
        DayViewScreen(
            pageIndex = 0,
            prayerInfoUiState = prayerInfoUiState
        )
    }
}

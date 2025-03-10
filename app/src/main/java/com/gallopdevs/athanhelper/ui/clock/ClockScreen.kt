@file:OptIn(ExperimentalFoundationApi::class)

package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.DAYS_IN_WEEK
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreen
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.PrayerInfoUiState
import com.gallopdevs.athanhelper.viewmodel.PrayerViewModel
import com.gallopdevs.athanhelper.viewmodel.SettingsViewModel

@Composable
fun ClockScreen(
    prayerViewModel: PrayerViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val prayerInfoUiState by prayerViewModel.prayerInfoUiState.collectAsState()
    val enableNotifications = settingsViewModel.getBoolean(ENABLE_NOTIFICATIONS, false)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { DAYS_IN_WEEK })
    ClockScreenContent(
        prayerInfoUiState = prayerInfoUiState,
        enableNotifications = enableNotifications,
        pagerState = pagerState
    )
}

@Composable
private fun ClockScreenContent(
    prayerInfoUiState: PrayerInfoUiState,
    enableNotifications: Boolean,
    pagerState: PagerState
) {
    Column {
        NextPrayerHeader(
            prayerInfoUiState = prayerInfoUiState,
            enableNotifications = enableNotifications
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) {
            DayViewScreen(
                pageIndex = it,
                prayerInfoUiState = prayerInfoUiState
            )
        }
        TabDots(state = pagerState)
    }
}

object ClockScreenConstants {
    const val DAYS_IN_WEEK = 7
}

@Preview(showBackground = true)
@Composable
private fun ClockScreenContentPreview() {
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
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { DAYS_IN_WEEK })
        ClockScreenContent(
            prayerInfoUiState = prayerInfoUiState,
            enableNotifications = false,
            pagerState = pagerState
        )
    }
}

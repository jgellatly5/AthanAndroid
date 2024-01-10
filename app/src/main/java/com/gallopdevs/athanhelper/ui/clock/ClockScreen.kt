package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.DAYS_IN_WEEK
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClockScreen() {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { DAYS_IN_WEEK })
    Column {
        NextPrayerHeader()
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) {
            DayViewScreen(
                pageIndex = it
            )
        }
        TabDots(state = pagerState)
    }
}

object ClockScreenConstants {
    const val DAYS_IN_WEEK = 7
}

package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.DAYS_IN_WEEK
import com.gallopdevs.athanhelper.ui.clock.TabDots
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_OF_WEEK_PLUS_DATE_HEADER
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_VIEW_SCREEN
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.PRAYER_ROW
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun DayViewScreen(
    pageIndex: Int?,
    prayerInfo: PrayerInfo,
    pagerState: PagerState
) {
    pageIndex?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(DAY_VIEW_SCREEN),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            TabDots(state = pagerState)
        }
    }
}


object DayViewScreenConstants {
    const val DAY_VIEW_SCREEN = "DAY_VIEW_SCREEN"
    const val NEXT_PRAYER = "NEXT_PRAYER"
    const val DAY_OF_WEEK_PLUS_DATE_HEADER = "DAY_OF_WEEK_PLUS_DATE_HEADER"
    const val PRAYER_ROW = "PRAYER_ROW"
}

@Preview(showBackground = true)
@Composable
private fun DayViewScreenPreview() {
    AthanHelperTheme {
        val prayerInfo = PrayerInfo.test(
            prayerTimesList = listOf(
                PrayerTimes.test(
                    date = "24 Apr 2024",
                    timingsResponse = TimingsResponse.test()
                )
            )
        )
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { DAYS_IN_WEEK })
        DayViewScreen(
            pageIndex = 0,
            prayerInfo = prayerInfo,
            pagerState = pagerState
        )
    }
}

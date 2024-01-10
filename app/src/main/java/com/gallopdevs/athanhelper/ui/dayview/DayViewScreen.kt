package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_VIEW_SCREEN
import com.gallopdevs.athanhelper.viewmodel.PrayerViewModel

@Composable
fun DayViewScreen(
    pageIndex: Int?,
    prayerViewModel: PrayerViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(DAY_VIEW_SCREEN),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val prayerTitles = stringArrayResource(id = R.array.prayer_titles)
        pageIndex?.let {
            val prayerTimesInfo = prayerViewModel.getPrayerTimesInfo()
            val prayerTimesForDate = prayerTimesInfo.prayerTimesForDate[pageIndex]
            DayOfWeekPlusDateHeader(
                dayOfWeekPlusDate = prayerTimesInfo.dates[it]
            )
            val nextTimeIndex = prayerViewModel.getNextTimeInfo().nextTimeIndex
            for (i in prayerTimesForDate.indices) {
                PrayerRow(
                    prayerTitle = prayerTitles[i],
                    prayerTime = prayerTimesForDate[i][0],
                    prayerTimePostFix = prayerTimesForDate[i][1],
                    showHighlighted = i == nextTimeIndex
                )
            }
        }
    }
}

object DayViewScreenConstants {
    const val DAY_VIEW_SCREEN = "DAY_VIEW_SCREEN"
    const val NEXT_PRAYER = "NEXT_PRAYER"
}

package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_VIEW_SCREEN

@Composable
fun DayViewScreen(
    weekDay: Int?,
    month: Int?,
    dayOfMonth: Int?,
    pageIndex: Int?,
    clockViewModel: ClockViewModel = viewModel()
) {
    clockViewModel.setTimeFormat()
    Column(
        modifier = Modifier
            .testTag(DAY_VIEW_SCREEN)
    ) {
        if (weekDay != null && month != null && dayOfMonth != null) {
            DayOfWeekPlusDateHeader(
                dayOfWeekPlusDate = clockViewModel.formatDate(
                    weekDay,
                    month,
                    dayOfMonth
                )
            )
        }
        val prayerTitles = stringArrayResource(id = R.array.prayer_titles)
        pageIndex?.let {
            val prayerTimes = clockViewModel.formatTimes(it)
            val nextTimeIndex = clockViewModel.getNextTimeIndex()
            for (i in prayerTimes.indices) {
                val showHighlighted = i == nextTimeIndex
                PrayerRow(
                    prayerTitle = prayerTitles[i],
                    prayerTime = prayerTimes[i][0],
                    prayerTimePostFix = prayerTimes[i][1],
                    showHighlighted = showHighlighted
                )
            }
        }
    }
}

object DayViewScreenConstants {
    const val DAY_VIEW_SCREEN = "DAY_VIEW_SCREEN"
    const val NEXT_PRAYER = "NEXT_PRAYER"
}

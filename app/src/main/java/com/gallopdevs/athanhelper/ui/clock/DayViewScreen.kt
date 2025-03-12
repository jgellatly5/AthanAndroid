package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.DAYS_IN_WEEK
import com.gallopdevs.athanhelper.ui.clock.DayViewScreenConstants.DAY_OF_WEEK_PLUS_DATE_HEADER
import com.gallopdevs.athanhelper.ui.clock.DayViewScreenConstants.DAY_VIEW_SCREEN
import com.gallopdevs.athanhelper.ui.clock.DayViewScreenConstants.PRAYER_ROW
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun DayViewScreen(
    pageIndex: Int?,
    prayerInfo: PrayerInfo
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
                dayOfWeekPlusDate = prayerTimesList[it].date
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
    }
}

@Composable
private fun DayOfWeekPlusDateHeader(
    dayOfWeekPlusDate: String
) {
    Text(
        text = dayOfWeekPlusDate,
        fontSize = dimensionResource(id = R.dimen.day_text_size).value.sp,
        color = colorResource(id = R.color.colorPrimaryDark),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth()
            .testTag(DAY_OF_WEEK_PLUS_DATE_HEADER)
    )
}

@Composable
fun PrayerRow(
    prayerTitle: String,
    prayerTime: String,
    prayerTimePostFix: String? = null,
    showHighlighted: Boolean,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 60.dp,
                    vertical = 10.dp
                )
        ) {
            HighlightedPrayer(
                prayerTitle = prayerTitle,
                showHighlighted = showHighlighted
            )
            PrayerTime(
                prayerTime = prayerTime,
                prayerTimePostFix = prayerTimePostFix
            )
        }
        if (prayerTitle != "Midnight") {
            HorizontalDivider(
                modifier = Modifier
                    .padding(start = 60.dp, end = 60.dp)
            )
        }
    }
}

@Composable
private fun HighlightedPrayer(
    prayerTitle: String,
    showHighlighted: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showHighlighted) {
            Image(
                painterResource(id = R.drawable.green_oval),
                contentDescription = DayViewScreenConstants.NEXT_PRAYER,
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 20.dp)
            )
        }
        Text(
            text = prayerTitle,
            fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
        )
    }
}

@Composable
private fun PrayerTime(
    prayerTime: String,
    prayerTimePostFix: String? = null
) {
    Row {
        Text(
            text = prayerTime,
            fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
        )
        prayerTimePostFix?.let {
            Text(
                text = it,
                fontSize = dimensionResource(id = R.dimen.postfix_text_size).value.sp,
                color = colorResource(id = R.color.colorPrimaryDark)
            )
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
                    date = "11 Mar 2025",
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

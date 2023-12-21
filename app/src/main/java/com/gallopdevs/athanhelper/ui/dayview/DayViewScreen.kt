package com.gallopdevs.athanhelper.ui.dayview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.clock.ClockViewModel
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_VIEW_SCREEN
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.NEXT_PRAYER
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

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
            .padding(top = 32.dp, bottom = 40.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekPlusDateHeaderPreview() {
    AthanHelperTheme {
        DayOfWeekPlusDateHeader(
            dayOfWeekPlusDate = stringResource(id = R.string.day_placeholder)
        )
    }
}

@Composable
fun PrayerName(
    prayerTitle: String,
    showHighlighted: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showHighlighted) {
            Image(
                painterResource(id = R.drawable.green_oval),
                contentDescription = NEXT_PRAYER,
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

@Preview(showBackground = true)
@Composable
private fun PrayerNamePreview() {
    AthanHelperTheme {
        PrayerName(
            prayerTitle = stringResource(id = R.string.dawn),
            showHighlighted = true
        )
    }
}

@Composable
private fun PrayerTime(
    prayerTime: String,
    prayerTimePostFix: String
) {
    Row {
        Text(
            text = prayerTime,
            fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
        )
        Text(
            text = prayerTimePostFix,
            fontSize = dimensionResource(id = R.dimen.postfix_text_size).value.sp,
            color = colorResource(id = R.color.colorPrimaryDark)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrayerTimePreview() {
    AthanHelperTheme {
        PrayerTime(
            prayerTime = stringResource(id = R.string.dawn_time_placeholder),
            prayerTimePostFix = stringResource(id = R.string.postfix_am)
        )
    }
}

@Composable
private fun PrayerRow(
    prayerTitle: String,
    prayerTime: String,
    prayerTimePostFix: String,
    showHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 60.dp, end = 60.dp)
        ) {
            PrayerName(
                prayerTitle = prayerTitle,
                showHighlighted = showHighlighted
            )
            PrayerTime(
                prayerTime = prayerTime,
                prayerTimePostFix = prayerTimePostFix
            )
        }
        if (prayerTitle != stringResource(id = R.string.night)) {
            Divider(
                modifier = modifier
                    .padding(start = 60.dp, end = 60.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrayerRowPreview() {
    AthanHelperTheme {
        PrayerRow(
            prayerTitle = stringResource(id = R.string.dawn),
            prayerTime = stringResource(id = R.string.dawn_time_placeholder),
            prayerTimePostFix = stringResource(id = R.string.postfix_am),
            showHighlighted = true
        )
    }
}

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

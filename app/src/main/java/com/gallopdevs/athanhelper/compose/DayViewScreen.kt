package com.gallopdevs.athanhelper.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallopdevs.athanhelper.R

@Composable
private fun DayOfWeekPlusDateHeader() {
    Text(
        text = stringResource(id = R.string.day_placeholder),
        fontSize = dimensionResource(id = R.dimen.day_text_size).value.sp,
        color = colorResource(id = R.color.colorPrimaryDark),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun DayOfWeekPlusDateHeaderPreview() {
    DayOfWeekPlusDateHeader()
}

@Composable
private fun PrayerRow(
    prayerTitle: String,
    prayerTime: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 60.dp, end = 60.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            Text(
                text = prayerTitle,
                fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
                color = colorResource(id = R.color.colorPrimaryDark)
            )
            Text(
                text = prayerTime,
                fontSize = dimensionResource(id = R.dimen.prayer_name_text_size).value.sp,
                color = colorResource(id = R.color.colorPrimaryDark)
            )
        }
        Divider()
    }
}

@Preview(showBackground = true)
@Composable
private fun PrayerRowPreview() {
    PrayerRow(
        prayerTitle = stringResource(id = R.string.dawn),
        prayerTime = stringResource(id = R.string.dawn_time_placeholder)
    )
}

@Composable
fun DayViewScreen() {
    Column {
        DayOfWeekPlusDateHeader()
        PrayerRow(
            prayerTitle = stringResource(id = R.string.dawn),
            prayerTime = stringResource(id = R.string.dawn_time_placeholder),
            contentPadding = PaddingValues(top = 40.dp, start = 60.dp, end = 60.dp)
        )
        PrayerRow(
            prayerTitle = stringResource(id = R.string.mid_day),
            prayerTime = stringResource(id = R.string.midday_text_placeholder)
        )
        PrayerRow(
            prayerTitle = stringResource(id = R.string.afternoon),
            prayerTime = stringResource(id = R.string.afternoon_time_placeholder)
        )
        PrayerRow(
            prayerTitle = stringResource(id = R.string.sunset),
            prayerTime = stringResource(id = R.string.sunset_time_placeholder)
        )
        PrayerRow(
            prayerTitle = stringResource(id = R.string.night),
            prayerTime = stringResource(id = R.string.night_text_placeholder)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DayViewScreenPreview() {
    DayViewScreen()
}

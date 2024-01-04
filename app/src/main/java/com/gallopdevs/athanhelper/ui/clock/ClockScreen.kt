package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.NUM_ITEMS
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreen
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun NextPrayerHeader() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.moon),
            contentDescription = "Moon",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
        Column {
            Text(
                text = stringResource(id = R.string.next_prayer),
                fontSize = dimensionResource(id = R.dimen.next_prayer_text_size).value.sp
            )
            Text(
                text = "00:12:40s",
                fontSize = dimensionResource(id = R.dimen.prayer_timer_text_size).value.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NextPrayerHeaderPreview() {
    AthanHelperTheme {
        NextPrayerHeader()
    }
}

@Composable
fun ClockScreen(
//    clockViewModel: ClockViewModel = hiltViewModel()
) {
    Column {
        NextPrayerHeader()
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(NUM_ITEMS) {
                DayViewScreen(
                    pageIndex = it
                )
            }
        }
    }
}

object ClockScreenConstants {
    const val NUM_ITEMS = 7
}

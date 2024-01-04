package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
fun TabElement(index: Int, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Tab(
        text = { Text(text = "") },
        selected = index == selectedTabIndex,
        onClick = { onTabSelected(index) },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabDots() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { NUM_ITEMS })
    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            // Custom indicator
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                color = colorResource(id = R.color.colorPrimaryDark)
            )
        }
    ) {
        // Create tabs
        for (i in 0 until NUM_ITEMS) {
            TabElement(index = i, selectedTabIndex = selectedTabIndex) {
                selectedTabIndex = it
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabDotsPreview() {
    AthanHelperTheme {
        TabDots()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClockScreen(
//    clockViewModel: ClockViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { NUM_ITEMS })
    Column {
        NextPrayerHeader()
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) {
            DayViewScreen(
                pageIndex = it
            )
        }
        TabDots()
    }
}

object ClockScreenConstants {
    const val NUM_ITEMS = 7
}

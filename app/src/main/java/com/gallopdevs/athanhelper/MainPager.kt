package com.gallopdevs.athanhelper

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.MainPagerConstants.NUM_ITEMS
import com.gallopdevs.athanhelper.ui.clock.ClockScreen
import com.gallopdevs.athanhelper.ui.settings.SettingsScreen
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPager() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { NUM_ITEMS })
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = R.drawable.athan),
            contentDescription = stringResource(id = R.string.app_name),
            alignment = Alignment.Center,
            modifier = Modifier.size(100.dp)
        )
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) {
            when (it) {
                0 -> ClockScreen()
                1 -> SettingsScreen()
            }
        }
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
            for (i in 0 until NUM_ITEMS) {
                TabElement(index = i, selectedTabIndex = selectedTabIndex) {
                    selectedTabIndex = it
                }
            }
        }
    }
}

@Composable
private fun TabElement(
    index: Int,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Tab(
        icon = { TabIcon(index) },
        selected = index == selectedTabIndex,
        onClick = { onTabSelected(index) },
        modifier = Modifier
            .background(colorResource(id = R.color.colorPrimary))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun TabElementPreview() {
    AthanHelperTheme {
        TabElement(
            index = 0,
            selectedTabIndex = 0,
            onTabSelected = {}
        )
    }
}

@Composable
private fun TabIcon(index: Int) {
    val painterId = when (index) {
        0 -> R.drawable.clock_icon
        else -> R.drawable.settings_icon
    }
    val contentDescription = when (index) {
        0 -> "clock"
        else -> "settings"
    }
    Image(
        painterResource(id = painterId),
        contentDescription = contentDescription,
        modifier = Modifier.size(30.dp)
    )
}

object MainPagerConstants {
    const val NUM_ITEMS = 2
}

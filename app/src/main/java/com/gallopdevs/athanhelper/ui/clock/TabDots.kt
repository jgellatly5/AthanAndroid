package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
private fun TabImage() {
    Image(
        painterResource(id = R.drawable.grey_oval), contentDescription = null,
        modifier = Modifier
            .size(10.dp)
            .background(
                color = Color.Black,
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun TabImagePreview() {
    AthanHelperTheme {
        TabImage()
    }
}

@Composable
private fun TabElement(index: Int, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Tab(
        icon = { TabImage() },
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
        TabElement(index = 0, selectedTabIndex = 0, onTabSelected = {})
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabDots(state: PagerState) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            // Custom indicator
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[state.currentPage]),
                color = colorResource(id = R.color.colorPrimaryDark)
            )
        }
    ) {
        // Create tabs
        for (i in 0 until ClockScreenConstants.NUM_ITEMS) {
            TabElement(index = i, selectedTabIndex = selectedTabIndex) {
                selectedTabIndex = it
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun TabDotsPreview() {
    AthanHelperTheme {
        val pagerState =
            rememberPagerState(initialPage = 0, pageCount = { ClockScreenConstants.NUM_ITEMS })
        TabDots(state = pagerState)
    }
}

package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun TabbedViewPager() {
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    TabRowWithViewPager(tabs)
}

@Composable
fun TabRow(tabs: List<String>, index: Int, selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Tab(
        text = { Text(text = tabs[index]) },
        selected = index == selectedTabIndex,
        onClick = { onTabSelected(index) },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabRowWithViewPager(tabs: List<String>) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 7 })
    Column {
        // TabRow
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
            tabs.forEachIndexed { index, _ ->
                TabRow(tabs, index = index, selectedTabIndex = selectedTabIndex) {
                    selectedTabIndex = it
                }
            }
        }

        // ViewPager
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            // Content for each page
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(text = "Page ${page + 1}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabExamplePreview() {
    AthanHelperTheme {
        TabbedViewPager()
    }
}

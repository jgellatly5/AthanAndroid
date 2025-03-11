package com.gallopdevs.athanhelper.ui.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme

@Composable
fun TabDots(state: PagerState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until ClockScreenConstants.DAYS_IN_WEEK) {
            Dot(isSelected = i == state.currentPage)
        }
    }
}

@Composable
private fun Dot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .padding(horizontal = 4.dp)
            .background(
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun TabDotsPreview() {
    AthanHelperTheme {
        val pagerState =
            rememberPagerState(initialPage = 0, pageCount = { ClockScreenConstants.DAYS_IN_WEEK })
        TabDots(state = pagerState)
    }
}

@file:OptIn(ExperimentalMaterial3Api::class)

package com.gallopdevs.athanhelper

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gallopdevs.athanhelper.MainPagerConstants.NUM_ITEMS
import com.gallopdevs.athanhelper.ui.clock.ClockScreen
import com.gallopdevs.athanhelper.ui.settings.SettingsScreen
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import kotlinx.coroutines.launch

@Composable
fun MainPager() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { NUM_ITEMS })
    Scaffold(
        topBar = { AthanTopAppBar() },
        bottomBar = {
            AthanBottomAppBar(
                onClockClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                onSettingsClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) {
                when (it) {
                    0 -> ClockScreen()
                    1 -> SettingsScreen()
                }
            }
        }
    }
}

@Composable
private fun AthanTopAppBar() {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(id = R.drawable.athan),
                    contentDescription = stringResource(id = R.string.app_name),
                    alignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    )
}

@Composable
private fun AthanBottomAppBar(
    onClockClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    BottomAppBar(
        containerColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = onClockClick
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Clock"
                )
            }
            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    }
}

object MainPagerConstants {
    const val NUM_ITEMS = 2
}

@Preview(showBackground = true)
@Composable
private fun AthanTopAppBarPreview() {
    AthanHelperTheme {
        AthanTopAppBar()
    }
}

@Preview(showBackground = true)
@Composable
private fun AthanBottomAppBarPreview() {
    AthanHelperTheme {
        AthanBottomAppBar(onClockClick = {}, onSettingsClick = {})
    }
}

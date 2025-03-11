@file:OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)

package com.gallopdevs.athanhelper.ui.clock

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDE
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LONGITUDE
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.DAYS_IN_WEEK
import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants.LOADING_STATE
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreen
import com.gallopdevs.athanhelper.ui.shared.ErrorMessage
import com.gallopdevs.athanhelper.ui.shared.LoadingIndicator
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.PrayerInfoUiState
import com.gallopdevs.athanhelper.viewmodel.PrayerViewModel
import com.gallopdevs.athanhelper.viewmodel.SettingsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
fun ClockScreen(
    prayerViewModel: PrayerViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val locationState by prayerViewModel.locationState.collectAsState()
    val prayerInfoUiState by prayerViewModel.prayerInfoUiState.collectAsState()
    val enableNotifications = settingsViewModel.getBoolean(ENABLE_NOTIFICATIONS, false)
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { DAYS_IN_WEEK })

    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var hasRequestedPermissions by rememberSaveable { mutableStateOf(false) }
    var permissionRequestCompleted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (hasRequestedPermissions) {
            permissionRequestCompleted = permissionsState.revokedPermissions.isNotEmpty()
        }
    }

    when {
        permissionsState.allPermissionsGranted -> {
            when {
                locationState != null -> {
                    settingsViewModel.apply {
                        saveString(LATITUDE, locationState!!.lastLocation?.latitude.toString())
                        saveString(LONGITUDE, locationState!!.lastLocation?.longitude.toString())
                    }
                    when (prayerInfoUiState) {
                        PrayerInfoUiState.Loading -> LoadingIndicator(testTag = LOADING_STATE)

                        is PrayerInfoUiState.Success -> {
                            val prayerInfo =
                                (prayerInfoUiState as PrayerInfoUiState.Success).prayerInfo
                            ClockScreenContent(
                                nextPrayerTime = prayerInfo.nextPrayerTime,
                                prayerInfo = prayerInfo,
                                enableNotifications = enableNotifications,
                                pagerState = pagerState
                            )
                        }

                        is PrayerInfoUiState.Error -> ErrorMessage(message = (prayerInfoUiState as PrayerInfoUiState.Error).message)
                    }

                }

                else -> {
                    Text("We could not find your location.")
                }
            }

        }

        permissionsState.shouldShowRationale -> {
            PermissionRationale(
                onClick = {
                    permissionsState.launchMultiplePermissionRequest()
                    hasRequestedPermissions = true
                }
            )
        }

        else -> {
            when {
                permissionRequestCompleted -> {
                    PermissionDenied()
                }

                else -> {
                    PermissionRationale(
                        onClick = {
                            permissionsState.launchMultiplePermissionRequest()
                            hasRequestedPermissions = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ClockScreenContent(
    nextPrayerTime: NextPrayerTime,
    prayerInfo: PrayerInfo,
    enableNotifications: Boolean,
    pagerState: PagerState
) {
    Column {
        NextPrayerHeader(
            nextPrayerTime = nextPrayerTime,
            enableNotifications = enableNotifications
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) {
            DayViewScreen(
                pageIndex = it,
                prayerInfo = prayerInfo
            )
        }
        TabDots(state = pagerState)
    }
}

@Composable
private fun PermissionRationale(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text("Location permissions are required to use this feature.")
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = onClick
        ) {
            Text("Request Permissions")
        }
    }
}

@Composable
private fun PermissionDenied() {
    val context = LocalContext.current
    Text("Permissions denied. Please enable them in app settings to proceed.")
    Button(
        onClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Open App Settings")
    }
}

object ClockScreenConstants {
    const val DAYS_IN_WEEK = 7
    const val LOADING_STATE = "LOADING_STATE"
}

@Preview(showBackground = true)
@Composable
private fun ClockScreenContentPreview() {
    AthanHelperTheme {
        val nextPrayerTime = NextPrayerTime.test(
            nextPrayerTimeMillis = 10000,
            nextPrayer = NextPrayer.test(
                name = "Fajr",
                index = 0
            )
        )
        val prayerInfo = PrayerInfo.test(
            nextPrayerTime = NextPrayerTime.test(
                nextPrayerTimeMillis = 10000,
                nextPrayer = NextPrayer.test(
                    name = "Fajr",
                    index = 0
                )
            ),
            prayerTimesList = listOf(
                PrayerTimes.test(
                    date = "24 Apr 2024",
                    timingsResponse = TimingsResponse.test()
                )
            )
        )
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { DAYS_IN_WEEK })
        ClockScreenContent(
            nextPrayerTime = nextPrayerTime,
            prayerInfo = prayerInfo,
            enableNotifications = false,
            pagerState = pagerState
        )
    }
}

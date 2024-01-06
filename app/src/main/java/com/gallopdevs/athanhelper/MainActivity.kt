package com.gallopdevs.athanhelper

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.gallopdevs.athanhelper.MainActivity.Companion.NUM_ITEMS
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.SHAFII
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.ui.clock.ClockScreen
import com.gallopdevs.athanhelper.ui.settings.SettingsScreen
import com.gallopdevs.athanhelper.ui.theme.AthanHelperTheme
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val clockViewModel: ClockViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.forEach { actionMap ->
                when (actionMap.key) {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        if (actionMap.value) {
                            getLocation()
                        } else {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Location permissions denied.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()
        loadSettings()
        getLocation()

        setContent {
            MainPager()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val channel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun loadSettings() {
        clockViewModel.apply {
            setCalculations(
                calcMethod = getInt(CALCULATION_METHOD, JAFARI),
                asrJuristic = getInt(ASR_METHOD, SHAFII),
                adjustHighLats = getInt(LATITUDES_METHOD, MIDNIGHT)
            )
            // TODO add setting for adjusting time format
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissionLauncher.launch(permissions)
        } else {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this@MainActivity)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    clockViewModel.apply {
                        setLocation(location.latitude, location.longitude)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "We cannot find your location. Please enable in settings.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@MainActivity,
                    "We cannot find your location. Please enable in settings.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "NOTIFICATION"
        const val NUM_ITEMS = 2
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPager() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { NUM_ITEMS })
    Column {
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

@Preview(showBackground = true)
@Composable
private fun MainPagerPreview() {
    AthanHelperTheme {
        MainPager()
    }
}

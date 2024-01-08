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
import androidx.core.app.ActivityCompat
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.SHAFII
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.viewmodel.PrayerViewModel
import com.gallopdevs.athanhelper.viewmodel.SettingsViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val prayerViewModel: PrayerViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

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
                                    getString(R.string.location_permissions_denied),
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
        settingsViewModel.apply {
            prayerViewModel.setCalculations(
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
                    prayerViewModel.apply {
                        setLocation(location.latitude, location.longitude)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.cannot_find_location_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.cannot_find_location_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "NOTIFICATION"
    }
}

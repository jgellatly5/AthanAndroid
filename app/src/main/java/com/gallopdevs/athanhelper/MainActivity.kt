package com.gallopdevs.athanhelper

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.SHAFII
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.databinding.ActivityMainBinding
import com.gallopdevs.athanhelper.ui.clock.ClockFragment
import com.gallopdevs.athanhelper.ui.settings.SettingsFragment
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            createNotificationChannel()
            loadSettings()

            val mainActivityPagerAdapter = MainActivityPagerAdapter(this@MainActivity).apply {
                addFrag(ClockFragment())
                addFrag(SettingsFragment())
            }

            viewPagerActivity.apply {
                adapter = mainActivityPagerAdapter
                isUserInputEnabled = false
            }

            TabLayoutMediator(tabLayoutActivity, viewPagerActivity, true) { _, _ -> }.attach()
            tabLayoutActivity.apply {
                getTabAt(0)?.setIcon(R.drawable.clock_icon)
                getTabAt(1)?.setIcon(R.drawable.settings_icon)
            }

            getLocation()
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
                binding.apply {
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
    }
}

class MainActivityPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = ArrayList<Fragment>()

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    override fun getItemCount(): Int = fragmentList.size

    fun addFrag(fragment: Fragment) = fragmentList.add(fragment)
}

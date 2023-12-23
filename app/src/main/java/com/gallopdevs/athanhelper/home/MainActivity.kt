package com.gallopdevs.athanhelper.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.clock.ClockFragment
import com.gallopdevs.athanhelper.clock.ClockViewModel
import com.gallopdevs.athanhelper.databinding.ActivityMainBinding
import com.gallopdevs.athanhelper.model.PrayerCalculatorIpml
import com.gallopdevs.athanhelper.settings.PreferencesManager
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl.Companion.LATITUDES
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl.Companion.SETTINGS
import com.gallopdevs.athanhelper.settings.SettingsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val clockViewModel: ClockViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

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
        preferencesManager.apply {
            clockViewModel.setCalculations(
                calcMethod = getInt(CALCULATION_METHOD, PrayerCalculatorIpml.jafari),
                asrJuristic = getInt(ASR_METHOD, PrayerCalculatorIpml.shafii),
                adjustHighLats = getInt(LATITUDES, PrayerCalculatorIpml.midNight)
            )
            // TODO add setting for adjusting time format
        }
    }

    companion object {
        private const val CHANNEL_ID = "NOTIFICATION"
    }
}

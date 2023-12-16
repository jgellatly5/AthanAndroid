package com.gallopdevs.athanhelper.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.clock.ClockFragment
import com.gallopdevs.athanhelper.clock.ClockViewModel
import com.gallopdevs.athanhelper.databinding.ActivityMainBinding
import com.gallopdevs.athanhelper.model.PrayerCalculatorIpml
import com.gallopdevs.athanhelper.settings.SettingsFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ClockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            viewModel = ViewModelProvider(this@MainActivity)[ClockViewModel::class.java]

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
            tabLayoutActivity.getTabAt(0)?.setIcon(R.drawable.clock_icon)
            tabLayoutActivity.getTabAt(1)?.setIcon(R.drawable.settings_icon)
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
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        viewModel.setCalculations(
            calcMethod = sharedPreferences.getInt("calcMethod", PrayerCalculatorIpml.calcMethod),
            asrJuristic = sharedPreferences.getInt("asrMethod", PrayerCalculatorIpml.asrJuristic),
            adjustHighLats = sharedPreferences.getInt(
                "latitudes",
                PrayerCalculatorIpml.adjustHighLats
            )
        )
        // TODO add setting for adjusting time format
    }

    companion object {
        private const val CHANNEL_ID = "Notification"
    }
}

package com.gallopdevs.athanhelper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gallopdevs.athanhelper.databinding.ActivityMainBinding
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.SHAFII
import com.gallopdevs.athanhelper.ui.clock.ClockFragment
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.ui.settings.SettingsFragment
import com.gallopdevs.athanhelper.viewmodel.ClockViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val clockViewModel: ClockViewModel by viewModels()

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
        clockViewModel.apply {
            setCalculations(
                calcMethod = getInt(CALCULATION_METHOD, JAFARI),
                asrJuristic = getInt(ASR_METHOD, SHAFII),
                adjustHighLats = getInt(LATITUDES_METHOD, MIDNIGHT)
            )
            // TODO add setting for adjusting time format
        }
    }

    companion object {
        private const val CHANNEL_ID = "NOTIFICATION"
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

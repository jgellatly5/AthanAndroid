package com.gallopdevs.athanhelper.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.clock.ClockFragment
import com.gallopdevs.athanhelper.clock.DayViewAdapter
import com.gallopdevs.athanhelper.model.PrayTime
import com.gallopdevs.athanhelper.settings.SettingsFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_swiper.*

class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "Notification"
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swiper)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        createNotificationChannel()

        val dayViewAdapter = DayViewAdapter(this)
        val clockFragment = ClockFragment(dayViewAdapter)

        val mainActivityPagerAdapter = MainActivityPagerAdapter(supportFragmentManager)
        mainActivityPagerAdapter.addFrag(clockFragment)
        mainActivityPagerAdapter.addFrag(SettingsFragment())
        view_pager_activity.adapter = mainActivityPagerAdapter

        tab_layout_activity.setupWithViewPager(view_pager_activity)
        tab_layout_activity.tabGravity = TabLayout.GRAVITY_FILL
        tab_layout_activity.getTabAt(0)?.setIcon(R.drawable.clock_icon)
        tab_layout_activity.getTabAt(1)?.setIcon(R.drawable.settings_icon)

        view_pager_activity.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    val currentTimeMilliSeconds = PrayTime.currentTime
                    val getTimeDifference = clockFragment.getTimerDifference(currentTimeMilliSeconds)
                    val countDownTime = getTimeDifference[ClockFragment.nextTime]
                    dayViewAdapter.notifyDataSetChanged()
                    clockFragment.startNewTimer(countDownTime)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}

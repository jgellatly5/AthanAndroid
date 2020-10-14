package com.gallopdevs.athanhelper.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.clock.ClockFragment
import com.gallopdevs.athanhelper.clock.DayViewAdapter
import com.gallopdevs.athanhelper.model.PrayTime
import com.gallopdevs.athanhelper.settings.SettingsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_swiper.*
import kotlinx.android.synthetic.main.fragment_clock.*

class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "Notification"
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var dayViewAdapter: DayViewAdapter
    private lateinit var clockFragment: ClockFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swiper)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        createNotificationChannel()
        loadSettings()

        dayViewAdapter = DayViewAdapter(this)
        clockFragment = ClockFragment()

        getLocation()

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

    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val calcMethod = sharedPreferences.getInt("calcMethod", PrayTime.calcMethod)
        val asrMethod = sharedPreferences.getInt("asrMethod", PrayTime.asrJuristic)
        val latitudes = sharedPreferences.getInt("latitudes", PrayTime.adjustHighLats)
        PrayTime.calcMethod = calcMethod
        PrayTime.asrJuristic = asrMethod
        PrayTime.adjustHighLats = latitudes
        PrayTime.timeFormat = PrayTime.time24
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_FINE_LOCATION)
            }
            return
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            PrayTime.lat = location.latitude
                            PrayTime.lng = location.longitude

                            progress_bar.visibility = ProgressBar.INVISIBLE
                            moon_icon.visibility = ImageView.VISIBLE
                            prayer_timer_text.visibility = TextView.VISIBLE
                            next_prayer_text.visibility = TextView.VISIBLE

                            val currentTimeMilliSeconds = PrayTime.currentTime
                            clockFragment.startNewTimer(clockFragment.getTimerDifference(currentTimeMilliSeconds)[ClockFragment.nextTime])

                            view_pager_fragment.adapter = dayViewAdapter
                            TabLayoutMediator(tab_dots, view_pager_fragment, true) { _, _ -> }.attach()
                        } else {
                            Toast.makeText(this, "We cannot find your location. Please enable in settings.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "We cannot find your location. Please enable in settings.", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_FINE_LOCATION ->
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    if (timer != null) {
//                        timer?.cancel()
//                    }
                    getLocation()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Toast.makeText(this, "Location permissions denied.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    companion object {
        private const val REQUEST_FINE_LOCATION = 1
    }
}

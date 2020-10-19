package com.gallopdevs.athanhelper.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var dayViewAdapter: DayViewAdapter

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swiper)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        createNotificationChannel()
        loadSettings()

        dayViewAdapter = DayViewAdapter(this)

        getLocation()

        val mainActivityPagerAdapter = MainActivityPagerAdapter(supportFragmentManager)
        mainActivityPagerAdapter.addFrag(ClockFragment())
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
                    dayViewAdapter.notifyDataSetChanged()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val channel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        PrayTime.calcMethod = sharedPreferences.getInt("calcMethod", PrayTime.calcMethod)
        PrayTime.asrJuristic = sharedPreferences.getInt("asrMethod", PrayTime.asrJuristic)
        PrayTime.adjustHighLats = sharedPreferences.getInt("latitudes", PrayTime.adjustHighLats)
        // TODO add setting for adjusting time format
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, REQUEST_FINE_LOCATION)
            }
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

                            Log.w(TAG, "PrayTime.currentTime: " + PrayTime.getNextTimeMillis())
                            startNewTimer(PrayTime.getNextTimeMillis())

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

    private fun startNewTimer(countDownTime: Long) {
        if (timer != null) {
            timer?.cancel()
        }
        timer = object : CountDownTimer(countDownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val offset = SimpleDateFormat("HH:mm:ss", Locale.US)
                offset.timeZone = TimeZone.getTimeZone("GMT")
                prayer_timer_text.text = getString(R.string.count_down_time, offset.format(millisUntilFinished))
            }

            override fun onFinish() {
                prayer_timer_text.text = getString(R.string.end_time)
                val sharedPref = getPreferences(Context.MODE_PRIVATE)
                if (sharedPref.getBoolean("enableNotifications", false)) createNotification()
                val newCountDownTime = PrayTime.getNextTimeMillis()
                startNewTimer(newCountDownTime)
            }
        }.start()
    }

    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val prayerNames = resources.getStringArray(R.array.ui_names)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.moon)
                .setContentTitle("Athan")
                .setContentText("Next prayer time: " + prayerNames[PrayTime.nextTimeIndex])
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(0, builder.build())
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_FINE_LOCATION = 1
        private const val CHANNEL_ID = "Notification"
    }
}

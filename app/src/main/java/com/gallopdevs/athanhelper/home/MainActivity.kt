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
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    private lateinit var clockFragment: ClockFragment

    private var timer: CountDownTimer? = null

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
                    val getTimeDifference = PrayTime.getTimerDifference(currentTimeMilliSeconds)
                    val countDownTime = getTimeDifference[PrayTime.nextTime]
                    dayViewAdapter.notifyDataSetChanged()
                    startNewTimer(countDownTime)
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
                            startNewTimer(PrayTime.getTimerDifference(currentTimeMilliSeconds)[PrayTime.nextTime])

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
                    if (timer != null) {
                        timer?.cancel()
                    }
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

    fun startNewTimer(countDownTime: Long) {
        if (timer != null) {
            timer?.cancel()
        }
        timer = object : CountDownTimer(countDownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val offset = SimpleDateFormat("HH:mm:ss", Locale.US)
                offset.timeZone = TimeZone.getTimeZone("GMT")
                val newDateTimer = Date()
                newDateTimer.time = millisUntilFinished
                prayer_timer_text.text = offset.format(newDateTimer) + "s"
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onFinish() {
                prayer_timer_text.text = "00:00:00s"
                val sharedPref = getPreferences(Context.MODE_PRIVATE)
                val enableNotifications = sharedPref.getBoolean("enableNotifications", false)
                if (enableNotifications) createNotification()
                val currentTimeMilliSeconds = PrayTime.currentTime
                val getTimeDifference = PrayTime.getTimerDifference(currentTimeMilliSeconds)
                val newCountDownTime = getTimeDifference[PrayTime.nextTime]
                startNewTimer(newCountDownTime)
            }
        }.start()
    }

    private fun createNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val prayerNames = ArrayList<String>()
        prayerNames.add("Dawn")
        prayerNames.add("Mid-Day")
        prayerNames.add("Afternoon")
        prayerNames.add("Sunset")
        prayerNames.add("Night")

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.moon)
                .setContentTitle("Athan")
                .setContentText("Next prayer time: " + prayerNames[PrayTime.nextTime])
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)

        val notificationId = 0
        notificationManager.notify(notificationId, builder.build())
    }

    companion object {
        private const val REQUEST_FINE_LOCATION = 1
        private const val CHANNEL_ID = "Notification"
    }
}

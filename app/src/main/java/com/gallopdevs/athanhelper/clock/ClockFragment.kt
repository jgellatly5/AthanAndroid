package com.gallopdevs.athanhelper.clock

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.home.MainActivity
import com.gallopdevs.athanhelper.model.PrayTime
import kotlinx.android.synthetic.main.fragment_clock.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ClockFragment : Fragment() {

    private var timer: CountDownTimer? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_clock, container, false)

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

                val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)
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
        val intent = Intent(activity, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)

        val prayerNames = ArrayList<String>()
        prayerNames.add("Dawn")
        prayerNames.add("Mid-Day")
        prayerNames.add("Afternoon")
        prayerNames.add("Sunset")
        prayerNames.add("Night")

        val builder = NotificationCompat.Builder(activity!!, CHANNEL_ID)
                .setSmallIcon(R.drawable.moon)
                .setContentTitle("Athan")
                .setContentText("Next prayer time: " + prayerNames[PrayTime.nextTime])
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(activity!!)

        val notificationId = 0
        notificationManager.notify(notificationId, builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "Notification"
    }
}

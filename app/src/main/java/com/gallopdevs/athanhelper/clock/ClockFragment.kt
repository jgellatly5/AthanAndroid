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

    fun getTimerDifference(currentTime: Long): LongArray {
        val newTimes = PrayTime.getDatePrayerTimes(
                PrayTime.year,
                PrayTime.month + 1,
                PrayTime.dayOfMonth,
                PrayTime.lat,
                PrayTime.lng,
                PrayTime.timeZoneOffset.toDouble()
        )
        val nextDayTimes = PrayTime.getDatePrayerTimes(
                PrayTime.year,
                PrayTime.month + 1,
                PrayTime.dayOfMonth + NEXT_DAY_TIMES,
                PrayTime.lat,
                PrayTime.lng,
                PrayTime.timeZoneOffset.toDouble()
        )

        // format times received from PrayTime model
        val dawnTime = newTimes[0] + ":00"
        val middayTime = newTimes[2] + ":00"
        val afternoonTime = newTimes[3] + ":00"
        val sunsetTime = newTimes[4] + ":00"
        val nightTime = newTimes[6] + ":00"
        val nextDawnTime = nextDayTimes[0] + ":00"
        try {
            // get milliseconds from parsing dates
            val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            val dawnMillis = simpleDateFormat.parse(dawnTime).time
            val middayMillis = simpleDateFormat.parse(middayTime).time
            val afMillis = simpleDateFormat.parse(afternoonTime).time
            val sunsetMillis = simpleDateFormat.parse(sunsetTime).time
            val nightMillis = simpleDateFormat.parse(nightTime).time
            val nextDawnMillis = simpleDateFormat.parse(nextDawnTime).time

            //get intervals between times
            difference1 = dawnMillis - currentTime
            difference2 = middayMillis - currentTime
            difference3 = afMillis - currentTime
            difference4 = sunsetMillis - currentTime
            difference5 = nightMillis - currentTime
            difference6 = nextDawnMillis - currentTime + 86400000

            // set index of each element in differences array
            differences[0] = difference1
            differences[1] = difference2
            differences[2] = difference3
            differences[3] = difference4
            differences[4] = difference5
            differences[5] = difference6
            return differences
        } catch (e: ParseException) {
            Toast.makeText(activity, "Cannot parse that date", Toast.LENGTH_SHORT).show()
        }

        differences[0] = difference1
        differences[1] = difference2
        differences[2] = difference3
        differences[3] = difference4
        differences[4] = difference5
        differences[5] = difference6
        return differences
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

                val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)
                val enableNotifications = sharedPref.getBoolean("enableNotifications", false)
                if (enableNotifications) createNotification()
                val currentTimeMilliSeconds = PrayTime.currentTime
                val getTimeDifference = getTimerDifference(currentTimeMilliSeconds)
                val newCountDownTime = getTimeDifference[nextTime]
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
                .setContentText("Next prayer time: " + prayerNames[nextTime])
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(activity!!)

        val notificationId = 0
        notificationManager.notify(notificationId, builder.build())
    }

    companion object {
        private const val NEXT_DAY_TIMES = 1
        private const val CHANNEL_ID = "Notification"

        private var currentTimeIndex = 0

        private var difference1: Long = 0
        private var difference2: Long = 0
        private var difference3: Long = 0
        private var difference4: Long = 0
        private var difference5: Long = 0
        private var difference6: Long = 0
        private val differences = longArrayOf(difference1, difference2, difference3, difference4, difference5, difference6)

        val nextTime: Int
            get() {
                for (i in differences.indices) {
                    if (differences[i] < 0) {
                        currentTimeIndex = i + 1
                        if (currentTimeIndex > 5) {
                            currentTimeIndex = 0
                        }
                    }
                }
                return currentTimeIndex
            }
    }
}

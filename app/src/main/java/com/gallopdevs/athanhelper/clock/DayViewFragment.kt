package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.model.PrayTime
import com.gallopdevs.athanhelper.utils.CalendarPrayerTimes
import kotlinx.android.synthetic.main.fragment_page.*
import java.util.*

class DayViewFragment : Fragment() {
    private val TAG = "DayViewFragment"
    private val DEFAULT_TIME_FORMAT = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prayerTime = PrayTime.instance
        prayerTime?.timeFormat = DEFAULT_TIME_FORMAT

        val bundle = arguments
        setDate(bundle!!)
        updateTimes(bundle)
    }

    private fun updateTimes(bundle: Bundle) {
        val count = bundle.getInt("count")
        val nextDayTimes = CalendarPrayerTimes.getNextDayTimes(count)

        val newDawnTime = nextDayTimes[0].replaceFirst("^0+(?!$)".toRegex(), "")
        val newMiddayTime = nextDayTimes[2].replaceFirst("^0+(?!$)".toRegex(), "")
        val newAfternoonTime = nextDayTimes[3].replaceFirst("^0+(?!$)".toRegex(), "")
        val newSunsetTime = nextDayTimes[5].replaceFirst("^0+(?!$)".toRegex(), "")
        val newNightTime = nextDayTimes[6].replaceFirst("^0+(?!$)".toRegex(), "")

        val splitDawnTime = newDawnTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitMiddayTime = newMiddayTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitAfternoonTime = newAfternoonTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitSunsetTime = newSunsetTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitNightTime = newNightTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        dawn_time_text_view.text = splitDawnTime[0]
        dawn_post_fix.text = splitDawnTime[1]
        midday_time_text_view.text = splitMiddayTime[0]
        midday_post_fix.text = splitMiddayTime[1]
        afternoon_time_text_view.text = splitAfternoonTime[0]
        afternoon_post_fix.text = splitAfternoonTime[1]
        sunset_time_text_view.text = splitSunsetTime[0]
        sunset_post_fix.text = splitSunsetTime[1]
        night_time_text_view.text = splitNightTime[0]
        night_post_fix.text = splitNightTime[1]
        setOvalVisibility(ClockFragment.nextTime)
    }

    private fun setDate(bundle: Bundle) {
        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH)
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)
        val year = c.get(Calendar.YEAR)

        val nextDay = Calendar.getInstance()
        val count = bundle.getInt("count")
        nextDay.set(year, month, dayOfMonth + count)

        val numberDay = nextDay.get(Calendar.DAY_OF_MONTH)
        val numberString = numberDay.toString()

        val monthString: String
        val monthDay = nextDay.get(Calendar.MONTH) + 1
        if (monthDay < 10) {
            monthString = "0" + monthDay.toString()
        } else {
            monthString = monthDay.toString()
        }

        var weekDay = bundle.getInt("day")
        if (weekDay >= 8) {
            weekDay -= 7
        }
        val weekDayString: String
        when (weekDay) {
            1 -> weekDayString = "Sunday"
            2 -> weekDayString = "Monday"
            3 -> weekDayString = "Tuesday"
            4 -> weekDayString = "Wednesday"
            5 -> weekDayString = "Thursday"
            6 -> weekDayString = "Friday"
            7 -> weekDayString = "Saturday"
            else -> weekDayString = "This is not a day"
        }
        if (numberDay < 10) {
            day_text_view.text = "$weekDayString, $monthString/0$numberString"
        } else {
            day_text_view.text = "$weekDayString, $monthString/$numberString"
        }
    }

    private fun setOvalVisibility(item: Int) {
        var item = item
        val timeViewList = ArrayList<ImageView>()
        timeViewList.add(green_oval_dawn)
        timeViewList.add(green_oval_midday)
        timeViewList.add(green_oval_afternoon)
        timeViewList.add(green_oval_sunset)
        timeViewList.add(green_oval_night)

        if (item >= 5) {
            item -= 5
        }

        timeViewList[item].visibility = View.VISIBLE
    }
}

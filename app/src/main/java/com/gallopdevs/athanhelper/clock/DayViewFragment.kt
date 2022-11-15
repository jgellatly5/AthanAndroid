package com.gallopdevs.athanhelper.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gallopdevs.athanhelper.R
import com.gallopdevs.athanhelper.databinding.FragmentDayviewBinding
import com.gallopdevs.athanhelper.model.PrayTime

class DayViewFragment : Fragment() {

    private var _binding: FragmentDayviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDayviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrayTime.timeFormat = PrayTime.time12
        setDate(requireArguments())
        updateTimes(requireArguments())
        setOvalVisibility(PrayTime.nextTimeIndex)
    }

    private fun setDate(bundle: Bundle) {
        val dayOfMonth = bundle.getInt("dayOfMonth")
        val dayOfMonthString = dayOfMonth.toString()

        val month = bundle.getInt("month")
        val monthString = if (month < 10) {
            "0$month"
        } else {
            month.toString()
        }

        var weekDay = bundle.getInt("day")
        if (weekDay >= 8) {
            weekDay -= 7
        }
        val weekDayString = when (weekDay) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "This is not a day"
        }
        if (dayOfMonth < 10) {
            binding.dayTextView.text = "$weekDayString, $monthString/0$dayOfMonthString"
        } else {
            binding.dayTextView.text = "$weekDayString, $monthString/$dayOfMonthString"
        }
    }

    private fun updateTimes(bundle: Bundle) {
        val count = bundle.getInt("count")
        val nextDayTimes = PrayTime.getDatePrayerTimes(PrayTime.year, PrayTime.month + 1, PrayTime.dayOfMonth + count, PrayTime.lat, PrayTime.lng, PrayTime.timeZoneOffset.toDouble())

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

        binding.apply {
            dawnTimeTextView.text = splitDawnTime[0]
            dawnPostFix.text = splitDawnTime[1]
            middayTimeTextView.text = splitMiddayTime[0]
            middayPostFix.text = splitMiddayTime[1]
            afternoonTimeTextView.text = splitAfternoonTime[0]
            afternoonPostFix.text = splitAfternoonTime[1]
            sunsetTimeTextView.text = splitSunsetTime[0]
            sunsetPostFix.text = splitSunsetTime[1]
            nightTimeTextView.text = splitNightTime[0]
            nightPostFix.text = splitNightTime[1]
        }
    }

    private fun setOvalVisibility(i: Int) {
        binding.apply {
            var item = i

            val timeViewList = ArrayList<TextView>()
            timeViewList.add(dawnTextView)
            timeViewList.add(middayTextView)
            timeViewList.add(afternoonTextView)
            timeViewList.add(sunsetTextView)
            timeViewList.add(nightTextView)

            if (item >= 5) {
                item -= 5
            }

            timeViewList[item].addDrawable(R.drawable.green_oval)
        }
    }
}

fun TextView.addDrawable(drawable: Int) {
    val imgDrawable = ContextCompat.getDrawable(context, drawable)
    compoundDrawablePadding = 30
    setCompoundDrawablesWithIntrinsicBounds(imgDrawable, null, null, null)
}

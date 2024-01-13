package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.ui.clock.ClockScreenConstants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetDatesForWeekUseCase @Inject constructor() {
    operator fun invoke(): List<String> {
        val dates = mutableListOf<String>()
        for (i in 0 until ClockScreenConstants.DAYS_IN_WEEK) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, i)
            val sdf = SimpleDateFormat("EEEE, MM/dd", Locale.getDefault())
            dates.add(sdf.format(calendar.time))
        }
        return dates
    }
}

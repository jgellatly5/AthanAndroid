package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.models.Date
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import javax.inject.Inject

class GetPrayerTimesForWeekUseCase @Inject constructor(
    private val getDatesForApiUseCase: GetDatesForApiUseCase,
    private val getPrayerTimeResponsesForMonthUseCase: GetPrayerTimesForMonthUseCase
) {
    suspend operator fun invoke(): List<TimingsResponse?> {
        val dates = getDatesForApiUseCase()
        val prayerTimeResponsesForMonth = getPrayerTimeResponsesForMonthUseCase()
        val prayerTimeResponsesForWeek = prayerTimeResponsesForMonth.filter { timingsResponse ->
            timingsResponse?.date?.let {
                dates.contains(it.timestamp)
            } ?: false
        }
        return prayerTimeResponsesForWeek
    }
}

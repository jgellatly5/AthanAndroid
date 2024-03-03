package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject

class FormatTimesUseCase @Inject constructor(
    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase,
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase
) {
    suspend operator fun invoke(simpleDateFormat: SimpleDateFormat): Flow<Result<List<Long>>> {
        val prayerTimesForWeek = getPrayerTimesForWeekUseCase()
        return prayerTimesForWeek.map { result ->
            when (result) {
                Result.Loading -> Result.Loading

                is Result.Success -> {
                    val prayerTimesList = result.data
                    val newTimes = prayerTimesList.first().timingsResponse.timings
                    val nextMorningTimes = prayerTimesList[1].timingsResponse.timings
                    try {
                        val times = arrayOf(
                            newTimes?.fajr,
                            newTimes?.sunrise,
                            newTimes?.dhuhr,
                            newTimes?.asr,
                            newTimes?.sunset,
                            newTimes?.maghrib,
                            newTimes?.isha,
                            newTimes?.imsak,
                            nextMorningTimes?.fajr
                        )
                        val millisList = times.map { parseTimeToMillisUseCase(simpleDateFormat, it) }
                        Result.Success(millisList)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        Result.Error("Error")
                    }
                }

                is Result.Error -> Result.Error("Error")
            }
        }
    }
}

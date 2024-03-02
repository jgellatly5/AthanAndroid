package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetNextPrayerTimeUseCase @Inject constructor(
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase
) {

    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    suspend operator fun invoke(): Flow<Result<Long>> {
        val prayerTimesForWeek = getPrayerTimesForWeekUseCase()
        return prayerTimesForWeek.map { result ->
            when (result) {
                Result.Loading -> Result.Loading

                is Result.Success -> {
                    val prayerTimesList = result.data
                    val newTimes = prayerTimesList.first().timingsResponse.timings
                    val nextMorningTimes = prayerTimesList[1].timingsResponse.timings
                    val differences = LongArray(6)


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
                        val millisArray = times.map { parseTimeToMillis(it) }
                        val currentTimeMillis = getCurrentTimeMillis()
                        for (i in differences.indices) {
                            differences[i] = millisArray[i] - currentTimeMillis
                            if (i == differences.lastIndex) {
                                differences[i] = differences[i] + 86400000
                            }
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    var nextTimeIndex = 0
                    for (i in differences.indices) {
                        if (differences[i] < 0) {
                            nextTimeIndex = (i + 1) % 5
                        }
                    }
                    Result.Success(differences[nextTimeIndex])
                }

                is Result.Error -> Result.Error("Error")
            }
        }
    }

    private fun getCurrentTimeMillis(): Long {
        val currentTime = simpleDateFormat.format(Calendar.getInstance().time)
        return parseTimeToMillis(currentTime)
    }

    private fun parseTimeToMillis(time: String?): Long {
        return try {
            simpleDateFormat.parse("$time:00")?.time ?: 0
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }
}

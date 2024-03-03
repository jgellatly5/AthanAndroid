package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class GetNextPrayerTimeUseCase @Inject constructor(
    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase,
    private val formatTimesUseCase: FormatTimesUseCase
) {
    suspend operator fun invoke(): Flow<Result<Long>> {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val currentTime = simpleDateFormat.format(Calendar.getInstance().time)
        val currentTimeMillis = parseTimeToMillisUseCase(simpleDateFormat, currentTime)
        val parsedTimes = formatTimesUseCase(simpleDateFormat)
        return parsedTimes.map { result ->
            when (result) {
                Result.Loading -> Result.Loading

                is Result.Success -> {
                    val millisList = result.data
                    val differences = LongArray(9)
                    for (i in differences.indices) {
                        differences[i] = millisList[i] - currentTimeMillis
                        if (i == differences.lastIndex) {
                            differences[i] = differences[i] + 86400000
                        }
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
}

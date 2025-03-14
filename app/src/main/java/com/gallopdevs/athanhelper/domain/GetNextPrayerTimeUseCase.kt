package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class GetNextPrayerTimeUseCase @Inject constructor(
    private val formatCurrentTimeUseCase: FormatCurrentTimeUseCase,
    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase,
    private val formatTimesUseCase: FormatTimesUseCase,
    private val getNextPrayerUseCase: GetNextPrayerUseCase
) {
    suspend operator fun invoke(): Flow<Result<NextPrayerTime>> {
        val formatCurrentTime = formatCurrentTimeUseCase()
        val currentTimeMillisSdf = SimpleDateFormat("HH:mm:ss", Locale.US)
        val currentTimeMillis = parseTimeToMillisUseCase(currentTimeMillisSdf, formatCurrentTime)
        val parsedTimesSdf = SimpleDateFormat("HH:mm", Locale.US)
        val parsedTimes = formatTimesUseCase(parsedTimesSdf)
        return parsedTimes.map { result ->
            when (result) {
                Result.Loading -> Result.Loading

                is Result.Success -> {
                    val millisList = result.data
                    val differences = LongArray(8)
                    for (i in differences.indices) {
                        differences[i] = millisList[i] - currentTimeMillis
                        if (i == differences.lastIndex) {
                            differences[i] = differences[i] + 86400000
                        }
                    }
                    val nextPrayer = getNextPrayerUseCase(differences)
                    val nextPrayerTime = NextPrayerTime(
                        nextPrayerTimeMillis = differences[nextPrayer.index],
                        nextPrayer = nextPrayer
                    )
                    Result.Success(nextPrayerTime)
                }

                is Result.Error -> Result.Error("Error")
            }
        }
    }
}

data class NextPrayerTime(
    val nextPrayerTimeMillis: Long,
    val nextPrayer: NextPrayer
) {
    companion object
}

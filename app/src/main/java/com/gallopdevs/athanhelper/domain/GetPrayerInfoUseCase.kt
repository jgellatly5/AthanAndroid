package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetPrayerInfoUseCase @Inject constructor(
    private val getNextPrayerTimeUseCase: GetNextPrayerTimeUseCase,
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase
) {
    suspend operator fun invoke(): Flow<Result<PrayerInfo>> {
        val nextPrayerTimeFlow = getNextPrayerTimeUseCase()
        val prayerTimesListFlow = getPrayerTimesForWeekUseCase()
        return nextPrayerTimeFlow.combine(prayerTimesListFlow) { nextPrayerTimeResult, prayerTimesListResult ->
            if (nextPrayerTimeResult is Result.Success && prayerTimesListResult is Result.Success) {
                Result.Success(
                    PrayerInfo(
                        nextPrayerTime = nextPrayerTimeResult.data,
                        prayerTimesList = prayerTimesListResult.data
                    )
                )
            } else {
                Result.Error("Failed to combine PrayerInfo")
            }
        }.onStart { emit(Result.Loading) }
    }
}

data class PrayerInfo(
    val nextPrayerTime: NextPrayerTime,
    val prayerTimesList: List<PrayerTimes>
) {
    companion object
}

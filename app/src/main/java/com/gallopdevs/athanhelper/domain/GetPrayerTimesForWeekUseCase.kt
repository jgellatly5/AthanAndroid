package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPrayerTimesForWeekUseCase @Inject constructor(
    private val getDatesForApiUseCase: GetDatesForApiUseCase,
    private val getPrayerTimeResponsesForMonthUseCase: GetPrayerTimesForMonthUseCase
) {
    suspend operator fun invoke(): Flow<Result<List<TimingsResponse?>>> {
        val dates = getDatesForApiUseCase()
        val prayerTimeResponsesForMonth = getPrayerTimeResponsesForMonthUseCase()
        return prayerTimeResponsesForMonth.map { result ->
            when (result) {
                Result.Loading -> result

                is Result.Success -> {
                    val timingsResponses = result.data.filter { timingsResponse ->
                        timingsResponse?.date.let {
                            dates.contains(it?.readable)
                        }
                    }
                    Result.Success(timingsResponses)
                }

                is Result.Error -> result
            }
        }
    }
}

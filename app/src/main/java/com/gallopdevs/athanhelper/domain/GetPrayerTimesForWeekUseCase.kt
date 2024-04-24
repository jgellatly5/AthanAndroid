package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPrayerTimesForWeekUseCase @Inject constructor(
    private val getDatesUseCase: GetDatesUseCase,
    private val getPrayerTimeResponsesForMonthUseCase: GetPrayerTimesForMonthUseCase
) {
    suspend operator fun invoke(): Flow<Result<List<PrayerTimes>>> {
        val datesToBeFiltered = getDatesUseCase(pattern = "dd MMM yyyy")
        val prayerTimeResponsesForMonth = getPrayerTimeResponsesForMonthUseCase()
        return prayerTimeResponsesForMonth.map { result ->
            when (result) {
                Result.Loading -> Result.Loading

                is Result.Success -> {
                    val prayerTimesList = result.data
                        .filter { timingsResponse ->
                            timingsResponse?.date.let {
                                datesToBeFiltered.contains(it?.readable)
                            }
                        }
                        .map { timingsResponse ->
                            PrayerTimes(
                                date = timingsResponse?.date?.readable ?: "",
                                timingsResponse = timingsResponse ?: TimingsResponse() // You may need to handle null case appropriately
                            )
                        }
                    Result.Success(prayerTimesList)
                }

                is Result.Error -> Result.Error("Error")
            }
        }
    }
}

data class PrayerTimes(
    val date: String,
    val timingsResponse: TimingsResponse
) {
    companion object
}

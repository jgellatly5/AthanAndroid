package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.api.AladhanApi
import com.gallopdevs.athanhelper.data.models.AladhanResponse
import com.gallopdevs.athanhelper.data.models.Timings
import retrofit2.HttpException
import javax.inject.Inject

class NetworkRemoteDataSource @Inject constructor(
    private val aladhanApi: AladhanApi
) : RemoteDataSource {

    override suspend fun getPrayerTimesForDate(
        date: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Result<Timings> =
        aladhanApi.getPrayerTimesForDate(date, latitude, longitude, method).let {
            if (it.isSuccessful) {
                val timings = it.body()?.timingsResponseList?.first()?.timings
                if (timings != null) {
                    Result.Success(timings)
                } else {
                    Result.Error(Exception("Response is null"))
                }
            } else {
                Result.Error(HttpException(it))
            }
        }

    override suspend fun getPrayerTimesForMonth(
        year: String,
        month: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Result<AladhanResponse?> =
        aladhanApi.getPrayerTimesForMonth(year, month, latitude, longitude, method).let {
            if (it.isSuccessful) {
                Result.Success(it.body())
            } else {
                Result.Error(HttpException(it))
            }
        }
}

interface RemoteDataSource {

    suspend fun getPrayerTimesForDate(
        date: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Result<Timings>

    suspend fun getPrayerTimesForMonth(
        year: String,
        month: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Result<AladhanResponse?>
}

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

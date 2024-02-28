package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.api.AladhanApi
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import okio.IOException
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
    ): Result<Timings> = try {
        val response = aladhanApi.getPrayerTimesForDate(date, latitude, longitude, method)
        if (response.isSuccessful) {
            val timings = response.body()?.timingsResponseList?.first()?.timings
            if (timings != null) {
                Result.Success(timings)
            } else {
                Result.Error("Null Response")
            }
        } else {
            Result.Error("API Error")
        }
    } catch (e: Exception) {
        Result.Error("Unknown")
    }

    override suspend fun getPrayerTimesForMonth(
        year: String,
        month: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Result<List<TimingsResponse?>> =
        aladhanApi.getPrayerTimesForMonth(year, month, latitude, longitude, method).let {
            if (it.isSuccessful) {
                val timingsResponseList = it.body()?.timingsResponseList
                if (timingsResponseList != null) {
                    Result.Success(timingsResponseList)
                } else {
                    Result.Error("Null Response")
                }
            } else {
                Result.Error("API Error")
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
    ): Result<List<TimingsResponse?>>
}

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

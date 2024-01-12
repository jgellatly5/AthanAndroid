package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.api.AladhanApi
import com.gallopdevs.athanhelper.data.models.AladhanResponse
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
    ): AladhanResponse? = aladhanApi.getPrayerTimesForDate(date, latitude, longitude, method).let {
        if (it.isSuccessful) {
            it.body()
        } else {
            throw HttpException(it)
        }
    }
}

interface RemoteDataSource {
    suspend fun getPrayerTimesForDate(
        date: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): AladhanResponse?
}

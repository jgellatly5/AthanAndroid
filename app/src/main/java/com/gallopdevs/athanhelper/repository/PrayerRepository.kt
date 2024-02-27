package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.PrayerInfo
import com.gallopdevs.athanhelper.data.PrayerLocalDataSource
import com.gallopdevs.athanhelper.data.RemoteDataSource
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: PrayerLocalDataSource,
    private val prayerCalc: PrayerCalc
) : PrayerRepo {

    override suspend fun getPrayerTimesForDate(
        date: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Flow<Result<Timings>> {
        return flow {
            emit(Result.Loading)
            val result = remoteDataSource.getPrayerTimesForDate(
                date,
                latitude,
                longitude,
                method
            )
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getPrayerTimeResponsesForMonth(
        year: String,
        month: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Flow<Result<List<TimingsResponse?>>> {
        return flow {
            emit(Result.Loading)
            val result = remoteDataSource.getPrayerTimesForMonth(
                year,
                month,
                latitude,
                longitude,
                method
            )
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun getPrayerInfo(): PrayerInfo = prayerCalc.getPrayerInfo()

    override fun setLocation(latitude: Double, longitude: Double) =
        prayerCalc.setLocation(latitude, longitude)

    override fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    ) = prayerCalc.setCalculations(calcMethod, asrJuristic, adjustHighLats, timeFormat)
}

interface PrayerRepo {

    suspend fun getPrayerTimesForDate(
        date: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Flow<Result<Timings>>

    suspend fun getPrayerTimeResponsesForMonth(
        year: String,
        month: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Flow<Result<List<TimingsResponse?>>>

    fun getPrayerInfo(): PrayerInfo

    fun setLocation(latitude: Double, longitude: Double)

    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    )
}

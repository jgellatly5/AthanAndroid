package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.AladhanResponse
import com.gallopdevs.athanhelper.data.NetworkRemoteDataSource
import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.PrayerInfo
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val prayerCalc: PrayerCalc,
    private val networkRemoteDataSource: NetworkRemoteDataSource
) : PrayerRepo {

    override suspend fun getPrayerTimesForDate(
        date: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): AladhanResponse? {
        return networkRemoteDataSource.getPrayerTimesForDate(date, latitude, longitude, method)
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
    ): AladhanResponse?
    fun getPrayerInfo(): PrayerInfo
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    )
}

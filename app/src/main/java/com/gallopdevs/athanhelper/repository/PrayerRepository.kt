package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.PrayerInfo
import com.gallopdevs.athanhelper.data.RemoteDataSource
import com.gallopdevs.athanhelper.data.models.Timings
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val prayerCalc: PrayerCalc,
    private val remoteDataSource: RemoteDataSource
) : PrayerRepo {

    override suspend fun getPrayerTimesForDate(
        date: String,
        latitude: Double,
        longitude: Double,
        method: Int
    ): Timings? {
        val aladhanResponse = remoteDataSource.getPrayerTimesForDate(date, latitude, longitude, method)
        return aladhanResponse?.data?.timings
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
    ): Timings?
    fun getPrayerInfo(): PrayerInfo
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    )
}

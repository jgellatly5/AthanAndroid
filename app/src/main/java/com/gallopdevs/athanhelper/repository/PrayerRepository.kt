package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.NextTimeInfo
import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.PrayerTimesInfo
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val prayerCalc: PrayerCalc
) : PrayerRepo {

    override fun getPrayerTimesInfo(): PrayerTimesInfo =
        prayerCalc.getPrayerTimesInfo()

    override fun getNextTimeInfo(): NextTimeInfo = prayerCalc.getNextTimeInfo()

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
    fun getPrayerTimesInfo(): PrayerTimesInfo
    fun getNextTimeInfo(): NextTimeInfo
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    )
}

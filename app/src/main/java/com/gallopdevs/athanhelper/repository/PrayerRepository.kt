package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.NextTimeInfo
import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.PrayerTimesInfo
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val prayerCalc: PrayerCalc
) : PrayerRepo {

    override fun getPrayerTimesInfo(pageIndex: Int): PrayerTimesInfo =
        prayerCalc.getPrayerTimesInfo(offset = pageIndex)

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
    fun getPrayerTimesInfo(pageIndex: Int): PrayerTimesInfo
    fun getNextTimeInfo(): NextTimeInfo
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    )
}

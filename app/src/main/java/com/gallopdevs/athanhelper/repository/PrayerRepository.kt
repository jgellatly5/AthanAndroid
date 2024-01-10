package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalc
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val prayerCalc: PrayerCalc
) : PrayerRepo {

    override fun getPrayerTimesForDate(pageIndex: Int): List<Array<String>> =
        prayerCalc.getPrayerTimesForDate(offset = pageIndex)

    override fun getNextTimeMillis(): Long = prayerCalc.getNextTimeMillis()

    override fun getNextTimeIndex(): Int = prayerCalc.getNextTimeIndex()

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
    fun getPrayerTimesForDate(pageIndex: Int): List<Array<String>>
    fun getNextTimeMillis(): Long
    fun getNextTimeIndex(): Int
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(
        calcMethod: Int,
        asrJuristic: Int,
        adjustHighLats: Int,
        timeFormat: Int
    )
}

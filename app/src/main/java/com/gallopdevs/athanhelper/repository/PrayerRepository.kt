package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.model.PrayerCalculator
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val prayerCalculator: PrayerCalculator
) : PrayerRepo {

    override fun getPrayerTimesForDate(pageIndex: Int): ArrayList<String> =
        prayerCalculator.getPrayerTimesForDate(offset = pageIndex)

    override fun getNextTimeMillis(): Long = prayerCalculator.getNextTimeMillis()

    override fun getNextPrayerName(): String = prayerCalculator.getNextPrayerName()

    override fun setLocation(latitude: Double, longitude: Double) =
        prayerCalculator.setLocation(latitude, longitude)

    override fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) =
        prayerCalculator.setCalculations(calcMethod, asrJuristic, adjustHighLats)

    override fun getNextTimeIndex(): Int = prayerCalculator.getNextTimeIndex()

    override fun setTimeFormat() = prayerCalculator.setTimeFormat()
}

interface PrayerRepo {
    fun getPrayerTimesForDate(pageIndex: Int): ArrayList<String>
    fun getNextTimeMillis(): Long
    fun getNextPrayerName(): String
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int)
    fun getNextTimeIndex(): Int
    fun setTimeFormat()
}

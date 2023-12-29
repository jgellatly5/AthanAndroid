package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.model.PrayerCalculator
import com.gallopdevs.athanhelper.ui.settings.PreferencesManager
import javax.inject.Inject

class PrayerRepository @Inject constructor(
    private val prayerCalculator: PrayerCalculator,
    private val preferencesManager: PreferencesManager
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
    override fun saveBoolean(key: String, value: Boolean) = preferencesManager.saveBoolean(key, value)

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = preferencesManager.getBoolean(key, defaultValue)

    override fun saveInt(key: String, value: Int) = preferencesManager.saveInt(key, value)

    override fun getInt(key: String, defaultValue: Int): Int = preferencesManager.getInt(key, defaultValue)
}

interface PrayerRepo {
    fun getPrayerTimesForDate(pageIndex: Int): ArrayList<String>
    fun getNextTimeMillis(): Long
    fun getNextPrayerName(): String
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int)
    fun getNextTimeIndex(): Int
    fun setTimeFormat()
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int): Int
}

package com.gallopdevs.athanhelper.model

class PrayerRepository(private val prayerTime: PrayerCalculator = PrayerCalculatorIpml) : PrayerRepo {

    override fun getPrayerTimesForDate(pageIndex: Int): ArrayList<String> =
        prayerTime.getPrayerTimesForDate(offset = pageIndex)

    override fun getNextTimeMillis(): Long = prayerTime.getNextTimeMillis()

    override fun getNextPrayerName(): String = prayerTime.getNextPrayerName()

    override fun setLocation(latitude: Double, longitude: Double) =
        prayerTime.setLocation(latitude, longitude)

    override fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) =
        prayerTime.setCalculations(calcMethod, asrJuristic, adjustHighLats)

    override fun getNextTimeIndex(): Int = prayerTime.getNextTimeIndex()

    override fun setTimeFormat() = prayerTime.setTimeFormat()
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

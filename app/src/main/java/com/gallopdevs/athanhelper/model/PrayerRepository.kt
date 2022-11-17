package com.gallopdevs.athanhelper.model

class PrayerRepository(private val prayerTime: PrayerTime = PrayerTime) : PrayerRepo {

    override fun getDatePrayerTimes(count: Int): ArrayList<String> {
        return prayerTime.getDatePrayerTimes(
                year = prayerTime.year,
                month = prayerTime.month + 1,
                day = prayerTime.dayOfMonth + count,
                latitude = prayerTime.lat,
                longitude = prayerTime.lng,
                tZone = prayerTime.timeZoneOffset.toDouble()
        )
    }

    override fun getNextTimeMillis(): Long = prayerTime.getNextTimeMillis()

    override fun getNextPrayerName(): String = prayerTime.getNextPrayerName()

    override fun setLocation(latitude: Double, longitude: Double) {
        prayerTime.lat = latitude
        prayerTime.lng = longitude
    }

    override fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) {
        prayerTime.calcMethod = calcMethod
        prayerTime.asrJuristic = asrJuristic
        prayerTime.adjustHighLats = adjustHighLats
    }

    override fun getNextTimeIndex(): Int = prayerTime.nextTimeIndex

    override fun setTimeFormat() {
        prayerTime.timeFormat = prayerTime.time12
    }
}

interface PrayerRepo {
    fun getDatePrayerTimes(count: Int): ArrayList<String>
    fun getNextTimeMillis(): Long
    fun getNextPrayerName(): String
    fun setLocation(latitude: Double, longitude: Double)
    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int)
    fun getNextTimeIndex(): Int
    fun setTimeFormat()
}

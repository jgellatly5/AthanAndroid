package com.gallopdevs.athanhelper.model

class PrayerRepository : PrayerRepo {
    override fun getDatePrayerTimes(count: Int): ArrayList<String> {
        return PrayerTime.getDatePrayerTimes(
                year = PrayerTime.year,
                month = PrayerTime.month + 1,
                day = PrayerTime.dayOfMonth + count,
                latitude = PrayerTime.lat,
                longitude = PrayerTime.lng,
                tZone = PrayerTime.timeZoneOffset.toDouble()
        )
    }

    override fun getNextTimeMillis(): Long = PrayerTime.getNextTimeMillis()

    override fun getNextPrayerName(): String = PrayerTime.getNextPrayerName()

    override fun setLocation(latitude: Double, longitude: Double) {
        PrayerTime.lat = latitude
        PrayerTime.lng = longitude
    }

    override fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) {
        PrayerTime.calcMethod = calcMethod
        PrayerTime.asrJuristic = asrJuristic
        PrayerTime.adjustHighLats = adjustHighLats
    }

    override fun getNextTimeIndex(): Int = PrayerTime.nextTimeIndex

    override fun setTimeFormat() {
        PrayerTime.timeFormat = PrayerTime.time12
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

package com.gallopdevs.athanhelper.model

class PrayerRepository : PrayerRepo {
    override fun getDatePrayerTimes(count: Int): ArrayList<String> {
        return PrayTime.getDatePrayerTimes(
                year = PrayTime.year,
                month = PrayTime.month + 1,
                day = PrayTime.dayOfMonth + count,
                latitude = PrayTime.lat,
                longitude = PrayTime.lng,
                tZone = PrayTime.timeZoneOffset.toDouble()
        )
    }

    override fun getNextTimeMillis(): Long = PrayTime.getNextTimeMillis()

    override fun getNextPrayerName(): String = PrayTime.getNextPrayerName()

    override fun setLocation(latitude: Double, longitude: Double) {
        PrayTime.lat = latitude
        PrayTime.lng = longitude
    }

    override fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) {
        PrayTime.calcMethod = calcMethod
        PrayTime.asrJuristic = asrJuristic
        PrayTime.adjustHighLats = adjustHighLats
    }

    override fun getNextTimeIndex(): Int = PrayTime.nextTimeIndex

    override fun setTimeFormat() {
        PrayTime.timeFormat = PrayTime.time12
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

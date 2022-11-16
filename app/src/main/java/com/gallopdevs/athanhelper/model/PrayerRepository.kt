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
}

interface PrayerRepo {
    fun getDatePrayerTimes(count: Int): ArrayList<String>
}

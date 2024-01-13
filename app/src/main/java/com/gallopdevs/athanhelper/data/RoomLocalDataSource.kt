package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.data.models.Timings
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(
    private val timingsDao: TimingsDao
) : PrayerLocalDataSource {
    override fun getTimings(): List<Timings> {
        TODO("Not yet implemented")
    }

    override fun saveTimings(timingsList: List<Timings>) {
        TODO("Not yet implemented")
    }
}

interface PrayerLocalDataSource {
    fun getTimings(): List<Timings>
    fun saveTimings(timingsList: List<Timings>)
}

package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(
    private val timingsDao: TimingsDao
) : PrayerLocalDataSource {
    override fun getTimings(): Flow<List<TimingsResponse>> {
        return timingsDao.getAllPrayers()
    }

    override suspend fun saveTimings(timingsList: List<TimingsResponse>) {
        timingsDao.saveTimings(timingsList)
    }
}

interface PrayerLocalDataSource {
    fun getTimings(): Flow<List<TimingsResponse>>
    suspend fun saveTimings(timingsList: List<TimingsResponse>)
}

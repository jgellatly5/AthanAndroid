package com.gallopdevs.athanhelper.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface TimingsDao {

    @Query("SELECT * FROM timingsResponses")
    fun getAllPrayers(): Flow<List<TimingsResponse>>

    @Insert
    suspend fun saveTimings(timingsList: List<TimingsResponse>)
}

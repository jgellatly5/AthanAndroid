package com.gallopdevs.athanhelper.api

import com.gallopdevs.athanhelper.data.models.AladhanResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AladhanApi {

    @GET("timings/{date}")
    suspend fun getPrayerTimesForDate(
        @Path("date") date: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int
    ): Response<AladhanResponse>
}

package com.gallopdevs.athanhelper.data.models

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val timingsResponseType =
        Types.newParameterizedType(List::class.java, TimingsResponse::class.java)
    private val timingsResponseAdapter = moshi.adapter<List<TimingsResponse>>(timingsResponseType)

    private val dateAdapter = moshi.adapter(Date::class.java)

    private val timingsAdapter = moshi.adapter(Timings::class.java)

    private val metaAdapter = moshi.adapter(Meta::class.java)

    @TypeConverter
    fun jsonToTimingsResponseList(json: String): List<TimingsResponse?> =
        timingsResponseAdapter.fromJson(json).orEmpty()

    @TypeConverter
    fun timingsResponseListToJson(timingsResponseList: List<TimingsResponse>): String =
        timingsResponseAdapter.toJson(timingsResponseList)

    @TypeConverter
    fun jsonToDate(json: String): Date? = dateAdapter.fromJson(json)

    @TypeConverter
    fun dateToJson(date: Date): String = dateAdapter.toJson(date)

    @TypeConverter
    fun jsonToTimings(json: String): Timings? = timingsAdapter.fromJson(json)

    @TypeConverter
    fun timingsToJson(timings: Timings): String = timingsAdapter.toJson(timings)

    @TypeConverter
    fun jsonToMeta(json: String): Meta? = metaAdapter.fromJson(json)

    @TypeConverter
    fun metasToJson(meta: Meta): String = metaAdapter.toJson(meta)
}

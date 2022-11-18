package com.gallopdevs.athanhelper.model

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.math.exp

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val mockPrayerTime: PrayerCalculator = mock()

    @Test
    fun get_date_prayer_times_successful() {
        val pageIndex = 0
        val expectedList = arrayListOf("5:00", "10:00", "12:00", "3:00", "6:00")

        whenever(mockPrayerTime.getDatePrayerTimes(offset = pageIndex))
            .thenReturn(expectedList)

        testObject = PrayerRepository(mockPrayerTime)
        assertEquals(expectedList, testObject.getDatePrayerTimes(pageIndex))
    }

//    @Test
//    fun get_next_prayer_name_successful() {
//
//        val expectedName = "Dawn"
//
//        whenever(mockPrayerTime.getNextPrayerName())
//            .thenReturn(expectedName)
//    }
//
//    @Test
//    fun get_next_time_millis_successful() {
//        val expectedMillis = 0L
//
//        whenever(mockPrayerTime.getNextTimeMillis())
//            .thenReturn(expectedMillis)
//    }
}
package com.gallopdevs.athanhelper.model

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val mockPrayerTime: PrayerCalculator = mock()

    @Test
    fun get_prayer_times_for_date_successful() {
        val pageIndex = 0
        val expectedList = arrayListOf("5:00", "10:00", "12:00", "3:00", "6:00")

        whenever(mockPrayerTime.getPrayerTimesForDate(offset = pageIndex))
            .thenReturn(expectedList)

        testObject = PrayerRepository(mockPrayerTime)
        assertEquals(expectedList, testObject.getPrayerTimesForDate(pageIndex))
    }

    @Test
    fun get_next_prayer_name_successful() {

        val expectedName = "Dawn"

        whenever(mockPrayerTime.getNextPrayerName())
            .thenReturn(expectedName)

        testObject = PrayerRepository(mockPrayerTime)
        assertEquals(expectedName, testObject.getNextPrayerName())
    }
//
//    @Test
//    fun get_next_time_millis_successful() {
//        val expectedMillis = 0L
//
//        whenever(mockPrayerTime.getNextTimeMillis())
//            .thenReturn(expectedMillis)
//    }
}

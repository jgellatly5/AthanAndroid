package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalc
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val mockPrayerCalc: PrayerCalc = mock()

//    @Test
//    fun get_prayer_times_for_date_successful() {
//        val pageIndex = 0
//        val expectedList = listOf(
//            arrayOf("5:00", "am"),
//            arrayOf("10:00", "am"),
//            arrayOf("12:00", "pm"),
//            arrayOf("3:00", "pm"),
//            arrayOf("6:00", "pm")
//        )
//
//        whenever(mockPrayerCalc.getPrayerTimesForDate(offset = pageIndex))
//            .thenReturn(expectedList)
//
//        testObject = PrayerRepository(mockPrayerCalc)
//        assertEquals(expectedList, testObject.getPrayerTimesForDate(pageIndex))
//    }
//
//    @Test
//    fun get_next_time_millis_successful() {
//        val expectedNextTimeMillis = 80_000_000L
//
//        whenever(mockPrayerCalc.getNextTimeMillis())
//            .thenReturn(expectedNextTimeMillis)
//
//        testObject = PrayerRepository(mockPrayerCalc)
//        assertEquals(expectedNextTimeMillis, testObject.getNextTimeMillis())
//    }
//
//    @Test
//    fun get_next_time_index_successful() {
//        val expectedNextTimeIndex = 1
//        whenever(mockPrayerCalc.getNextTimeIndex()).thenReturn(expectedNextTimeIndex)
//
//        testObject = PrayerRepository(mockPrayerCalc)
//        assertEquals(expectedNextTimeIndex, testObject.getNextTimeIndex())
//    }
}

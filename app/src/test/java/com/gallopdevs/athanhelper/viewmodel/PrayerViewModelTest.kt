package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.repository.PrayerRepo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class PrayerViewModelTest {

    private lateinit var testObject: PrayerViewModel

    private val mockPrayerRepo: PrayerRepo = mock()

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
//        whenever(mockPrayerRepo.getPrayerInfo())
//            .thenReturn(expectedList)
//
//        testObject = PrayerViewModel(mockPrayerRepo)
//        assertEquals(expectedList, testObject.getPrayerTimesForDate(pageIndex))
//    }
//
//    @Test
//    fun get_next_time_millis_successful() {
//        val expectedNextTimeMillis = 80_000_000L
//        whenever(mockPrayerRepo.getNextTimeMillis()).thenReturn(expectedNextTimeMillis)
//
//        testObject = PrayerViewModel(mockPrayerRepo)
//        assertEquals(expectedNextTimeMillis, testObject.getNextTimeMillis())
//    }
//
//    @Test
//    fun get_next_time_index_successful() {
//        val expectedNextTimeIndex = 1
//        whenever(mockPrayerRepo.getNextTimeIndex()).thenReturn(expectedNextTimeIndex)
//
//        testObject = PrayerViewModel(mockPrayerRepo)
//        assertEquals(expectedNextTimeIndex, testObject.getNextTimeIndex())
//    }
}

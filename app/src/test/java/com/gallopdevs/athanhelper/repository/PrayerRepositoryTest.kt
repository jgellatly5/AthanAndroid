package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val mockPrayerCalculator: PrayerCalculator = mock()

    @Test
    fun get_prayer_times_for_date_successful() {
        val pageIndex = 0
        val expectedList = arrayListOf("5:00", "10:00", "12:00", "3:00", "6:00")

        whenever(mockPrayerCalculator.getPrayerTimesForDate(offset = pageIndex))
            .thenReturn(expectedList)

        testObject = PrayerRepository(mockPrayerCalculator)
        assertEquals(expectedList, testObject.getPrayerTimesForDate(pageIndex))
    }

    @Test
    fun get_next_time_millis_successful() {
        val expectedMillis = 0L

        whenever(mockPrayerCalculator.getNextTimeMillis())
            .thenReturn(expectedMillis)

        testObject = PrayerRepository(mockPrayerCalculator)
        assertEquals(expectedMillis, testObject.getNextTimeMillis())
    }
}

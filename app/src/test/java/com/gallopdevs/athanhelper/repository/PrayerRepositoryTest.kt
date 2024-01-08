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
        val expectedList = listOf(
            arrayOf("5:00", "am"),
            arrayOf("10:00", "am"),
            arrayOf("12:00", "pm"),
            arrayOf("3:00", "pm"),
            arrayOf("6:00", "pm")
        )

        whenever(mockPrayerCalculator.getPrayerTimesForDate(offset = pageIndex))
            .thenReturn(expectedList)

        testObject = PrayerRepository(mockPrayerCalculator)
        assertEquals(expectedList, testObject.getPrayerTimesForDate(pageIndex))
    }

    @Test
    fun get_next_time_millis_successful() {
        val expectedNextTimeMillis = 80_000_000L

        whenever(mockPrayerCalculator.getNextTimeMillis())
            .thenReturn(expectedNextTimeMillis)

        testObject = PrayerRepository(mockPrayerCalculator)
        assertEquals(expectedNextTimeMillis, testObject.getNextTimeMillis())
    }

    @Test
    fun get_next_time_index_successful() {
        val expectedNextTimeIndex = 1
        whenever(mockPrayerCalculator.getNextTimeIndex()).thenReturn(expectedNextTimeIndex)

        testObject = PrayerRepository(mockPrayerCalculator)
        assertEquals(expectedNextTimeIndex, testObject.getNextTimeIndex())
    }
}

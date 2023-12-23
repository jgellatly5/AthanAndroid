package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.repository.PrayerRepo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ClockViewModelTest {

    private lateinit var testObject: ClockViewModel

    private val mockPrayerRepo: PrayerRepo = mock()

    @Test
    fun get_next_prayer_name_successful() {
        val expectedNextPrayerName = "Sunset"
        whenever(mockPrayerRepo.getNextPrayerName()).thenReturn(expectedNextPrayerName)

        testObject = ClockViewModel(mockPrayerRepo)
        assertEquals(expectedNextPrayerName, testObject.getNextPrayerName())
    }

    @Test
    fun get_next_time_index_successful() {
        val expectedNextTimeIndex = 1
        whenever(mockPrayerRepo.getNextTimeIndex()).thenReturn(expectedNextTimeIndex)

        testObject = ClockViewModel(mockPrayerRepo)
        assertEquals(expectedNextTimeIndex, testObject.getNextTimeIndex())
    }
}

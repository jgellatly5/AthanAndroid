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
    fun get_next_time_index_successful() {
        val expectedNextTimeIndex = 1
        whenever(mockPrayerRepo.getNextTimeIndex()).thenReturn(expectedNextTimeIndex)

        testObject = ClockViewModel(mockPrayerRepo)
        assertEquals(expectedNextTimeIndex, testObject.getNextTimeIndex())
    }

    @Test
    fun format_date_successful() {
//        val expectedFormatDate = "Saturday, December 23"
//        whenever(mockPrayerRepo.format()).thenReturn(expectedformatDate)
//
//        testObject = ClockViewModel(mockPrayerRepo)
//        assertEquals(expectedformatDate, testObject.getNextTimeIndex())
    }
}

package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.SettingsRepo
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
}

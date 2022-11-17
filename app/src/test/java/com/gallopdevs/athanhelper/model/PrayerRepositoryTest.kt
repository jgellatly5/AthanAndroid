package com.gallopdevs.athanhelper.model

import org.junit.Test

import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val mockPrayerTime: PrayerTime = mock()

    @Test
    fun get_date_prayer_times_successful() {
        val pageIndex = 0

//        whenever(mockPrayerTime.getDatePrayerTimes(
//            year = mockPrayerTime.year,
//            month = mockPrayerTime.month + 1,
//            day = mockPrayerTime.dayOfMonth + pageIndex,
//            latitude = mockPrayerTime.lat,
//            longitude = mockPrayerTime.lng,
//            tZone = mockPrayerTime.timeZoneOffset.toDouble()
//        )).thenReturn(arrayListOf("5:00", "10:00", "12:00", "3:00", "6:00"))

        testObject = PrayerRepository(mockPrayerTime)

//        testObject.getDatePrayerTimes(pageIndex)
        verify(testObject).getDatePrayerTimes(pageIndex)
    }
}
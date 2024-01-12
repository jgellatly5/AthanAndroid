package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.RemoteDataSource
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val mockPrayerCalc: PrayerCalc = mock()

    private val mockRemoteDataSource: RemoteDataSource = mock()

//    @Test
//    fun get_prayer_times_for_date_successful() {
//        val date = "11-01-2024"
//        val expectedList = listOf(
//            arrayOf("5:00", "am"),
//            arrayOf("10:00", "am"),
//            arrayOf("12:00", "pm"),
//            arrayOf("3:00", "pm"),
//            arrayOf("6:00", "pm")
//        )
//
//        whenever(mockRemoteDataSource.getPrayerTimesForDate(date = date, latitude = 33.860889, longitude = -118.392632, method = 2))
//            .thenReturn(expectedList)
//
//        testObject = PrayerRepository(mockPrayerCalc, mockRemoteDataSource)
//        assertEquals(expectedList, testObject.getPrayerTimesForDate(pageIndex))
//    }
}

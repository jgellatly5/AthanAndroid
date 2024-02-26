package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalc
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerLocalDataSource
import com.gallopdevs.athanhelper.data.RemoteDataSource
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.AladhanResponse
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class PrayerRepositoryTest {

    private lateinit var testObject: PrayerRepo

    private val remoteDataSource: RemoteDataSource = mock()
    private val prayerLocalDataSource: PrayerLocalDataSource = mock()
    private val prayerCalc: PrayerCalc = mock()

    @Test
    fun `getPrayerTimesForDate Result Success`() = runTest {
        val date = "13-02-2024"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val expectedResponse = Timings()

        Mockito.lenient()
            .`when`(
                remoteDataSource.getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Result.Success(expectedResponse)

        testObject = PrayerRepository(remoteDataSource, prayerLocalDataSource, prayerCalc)
        assertEquals(
            expectedResponse,
            testObject.getPrayerTimesForDate(date, latitude, longitude, method)
        )
    }

    @Test
    fun `getPrayerTimesForDate Result Error`() = runTest {
        val date = "13-02-2024"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val expectedResponse = null
        val exception = RuntimeException("API Error")

        Mockito.lenient()
            .`when`(
                remoteDataSource.getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Result.Error(exception)

        testObject = PrayerRepository(remoteDataSource, prayerLocalDataSource, prayerCalc)
        assertEquals(
            expectedResponse,
            testObject.getPrayerTimesForDate(date, latitude, longitude, method)
        )
    }
}

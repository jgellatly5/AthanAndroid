package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerLocalDataSource
import com.gallopdevs.athanhelper.data.RemoteDataSource
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.utilities.JAFARI
import kotlinx.coroutines.flow.toList
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

    @Test
    fun `getPrayerTimesForDate Result Success`() = runTest {
        val date = "13-02-2024"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val timings = Timings()

        Mockito.lenient()
            .`when`(
                remoteDataSource.getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Result.Success(timings)

        testObject = PrayerRepository(remoteDataSource, prayerLocalDataSource)
        val actualResult =
            testObject.getPrayerTimesForDate(date, latitude, longitude, method).toList()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(timings), actualResult.last())
    }

    @Test
    fun `getPrayerTimesForDate Result Error`() = runTest {
        val date = "13-02-2024"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val errorMessage = "API Error"

        Mockito.lenient()
            .`when`(
                remoteDataSource.getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Result.Error(errorMessage)

        testObject = PrayerRepository(remoteDataSource, prayerLocalDataSource)
        val actualResult =
            testObject.getPrayerTimesForDate(date, latitude, longitude, method).toList()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }

    @Test
    fun `getPrayerTimesForMonth Result Success`() = runTest {
        val year = "2024"
        val month = "2"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val timingsResponseList = listOf(
            TimingsResponse(
                timings = Timings()
            )
        )

        Mockito.lenient()
            .`when`(
                remoteDataSource.getPrayerTimesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Result.Success(timingsResponseList)

        testObject = PrayerRepository(remoteDataSource, prayerLocalDataSource)
        val actualResult =
            testObject.getPrayerTimeResponsesForMonth(year, month, latitude, longitude, method)
                .toList()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(timingsResponseList), actualResult.last())
    }

    @Test
    fun `getPrayerTimesForMonth Result Error`() = runTest {
        val year = "2024"
        val month = "2"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val errorMessage = "API Error"

        Mockito.lenient()
            .`when`(
                remoteDataSource.getPrayerTimesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Result.Error(errorMessage)

        testObject = PrayerRepository(remoteDataSource, prayerLocalDataSource)
        val actualResult =
            testObject.getPrayerTimeResponsesForMonth(year, month, latitude, longitude, method)
                .toList()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

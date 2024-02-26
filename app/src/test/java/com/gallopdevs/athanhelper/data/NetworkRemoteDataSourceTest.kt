package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.api.AladhanApi
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.JAFARI
import com.gallopdevs.athanhelper.data.models.AladhanResponse
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.HttpException
import retrofit2.Response

class NetworkRemoteDataSourceTest {

    private lateinit var testObject: NetworkRemoteDataSource

    private val aladhanApi: AladhanApi = mock()

    @Test
    fun `getPrayerTimesForDate API Response Success`() = runTest {
        val date = "13-02-2024"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val aladhanResponse = AladhanResponse(
            timingsResponseList = listOf(
                TimingsResponse(
                    timings = Timings()
                )
            )
        )
        val expectedResult = Result.Success(aladhanResponse)

        Mockito.lenient()
            .`when`(
                aladhanApi.getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Response.success(aladhanResponse)

        testObject = NetworkRemoteDataSource(aladhanApi)
        assertEquals(
            expectedResult,
            testObject.getPrayerTimesForDate(date, latitude, longitude, method)
        )
    }

    @Test
    fun `getPrayerTimesForDate API Response Error`() = runTest {
        val date = "13-02-2024"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI

        val responseBody = "API Error".toResponseBody("text/plain".toMediaTypeOrNull())
        val failedResponse = HttpException(Response.error<String>(500, responseBody))
        val expectedResult = Result.Error(failedResponse)

        Mockito.lenient()
            .`when`(
                aladhanApi.getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Response.error(500, responseBody)

        testObject = NetworkRemoteDataSource(aladhanApi)
        assertEquals(
            expectedResult,
            testObject.getPrayerTimesForDate(date, latitude, longitude, method)
        )
    }

    @Test
    fun `getPrayerTimesForMonth API Response Success`() = runTest {
        val year = "2024"
        val month = "2"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val aladhanResponse = AladhanResponse(
            timingsResponseList = listOf(
                TimingsResponse(
                    timings = Timings()
                )
            )
        )
        val expectedResult = Result.Success(aladhanResponse)

        Mockito.lenient()
            .`when`(
                aladhanApi.getPrayerTimesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn Response.success(aladhanResponse)

        testObject = NetworkRemoteDataSource(aladhanApi)
        assertEquals(
            expectedResult,
            testObject.getPrayerTimesForMonth(year, month, latitude, longitude, method)
        )
    }
}

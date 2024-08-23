package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.api.AladhanApi
import com.gallopdevs.athanhelper.data.models.AladhanResponse
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.utilities.JAFARI
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
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
        val timings = Timings()
        val aladhanResponse = AladhanResponse(
            timingsResponseList = listOf(
                TimingsResponse(
                    timings = timings
                )
            )
        )
        val expectedResult = Result.Success(timings)

        aladhanApi.stub {
            onBlocking {
                getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            } doReturn Response.success(aladhanResponse)
        }

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

        val errorMessage = "API Error"
        val responseBody = errorMessage.toResponseBody("text/plain".toMediaTypeOrNull())
        val expectedResult = Result.Error(errorMessage)

        aladhanApi.stub {
            onBlocking {
                getPrayerTimesForDate(
                    date,
                    latitude,
                    longitude,
                    method
                )
            } doReturn Response.error(500, responseBody)
        }

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
        val timingsResponseList = listOf(
            TimingsResponse(
                timings = Timings()
            )
        )
        val aladhanResponse = AladhanResponse(timingsResponseList = timingsResponseList)
        val expectedResult = Result.Success(timingsResponseList)

        aladhanApi.stub {
            onBlocking {
                getPrayerTimesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            } doReturn Response.success(aladhanResponse)
        }

        testObject = NetworkRemoteDataSource(aladhanApi)
        assertEquals(
            expectedResult,
            testObject.getPrayerTimesForMonth(year, month, latitude, longitude, method)
        )
    }

    @Test
    fun `getPrayerTimesForMonth API Response Error`() = runTest {
        val year = "2024"
        val month = "2"
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI

        val errorMessage = "API Error"
        val responseBody = errorMessage.toResponseBody("text/plain".toMediaTypeOrNull())
        val expectedResult = Result.Error(errorMessage)

        aladhanApi.stub {
            onBlocking {
                getPrayerTimesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            } doReturn Response.error(500, responseBody)
        }

        testObject = NetworkRemoteDataSource(aladhanApi)
        assertEquals(
            expectedResult,
            testObject.getPrayerTimesForMonth(year, month, latitude, longitude, method)
        )
    }
}

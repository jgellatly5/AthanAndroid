package com.gallopdevs.athanhelper.data

import com.gallopdevs.athanhelper.api.AladhanApi
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.JAFARI
import com.gallopdevs.athanhelper.data.models.AladhanResponse
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import retrofit2.Response

class NetworkRemoteDataSourceTest {

    private lateinit var testObject: NetworkRemoteDataSource

    private val aladhanApi: AladhanApi = mock()

    @Test
    fun `getPrayerTimesForDate success`() = runTest {
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
        val expectedResponse = Result.Success(aladhanResponse)

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
            expectedResponse,
            testObject.getPrayerTimesForDate(date, latitude, longitude, method)
        )
    }
}

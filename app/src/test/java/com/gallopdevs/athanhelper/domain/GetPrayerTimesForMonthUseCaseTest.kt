package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.SettingsRepo
import com.gallopdevs.athanhelper.utilities.JAFARI
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import java.util.Calendar

class GetPrayerTimesForMonthUseCaseTest {

    private lateinit var testObject: GetPrayerTimesForMonthUseCase

    private val prayerRepo: PrayerRepo = mock()
    private val settingsRepo: SettingsRepo = mock()

    @Before
    fun setup() {
        testObject = GetPrayerTimesForMonthUseCase(prayerRepo, settingsRepo)
    }

    @Test
    fun `getPrayerTimesForMonth Result Success`() = runTest {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val expectedTimingsResponseList = listOf(
            TimingsResponse(
                timings = Timings()
            )
        )

        prayerRepo.stub {
            onBlocking {
                getPrayerTimeResponsesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            } doReturn flowOf(Result.Loading, Result.Success(expectedTimingsResponseList))
        }

        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(expectedTimingsResponseList), actualResult.last())
    }

    @Test
    fun `getPrayerTimesForMonth Result Error`() = runTest {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val expectedErrorMessage = "API Error"

        prayerRepo.stub {
            onBlocking {
                getPrayerTimeResponsesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            } doReturn flowOf(Result.Loading, Result.Error(expectedErrorMessage))
        }

        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(expectedErrorMessage), actualResult.last())
    }
}

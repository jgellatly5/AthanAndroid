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
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.Calendar

class GetPrayerTimesForMonthUseCaseTest {

    private lateinit var testObject: GetPrayerTimesForMonthUseCase

    private val prayerRepo: PrayerRepo = mock()
    private val settingsRepo: SettingsRepo = mock()

    @Test
    fun `getPrayerTimesForMonth Result Success`() = runTest {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
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
                prayerRepo.getPrayerTimeResponsesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn flowOf(Result.Loading, Result.Success(timingsResponseList))

        testObject = GetPrayerTimesForMonthUseCase(prayerRepo, settingsRepo)
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(timingsResponseList), actualResult.last())
    }

    @Test
    fun `getPrayerTimesForMonth Result Error`() = runTest {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val latitude = 0.01
        val longitude = 0.01
        val method = JAFARI
        val errorMessage = "API Error"

        Mockito.lenient()
            .`when`(
                prayerRepo.getPrayerTimeResponsesForMonth(
                    year,
                    month,
                    latitude,
                    longitude,
                    method
                )
            ) doReturn flowOf(Result.Loading, Result.Error(errorMessage))

        testObject = GetPrayerTimesForMonthUseCase(prayerRepo, settingsRepo)
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

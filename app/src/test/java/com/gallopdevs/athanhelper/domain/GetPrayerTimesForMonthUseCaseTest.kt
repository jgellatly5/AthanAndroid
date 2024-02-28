package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.PrayerCalculator
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.SettingsRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetPrayerTimesForMonthUseCaseTest {

    private lateinit var testObject: GetPrayerTimesForMonthUseCase

    private val prayerRepo: PrayerRepo = mock()
    private val settingsRepo: SettingsRepo = mock()

    @Test
    fun `getPrayerTimesForMonth Result Success`() = runTest {
        val year = "2024"
        val month = "2"
        val latitude = 0.01
        val longitude = 0.01
        val method = PrayerCalculator.JAFARI
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
}

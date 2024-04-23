package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.Date
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetPrayerTimesForWeekUseCaseTest {

    private lateinit var testObject: GetPrayerTimesForWeekUseCase

    private val getDatesUseCase: GetDatesUseCase = mock()
    private val getPrayerTimeResponsesForMonthUseCase: GetPrayerTimesForMonthUseCase = mock()

    @Test
    fun `getPrayerTimesForWeek Result Success`() = runTest {
        val expectedDates = listOf(
            "23 Apr 2024",
            "24 Apr 2024",
            "25 Apr 2024",
            "26 Apr 2024",
            "27 Apr 2024",
            "28 Apr 2024",
            "29 Apr 2024"
        )
        val expectedTimingsResponseList = listOf(
            TimingsResponse(
                date = Date(readable = "23 Apr 2024"),
                timings = Timings()
            )
        )
        val expectedPrayerTimeResponses = listOf(
            PrayerTimes(
                date = "23 Apr 2024",
                timingsResponse = expectedTimingsResponseList.first()
            )
        )

        Mockito.lenient()
            .`when`(
                getDatesUseCase("dd MMM yyyy")
            ) doReturn expectedDates

        Mockito.lenient()
            .`when`(
                getPrayerTimeResponsesForMonthUseCase()
            ) doReturn flowOf(Result.Loading, Result.Success(expectedTimingsResponseList))

        testObject = GetPrayerTimesForWeekUseCase(
            getDatesUseCase,
            getPrayerTimeResponsesForMonthUseCase
        )
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(expectedPrayerTimeResponses), actualResult.last())
    }

    @Test
    fun `getPrayerTimesForWeek Result Error`() = runTest {
        val expectedDates = listOf(
            "23 Apr 2024",
            "24 Apr 2024",
            "25 Apr 2024",
            "26 Apr 2024",
            "27 Apr 2024",
            "28 Apr 2024",
            "29 Apr 2024"
        )
        val expectedErrorMessage = "Error"

        Mockito.lenient()
            .`when`(
                getDatesUseCase("dd MMM yyyy")
            ) doReturn expectedDates

        Mockito.lenient()
            .`when`(
                getPrayerTimeResponsesForMonthUseCase()
            ) doReturn flowOf(Result.Loading, Result.Error(expectedErrorMessage))

        testObject = GetPrayerTimesForWeekUseCase(
            getDatesUseCase,
            getPrayerTimeResponsesForMonthUseCase
        )
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(expectedErrorMessage), actualResult.last())
    }
}

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

    // TODO fix
    private lateinit var testObject: GetPrayerTimesForWeekUseCase

    private val getDatesUseCase: GetDatesUseCase = mock()
    private val getPrayerTimeResponsesForMonthUseCase: GetPrayerTimesForMonthUseCase = mock()

    @Test
    fun `getPrayerTimesForWeek Result Success`() = runTest {
        val timingsResponseList = listOf(
            TimingsResponse(
                date = Date(timestamp = "2024-02-25"),
                timings = Timings()
            )
        )

        Mockito.lenient()
            .`when`(
                getDatesUseCase("yyyy-MM-dd")
            ) doReturn listOf(
            "2024-02-25",
            "2024-02-26",
            "2024-02-27",
            "2024-02-28",
            "2024-02-29",
            "2024-03-01",
            "2024-03-02"
        )

        Mockito.lenient()
            .`when`(
                getPrayerTimeResponsesForMonthUseCase()
            ) doReturn flowOf(Result.Loading, Result.Success(timingsResponseList))

        testObject = GetPrayerTimesForWeekUseCase(
            getDatesUseCase,
            getPrayerTimeResponsesForMonthUseCase
        )
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(timingsResponseList), actualResult.last())
    }

    @Test
    fun `getPrayerTimesForWeek Result Error`() = runTest {
        val errorMessage = "API Error"

        Mockito.lenient()
            .`when`(
                getDatesUseCase("yyyy-MM-dd")
            ) doReturn listOf(
            "2024-02-25",
            "2024-02-26",
            "2024-02-27",
            "2024-02-28",
            "2024-02-29",
            "2024-03-01",
            "2024-03-02"
        )

        Mockito.lenient()
            .`when`(
                getPrayerTimeResponsesForMonthUseCase()
            ) doReturn flowOf(Result.Loading, Result.Error(errorMessage))

        testObject = GetPrayerTimesForWeekUseCase(
            getDatesUseCase,
            getPrayerTimeResponsesForMonthUseCase
        )
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

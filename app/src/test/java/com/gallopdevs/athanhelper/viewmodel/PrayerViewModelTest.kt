package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.MainDispatcherRule
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.JAFARI
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.domain.GetDatesForWeekUseCase
import com.gallopdevs.athanhelper.domain.GetPrayerTimesForWeekUseCase
import com.gallopdevs.athanhelper.repository.PrayerRepo
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow

class PrayerViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testObject: PrayerViewModel

    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase = mock()
    private val getDatesForWeekUseCase: GetDatesForWeekUseCase = mock()
    private val prayerRepo: PrayerRepo = mock()

    @Test
    fun `dayViewScreenUiState success`() = runTest {
        val pageIndex = 0
        val expectedPrayerTimesForWeekUseCase = listOf(TimingsResponse())
        val expectedDatesForWeekUseCase = listOf(
            "2024-02-11",
            "2024-02-12",
            "2024-02-13",
            "2024-02-14",
            "2024-02-15",
            "2024-02-16",
            "2024-02-17"
        )

        Mockito.lenient()
            .`when`(getPrayerTimesForWeekUseCase.invoke()) doReturn flowOf(Result.Success(expectedPrayerTimesForWeekUseCase))
        Mockito.lenient()
            .`when`(getDatesForWeekUseCase.invoke()) doReturn expectedDatesForWeekUseCase
        Mockito.lenient().`when`(
            prayerRepo.getPrayerTimeResponsesForMonth(
                year = "2024",
                month = "2",
                latitude = 0.01,
                longitude = 0.01,
                method = JAFARI
            )
        ) doReturn flowOf(Result.Success(expectedPrayerTimesForWeekUseCase))

        testObject =
            PrayerViewModel(getPrayerTimesForWeekUseCase, getDatesForWeekUseCase, prayerRepo)

        assertEquals(DayViewScreenUiState.Loading, testObject.uiState.value)
        testObject.getPrayerTimesForDate(pageIndex)
        assertEquals(
            DayViewScreenUiState.Success(
                expectedPrayerTimesForWeekUseCase.first(),
                expectedDatesForWeekUseCase
            ),
            testObject.uiState.value
        )
    }

    @Test
    fun `dayViewScreenUiState error`() = runTest {
        val pageIndex = 0
        val exception = RuntimeException("API Error")

        Mockito.lenient()
            .`when`(getPrayerTimesForWeekUseCase.invoke()) doThrow exception
        Mockito.lenient()
            .`when`(getDatesForWeekUseCase.invoke()) doThrow exception
        Mockito.lenient().`when`(
            prayerRepo.getPrayerTimeResponsesForMonth(
                year = "2024",
                month = "2",
                latitude = 0.01,
                longitude = 0.01,
                method = JAFARI
            )
        ) doThrow exception

        testObject =
            PrayerViewModel(getPrayerTimesForWeekUseCase, getDatesForWeekUseCase, prayerRepo)

        assertEquals(DayViewScreenUiState.Loading, testObject.uiState.value)
        testObject.getPrayerTimesForDate(pageIndex)
        assertEquals(DayViewScreenUiState.Error("An error has occurred."), testObject.uiState.value)
    }
}

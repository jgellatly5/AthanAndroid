package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.MainDispatcherRule
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.JAFARI
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.GetPrayerTimesForWeekUseCase
import com.gallopdevs.athanhelper.domain.PrayerTimes
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
    private val prayerRepo: PrayerRepo = mock()

    @Test
    fun `prayerTimesUiState success`() = runTest {
        val expectedPrayerTimesForWeekUseCase =
            listOf(PrayerTimes(date = "2024-02-11", timingsResponse = TimingsResponse()))
        val expectedPrayerRepoResponse = listOf(TimingsResponse())

        Mockito.lenient()
            .`when`(getPrayerTimesForWeekUseCase()) doReturn flowOf(
            Result.Success(
                expectedPrayerTimesForWeekUseCase
            )
        )
        Mockito.lenient().`when`(
            prayerRepo.getPrayerTimeResponsesForMonth(
                year = "2024",
                month = "2",
                latitude = 0.01,
                longitude = 0.01,
                method = JAFARI
            )
        ) doReturn flowOf(Result.Success(expectedPrayerRepoResponse))

        testObject =
            PrayerViewModel(getPrayerTimesForWeekUseCase, prayerRepo)

        testObject.getPrayerTimesForWeek()
        assertEquals(
            PrayerTimesUiState.Success(expectedPrayerTimesForWeekUseCase),
            testObject.prayerTimesUiState.value
        )
    }

    @Test
    fun `prayerTimesUiState error`() = runTest {
        val exception = RuntimeException("API Error")

        Mockito.lenient()
            .`when`(getPrayerTimesForWeekUseCase()) doThrow exception
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
            PrayerViewModel(getPrayerTimesForWeekUseCase, prayerRepo)

        testObject.getPrayerTimesForWeek()
        assertEquals(
            PrayerTimesUiState.Error("Coroutine Error"),
            testObject.prayerTimesUiState.value
        )
    }
}

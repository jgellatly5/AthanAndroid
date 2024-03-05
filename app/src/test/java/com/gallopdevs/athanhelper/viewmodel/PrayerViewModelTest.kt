package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.MainDispatcherRule
import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.GetPrayerInfoUseCase
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
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

    private val getPrayerInfoUseCase: GetPrayerInfoUseCase = mock()

    @Test
    fun `prayerInfoUiState success`() = runTest {
        val expectedPrayerInfoUseCase = PrayerInfo(
            nextPrayerTime = NextPrayerTime(
                nextPrayerTimeMillis = 1000,
                nextPrayer = NextPrayer(
                    name = "Fajr",
                    index = 0
                )
            ),
            prayerTimesList = listOf(
                PrayerTimes(
                    date = "2024-02-11",
                    timingsResponse = TimingsResponse()
                )
            )
        )


        Mockito.lenient()
            .`when`(getPrayerInfoUseCase()) doReturn flowOf(Result.Success(expectedPrayerInfoUseCase))

        testObject = PrayerViewModel(getPrayerInfoUseCase)

        testObject.getPrayerInfo()
        assertEquals(
            PrayerInfoUiState.Success(expectedPrayerInfoUseCase),
            testObject.prayerInfoUiState.value
        )
    }

    @Test
    fun `prayerInfoUiState error`() = runTest {
        val exception = RuntimeException("API Error")

        Mockito.lenient()
            .`when`(getPrayerInfoUseCase()) doThrow exception

        testObject = PrayerViewModel(getPrayerInfoUseCase)

        testObject.getPrayerInfo()
        assertEquals(
            PrayerInfoUiState.Error("Coroutine Error"),
            testObject.prayerInfoUiState.value
        )
    }
}

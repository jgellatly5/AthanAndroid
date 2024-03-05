package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.text.SimpleDateFormat
import java.util.Locale

class GetNextPrayerTimeUseCaseTest {

    private lateinit var testObject: GetNextPrayerTimeUseCase

    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase = mock()
    private val formatTimesUseCase: FormatTimesUseCase = mock()
    private val getNextPrayerUseCase: GetNextPrayerUseCase = mock()

    @Test
    fun `getNextPrayerTime Result Success`() = runTest {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val expectedTimes = listOf(
            1954000L,
            1964000L,
            1974000L,
            1984000L,
            1994000L,
            2004000L,
            2014000L,
            2024000L,
            2034000L
        )
        val expectedTime = 1954000L

        Mockito.lenient()
            .`when`(
                formatTimesUseCase(simpleDateFormat)
            ) doReturn flowOf(Result.Loading, Result.Success(expectedTimes))

        testObject = GetNextPrayerTimeUseCase(parseTimeToMillisUseCase, formatTimesUseCase, getNextPrayerUseCase)
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(expectedTime), actualResult.last())
    }

    @Test
    fun `getNextPrayerTime Result Error`() = runTest {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val errorMessage = "Error"

        Mockito.lenient()
            .`when`(
                formatTimesUseCase(simpleDateFormat)
            ) doReturn flowOf(Result.Loading, Result.Error(errorMessage))

        testObject = GetNextPrayerTimeUseCase(parseTimeToMillisUseCase, formatTimesUseCase, getNextPrayerUseCase)
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

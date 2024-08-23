package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
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
import java.text.SimpleDateFormat
import java.util.Locale

class GetNextPrayerTimeUseCaseTest {

    private lateinit var testObject: GetNextPrayerTimeUseCase

    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase = mock()
    private val formatCurrentTimeUseCase: FormatCurrentTimeUseCase = mock()
    private val formatTimesUseCase: FormatTimesUseCase = mock()
    private val getNextPrayerUseCase: GetNextPrayerUseCase = mock()

    @Before
    fun setup() {
        testObject = GetNextPrayerTimeUseCase(
            formatCurrentTimeUseCase,
            parseTimeToMillisUseCase,
            formatTimesUseCase,
            getNextPrayerUseCase
        )
    }

    @Test
    fun `getNextPrayerTime Result Success`() = runTest {
        val currentTimeMillisSdf = SimpleDateFormat("HH:mm:ss", Locale.US)
        val parsedTimesSdf = SimpleDateFormat("HH:mm", Locale.US)
        val expectedCurrentTime = "10:00:00"
        val expectedGetNextPrayerUseCaseResponse =
            longArrayOf(-10000, 0, 10000, 20000, 30000, 40000, 50000, 86460000)
        val expectedNextPrayer = NextPrayer(
            name = "Fajr",
            index = 7
        )
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
        val expectedNextPrayerTime = NextPrayerTime(
            nextPrayerTimeMillis = 86460000L,
            nextPrayer = expectedNextPrayer
        )

        formatCurrentTimeUseCase.stub {
            on {
                invoke()
            } doReturn expectedCurrentTime
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(currentTimeMillisSdf, expectedCurrentTime)
            } doReturn 1964000L
        }

        formatTimesUseCase.stub {
            onBlocking {
                invoke(parsedTimesSdf)
            } doReturn flowOf(Result.Loading, Result.Success(expectedTimes))
        }

        getNextPrayerUseCase.stub {
            on {
                invoke(expectedGetNextPrayerUseCaseResponse)
            } doReturn expectedNextPrayer
        }

        val actualResult = testObject()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(expectedNextPrayerTime), actualResult.last())
    }

    @Test
    fun `getNextPrayerTime Result Error`() = runTest {
        val errorMessage = "Error"
        val currentTimeMillisSdf = SimpleDateFormat("HH:mm:ss", Locale.US)
        val parsedTimesSdf = SimpleDateFormat("HH:mm", Locale.US)
        val expectedCurrentTime = "10:00:00"
        val expectedGetNextPrayerUseCaseResponse =
            longArrayOf(-10000, 0, 10000, 20000, 30000, 40000, 50000, 86460000)
        val expectedNextPrayer = NextPrayer(
            name = "Fajr",
            index = 7
        )

        formatCurrentTimeUseCase.stub {
            on {
                invoke()
            } doReturn expectedCurrentTime
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(currentTimeMillisSdf, expectedCurrentTime)
            } doReturn 1964000L
        }

        formatTimesUseCase.stub {
            onBlocking {
                invoke(parsedTimesSdf)
            } doReturn flowOf(Result.Loading, Result.Error(errorMessage))
        }

        getNextPrayerUseCase.stub {
            on {
                invoke(expectedGetNextPrayerUseCaseResponse)
            } doReturn expectedNextPrayer
        }

        val actualResult = testObject()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

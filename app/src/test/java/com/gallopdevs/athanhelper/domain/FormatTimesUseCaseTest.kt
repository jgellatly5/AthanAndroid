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
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import java.text.SimpleDateFormat
import java.util.Locale

class FormatTimesUseCaseTest {

    private lateinit var testObject: FormatTimesUseCase

    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase = mock()
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase = mock()

    @Before
    fun setup() {
        testObject = FormatTimesUseCase(parseTimeToMillisUseCase, getPrayerTimesForWeekUseCase)
    }

    @Test
    fun `formatTimes Result Success`() = runTest {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val prayerTimesList = listOf(
            PrayerTimes(
                date = "2024-02-25",
                timingsResponse = TimingsResponse(
                    date = Date(timestamp = "2024-02-25"),
                    timings = Timings(
                        fajr = "5:00",
                        sunrise = "6:00",
                        dhuhr = "12:00",
                        asr = "15:00",
                        sunset = "18:00",
                        maghrib = "18:30",
                        isha = "19:00",
                        imsak = "4:00",
                        midnight = "23:59"
                    )
                )
            ),
            PrayerTimes(
                date = "2024-02-26",
                timingsResponse = TimingsResponse(
                    date = Date(timestamp = "2024-02-26"),
                    timings = Timings(
                        fajr = "5:00",
                        sunrise = "6:00",
                        dhuhr = "12:00",
                        asr = "15:00",
                        sunset = "18:00",
                        maghrib = "18:30",
                        isha = "19:00",
                        imsak = "4:00",
                        midnight = "23:59"
                    )
                )
            )
        )
        val firstTimings = prayerTimesList.first().timingsResponse.timings
        val secondTimings = prayerTimesList[1].timingsResponse.timings
        val expectedTimes = listOf(
            1954000L,
            1964000L,
            1974000L,
            1984000L,
            1994000L,
            2004000L,
            2014000L,
            1954000L
        )

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, firstTimings?.fajr)
            } doReturn expectedTimes[0]
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, firstTimings?.sunrise)
            } doReturn expectedTimes[1]
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, firstTimings?.dhuhr)
            } doReturn expectedTimes[2]
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, firstTimings?.asr)
            } doReturn expectedTimes[3]
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, firstTimings?.sunset)
            } doReturn expectedTimes[4]
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, firstTimings?.maghrib)
            } doReturn expectedTimes[5]
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, firstTimings?.isha)
            } doReturn expectedTimes[6]
        }

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, secondTimings?.fajr)
            } doReturn expectedTimes[0]
        }

        getPrayerTimesForWeekUseCase.stub {
            onBlocking {
                invoke()
            } doReturn flowOf(Result.Loading, Result.Success(prayerTimesList))
        }

        val actualResult = testObject.invoke(simpleDateFormat)

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(expectedTimes), actualResult.last())
    }

    @Test
    fun `formatTimes Result Error`() = runTest {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val errorMessage = "Error"

        parseTimeToMillisUseCase.stub {
            on {
                invoke(simpleDateFormat, "5:00")
            } doThrow RuntimeException(errorMessage)
        }

        getPrayerTimesForWeekUseCase.stub {
            onBlocking {
                invoke()
            } doReturn flowOf(Result.Loading, Result.Error(errorMessage))
        }

        val actualResult = testObject.invoke(simpleDateFormat)

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

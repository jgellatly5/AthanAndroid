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
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import java.text.SimpleDateFormat
import java.util.Locale

class FormatTimesUseCaseTest {

    private lateinit var testObject: FormatTimesUseCase

    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase = mock()
    private val getPrayerTimesForWeekUseCase: GetPrayerTimesForWeekUseCase = mock()

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

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, firstTimings?.fajr)
            ) doReturn expectedTimes[0]

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, firstTimings?.sunrise)
            ) doReturn expectedTimes[1]

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, firstTimings?.dhuhr)
            ) doReturn expectedTimes[2]

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, firstTimings?.asr)
            ) doReturn expectedTimes[3]

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, firstTimings?.sunset)
            ) doReturn expectedTimes[4]

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, firstTimings?.maghrib)
            ) doReturn expectedTimes[5]

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, firstTimings?.isha)
            ) doReturn expectedTimes[6]

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, secondTimings?.fajr)
            ) doReturn expectedTimes[0]

        Mockito.lenient()
            .`when`(
                getPrayerTimesForWeekUseCase()
            ) doReturn flowOf(Result.Loading, Result.Success(prayerTimesList))

        testObject = FormatTimesUseCase(parseTimeToMillisUseCase, getPrayerTimesForWeekUseCase)
        val actualResult = testObject.invoke(simpleDateFormat)

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Success(expectedTimes), actualResult.last())
    }

    @Test
    fun `formatTimes Result Error`() = runTest {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val errorMessage = "Error"

        Mockito.lenient()
            .`when`(
                parseTimeToMillisUseCase(simpleDateFormat, "5:00")
            ) doThrow RuntimeException(errorMessage)

        Mockito.lenient()
            .`when`(
                getPrayerTimesForWeekUseCase()
            ) doReturn flowOf(Result.Loading, Result.Error(errorMessage))

        testObject = FormatTimesUseCase(parseTimeToMillisUseCase, getPrayerTimesForWeekUseCase)
        val actualResult = testObject.invoke(simpleDateFormat)

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

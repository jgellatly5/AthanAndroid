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
import java.text.SimpleDateFormat
import java.util.Locale

class GetNextPrayerTimeUseCaseTest {

    private lateinit var testObject: GetNextPrayerTimeUseCase

    private val parseTimeToMillisUseCase: ParseTimeToMillisUseCase = mock()
    private val formatTimesUseCase: FormatTimesUseCase = mock()

    @Test
    fun `getNextPrayerTime Result Success`() = runTest {
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
        val expectedTime = 1954000L

        Mockito.lenient()
            .`when`(
                formatTimesUseCase(simpleDateFormat)
            ) doReturn flowOf(Result.Loading, Result.Success(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)))

        testObject = GetNextPrayerTimeUseCase(parseTimeToMillisUseCase, formatTimesUseCase)
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

        testObject = GetNextPrayerTimeUseCase(parseTimeToMillisUseCase, formatTimesUseCase)
        val actualResult = testObject.invoke()

        assertEquals(Result.Loading, actualResult.first())
        assertEquals(Result.Error(errorMessage), actualResult.last())
    }
}

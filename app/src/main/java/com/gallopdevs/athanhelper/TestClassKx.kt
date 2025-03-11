package com.gallopdevs.athanhelper

import com.gallopdevs.athanhelper.data.models.Date
import com.gallopdevs.athanhelper.data.models.Gregorian
import com.gallopdevs.athanhelper.data.models.Hijri
import com.gallopdevs.athanhelper.data.models.Meta
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes

fun PrayerInfo.Companion.test(
    nextPrayerTime: NextPrayerTime = NextPrayerTime.test(),
    prayerTimesList: List<PrayerTimes> = listOf()
): PrayerInfo = PrayerInfo(
    nextPrayerTime,
    prayerTimesList
)

fun NextPrayerTime.Companion.test(
    nextPrayerTimeMillis: Long = 0,
    nextPrayer: NextPrayer = NextPrayer.test()
): NextPrayerTime = NextPrayerTime(
    nextPrayerTimeMillis,
    nextPrayer
)

fun NextPrayer.Companion.test(
    name: String = "Fajr",
    index: Int = 0
): NextPrayer = NextPrayer(
    name,
    index
)

fun PrayerTimes.Companion.test(
    date: String = "24 Apr 2024",
    timingsResponse: TimingsResponse = TimingsResponse.test()
): PrayerTimes = PrayerTimes(
    date,
    timingsResponse
)

fun TimingsResponse.Companion.test(
    date: Date = Date.test(),
    timings: Timings = Timings.test(),
    meta: Meta = Meta()
): TimingsResponse = TimingsResponse(
    date,
    timings,
    meta
)

fun Date.Companion.test(
    readable: String = "24 Apr 2024",
    timestamp: String = "",
    gregorian: Gregorian = Gregorian(),
    hijri: Hijri = Hijri()
): Date = Date(
    readable,
    timestamp,
    gregorian,
    hijri
)

fun Timings.Companion.test(
    fajr: String = "5:00",
    sunrise: String = "7:00",
    dhuhr: String = "13:00",
    asr: String = "17:00",
    sunset: String = "19:00",
    maghrib: String = "19:30",
    isha: String = "20:00",
    imsak: String = "6:00",
    midnight: String = "1:00",
): Timings = Timings(
    fajr,
    sunrise,
    dhuhr,
    asr,
    sunset,
    maghrib,
    isha,
    imsak,
    midnight
)

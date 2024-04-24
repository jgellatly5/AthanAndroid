package com.gallopdevs.athanhelper

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

fun TimingsResponse.Companion.test(): TimingsResponse = TimingsResponse()

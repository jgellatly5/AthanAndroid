package com.gallopdevs.athanhelper.domain

import javax.inject.Inject

class GetNextPrayerUseCase @Inject constructor() {
    operator fun invoke(differences: LongArray): NextPrayer {
        val prayerNames = listOf(
            "Fajr",
            "Sunrise",
            "Dhuhr",
            "Asr",
            "Sunset",
            "Maghrib",
            "Isha",
            "Imsak",
            "Fajr"
        )
        var nextTimeIndex = 0
        for (i in differences.indices) {
            if (differences[i] < 0) {
                nextTimeIndex = (i + 1) % 5
            }
        }
        return NextPrayer(
            name = prayerNames[nextTimeIndex],
            index = nextTimeIndex
        )
    }
}

data class NextPrayer(
    val name: String,
    val index: Int
)

package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.repository.PrayerRepo
import javax.inject.Inject

class GetNextTimeMillisUseCase @Inject constructor(
    private val prayerRepo: PrayerRepo
) {
//    operator fun invoke(): Long = prayerRepo.getNextTimeMillis()
}

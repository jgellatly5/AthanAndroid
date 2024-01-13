package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.PrayerCalculator
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDE
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LONGITUDE
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.SettingsRepo
import javax.inject.Inject

class GetPrayerTimesForWeekUseCase @Inject constructor(
    private val getDatesForApiUseCase: GetDatesForApiUseCase,
    private val prayerRepo: PrayerRepo,
    private val settingsRepo: SettingsRepo
) {
    suspend operator fun invoke(): List<Timings> {
        val dates = getDatesForApiUseCase()
        val latitude = settingsRepo.getString(LATITUDE, "0.01")?.toDouble() ?: 0.01
        val longitude = settingsRepo.getString(LONGITUDE, "0.01")?.toDouble() ?: 0.01
        val calculationMethod = settingsRepo.getInt(CALCULATION_METHOD, PrayerCalculator.JAFARI)
        val timings = mutableListOf<Timings>()
        for (date in dates) {
            val timingsForDate = prayerRepo.getPrayerTimesForDate(date, latitude, longitude, calculationMethod)
            timingsForDate?.let {
                timings.add(it)
            }
        }
        return timings
    }
}

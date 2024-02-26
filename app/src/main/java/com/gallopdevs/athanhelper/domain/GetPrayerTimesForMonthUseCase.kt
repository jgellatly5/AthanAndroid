package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.PrayerCalculator
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDE
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LONGITUDE
import com.gallopdevs.athanhelper.data.models.Timings
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.repository.PrayerRepo
import com.gallopdevs.athanhelper.repository.SettingsRepo
import java.util.Calendar
import javax.inject.Inject

class GetPrayerTimesForMonthUseCase @Inject constructor(
    private val prayerRepo: PrayerRepo,
    private val settingsRepo: SettingsRepo
) {
    suspend operator fun invoke(): List<TimingsResponse?>? {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val latitude = settingsRepo.getString(LATITUDE, "0.01")?.toDouble() ?: 0.01
        val longitude = settingsRepo.getString(LONGITUDE, "0.01")?.toDouble() ?: 0.01
        val calculationMethod = settingsRepo.getInt(CALCULATION_METHOD, PrayerCalculator.JAFARI)
        return prayerRepo.getPrayerTimeResponsesForMonth(
            year,
            month,
            latitude,
            longitude,
            calculationMethod
        )
    }
}

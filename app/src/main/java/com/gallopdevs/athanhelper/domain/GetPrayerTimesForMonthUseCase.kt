package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.data.Result
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDE
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LONGITUDE
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.repository.PrayerRepository
import com.gallopdevs.athanhelper.repository.SettingsRepository
import com.gallopdevs.athanhelper.utilities.JAFARI
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class GetPrayerTimesForMonthUseCase @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Flow<Result<List<TimingsResponse?>>> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val latitude = settingsRepository.getString(LATITUDE, "0.01")?.toDouble() ?: 0.01
        val longitude = settingsRepository.getString(LONGITUDE, "0.01")?.toDouble() ?: 0.01
        val calculationMethod = settingsRepository.getInt(CALCULATION_METHOD, JAFARI)
        return prayerRepository.getPrayerTimeResponsesForMonth(
            year,
            month,
            latitude,
            longitude,
            calculationMethod
        )
    }
}

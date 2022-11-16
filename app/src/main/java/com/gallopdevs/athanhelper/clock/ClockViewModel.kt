package com.gallopdevs.athanhelper.clock

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gallopdevs.athanhelper.model.PrayerRepository

class ClockViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PrayerRepository = PrayerRepository()

    fun getDatePrayerTimes(count: Int) = repository.getDatePrayerTimes(count)

    fun getNextTimeMillis() = repository.getNextTimeMillis()

    fun getNextPrayerName() = repository.getNextPrayerName()

    fun setLocation(latitude: Double, longitude: Double) = repository.setLocation(latitude, longitude)

    fun setCalculations(calcMethod: Int, asrJuristic: Int, adjustHighLats: Int) = repository.setCalculations(calcMethod, asrJuristic, adjustHighLats)
}